package com.icode.generic.base;


public interface ICGenDataManageable extends ICGenConfigurable {
	void storeDataInto(ICGenTreeNode node, Object hint);
}
