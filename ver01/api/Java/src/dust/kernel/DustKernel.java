package dust.kernel;

import java.util.ArrayList;

import dust.shared.*;
import dust.shared.DustLogic.Return;
import dust.shared.DustLogic.ReturnType;

public interface DustKernel extends DustConsts {
	
	String ID_VENDOR_ROOT = "dust";
	String ID_DOMAIN_FRAME = "frame";

	int LEN_ID = 30;
	int LEN_LONG = 200;

	class ConstIdentifier implements DustIdentifier {
		String name;
		
		public ConstIdentifier(String name) {
			this.name = name;
		}

		@Override
		public String asPath() {
			return null;
		}

		@Override
		public String asReference() {
			return null;
		}
		
		@Override
		public String getName() {
			return name;
		}
	}

//	DustIdentifier THIS = new ConstIdentifier("this");
//	DustIdentifier CALL_CTX = new ConstIdentifier("call context");

	enum VariantSetMode { set, addFirst, addLast, insert, remove, clear };

	enum VariantType {
		IDENTIFIER, BOOLEAN, CHAR, VALUESET, INTEGER, DOUBLE, STRING, DATE, REFERENCE, SET, ARRAY
	};
	

	public interface Variant extends DustKernel {
		String getName();
		VariantType getType();

		Object getKey();
		boolean isNull();
		
		void loadData(Variant from);
			
		Object getData();
		Iterable<? extends Variant> getMembers();
		void setData(Object value, VariantSetMode mode, Object key);
	}
	
	public abstract class Environment {
		protected static Environment env;
		
		protected abstract DustObject getInstanceI(DustIdentifier id);
		protected abstract DustIdentifier getIdI(String id);
		protected abstract DustIdentifier getTypeIdI(String vendor, String domain, String unit, String name);
		
		public static DustObject getInstance(DustIdentifier id) {
			return env.getInstanceI(id);
		}
		
		public static DustIdentifier getId(String id) {
			return env.getIdI(id);
		}
		
		public static DustIdentifier getTypeId(String vendor, String domain, String unit, String name) {
			return env.getTypeIdI(vendor, domain, unit, name);
		}
		
		public static Environment getEnv() {
			return env;
		}
	}
	
	interface LogicFactory {
		DustLogic<?, Enum<?>> newLogic(DustIdentifier typeId);
	}

	interface Entity {
	}

	interface DataInterface {
		DustIdentifier getObTypeID();

		int getFieldCount();
		
		boolean isNull(int idx);
		Variant getFieldVariant(int idx);
		
		Entity getEntity();
		
		DustObject getNeighbor(DustIdentifier typeId);
	}

	interface DataInstance extends DataInterface {
		void send(DataWrapper w, Enum<?> msgId, DustObject msgOb, boolean wait, ResponseProcessor respProc);
	}
	
	static class DataWrapper implements DustObject, DataInterface {
		private DataInstance data;
				
		public static abstract class Factory {
			protected final void setData(DataWrapper wrapper, DataInstance instance) {
				wrapper.data = instance;
			}
			
			public abstract DataWrapper newWrapper(DustIdentifier typeID, DataInstance instance);
		}

		public DataInstance getData() {
			return data;
		}
		
		@Override
		public Entity getEntity() {
			return data.getEntity();
		}
		@Override
		public DustIdentifier getObTypeID() {
			return data.getObTypeID();
		}

		@Override
		public int getFieldCount() {
			return data.getFieldCount();
		}

		@Override
		public boolean isNull(int idx) {
			return data.isNull(idx);
		}

		@Override
		public Variant getFieldVariant(int idx) {
			return data.getFieldVariant(idx);
		}

		@Override
		public DustObject getNeighbor(DustIdentifier typeId) {
			return data.getNeighbor(typeId);
		}

		@Override
		public void send(Enum<?> msgId, DustObject msgOb, boolean wait, ResponseProcessor respProc) {
			data.send(this, msgId, msgOb, wait, respProc);
		}
	}
	
	static abstract class LogicWrapper <Logic, MsgType extends Enum<?>> {
		Logic logic;
		
		public LogicWrapper overload;

