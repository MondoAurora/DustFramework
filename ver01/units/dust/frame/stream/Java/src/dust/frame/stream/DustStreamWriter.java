package dust.frame.stream;

import java.io.PrintStream;

import dust.frame.generic.IText;
import dust.shared.*;

public class DustStreamWriter extends DustLogic.Simple<IStreamWrite> implements SStreamWrite {
	PrintStream targetStream;
	boolean shared;

	@Override
	public void init(IStreamWrite config) {
		targetStream = System.out;
		shared = true;
	}

	@Override
	public Return write(IText text) {
		targetStream.print(text.getText());
		
		return SUCCESS;
	}

	@Override
	public Return endLine() {
		targetStream.println();
		
		return SUCCESS;
	}

	@Override
	public Return flush() {
		targetStream.flush();
		
		return SUCCESS;
	}

	@Override
	public Return close() {
		targetStream.flush();
		
		if ( !shared ) {
			targetStream.close();
		}
		
		return SUCCESS;
	}
}
