package dust.shared;


public interface DustObject extends DustConsts {
	
	DustObject getNeighbor(DustIdentifier typeId);

	void send(Enum<?> msgId, DustObject msgOb, boolean wait, ResponseProcessor respProc);
}
