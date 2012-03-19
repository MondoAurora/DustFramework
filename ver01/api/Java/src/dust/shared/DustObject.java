package dust.shared;

public interface DustObject extends DustConsts {
	
	
	void send(Enum<?> msgId, DustObject msgOb, boolean wait, ResponseProcessor respProc);
}
