package sandbox.persistence.db;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import dust.api.DustConstants.DustDeclId;
import dust.api.DustConstants.InvokeResponseProcessor;
import dust.api.DustConstants.VariantSetMode;
import dust.api.components.*;
import dust.api.utils.DustUtilFormatter;
import dust.api.utils.DustUtils;
import dust.api.wrappers.DustIdentifier;

import sandbox.persistence.PersistenceTransaction;
import sandbox.persistence.PersistenceValueExtractor;

public class DBPersistentStorage implements DBConstants {

	class DBTransaction extends PersistenceTransaction {
		Connection conn;

		DBTransaction() throws Exception {
			conn = DriverManager.getConnection(url, user, pass);
			conn.setAutoCommit(false);
		}

		ResultSet select(String query) throws Exception {
			System.out.println(query);

			Statement stmt = conn.createStatement();
			return stmt.executeQuery(query);
		}

		void delete(String query) throws Exception {
			System.out.println(query);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
		}

		String insert(String query) throws Exception {
			System.out.println(query);

			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet keys = stmt.getGeneratedKeys();
			return keys.first() ? keys.getString(1) : null;
		}

		public void commitInt() throws Exception {
			conn.commit();
		}
	}

	PersistenceValueExtractor valReader = new PersistenceValueExtractor();

	String url = "jdbc:mysql://localhost:3306/dust";
	String driverClass = "org.gjt.mm.mysql.Driver";
	String user = "dust";
	String pass = "dust";

	public DBPersistentStorage() throws Exception {
		Class.forName(driverClass);
	}

	DBTransaction getTransaction() throws Exception {
		return new DBTransaction();
	}

	void releaseTransaction(DBTransaction t) {

	}

