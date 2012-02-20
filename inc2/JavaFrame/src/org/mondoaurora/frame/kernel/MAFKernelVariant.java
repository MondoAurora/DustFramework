package org.mondoaurora.frame.kernel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mondoaurora.frame.kernel.MAFKernelDumper.Indent;
import org.mondoaurora.frame.shared.MAFConnector;
import org.mondoaurora.frame.shared.MAFDate;
import org.mondoaurora.frame.shared.MAFIdentifier;
import org.mondoaurora.frame.shared.MAFRuntimeException;
import org.mondoaurora.frame.shared.MAFVariant;

public class MAFKernelVariant implements MAFVariant, MAFKernelConsts {
	FieldType type;

	Object data;

	protected MAFKernelVariant(FieldType type) {
		this.type = type;
	}

	public MAFKernelVariant(FieldType type, Object value) {
		this(type);

		if (null != value) {
			setData(value, VariantSetMode.set, null);
		}
	}

	public FieldType getType() {
		return type;
	}

	Object getDataAssertType(FieldType reqType) {
		if (type != reqType) {
			throw new MAFRuntimeException.InvalidFieldType();
		}
		return data;
	}

	@Override
	public boolean isNull() {
		if (null == data) {
			return true;
		} else {
			switch (type) {
			case ARRAY:
			case SET:
				return ((Collection<?>) data).isEmpty();
			default:
				return false;
			}
		}
	}

	@Override
	public String getString() {
		return (String) getDataAssertType(FieldType.STRING);
	}

	@Override
	public MAFIdentifier getIdentifier() {
		return (MAFIdentifier) getDataAssertType(FieldType.IDENTIFIER);
	}

	@Override
	public boolean getBool(boolean defValue) {
		Object val = getDataAssertType(FieldType.BOOLEAN);
		return (null == val) ? defValue : (Boolean) val;
	}

	@Override
	public int getInt(int defValue) {
		Object val = getDataAssertType(FieldType.INTEGER);
		return (null == val) ? defValue : (Integer) val;
	}

	@Override
	public double getDouble(double defValue) {
		Object val = getDataAssertType(FieldType.DOUBLE);
		return (null == val) ? defValue : (Double) val;
	}

	@Override
	public MAFDate getDate() {
		return (MAFDate) getDataAssertType(FieldType.DATE);
	}

	@Override
	public MAFConnector getReference(String[] fields) {
		Object val = getDataAssertType(FieldType.REFERENCE);
		return (null == val) ? null : new MAFKernelConnector((MAFKernelAspect) val, fields);
	}

	@Override
	public String getCodeStr() {
		return (String) getDataAssertType(FieldType.VALUESET);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<? extends MAFVariant> getMembers() {
		switch (type) {
		case SET:
		case ARRAY:
			return ((Iterable<? extends MAFVariant>) data);
		}

		return null;
	}

	@Override
	public void setString(String val) {
		getDataAssertType(FieldType.STRING);
		data = val;
	}

	@Override
	public void setIdentifier(MAFIdentifier val) {
		getDataAssertType(FieldType.IDENTIFIER);
		data = val;
	}

	@Override
	public void setBool(boolean val) {
		getDataAssertType(FieldType.BOOLEAN);
		data = val;
	}

	@Override
	public void setInt(int val) {
		getDataAssertType(FieldType.INTEGER);
		data = val;
	}

	@Override
	public void setDouble(double val) {
		getDataAssertType(FieldType.DOUBLE);
		data = val;
	}

	@Override
	public void setDate(MAFDate val) {
		getDataAssertType(FieldType.DATE);
		data = val;
	}

	@Override
	public void setReference(MAFConnector val) {
		getDataAssertType(FieldType.REFERENCE);
		data = ((MAFKernelConnector) val).data;
	}

	@Override
	public void setCodeStr(String val) {
		getDataAssertType(FieldType.VALUESET);
		data = val;
	}

	@Override
	public void setData(MAFVariant from) {
		// TODO this is a shallow copy, bad for collections!
		this.data = ((MAFKernelVariant) from).data;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setData(Object value, VariantSetMode mode, String key) {
		switch (type) {
		case SET:
			if (null == data) {
				data = new HashSet<MAFKernelVariant>();
			}
			((Set<MAFKernelVariant>) data).add(new MAFKernelVariant(FieldType.REFERENCE, value));

			break;
		case ARRAY:
			if (null == data) {
				data = new ArrayList<MAFKernelVariant>();
			}
			((ArrayList<MAFKernelVariant>) data).add(new MAFKernelVariant(FieldType.REFERENCE, value));

			break;
		case REFERENCE:
			setReference((MAFConnector) value);
			break;
		default:
			this.data = value;
		}
	}
	
	void dump(MAFKernelDumper target) {
		switch (type) {
		case REFERENCE:
			target.dumpEntity(((MAFKernelAspect)data).entity);
			break;
		case ARRAY:
		case SET:
			Iterable<? extends MAFVariant> val = getMembers();
			target.put("[");
			target.endLine(Indent.inc);
			
			boolean add = false;
			
			for (MAFVariant var : val) {
				if ( add ) {
					target.put(",");
					target.endLine(Indent.keep);
				} else {
					add = true;
				}
				((MAFKernelVariant) var).dump(target);
			}
			target.endLine(Indent.dec);
			target.put("]");
			break;
		default:
			target.put("\"");
			target.put(data.toString());
			target.put("\"");
		}
	}
}
