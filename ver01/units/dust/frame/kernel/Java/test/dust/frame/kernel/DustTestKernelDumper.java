package dust.frame.kernel;

import java.util.*;

import dust.frame.generic.IText;
import dust.frame.stream.*;
import dust.kernel.DustKernel;
import dust.shared.*;

public class DustTestKernelDumper implements DustConsts {
	Set<DustKernelEntity> setEntities = new HashSet<DustKernelEntity>();
	IIndenter indenter;
	IStreamWrite target;

	String msg;
	IText txt = new IText() {
		@Override
		public void send(Enum<?> msgId, DustObject msgOb, boolean wait, ResponseProcessor respProc) {
		}

		@Override
		public DustObject getNeighbor(DustIdentifier typeId) {
			return null;
		}

		@Override
		public void setText(String text) {
		}

		@Override
		public String getText() {
			return msg;
		}
	};

	IIndent.IndentMode im;
	int el;
	IIndent ii = new IIndent() {

		@Override
		public void send(Enum<?> msgId, DustObject msgOb, boolean wait, ResponseProcessor respProc) {
		}

		@Override
		public DustObject getNeighbor(DustIdentifier typeId) {
			return null;
		}

		@Override
		public void setIndentMode(IndentMode indentMode) {
		}

		@Override
		public void setEmptyLines(int emptyLines) {
		}

		@Override
		public IndentMode getIndentMode() {
			return im;
		}

		@Override
		public int getEmptyLines() {
			return el;
		}
	};

	void write(String msg) {
		this.msg = msg;
		DustUtils.send(target, IStreamWrite.Messages.write, txt);
	}

	void endLine(IIndent.IndentMode im) {
		endLine(im, 0);
	}

	void endLine(IIndent.IndentMode im, int el) {
		if (null != ii) {
			this.im = im;
			this.el = el;

			DustUtils.send(indenter, IIndenter.Messages.endLine, ii);
		} else {
			DustUtils.send(target, IStreamWrite.Messages.endLine);
		}
	}

	public DustTestKernelDumper(DustIdentifier id) {
		target = (IStreamWrite) DustKernel.Environment.getInstance(id);
		indenter = (IIndenter) target.getNeighbor(KIndenter.ID);
	}

	public void dumpObject(DustObject ob) {
		dumpEntity(((DustKernel.DataWrapper) ob).getEntity());
	}

	public void dumpEntity(DustKernel.Entity entity) {
		DustKernelEntity e = (DustKernelEntity) entity;
		write("{ \"!ref\" : \"");
		write(e.id.asReference());
		write("\" ");

		if (!setEntities.contains(e)) {
			setEntities.add(e);

			boolean add = false;

			for (Map.Entry<DustIdentifier, DustKernelData> eData : e.mapAspects.entrySet()) {
				write(",");
				if (add) {
					endLine(IIndent.IndentMode.keep);
				} else {
					endLine(IIndent.IndentMode.inc);
					add = true;
				}

				write("\"");
				write(eData.getKey().asReference());
				write("\" : ");
				dumpData(eData.getValue());
			}

			endLine(IIndent.IndentMode.dec);

		}
		write("}");
	}

	void dumpData(DustKernelData data) {
		write("{");

		endLine(IIndent.IndentMode.inc);

		boolean add = false;

		for (DustKernelField fld : data.type.arrFields) {
			DustKernelVariant o = data.content[fld.idx];

			if ((null != o) && !o.isNull()) {
				if (add) {
					write(",");
					endLine(IIndent.IndentMode.keep);
				} else {
					add = true;
				}

				dumpVariant(o);
			}
		}

		endLine(IIndent.IndentMode.dec);

		write("}");
	}

	void dumpVariant(DustKernel.Variant var) {
		write("\"");
		write(var.getName());
		write("\" : ");

		switch (var.getType()) {
		case REFERENCE:
			dumpEntity(((DustKernel.DataWrapper) var.getData()).getEntity());
			break;
		case ARRAY:
		case SET:
			write("[");
			endLine(IIndent.IndentMode.inc);

			boolean add = false;

			for (DustKernel.Variant var1 : var.getMembers()) {
				if (add) {
					write(",");
					endLine(IIndent.IndentMode.keep);
				} else {
					add = true;
				}
				dumpEntity(((DustKernel.DataWrapper) var1.getData()).getEntity());
			}
			endLine(IIndent.IndentMode.dec);
			write("]");
			break;
		default:
			write("\"");
			write(var.getData().toString());
			write("\"");
		}
	}
}
