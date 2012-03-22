package dust.frame.stream;

import dust.frame.generic.IText;
import dust.frame.stream.IStreamWrite.Messages;
import dust.shared.*;

public class DustStreamIndenter extends DustLogic.Simple<IIndenter> implements SIndenter {
	StringBuilder sbIndent = new StringBuilder();
	String indentLead;
	boolean emptyLine;
	
	IStreamWrite stream;
	
	IText txt = new IText() {
		@Override
		public void setText(String text) {
			throw new DustRuntimeException("DustStreamIndenter", "Temp text can't accept setText");
		}
		
		@Override
		public String getText() {
			return sbIndent.toString();
		}
		
		@Override
		public void send(Enum<?> msgId, DustObject msgOb, boolean wait, ResponseProcessor respProc) {
		}
		
		@Override
		public DustObject getNeighbor(DustIdentifier typeId) {
			// TODO Auto-generated method stub
			return null;
		}
	};

	@Override
	public void init(IIndenter config) {
		indentLead = config.getIndentLead();
		emptyLine = true;
		
		stream = config.getStream();
	}

	@Override
	public Return endLine(IIndent indent) {
		emptyLine = true;

		switch ( indent.getIndentMode() ) {
		case dec:
			sbIndent.delete(0, indentLead.length());
			break;
		case inc:
			sbIndent.append(indentLead);
			break;
		}
		
		DustUtils.send(stream, Messages.endLine);
		
		return SUCCESS;
	}

	@Override
	public Return write(IText text) {
		if ( emptyLine ) {
			emptyLine = false;
			if ( 0 < sbIndent.length() ) {
				DustUtils.send(stream, Messages.write, txt);
			}
		}
		
		return PASS_DEFAULT;
	}

	@Override
	public Return endLine() {
		emptyLine = true;
		return PASS_DEFAULT;
	}

	@Override
	public Return flush() {
		return PASS_DEFAULT;
	}

	@Override
	public Return close() {
		return PASS_DEFAULT;
	}
}
