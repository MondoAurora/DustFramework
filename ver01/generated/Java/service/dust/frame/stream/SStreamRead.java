package dust.frame.stream;

import dust.shared.DustLogic.Return;

public interface SStreamRead {
	Return listen();
	Return readLine();
	Return readText();
}
