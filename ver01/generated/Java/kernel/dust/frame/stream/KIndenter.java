package dust.frame.stream;

import dust.frame.generic.IText;
import dust.kernel.DustKernel;
import dust.shared.DustLogic.Return;
import dust.shared.DustLogic;
import dust.shared.DustObject;

public interface KIndenter extends DustKernel, Consts {
	public static final DustIdentifier ID = Environment.getTypeId(ID_VENDOR_ROOT, ID_DOMAIN_FRAME, ID_UNIT_STREAM,
			"Indenter");
	public static TypeInfo INFO = new TypeInfo(ID, false, new FieldInfo[] { new FieldInfo("indentLead",
			VariantType.STRING, "  "), }, new DustIdentifier[]{KStreamWrite.ID});

	public static class Data extends DustKernel.DataWrapper implements IIndenter {
		@Override
		public String getIndentLead() {
			return (String) getFieldVariant(0).getData();
		}

		@Override
		public void setIndentLead(String indentLead) {
			getFieldVariant(0).setData(indentLead, VariantSetMode.set, null);
		}

		@Override
		public IStreamWrite getStream() {
			return (IStreamWrite) getNeighbor(KStreamWrite.ID);
		}
	}

	public static class Logic extends DustKernel.LogicWrapper<SIndenter, Enum<?>> {
		@SuppressWarnings("unchecked")
		@Override
		public void init(DustObject ob) {
			((DustLogic<IIndenter, Enum<?>>)getLogic()).init((IIndenter)ob);
		}

		@Override
		public Return processMessage(Enum<?> msgId, DustObject msgOb, Object ctx) {
			SIndenter l = getLogic();

			if (msgId instanceof IStreamWrite.Messages) {
				IStreamWrite.Messages mId = (IStreamWrite.Messages) msgId;

				switch (mId) {
				case write:
					return l.write((IText) msgOb);
				case endLine:
					return l.endLine();
				case flush:
					return l.flush();
				case close:
					return l.close();
				}
			} else if (msgId instanceof IIndenter.Messages) {
				IIndenter.Messages mId = (IIndenter.Messages) msgId;

				switch (mId) {
				case endLine:
					return l.endLine((IIndent) msgOb);
				}
			}

			return null;
		}
	}
}