		@SuppressWarnings("unchecked")
		private void setLogic(DustLogic<?, Enum<?>> l) {
			this.logic = (Logic) l;
		}
		
		public Logic getLogic() {
			return logic;
		}
		
		public Object processContextCreated(MsgType msg) {
			return null;
		}

		@SuppressWarnings("unchecked")
		public final Return processMessageI(Enum<?> msgId, DustObject msgOb, Object ctx) {
			Return ret = null;
			
			if ( null == overload ) {
				ret = processMessage((MsgType) msgId, msgOb, ctx);
			} else {
				ret = overload.processMessageI(msgId, msgOb, ctx);
				if ( ReturnType.Pass == ret.getType() ) {
					ret = processMessage((MsgType) msgId, msgOb, ctx);
				}
			}
			
			return ret;
		}

		public abstract Return processMessage(MsgType msgId, DustObject msgOb, Object ctx);
		public void initI(DustObject ob) {
			
			// overload initialization might come here
			
			init(ob);
		}
		
		public abstract void init(DustObject ob);
		
		public Return processRelayReturn(Return ob, Object ctx) {
			return SUCCESS;
		}
		
		public void processContextClosed(Object ctx) {
			
		}
		
		public static abstract class Factory {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			protected final void setLogic(LogicWrapper wrapper, DustLogic<?, Enum<?>> logic) {
				wrapper.setLogic(logic);
			}
			
			@SuppressWarnings("rawtypes")
			public abstract LogicWrapper newWrapper(DustIdentifier typeID, DustLogic<?, Enum<?>> logic);
		}
	}
	
	interface BootLogic {
		DustObject export(Environment env); 
	}	
	
	public final class FieldInfo {
		public final String name;
		
		public final VariantType type;
		public final int length;
		public final TypeInfo obType;
		public final Object defValue;

		private String[] valsetValues;
		
		public FieldInfo(String name, VariantType type) {
			this(name, type, -1, null, null, null);
		}
		
		public FieldInfo(String name, VariantType type, Object defValue) {
			this(name, type, -1, null, defValue, null);
		}
		
		public FieldInfo(String name, VariantType type, TypeInfo obType) {
			this(name, type, -1, obType, null, null);
		}
		
		public FieldInfo(String name, Iterable<?> valsetValues) {
			this(name, VariantType.VALUESET, -1, null, null, valsetValues);
		}
		
		public FieldInfo(String name, VariantType type, int length, TypeInfo obType, Object defValue, Iterable<?> valsetValues) {
			this.name = name;
			this.type = type;
			this.length = length;
			this.obType = obType;
			this.defValue = defValue;
			
			if ( null == valsetValues ) {
				this.valsetValues = null;				
			} else {
				ArrayList<String> v = new ArrayList<>();
				for ( Object o : valsetValues ) {
					v.add(o.toString());
				}
				this.valsetValues = new String[v.size()];
				v.toArray(this.valsetValues);
			}
		}

		public int getValsetCount() {
			return (null == valsetValues) ? 0 : valsetValues.length;
		}
		
		public Iterable<String> getValsetNames() {
			return new DustUtils.ArrayIter<String>(valsetValues);
		}
		
		public String getValsetName(int idx) {
			return valsetValues[idx];
		}
	}	
	
	public final class TypeInfo {
		public final DustIdentifier id;
		public final String name;
		public final boolean referrable;
		
		private FieldInfo[] fields;
		private DustIdentifier[] overrides;
		
		
		public TypeInfo(DustIdentifier id, boolean referrable, FieldInfo[] fields, DustIdentifier[] overrides) {
			this.id = id;
			
			this.name = id.getName();
			this.referrable = referrable;
			this.fields = fields;
			this.overrides = overrides;
		}
		
		public Iterable<FieldInfo> getFields() {
			return new DustUtils.ArrayIter<FieldInfo>(fields);
		}

		public int getFieldCount() {
			return fields.length;
		}
		
		public FieldInfo getFieldInfo(int idx) {
			return fields[idx];
		}
		
		public Iterable<DustIdentifier> getOverrides() {
			return new DustUtils.ArrayIter<DustIdentifier>(overrides);
		}		
	}
}
