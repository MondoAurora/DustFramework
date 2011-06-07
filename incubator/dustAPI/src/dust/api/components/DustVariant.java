package dust.api.components;

import java.util.Iterator;

import dust.api.DustConstants;
import dust.api.wrappers.DustByteBuffer;
import dust.api.wrappers.DustDateImmutable;
import dust.api.wrappers.DustIdentifier;

public interface DustVariant extends DustConstants {
	Enum<? extends FieldId> getId();

	boolean isNull();

	void loadContent(DustVariant from);

	DustIdentifier getValueIdentifier();
	boolean getValueBoolean();
	Long getValueLong();
	Double getValueDouble();
	DustDateImmutable getValueDate();
	String getValueString();
	String getValueValSet();
	<T extends Enum<T>> T getValueValSet(Class<T> enumType);
	DustByteBuffer getValueBuffer();
	DustEntity getValueObject();

	void setValueBoolean(boolean val);
	void setValueIdentifier(DustIdentifier val);
	void setValueString(String val);
	void setValueValSet(String val);
	void setValueValSet(Enum<?> val);

	Iterator<DustVariant> iterator();
}