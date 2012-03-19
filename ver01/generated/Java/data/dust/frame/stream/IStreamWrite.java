package dust.frame.stream;

import dust.shared.DustObject;

public interface IStreamWrite extends DustObject {
	public enum Messages {
		write, endLine, flush, close
	};
}
