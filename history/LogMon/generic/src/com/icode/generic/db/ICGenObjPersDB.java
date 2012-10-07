package com.icode.generic.db;

import java.sql.*;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.*;

import com.icode.generic.ICGenObjectDefault;
import com.icode.generic.ICGenPersistentStorage;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.*;
import com.icode.generic.obj.ICGenObjTranslator;

public class ICGenObjPersDB implements ICGenPersistentStorage, ICGenDataManageable, ICGenObjPersDBConstants {
	public interface FilterBuilder extends ICGenConfigurable {
		String getSQL(ICGenObject param, TableProvider tblProvider);
	}

	interface TableProvider {
		String getNextAlias();
	}

	class DefTableProvider implements TableProvider {
		String prefix;
		int count;

		public DefTableProvider(String prefix) {
			this.prefix = prefix;
		}

		public String getNextAlias() {
			return getAlias(count++);
		}

		public String getAlias(int idx) {
			return prefix + idx;
		}
	}

	String url = "jdbc:mysql://localhost:3306/test";
	String driverClass = "org.gjt.mm.mysql.Driver";
	String user = "root";
	String pass = "mason99";

	Connection con;
	Statement stmtValInsert;
	MessageFormat DETAIL_INSERT = new MessageFormat("INSERT INTO rec_data (rec_id, data_key, data_value) VALUES ({0}, ''{1}'', ''{2}'')");

	void setValue(int recId, String key, String value) throws SQLException {
		String insert = "INSERT INTO rec_data (rec_id, data_key, data_value) VALUES (" + recId + ", '" + key + "', '" + value + "')";
		System.out.println(insert);
		stmtValInsert.executeUpdate(insert);
	}

	void fill() throws SQLException {
		Statement stmt;

		String insert = "INSERT INTO rec_head (rec_type) VALUES ('pompom')";
		stmt = con.createStatement();
		stmt.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS);

		ResultSet keys = stmt.getGeneratedKeys();

		int lastKey = 1;
		while (keys.next()) {
			lastKey = keys.getInt(1);
			System.out.println(lastKey);
		}

		setValue(lastKey, "alma", "kukac");
		setValue(lastKey, "korte", "bubu");
		setValue(lastKey, "szilva", "csocso");

		con.commit();

