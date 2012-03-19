package dust.frame.kernel;

import dust.kernel.DustKernel;
import dust.shared.*;

public class DustKernelField extends DustLogic.Simple<IField> implements DustKernel.BootLogic, SField, DustKernelConsts {
	DustKernelType parentType;

	String name;
	int idx;

	VariantType type;
	int length;
	TypeInfo obType;
	Object defValue;

	String[] valsetValues;

	public static DustKernelType TYPE_FIELD = new DustKernelType(KField.INFO);

	DustKernelField(DustKernelType parentType, DustKernel.FieldInfo fi, int idx) {
		this.parentType = parentType;

		this.name = fi.name;
		this.type = fi.type;
		this.length = fi.length;
		this.obType = fi.obType;
		this.defValue = fi.defValue;

		int vc = fi.getValsetCount();

		if (0 < vc) {
			valsetValues = new String[vc];
			int i = 0;
			for (String n : fi.getValsetNames()) {
				valsetValues[i++] = n;
			}
		} else {
			valsetValues = null;
		}

		this.idx = idx;
	}

	public String getName() {
		return name;
	}

	public VariantType getType() {
		return type;
	}

	public TypeInfo getObType() {
		return obType;
	}

	public int getLength() {
		return length;
	}

	public int getIndex() {
		return idx;
	}

	@Override
	public void init(IField config) {
		// TODO Auto-generated method stub

	}

	@Override
	public DustObject export(Environment env) {
		DustKernelEntity e = ((DustKernelEnvironment)env).registerEntity(TYPE_FIELD, parentType.id.asPath(), name, this);
		
		IField tExp = (IField)e.getAspect(KField.ID);

		tExp.getName().setName(name);

		tExp.setType(type.toString());
		if (-1 != length) {
			tExp.setLength(length);
		}
		if (null != obType) {
			tExp.setObType(obType.name);
		}
		if (null != valsetValues) {
			tExp.setEnumNames(DustUtils.arr2str(valsetValues, SEP_VALSET));
		}

		return tExp;
	}

	@Override
	public String toString() {
		return "Field: " + name + " " + type;
	}
}