	public void loadEntity(DustDeclId primaryType, DustVariant[] knownFields, InvokeResponseProcessor rp)
		throws Exception {
		StringBuilder sbFrom = new StringBuilder();
		StringBuilder sbWhere = new StringBuilder();

		if (null != primaryType) {
			DustUtilFormatter fmt = TD_FIND_ENTITY_TYPE.getFormatter();
			fmt.setValue(0, primaryType);
			sbWhere.append(fmt.useFormat());
		}

		if (null != knownFields) {
			int varCount = 0;
			DustUtilFormatter fmtFrom = TD_FIND_ENTITY_FROM.getFormatter();
			DustUtilFormatter fmtWhere = TD_FIND_ENTITY_WHERE.getFormatter();

			for (DustVariant v : knownFields) {
				fmtFrom.setValue(0, varCount);
				sbFrom.append(fmtFrom.useFormat());

				fmtWhere.setValue(0, varCount);
				fmtWhere.setValue(1, v.getTypeId());
				fmtWhere.setValue(2, v.getId().name());
				fmtWhere.setValue(3, v.getValueIdentifier());
				sbWhere.append(fmtWhere.useFormat());
			}
		}

		DustUtilFormatter fmtFindEntity = TD_FIND_ENTITY.getFormatter();
		fmtFindEntity.setValue(0, sbFrom);
		fmtFindEntity.setValue(1, sbWhere);

		DBTransaction transaction = getTransaction();

		rp.searchStarted();

		ResultSet rsEntities = transaction.select(fmtFindEntity.useFormat());
		for (boolean doRead = rsEntities.first(); doRead; doRead = rsEntities.next()) {
			String eId = rsEntities.getString(1);
			String eType = rsEntities.getString(2);
			rp.entityFound(loadEntity(eId, eType, transaction));
		}

		rp.searchFinished();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	DustDeclId getId(String typeId) throws Exception {
		Class c = Class.forName(typeId);
		return DustUtils.getWorld().getTypeId(c);
	}

	DustEntity loadEntity(String entityID, String primaryType, DBTransaction transaction) throws Exception {
		DustDeclId id = getId(primaryType);
		String[] entityId = new String[] { entityID };

		DustEntity ret = DustUtils.getEntity(id, null);
		ret.getState();

		PersistenceValueExtractor.Value val = null;

		ResultSet rs;
		rs = transaction.select(TD_SELECT_ENTITY_ASPECT.getFormatter().useFormat(entityId));
		for (boolean doRead = rs.first(); doRead; doRead = rs.next()) {
			DustDeclId aspId = getId(rs.getString(3));
			ret.getAspect(aspId, true);
		}

		rs = transaction.select(TD_SELECT_ENTITY_VALUES.getFormatter().useFormat(entityId));
		for (boolean doRead = rs.first(); doRead; doRead = rs.next()) {
			DustDeclId aspId = getId(rs.getString(1));
			DustAspect asp = ret.getAspect(aspId, true);
			val = valReader.getField(asp, rs.getString(2), val);

			switch (val.getType()) {
			case Boolean:
				val.getValue().setValueBoolean("1".equals(rs.getString(3)));
				break;
			case Identifier:
				val.getValue().setValueIdentifier(new DustIdentifier(rs.getString(3)));
				break;
			case Integer:
				val.getValue().setValueInteger(rs.getInt(3));
				break;
			}
		}

		rs = transaction.select(TD_SELECT_ENTITY_LINKS.getFormatter().useFormat(entityId));
		for (boolean doRead = rs.first(); doRead; doRead = rs.next()) {
			DustDeclId aspId = getId(rs.getString(1));
			DustAspect asp = ret.getAspect(aspId, true);
			val = valReader.getField(asp, rs.getString(2), val);

			switch (val.getType()) {
			case ObSet:
				val.getValue().setData(loadEntity(rs.getString(3), rs.getString(4), transaction), VariantSetMode.insert, null);
				break;
			}
		}

		return ret;
	}

	public void storeEntity(DustEntity entity) throws Exception {
		DBTransaction transaction = new DBTransaction();

		store(entity, transaction);

		transaction.commit();
	}

	String store(DustEntity entity, DBTransaction transaction) throws Exception {
		String entityKey = (String) entity.getPersistentId();	
		DustDeclId idPType = entity.getPrimaryTypeId();

		if (null == entityKey) {
			entityKey = storeAspect(entity, "-1", idPType, null, transaction);

			DustUtilFormatter fmt = TD_INSERT_ENTITY.getFormatter();
			fmt.setValue(0, entityKey);
			fmt.setValue(1, idPType);
			transaction.insert(fmt.useFormat());

			fmt = TD_UPDATE_ASPECT_ENTITYID.getFormatter();
			fmt.setValue(0, entityKey);
			transaction.insert(fmt.useFormat());

			entity.setPersistentId(entityKey);
			transaction.add(entity);

			for (DustDeclId idAspType : entity.getTypes()) {
				if (!DustUtils.isEqual(idPType, idAspType)) {
					storeAspect(entity, entityKey, idAspType, null, transaction);
				}
			}
		} else if (transaction.add(entity)) {
			ResultSet rs;
			DustUtilFormatter fmt = TD_SELECT_ENTITY_ASPECT.getFormatter();
			fmt.setValue(0, entityKey);
			rs = transaction.select(fmt.useFormat());

			Map<DustDeclId, String> mapAspIds = new HashMap<DustDeclId, String>();

			for (boolean doRead = rs.first(); doRead; doRead = rs.next()) {
				DustDeclId type = getId(rs.getString(3));
				String aspId = rs.getString(2);
				if (null == entity.getAspect(type, false)) {
					deleteAspect(aspId, transaction);
				} else {
					mapAspIds.put(type, aspId);
				}
			}

			for (DustDeclId idAspType : entity.getTypes()) {
				storeAspect(entity, entityKey, idAspType, mapAspIds.get(idAspType), transaction);
			}
		}

		return entityKey;
	}

	void deleteAspectContent(String aspId, DBTransaction transaction) throws Exception {
		// the linked member entities detection is NOT the persistence connector's task; the context must
		// follow the paths and mark the linked entities as deleted; the connector should delete them on commit 

		String[] aspKey = new String[]{aspId};
		
		DustUtilFormatter fmt = TD_DELETE_ASPECT_VALUES.getFormatter();
		transaction.delete(fmt.useFormat(aspKey));
		
		fmt = TD_DELETE_ASPECT_LINKS.getFormatter();
		transaction.delete(fmt.useFormat(aspKey));
		
	}

	void deleteAspect(String aspId, DBTransaction transaction) throws Exception {
		deleteAspectContent(aspId, transaction);
		
		DustUtilFormatter fmt = TD_DELETE_ASPECT.getFormatter();
		fmt.setValue(0, aspId);
		transaction.delete(fmt.useFormat());
	}

	void deleteEntity(DustEntity entity, String entityKey, DBTransaction transaction) throws Exception {
	}

	String storeAspect(DustEntity entity, String entityKey, DustDeclId idAspType, String oldAspId,
		DBTransaction transaction) throws Exception {
		DustAspect target = entity.getAspect(idAspType, false);

		String aspKey;

		DustUtilFormatter fmtInsertLink = TD_INSERT_LINK.getFormatter();
		DustUtilFormatter fmtInsertValue = TD_INSERT_VALUE.getFormatter();

		if (null != oldAspId) {
			deleteAspectContent(oldAspId, transaction);
			aspKey = oldAspId;
		} else {
			DustUtilFormatter fmtInsertAspect = TD_INSERT_ASPECT.getFormatter();
			fmtInsertAspect.setValue(0, entityKey);
			fmtInsertAspect.setValue(1, idAspType);
			aspKey = transaction.insert(fmtInsertAspect.useFormat());
		}

		for (PersistenceValueExtractor.Value val : valReader.getFields(target)) {
			DustVariant vTargetFld = val.getValue();

			if ((null != vTargetFld) && !vTargetFld.isNull()) {
				String value = null;
				boolean link = false;

				switch (val.getType()) {
				case Boolean:
					value = vTargetFld.getValueBoolean() ? "1" : "0";
					break;
				case Identifier:
					value = vTargetFld.getValueIdentifier().toString();
					break;
				case Integer:
					value = vTargetFld.getValueInteger().toString();
					break;
				case ObSet:
					link = true;
					for (DustVariant varMem : vTargetFld.getMembers()) {
						String childKey = store(varMem.getValueObject(), transaction);

						fmtInsertLink.setValue(0, aspKey);
						fmtInsertLink.setValue(1, childKey);
						fmtInsertLink.setValue(2, val.getId().name());
						fmtInsertLink.setValue(3, null);
						transaction.insert(fmtInsertLink.useFormat());
					}
					break;
				}

				if (!link) {
					if (null != value) {
						fmtInsertValue.setValue(0, aspKey);
						fmtInsertValue.setValue(1, val.getId().name());
						fmtInsertValue.setValue(2, value);
						transaction.insert(fmtInsertValue.useFormat());
					}
				}
			}
		}

		return aspKey;
	}
}
