package dust.frame.kernel;

import java.util.*;

import dust.kernel.DustKernel;
import dust.shared.DustConsts;
import dust.shared.DustObject;

public class DustTestKernelDumper implements DustConsts {
	Set<DustKernelEntity> setEntities = new HashSet<DustKernelEntity>();
	DustStream.Out target;

	public DustTestKernelDumper(DustStream.Out target) {
		this.target = target;
	}
		
	public void dumpObject(DustObject ob) {
		dumpEntity(((DustKernel.DataWrapper)ob).getEntity());
	}

	public void dumpEntity(DustKernel.Entity entity) {
		DustKernelEntity e = (DustKernelEntity) entity;
		target.put("{ \"!ref\" : \"");
		target.put(e.id.asReference());
		target.put("\" ");

		if ( !setEntities.contains(e) ) {
			setEntities.add(e);
			
			boolean add = false;

			for (Map.Entry<DustIdentifier, DustKernelData> eData : e.mapAspects.entrySet()) {
				target.put(",");
				if ( add ) {
					target.endLine(Indent.keep);
				} else {
					target.endLine(Indent.inc);
					add = true;
				}
				
				target.put("\"");
				target.put(eData.getKey().asReference());
				target.put("\" : ");
				dumpData(eData.getValue());
			}
			
			target.endLine(Indent.dec);
		
		}
		target.put("}");
	}
	
	void dumpData(DustKernelData data) {
		target.put("{");

		target.endLine(Indent.inc);

		boolean add = false;

		for (DustKernelField fld : data.type.arrFields) {
			DustKernelVariant o = data.content[fld.idx];

			if ((null != o) && !o.isNull()) {
				if (add) {
					target.put(",");
					target.endLine(Indent.keep);
				} else {
					add = true;
				}
				
				dumpVariant(o);
			}
		}

		target.endLine(Indent.dec);

		target.put("}");
	}

	void dumpVariant(DustKernel.Variant var) {
		target.put("\"");
		target.put(var.getName());
		target.put("\" : ");

		switch (var.getType()) {
		case REFERENCE:
			dumpEntity(((DustKernel.DataWrapper)var.getData()).getEntity());
			break;
		case ARRAY:
		case SET:
			target.put("[");
			target.endLine(Indent.inc);
			
			boolean add = false;
			
			for (DustKernel.Variant var1 : var.getMembers()) {
				if ( add ) {
					target.put(",");
					target.endLine(Indent.keep);
				} else {
					add = true;
				}
				dumpEntity(((DustKernel.DataWrapper)var1.getData()).getEntity());
			}
			target.endLine(Indent.dec);
			target.put("]");
			break;
		default:
			target.put("\"");
			target.put(var.getData().toString());
			target.put("\"");
		}
	}
}
