package dust.frame.stream;

import dust.frame.generic.IText;
import dust.frame.stream.IStreamWrite.Messages;
import dust.kernel.DustKernel;
import dust.shared.DustLogic.Return;
import dust.shared.DustLogic;
import dust.shared.DustObject;

public interface KStreamWrite extends DustKernel, Consts {
	public static final DustIdentifier ID = Environment.getTypeId(ID_VENDOR_ROOT, ID_DOMAIN_FRAME, ID_UNIT_STREAM, "StreamWrite");
	public static TypeInfo INFO = new TypeInfo(ID, true, null, null);

	public static class Data extends DustKernel.DataWrapper implements IStreamWrite {

	}

	
	public static class Logic extends DustKernel.LogicWrapper<SStreamWrite, IStreamWrite.Messages> {
		@SuppressWarnings("unchecked")
		@Override
		public void init(DustObject ob) {
			((DustLogic<IStreamWrite, Enum<?>>)getLogic()).init((IStreamWrite)ob);
		}

		@Override
		public Return processMessage(Messages msgId, DustObject msgOb, Object ctx) {
			SStreamWrite l = getLogic();
			
			switch ( msgId ) {
			case write:
				return l.write((IText)msgOb);
			case endLine:
				return l.endLine();
			case flush:
				return l.flush();
			case close:
				return l.close();
			}
			
			return null;
		}


	}

}
