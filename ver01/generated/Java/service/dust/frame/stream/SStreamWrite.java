package dust.frame.stream;

import dust.frame.generic.IText;
import dust.shared.DustLogic.Return;

public interface SStreamWrite {
	Return write(IText text);
	Return endLine();
	
	Return flush();
	Return close();
}
