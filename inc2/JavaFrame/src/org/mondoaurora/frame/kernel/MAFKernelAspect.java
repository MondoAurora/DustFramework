package org.mondoaurora.frame.kernel;

import java.util.Iterator;

import org.mondoaurora.frame.kernel.MAFKernelConsts.EntityState;
import org.mondoaurora.frame.shared.*;
import org.mondoaurora.frame.tools.MAFToolsVariantWrapper;

public class MAFKernelAspect {

	public static class Variant extends MAFToolsVariantWrapper implements Iterable<MAFVariant>, Iterator<MAFVariant> {
		String key;
		MAFKernelAspect aspect;
		int currItem;
		int count;

		public Variant(String key, MAFKernelAspect aspect) {
			this.key = key;
			currItem = 0;

			this.aspect = aspect;
			count = aspect.getVarCount();
		}

		Variant(String ref) {
			this(ref, new MAFKernelAspect(MAFKernelType.getType(ref)));
//			this(ref, new MAFKernelAspect(MAFKernelType.getType(MAFKernelIdentifier.fromString(ref).getType())));
		}
		
		public int select(String fieldName) {
			MAFKernelField fld = aspect.type.getField(fieldName);
			if ( null == fld ) {
				throw new MAFRuntimeException("MAFKernelAspect", "Unknown field '" + fieldName + "' referred in type '" + aspect.type.id);
			}
			
			System.out.println("Select field " + fieldName);
	
			currItem = fld.idx;
			
			return currItem;
		}
		
		public void setFromString(String value) {
			System.out.println("Setting value " + aspect.getField(currItem).getName() + " = " + value);

			MAFKernelField fld = aspect.getField(currItem);
			MAFVariant var = aspect.getVar(currItem);
			
			switch (fld.getType()) {
			case IDENTIFIER:
				var.setIdentifier(MAFKernelIdentifier.fromString(value));
				break;
			case BOOLEAN:
				var.setBool(Boolean.parseBoolean(value));
				break;
			case VALUESET:
				var.setCodeStr(value);
				break;
			case INTEGER:
				var.setInt(Integer.parseInt(value));
				break;
			case DOUBLE:
				var.setDouble(Double.parseDouble(value));
				break;
			case STRING:
				var.setString(value);
				break;
//			case DATE:
//				var.setBool(Boolean.parseBoolean(value));
//				break;
				default:
					throw new MAFRuntimeException("setFromString", "Field '" + fld.getName() + "' of type '" + fld.getType() + " cannot be set from string '" + value + "'");
			}			
		}

		public MAFVariant getVar() {
			return aspect.getVar(currItem);
		}

		public void setFromVariant(MAFKernelEntity.Variant ev) {
			if ( EntityState.NEW == ev.entity.state ) {
				aspect.getVar(currItem).setData(new MAFKernelConnector(ev.entity.primaryAspect), VariantSetMode.addLast, null);
			}
		}		

		@Override
		public String getKey() {
			return key;
		}

		@Override
		public Iterable<? extends MAFVariant> getMembers() {
			return this;
		}

		@Override
		public Iterator<MAFVariant> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return currItem < count;
		}

		@Override
		public MAFVariant next() {
			return aspect.getVar(currItem++);
		}

		@Override
		public void remove() {
			throw new MAFRuntimeException("MAFToolsVariantWrapper.Aspect", "remove() not supported!");
		}
	}

	MAFKernelEntity entity;

	MAFKernelType type;
	MAFKernelVariant[] content;

	MAFLogic logic;

	public MAFKernelAspect(MAFKernelType type) {
		this.type = type;

		content = new MAFKernelVariant[type.getFieldCount()];

		for (MAFKernelField fld : type.arrFields) {
			content[fld.idx] = new MAFKernelVariant.Aspect(fld);
		}
	}

	public MAFKernelAspect(MAFKernelType type, MAFKernelLogic logic) {
		this(type);

		this.logic = logic;
	}

	void dump(MAFKernelDumper target) {
		target.put("{");

		target.endLine(MAFStream.Indent.inc);

		boolean add = false;

		for (MAFKernelField fld : type.arrFields) {
			MAFKernelVariant o = content[fld.idx];

			if ((null != o) && !o.isNull()) {
				if (add) {
					target.put(",");
					target.endLine(MAFStream.Indent.keep);
				} else {
					add = true;
				}
				fld.dump(target, o);
			}
		}

		target.endLine(MAFStream.Indent.dec);

		target.put("}");
	}

	public int getVarCount() {
		return content.length;
	}

	public MAFKernelVariant getVar(int idx) {
		return content[idx];
	}

	public MAFKernelField getField(int idx) {
		return type.arrFields.get(idx);
	}
}
