package dust.frame.stream;

import dust.shared.DustLogic.Return;

public interface SIndenter extends SStreamWrite {
	Return endLine(IIndent indent);	
}
