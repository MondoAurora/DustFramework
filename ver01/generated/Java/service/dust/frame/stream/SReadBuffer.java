package dust.frame.stream;

import dust.shared.DustLogic.Return;

public interface SReadBuffer {
	Return markSet();
	Return markDrop(IIndent indent);
	Return markRollback(IIndent indent);	
}
