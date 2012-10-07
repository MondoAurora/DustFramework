package com.icode.generic.filter;

import com.icode.generic.base.*;

public interface ICGenFilterConstants extends ICGenConstants {
	byte OP_INVALID = -1;

	String OP_EQU = "==";  
	String OP_NEQU = "!=";  
	String OP_LT = "<";  
	String OP_LTE = "<=";  
	String OP_GT = ">";  
	String OP_GTE = ">=";  
	
	String[] CMP_OPS = new String[]{OP_EQU, OP_NEQU, OP_LT, OP_LTE, OP_GT, OP_GTE };
	byte CMP_OP_EQU = 0;
	byte CMP_OP_NEQU = 1;
	byte CMP_OP_LT = 2;
	byte CMP_OP_LTE = 3;
	byte CMP_OP_GT = 4;
	byte CMP_OP_GTE = 5;
	
	
	String OP_OR = "or";  
	String OP_AND = "and";  
	
	String[] COMBINE_OPS = new String[]{OP_OR, OP_AND };
	byte COMBINE_OP_OR = 0;
	byte COMBINE_OP_AND = 1;

	
	String CFG_FILTER_OPTS = "filterOpts";
	
	String CFG_NOT = "not";
	String CFG_OP = "op";
	String CFG_LVALUE = "lvalue";
	String CFG_RVALUE = "rvalue";

	String CFG_MEMBERS = "members";

	String[] FILTER_BASE_NAMES = new String[] {CFG_OP, CFG_NOT};
	int BASE_OP = 0;
	int BASE_NOT = 1;

	String[] FILTER_2OP_NAMES = new String[] {CFG_LVALUE, CFG_RVALUE, CFG_GEN_TYPE};
	int OP2_LVALUE = 0;
	int OP2_RVALUE = 1;
	int OP2_TYPE = 2;

	ICGenObject.ObDef OBDEF_FILTER_BASE = new ICGenObjectDefDefault("FilterBase", 1, FILTER_BASE_NAMES);
	ICGenObject.ObDef OBDEF_FILTER_2OP = new ICGenObjectDefDefault(OBDEF_FILTER_BASE, "Filter2Op", 1, FILTER_2OP_NAMES);
	ICGenObject.ObDef OBDEF_FILTER_COMBINE = new ICGenObjectDefDefault(OBDEF_FILTER_BASE, "FilterCombine", 1, ICGenUtilsBase.EMPTYARR);
}
