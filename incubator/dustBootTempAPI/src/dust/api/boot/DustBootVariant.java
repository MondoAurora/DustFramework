package dust.api.boot;

import dust.api.components.DustVariant;

public class DustBootVariant extends DustVariant {
	Object data;

	protected DustBootVariant(Enum<? extends FieldId> fieldId, FieldType fldType) {
		super(fieldId, fldType);
	}
	
	public boolean isNull() {
		return (null == data);
	}
	
	protected Object getData() {
		return data;
	}
	
	public void setData(Object value, VariantSetMode mode, String key) {
		this.data = value;
	}

	public void loadContent(DustVariant from) {
		this.data = ((DustBootVariant)from).getData();
	}
	
}
