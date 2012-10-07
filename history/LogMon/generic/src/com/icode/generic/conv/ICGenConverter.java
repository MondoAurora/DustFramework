/**
 * 
 */
package com.icode.generic.conv;

import java.util.Date;

import com.icode.generic.base.*;
import com.icode.generic.obj.ICGenObjTranslator;

public abstract class ICGenConverter implements ICGenConstants {
	public static final byte TYPE_LOCATION = 0;
	public static final byte TYPE_AGGREGATION = 1;
	
	private String[] fldInput;
	private String outName;

	private ICGenObjTranslator trIn;
	private ICGenObjTranslator trOut;
	
	public final byte type;

	protected ICGenConverter(byte type) {
		this.type = type;
	}

	public String[] getFldInput() {
		return fldInput;
	}

	protected String getOutName() {
		return outName;
	}

	protected ICGenConverter(byte type, int inputCount) {
		this(type);
		
		fldInput = new String[inputCount];
	}
	
	protected ICGenConverter(byte type, String name) {
		this(type);
		
		fldInput = new String[1];
		fldInput[0] = outName = name;
		
		init();
	}
	
	public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
		outName = node.getParent().getNameAtt(CFG_GEN_NAME);
		if ( 1 == fldInput.length ) {
			fldInput[0] = node.getOptional(CFG_GEN_FROM, outName);
		}
		
		init();
	}

	public abstract void convert(ICGenObject from, ICGenObject to);

	protected void init() {
		trIn = new ICGenObjTranslator(fldInput);
		trOut = new ICGenObjTranslator(new String[]{outName});
	}
	
	protected void setOut(ICGenObject to, Object value) {
		trOut.setAttByIdx(to, 0, value);
	}
	
	protected Object getOut(ICGenObject to) {
		return trOut.getAttByIdx(to, 0);
	}
	
	protected Object getIn(ICGenObject from, int idx) {
		return trIn.getAttByIdx(from, idx);
	}
	
	
	public static class LoaderCopy extends ICGenConverter implements ICGenConfigurable {
		public LoaderCopy() {
			super(TYPE_LOCATION, 1);
		}
		
		public LoaderCopy(String attName_) {
			super(TYPE_LOCATION, attName_);
			init();
		}
				
		public void convert(ICGenObject from, ICGenObject to) {
			setOut(to, getIn(from, 0));
		}
	}

	public static class TimeSegmenter extends ICGenConverter implements ICGenConfigurable {
		long segmentSizeMsec;
		
		public TimeSegmenter() {
			super(TYPE_LOCATION, 1);
		}
		
		public TimeSegmenter(String name, long segmentSizeSec) {
			super(TYPE_LOCATION, name);
			this.segmentSizeMsec = segmentSizeSec * 1000;
			init();
		}
		
		public void convert(ICGenObject from, ICGenObject to) {
			Date val = (Date) getIn(from, 0);
			long seg = ICGenUtilsBase.getTimeSegment(val.getTime(), segmentSizeMsec);
			setOut(to, new Long(seg));
		}

		public void loadDataFrom(ICGenTreeNode node, Object hint) throws Exception {
			super.loadDataFrom(node, hint);
			segmentSizeMsec = node.getOptionalLong(CFG_SEGMENT_SEC, 60) * 1000;
		}
	}

}