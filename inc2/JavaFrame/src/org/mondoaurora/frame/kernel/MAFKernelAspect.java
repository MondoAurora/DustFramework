package org.mondoaurora.frame.kernel;

import java.util.Iterator;

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
			this.aspect = aspect;
			count = aspect.getVarCount();
	
			currItem = 0;
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
				if ( add ) {
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
