package org.mondoaurora.frame.tools;

import org.mondoaurora.frame.shared.*;

public class MAFToolsVariantWrapper implements MAFVariant {

	public static final MAFToolsVariantWrapper True = new MAFToolsVariantWrapper() {
		@Override
		public boolean getBool(boolean defValue) {
			return true;
		}
	};

	public static final MAFToolsVariantWrapper False = new MAFToolsVariantWrapper() {
		@Override
		public boolean getBool(boolean defValue) {
			return false;
		}
	};

	public static class ConstString extends MAFToolsVariantWrapper {
		String key;
		String str;

		public ConstString(String str) {
			this.str = str;
		}

		public ConstString(String key, String str) {
			this.key = key;
			this.str = str;
		}

		@Override
		public String getKey() {
			return key;
		}

		@Override
		public boolean isNull() {
			return MAFUtils.isEmpty(str);
		}

		@Override
		public String getString() {
			return str;
		}

		@Override
		public String toString() {
			return str;
		}
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNull() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MAFIdentifier getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getBool(boolean defValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInt(int defValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDouble(double defValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MAFDate getDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCodeStr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setString(String val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIdentifier(MAFIdentifier val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBool(boolean val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInt(int val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDouble(double val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDate(MAFDate val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setReference(MAFConnector val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCodeStr(String val) {
		// TODO Auto-generated method stub

	}

	@Override
	public MAFConnector getReference(String[] fields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getReference(MAFConnector conn) {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterable<? extends MAFVariant> getMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setData(MAFVariant from) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setData(Object value, VariantSetMode mode, String key) {
		// TODO Auto-generated method stub

	}

}
