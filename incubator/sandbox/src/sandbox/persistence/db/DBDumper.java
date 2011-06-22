package sandbox.persistence.db;

import java.sql.*;
import java.text.FieldPosition;
import java.text.MessageFormat;

import dust.api.DustConstants.DustDeclId;
import dust.api.components.*;
import dust.api.utils.DustUtils;

import sandbox.persistence.PersistenceValueExtractor;

public class DBDumper {
	enum SQLCommands {
		InsertEntity, InsertAspect, UpdatePrimaryAspect, InsertValue, InsertLink
	}
	
	PersistenceValueExtractor valReader = new PersistenceValueExtractor();

	String url = "jdbc:mysql://localhost:3306/dust";
	String driverClass = "org.gjt.mm.mysql.Driver";
	String user = "dust";
	String pass = "dust";

	Connection con;
	
	class SQLCmd {
		Statement stmt;
		String[] data;
		MessageFormat fmt;
		
		SQLCmd(String strSQLFormat, int dataCount) throws Exception {
			stmt = con.createStatement();
			fmt = new MessageFormat(strSQLFormat);
			data = new String[dataCount];
		}
		
		String execute() throws Exception {
			final FieldPosition fp = new FieldPosition(0);
			final StringBuffer sb = new StringBuffer();

			fp.setBeginIndex(0);
			DustUtils.delSB(sb);

			fmt.format(data, sb, fp);
			System.out.println(sb);
			stmt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			ResultSet keys = stmt.getGeneratedKeys();

			return keys.first() ? keys.getString(1) : null;
		}
		
		void close() throws Exception {
			stmt.close();
		}
	}
	
	SQLCmd[] commands;

	public DBDumper() throws Exception {
		Class.forName(driverClass);

		con = DriverManager.getConnection(url, user, pass);
		con.setAutoCommit(false);

		commands = new SQLCmd[] {
			new SQLCmd("INSERT INTO entities (EntityID, PrimaryAspectTypeID) VALUES ({0}, ''{1}'')", 2),
			new SQLCmd("INSERT INTO aspects (EntityID, TypeID) VALUES ({0}, ''{1}'')", 2),
			new SQLCmd("UPDATE aspects SET EntityID = {0} WHERE AspectID = {0}", 1),
			new SQLCmd("INSERT INTO aspValues (AspectID, FieldID, ValueStr) VALUES ({0}, ''{1}'', ''{2}'')", 3),
			new SQLCmd("INSERT INTO links (FromAspectID, ToAspectID, LinkID, LinkData) VALUES ({0}, {1}, ''{2}'', ''{3}'')", 4),
		};
	}
	
	SQLCmd getCmd(SQLCommands cmd) {
		return commands[cmd.ordinal()];
	}
	
	void commit() throws Exception {
		con.commit();
		for ( SQLCmd cmd : commands ) {
			cmd.close();
		}
	}
	
	public void dumpEntity(DustEntity entity) throws Exception {
		dump(entity);
		commit();
	}

	String dump(DustEntity entity) throws Exception {
		String entityKey = "-1";
		
		DustDeclId idPType = entity.getPrimaryTypeId();
		entityKey = dumpAspect(entity, entityKey, idPType);

		SQLCmd cmd = getCmd(SQLCommands.InsertEntity);
		cmd.data[0] = entityKey;
		cmd.data[1] = idPType.toString();
		cmd.execute();

		cmd = getCmd(SQLCommands.UpdatePrimaryAspect);
		cmd.data[0] = entityKey;
		cmd.execute();

		for (DustDeclId idAspType : entity.getTypes()) {
			if (!DustUtils.isEqual(idPType, idAspType)) {
				dumpAspect(entity, entityKey, idAspType);
			}
		}
		
		return entityKey;
	}

	String dumpAspect(DustEntity entity, String entityKey, DustDeclId idAspType) throws Exception {
		DustAspect target = entity.getAspect(idAspType, false);
		
		SQLCmd cmd = getCmd(SQLCommands.InsertAspect);
		cmd.data[0] = entityKey;
		cmd.data[1] = idAspType.toString();
		String aspKey = cmd.execute();

		for (PersistenceValueExtractor.Value val : valReader.getFields(target)) {
			DustVariant vTargetFld = val.getValue();

			if ((null != vTargetFld) && !vTargetFld.isNull()) {
				String value = null;
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
					for (DustVariant varMem : vTargetFld.getMembers()) {
						String childKey = dump(varMem.getValueObject());
						
						cmd = getCmd(SQLCommands.InsertLink);
						cmd.data[0] = aspKey;
						cmd.data[1] = childKey;
						cmd.data[2] = val.getId().name();
						cmd.data[3] = null;
						cmd.execute();
						
					}
					break;
				}
				
				if ( null != value ) {
					cmd = getCmd(SQLCommands.InsertValue);
					cmd.data[0] = aspKey;
					cmd.data[1] = val.getId().name();
					cmd.data[2] = value;
					cmd.execute();
				}
			}
		}

		return aspKey;
	}
}
