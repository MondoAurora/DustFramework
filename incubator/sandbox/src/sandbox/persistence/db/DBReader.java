package sandbox.persistence.db;

import java.sql.*;
import java.text.FieldPosition;
import java.text.MessageFormat;

import dust.api.DustConstants.DustDeclId;
import dust.api.DustConstants.VariantSetMode;
import dust.api.components.*;
import dust.api.utils.DustUtils;
import dust.api.wrappers.DustIdentifier;

import sandbox.persistence.PersistenceValueExtractor;

public class DBReader {
	enum SQLCommands {
		FindEntity, SelectAspects, SelectValues, SelectLinks
	}

	PersistenceValueExtractor valReader = new PersistenceValueExtractor();

	String url = "jdbc:mysql://localhost:3306/dust";
	String driverClass = "org.gjt.mm.mysql.Driver";
	String user = "dust";
	String pass = "dust";

	Connection con;

	class SQLSelect {
		String[] data;
		MessageFormat fmt;

		SQLSelect(String strSQLFormat) throws Exception {
			this(strSQLFormat, 1);
		}

		SQLSelect(String strSQLFormat, int dataCount) throws Exception {
			fmt = new MessageFormat(strSQLFormat);
			data = new String[dataCount];
		}

		ResultSet select(String key) throws Exception {
			data[0] = key;
			return select();
		}

		ResultSet select() throws Exception {
			String query = useFormat(fmt, data);

			System.out.println(query);

			Statement stmt = con.createStatement();
			return stmt.executeQuery(query);
		}
	}

	SQLSelect[] commands;

	public DBReader() throws Exception {
		Class.forName(driverClass);

		con = DriverManager.getConnection(url, user, pass);
		con.setAutoCommit(false);

		commands = new SQLSelect[] {
			new SQLSelect("SELECT e.EntityID, e.PrimaryAspectTypeID from entities e {0} WHERE {1};", 2),
			new SQLSelect("SELECT EntityID, AspectID, TypeID FROM aspects WHERE EntityID = {0}"),
			new SQLSelect(
				"SELECT a0.TypeID, v0.FieldID, v0.ValueStr FROM aspects a0, aspValues v0 WHERE a0.EntityID = {0} AND a0.AspectID = v0.AspectID"),
			new SQLSelect(
				"SELECT a0.TypeID, l0.LinkID, l0.ToAspectID, e0.PrimaryAspectTypeID, l0.LinkData FROM aspects a0, links l0, entities e0 WHERE  a0.EntityID = {0} AND a0.AspectID = l0.FromAspectID AND e0.EntityID = l0.ToAspectID"), };
	}

	SQLSelect getCmd(SQLCommands cmd) {
		return commands[cmd.ordinal()];
	}

	String useFormat(MessageFormat fmt, String[] data) {
		final FieldPosition fp = new FieldPosition(0);
		final StringBuffer sb = new StringBuffer();

		fp.setBeginIndex(0);
		DustUtils.delSB(sb);

		fmt.format(data, sb, fp);

		return sb.toString();
	}

	public DustEntity loadEntity(DustDeclId primaryType, DustVariant[] knownFields) throws Exception {
		final MessageFormat fmtPrimaryType = new MessageFormat("e.PrimaryAspectTypeID = ''{0}''");
		final MessageFormat fmtFrom = new MessageFormat(", aspects a{0}, aspValues v{0}");
		final MessageFormat fmtValueFilter = new MessageFormat(
			" AND e.EntityID = a{0}.EntityID AND a{0}.TypeID = ''{1}'' AND a{0}.aspectID = v{0}.aspectID and v{0}.FieldID = ''{2}'' and v{0}.ValueStr = ''{3}''");

		final String[] data = new String[4];
		StringBuilder sbFrom = new StringBuilder();
		StringBuilder sbWhere = new StringBuilder();

		if (null != primaryType) {
			data[0] = primaryType.toString();
			sbWhere.append(useFormat(fmtPrimaryType, data));
		}

		int varCount = 0;
		for (DustVariant v : knownFields) {
			data[0] = String.valueOf(varCount);
			sbFrom.append(useFormat(fmtFrom, data));

			data[1] = v.getTypeId().toString();
			data[2] = v.getId().name();
			data[3] = v.getValueIdentifier().toString(); // this is TEMPORAL!!!
			sbWhere.append(useFormat(fmtValueFilter, data));
		}

		SQLSelect selectEntity = getCmd(SQLCommands.FindEntity);

		selectEntity.data[0] = sbFrom.toString();
		selectEntity.data[1] = sbWhere.toString();

		ResultSet rsEntities = selectEntity.select();

		DustEntity ret = null;

		if (rsEntities.first()) {
			ret = loadEntity(rsEntities.getString(1), rsEntities.getString(2));
		}

		return ret;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	DustDeclId getId(String typeId) throws Exception {
		Class c = Class.forName(typeId);
		return DustUtils.getWorld().getTypeId(c);
	}

	DustEntity loadEntity(String entityID, String primaryType) throws Exception {
		DustDeclId id = getId(primaryType);

		DustEntity ret = DustUtils.getEntity(id, null);
		ret.getState();

		ResultSet rs;
		PersistenceValueExtractor.Value val = null;

		rs = getCmd(SQLCommands.SelectAspects).select(entityID);
		for (boolean doRead = rs.first(); doRead; doRead = rs.next()) {
			DustDeclId aspId = getId(rs.getString(3));
			ret.getAspect(aspId, true);
		}

		rs = getCmd(SQLCommands.SelectValues).select(entityID);
		for (boolean doRead = rs.first(); doRead; doRead = rs.next()) {
			DustDeclId aspId = getId(rs.getString(1));
			DustAspect asp = ret.getAspect(aspId, true);
			val = valReader.getField(asp, rs.getString(2), val);

			switch (val.getType()) {
			case Identifier:
				val.getValue().setValueIdentifier(new DustIdentifier(rs.getString(3)));
				break;
			case Integer:
				val.getValue().setValueInteger(rs.getInt(3));
				break;
			}
		}

		rs = getCmd(SQLCommands.SelectLinks).select(entityID);
		for (boolean doRead = rs.first(); doRead; doRead = rs.next()) {
			DustDeclId aspId = getId(rs.getString(1));
			DustAspect asp = ret.getAspect(aspId, true);
			val = valReader.getField(asp, rs.getString(2), val);

			switch (val.getType()) {
			case ObSet:
				val.getValue().setData(loadEntity(rs.getString(3), rs.getString(4)), VariantSetMode.insert, null);
				break;
			}
		}

		return ret;
	}

}
