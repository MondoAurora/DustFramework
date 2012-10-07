package com.icode.generic.filter;

import com.icode.generic.base.ICGenObject;
import com.icode.generic.base.ICGenUtilsBase;

public class ICGenFilterCompare extends ICGenFilterBase {
	public ICGenFilterCompare() {
		super(FILTER_2OP_NAMES);
	}

	protected byte resolveOp(String opString) {
		return (byte) ICGenUtilsBase.indexOf(CMP_OPS, opString);
	}

	public boolean isMatchingInt(ICGenObject object) {
		boolean ret = false;

		Comparable lVal = (Comparable) getCtxValue(object, OP2_LVALUE);
		Comparable rVal = (Comparable) getCtxValue(object, OP2_RVALUE);

		switch (getOpCode()) {
		case CMP_OP_EQU:
			ret = ICGenUtilsBase.isEqual(lVal, rVal);
			break;
		case CMP_OP_NEQU:
			ret = !ICGenUtilsBase.isEqual(lVal, rVal);
			break;
		case CMP_OP_LT:
			ret = (null == lVal) ? (null == rVal) ? false : true : (null == rVal) ? false : (0 > lVal.compareTo(rVal));
			break;
		case CMP_OP_LTE:
			ret = (null == lVal) ? true : (null == rVal) ? false : (0 >= lVal.compareTo(rVal));
			break;
		case CMP_OP_GT:
			ret = (null == lVal) ? false : (null == rVal) ? true : (0 < lVal.compareTo(rVal));
			break;
		case CMP_OP_GTE:
			ret = (null == lVal) ? (null == rVal) ? true : false : (null == rVal) ? true : (0 <= lVal.compareTo(rVal));
		}
		return ret;
	}

}
