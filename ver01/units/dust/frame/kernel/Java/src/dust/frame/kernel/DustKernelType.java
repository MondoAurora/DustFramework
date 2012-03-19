package dust.frame.kernel;

import java.util.*;

import dust.kernel.DustKernel;
import dust.shared.*;

public class DustKernelType extends DustLogic.Simple<IType> implements DustKernel.BootLogic, SType, DustKernelConsts {
	public static DustKernelType TYPE_NAME = new DustKernelType(KName.INFO);
	public static DustKernelType TYPE_TYPE = new DustKernelType(KType.INFO);

	DustKernel.TypeInfo ti;
	
	DustIdentifier id;
	String name;
	boolean referrable;

	
	Map<String, DustKernelField> mapFields = new TreeMap<String, DustKernelField>();
	ArrayList<DustKernelField> arrFields = new ArrayList<DustKernelField>();

	Map<String, DustKernelType> mapMessages = new TreeMap<String, DustKernelType>();

	public static DustKernelType getType(String id) {
		if ( SEP_TYPE_START != id.charAt(0) ) {
			int l = id.lastIndexOf(SEP_PATH_SEP);
			
			id = DustKernelIdentifier.buildRef(KType.ID.asPath(), id.substring(0, l), id.substring(l+1));
		}
		return getType(DustKernel.Environment.getId(id));
	}
	
	public static DustKernelType getType(DustIdentifier typeId) {
		DustKernelData dd = (DustKernelData)((DustKernel.DataWrapper)DustKernel.Environment.getInstance(typeId)).getData();
		
		if ( null == dd ) {
			throw new DustRuntimeException("DustKernelType", "Unknown type referred: " + typeId);
		}
		
		return (DustKernelType) dd.logicWrapper.getLogic();
	}

	public DustKernelType() {
	}

	DustKernelType(DustKernel.TypeInfo ti) {
		this.ti = ti;
		
		this.id = ti.id;
		this.name = ti.name;
		this.referrable = ti.referrable;

		for (DustKernel.FieldInfo fi : ti.getFields()) {
			addField(fi);
		}
	}
	
	public DustIdentifier getID() {
		return ti.id;
	}


	void addField(DustKernel.FieldInfo fi) {
		DustKernelField f = new DustKernelField(this, fi, mapFields.size());
		mapFields.put(f.getName(), f);
		arrFields.add(f);
	}
	
	int getFieldCount() {
		return arrFields.size();
	}

	@Override
	public void init(IType config) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public DustObject export(Environment env) {
		DustKernelEntity e = ((DustKernelEnvironment)env).registerEntity(TYPE_TYPE, id, this);
		
		IType tExp = (IType)e.getAspect(KType.ID);
		
		tExp.getName().setName(name);
		
		tExp.setReferrable(referrable);
		
		for (DustKernelField fld: arrFields) {
			tExp.addField((IField)fld.export(env), fld.idx);
		}
		
		return tExp;
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
}