		stmt.close();
	}

	public Enumeration find(ICGenObject.ObDef type, ICGenObject parent, ICGenTreeNode filter, ICGenObject filterParam) throws SQLException {
		return new DataEnum(type, parent, filter, filterParam);
	}

	public void store(ICGenObject ob, ICGenObject master) throws SQLException {
		ICGenObject.ObDef def = ob.getDefinition();

		Statement stmt;
		StringBuffer sb = new StringBuffer();
		String[] data = new String[3];

		long mKey = (null == master) ? -1 : master.getPersKey();

		String insert = (-1 == mKey) ? "INSERT INTO rec_head (rec_type, rec_type_ver) VALUES ('" + def.getName() + "', " + def.getVersion() + ")"
				: "INSERT INTO rec_head (rec_type, rec_type_ver, rec_master_id ) VALUES ('" + def.getName() + "', " + def.getVersion() + ", " + mKey + ")";
		stmt = con.createStatement();
		stmt.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS);
		ResultSet keys = stmt.getGeneratedKeys();

		keys.first();
		ob.setPersKey(keys.getLong(1));

		data[0] = keys.getString(1);
		FieldPosition fp = new FieldPosition(0);

		for (int i = 0; i < def.getAttCount(); ++i) {
			data[1] = def.getAtt(i).getName();
			data[2] = ob.getAttrib(i);
			DETAIL_INSERT.format(data, sb, fp);
			stmt.executeUpdate(sb.toString());
			ICGenUtilsBase.delSB(sb);
			fp.setBeginIndex(0);
		}

		con.commit();
		stmt.close();
	}

	class DataEnum implements Enumeration {
		ICGenObject.ObDef obType;
		ICGenObjTranslator trOb;;

		Statement stHead;
		ResultSet rsHead;

		Statement stDetail;

		boolean recOK;

		public DataEnum(ICGenObject.ObDef type, ICGenObject parent, ICGenTreeNode filter, ICGenObject filterParam) throws SQLException {
			this.obType = type;
			trOb = new ICGenObjTranslator(obType);

			StringBuffer select = new StringBuffer("select ");
			StringBuffer tables = new StringBuffer("rec_head h");
			StringBuffer fields = new StringBuffer("h.rec_id, h.rec_type, h.rec_type_ver ");
			StringBuffer where = new StringBuffer("h.rec_type='").append(type.getName()).append("' ");

			if (null != parent) {
				where.append("and h.rec_master_id=").append(parent.getPersKey()).append(" ");
			}

			if (null != filter) {
				select.append("distinct ");

				DefTableProvider tp = new DefTableProvider("d");

				FilterBuilder fb = (FilterBuilder) ICAppFrame.getComponent(filter, FilterBuilder.class);
				where.append("and ").append(fb.getSQL(filterParam, tp)).append(" ");

				for (int i = 0; i < tp.count; ++i) {
					String alias = tp.getAlias(i);
					tables.append(", rec_data ").append(alias);
					where.append("and h.rec_id=").append(alias).append(".rec_id ");
				}
			}

			stHead = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			select.append(fields).append("from ").append(tables).append(" where ").append(where);
			rsHead = stHead.executeQuery(select.toString());
			recOK = rsHead.first();

			if (recOK) {
				stDetail = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			} else {
				close();
			}
		}

		public boolean hasMoreElements() {
			return recOK;
		}

		public Object nextElement() {
			ICGenObject currObj = new ICGenObjectDefault(obType);

			if (recOK) {
				try {
					String search;
					search = "select data_key, data_value from rec_data where rec_id=" + rsHead.getInt(1);
					ResultSet rsDetail = stDetail.executeQuery(search);

					currObj.setPersKey(rsHead.getLong(1));
					/*
					 * valMap.put("rec_id", rsHead.getString(1)); valMap.put("rec_type", rsHead.getString(2));
					 */
					for (boolean doRead = rsDetail.first(); doRead; doRead = rsDetail.next()) {
						int idx = trOb.getAttIdx(rsDetail.getString(1));
						trOb.setAttStrByIdx(currObj, idx, rsDetail.getString(2));
					}

					rsDetail.close();
					recOK = rsHead.next();
					optClose();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return currObj;
		}

		void optClose() throws SQLException {
			if (!recOK) {
				close();
			}
		}

		public void close() throws SQLException {
			if (null != stHead) {
				stDetail.close();
				rsHead.close();
				stHead.close();

				stHead = null;
				recOK = false;
			}
		}
	}

	void release() throws Exception {
		stmtValInsert.close();
		con.close();
	}

	public void connect(Map credentials) throws Exception {
		user = (String) credentials.get("user");
		pass = (String) credentials.get("pass");

		connectDefault();
	}

	public void connectDefault() throws Exception {
		if (null == con) {
			Class.forName(driverClass);

			con = DriverManager.getConnection(url, user, pass);
			con.setAutoCommit(false);
			stmtValInsert = con.createStatement();
		}
	}

	public Map readCustom(ICGenTreeNode customSearch) throws Exception {
		Map ret = null;
		String select = customSearch.getMandatory(CUSTOM_SELECT);

		Statement stCustom = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		ResultSet rsCustom = stCustom.executeQuery(select);
		if (rsCustom.first()) {
			ret = new HashMap();
			ResultSetMetaData md = rsCustom.getMetaData();
			
			for ( int i = 1; i <= md.getColumnCount(); ++i ) {
				ret.put(md.getColumnName(i), rsCustom.getObject(i));
			}
		}

		return ret;
	}

	public void storeDataInto(ICGenTreeNode node, Object hint) {
		// TODO Auto-generated method stub

	}

	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		url = node.getMandatory("url");
		driverClass = node.getMandatory("driverClass");
		user = node.getMandatory("user");
		pass = node.getMandatory("pass");

		connectDefault();
	}
}
