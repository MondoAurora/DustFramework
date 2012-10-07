package com.icode.generic;

import java.util.Random;

public class MtxUtilPassCreator {
	public static class Criteria {
		public final String charsetFull;
		public final String charsetGen;
		public final String name;
		public final int count;

		public Criteria(String name, String charsetForGen, String charsNotInGen, int count) {
			this.name = name;
			this.charsetGen = charsetForGen;
			this.charsetFull = charsetForGen + charsNotInGen;
			this.count = count;
		}

		public Criteria(Criteria src, int count) {
			this.name = src.name;
			this.charsetGen = src.charsetGen;
			this.charsetFull = src.charsetFull;
			this.count = count;
		}
	}

	public static final String RESID_CHAR_LOW = "charLow";
	public static final String RESID_CHAR_UP = "charUp";
	public static final String RESID_CHAR_NUM = "charNum";
	public static final String RESID_CHAR_SPEC = "charSpec";
	public static final String RESID_TOOSHORT = "tooShort";
	public static final String RESID_CHARSETERR = "charsetErr";
	public static final String RESID_CHARSETERROPT = "charsetErrOpt";
	public static final String RESID_UNKCHRERR = "unkCharErr";
	public static final String RESID_ERRLEAD = "errorLead";

	/* small alpha, NO 'l' */
	public static final String PASS_CHAR_LOW = "abcdefghijkmnopqrstuvwxyz";
	public static final Criteria CRIT_ONE_LOWERCASE = new Criteria(RESID_CHAR_LOW,
			PASS_CHAR_LOW, "l", 1);

	/* big alpha, NO 'O' */
	public static final String PASS_CHAR_UP = "ABCDEFGHIJKLMNPQRSTUVWXYZ";
	public static final Criteria CRIT_ONE_UPPERCASE = new Criteria(RESID_CHAR_UP,
			PASS_CHAR_UP, "O", 1);

	/* numeric, NO '0' and '1' */
	public static final String PASS_CHAR_NUM = "23456789";
	public static final Criteria CRIT_ONE_NUMERIC = new Criteria(RESID_CHAR_NUM,
			PASS_CHAR_NUM, "01", 1);

	public static final String PASS_CHAR_SPEC = "%!+=?*-#&$._";
	public static final Criteria CRIT_ONE_SPECIAL = new Criteria(RESID_CHAR_SPEC,
			PASS_CHAR_SPEC, "", 0);

	private static final Criteria[] DEF_PASS_SET = new Criteria[] {
			CRIT_ONE_LOWERCASE, CRIT_ONE_UPPERCASE, CRIT_ONE_NUMERIC,
			CRIT_ONE_SPECIAL };

	public static final char NOCHAR = 0x255;

	Random rnd;

	int passMin;
	int passMax;
	Criteria[] criteria;
	String allChars;

	int setCount;
	int allCharCount;
	
	boolean denyUnknown;

	public MtxUtilPassCreator(int length) {
		this(length, length, DEF_PASS_SET, true);
	}

	public MtxUtilPassCreator(int passMin, int passMax, Criteria[] criteria, boolean denyUnknown) {
		rnd = new Random(System.currentTimeMillis());

		this.passMin = passMin;
		this.passMax = passMax;
		this.criteria = criteria;
		this.denyUnknown = denyUnknown;

		setCount = criteria.length;
		allCharCount = 0;

		for (int i = 0; i < setCount; ++i) {
			allCharCount += criteria[i].charsetGen.length();
			allChars = allChars + criteria[i].charsetGen;
		}
	}

	void setPassChar(StringBuffer sb, char ch, int pos) {
		int cPos = -1;
		int i = pos + 1;

		do {
			++cPos;
			if (NOCHAR == sb.charAt(cPos)) {
				--i;
			}
		} while (0 < i);

		sb.setCharAt(cPos, ch);
	}

	public String createPass() {
		int passLength = (passMin < passMax) ? passMin
				+ rnd.nextInt(passMax - passMin + 1) : passMin;
		StringBuffer pass = new StringBuffer(passLength);

		for (int i = 0; i < passLength; ++i) {
			pass.append(NOCHAR);
		}

		int toSet = passLength;
		char ch;

		for (int i = 0; i < criteria.length; ++i) {
			String set = criteria[i].charsetGen;
			for (int j = criteria[i].count; j-- > 0;) {
				ch = set.charAt(rnd.nextInt(set.length()));
				setPassChar(pass, ch, rnd.nextInt(toSet--));
			}
		}

		for (int i = passLength; i-- > 0;) {
			if (NOCHAR == pass.charAt(i)) {
				pass.setCharAt(i, allChars.charAt(rnd.nextInt(allCharCount)));
			}
		}

		return pass.toString();
	}

}
