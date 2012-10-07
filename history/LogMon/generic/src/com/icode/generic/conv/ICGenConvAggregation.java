package com.icode.generic.conv;

import java.util.HashSet;
import java.util.Set;

import com.icode.generic.base.*;
import com.icode.generic.obj.ICGenObjTranslator;

public abstract class ICGenConvAggregation extends ICGenConverter  implements ICGenConfigurable, ICGenConstants {
	
	protected ICGenConvAggregation() {
		super(TYPE_AGGREGATION);
	}

	protected ICGenConvAggregation(int fldCount) {
		super(TYPE_AGGREGATION, fldCount);
	}

	private static final Long ZERO = new Long(0);
	private static final Long ONE = new Long(1);
	
	private ICGenObjTranslator trCombineFrom;
	
	public abstract Object combine(Object valIn, Object valOut);

	protected void init() {
		super.init();
		trCombineFrom = new ICGenObjTranslator(new String[]{getOutName()});
	}

	public final void combine(ICGenObject cell, ICGenObject into) {
		Object valOut = getOut(into);
		Object valIn = trCombineFrom.getAttByIdx(cell, 0);
		
		valOut = combine(valIn, valOut);
		setOut(into, valOut);
	}

	public Number addNumber(Number l1, Number l2) {
		return (null == l1) ? ((null == l2) ? ZERO : l2) : ((null == l2) ? l1 : new Long(l1.longValue() + l2.longValue()));
	}

	public Number compareAndSelect(Number valIn, Number valOut, boolean smaller) {
		if ((null == valIn) || ((null != valOut) && (smaller ^ (valIn.longValue() < valOut.longValue())))) {
			return valOut;
		}

		return valIn;
	}

	public static abstract class SingleAtt extends ICGenConvAggregation {
		public SingleAtt() {
			super(1);
		}
	}

	public static class Count extends ICGenConvAggregation implements ICGenConfigurable {
		public Count() {
			super(0);
		}

		public void convert(ICGenObject from, ICGenObject to) {
			Long l = (Long) getOut(to);
			setOut(to, (null == l) ? ONE : new Long(l.longValue() + 1));
		}

		public Object combine(Object valIn, Object valOut) {
			return addNumber((Number)valIn, (Number)valOut);
		}
	}

	public static class Sum extends SingleAtt {
		public void convert(ICGenObject from, ICGenObject to) {
			Number val = (Number) getIn(from, 0);

			Long l = (Long) getOut(to);
			setOut(to, (null == l) ? ((null == val) ? ZERO : new Long(val.longValue())) : new Long(val.longValue() + l.longValue()));
		}

		public Object combine(Object valIn, Object valOut) {
			return addNumber((Number)valIn, (Number)valOut);
		}
	}

	public static abstract class Cmp extends SingleAtt {
		private final boolean smaller;

		public Cmp(boolean smaller) {
			super();
			this.smaller = smaller;
		}

		public void convert(ICGenObject from, ICGenObject to) {
			Number valIn = (Number) getIn(from, 0);
			Number valOut = (Number) getOut(to);			
			setOut(to, compareAndSelect(valIn, valOut, smaller));
		}

		public Object combine(Object valIn, Object valOut) {
			return compareAndSelect((Number) valIn, (Number) valOut, smaller);
		}

	}

	public static class Min extends Cmp {
		public Min() {
			super(true);
		}
	}

	public static class Max extends Cmp {
		public Max() {
			super(false);
		}
	}

	public static class CollectSet extends SingleAtt {
		public void convert(ICGenObject from, ICGenObject to) {
			Object val = getIn(from, 0);

			if (null != val) {
				Set s = (Set) getOut(to);

				if (null == s) {
					s = new HashSet();
					setOut(to, s);
				}
				s.add(val);
			}
		}

		public Object combine(Object valIn, Object valOut) {
			Set s1 = (Set) valIn;
			Set s2 = (Set) valOut;
			
			if ( null != s1 ) {
				if ( null == s2 ) {
					s2 = new HashSet();
				}				
				s2.addAll(s1);
			}
			
			return s2;
		}
	}

}
