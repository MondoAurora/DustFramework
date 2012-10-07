package com.icode.generic;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.ResourceBundle;

import com.icode.generic.base.ICGenUtilsBase;

public class ICGenUtils extends ICGenUtilsBase {

	public static final Locale localeFromString(String str) {
		String[] ls = str2arr(str, '_');

		switch (ls.length) {
		case 3:
			return new Locale(ls[0], ls[1], ls[2]);
		case 2:
			return new Locale(ls[0], ls[1]);
		case 1:
			return new Locale(ls[0]);
		default:
			return Locale.getDefault();
		}
	}

	public static final String getResValue(ResourceBundle bundle, String key, String defValue) {
		try {
			return bundle.getString(key);
		} catch (Exception ex) {

		}
		return defValue;
	}

	static final String UNEXPECTED_EOL = "Unexpected end of line";

	public static final void skipSeparators(String line, char separator, ParsePosition pos) throws ParseException {
		int idx = pos.getIndex();
		if (idx >= line.length()) {
			pos.setErrorIndex(line.length());
			throw new ParseException(UNEXPECTED_EOL, line.length());
		}
		for (; idx < line.length(); ++idx) {
			if (line.charAt(idx) != separator) {
				pos.setIndex(idx);
				return;
			}
		}
	}

	public static final String getWord(String line, char separator, ParsePosition pos) throws ParseException {
		skipSeparators(line, separator, pos);

		StringBuffer word = new StringBuffer();
		int from = pos.getIndex();
		int to = from;

		while (from < line.length() && line.charAt(from) != separator) {
			if (line.charAt(from) == '"') {
				++from;
				to = line.indexOf('"', from);
				if (to > 0) {
					word.append(line.substring(from, to));
					++to;
				} else {
					word.append(line.substring(from - 1));
					to = line.length();
				}
			} else {
				to = line.indexOf(separator, from);
				if (to > 0) {
					word.append(line.substring(from, to));
				} else {
					word.append(line.substring(from));
					to = line.length();
				}
			}
			from = to;
		}

		pos.setIndex(to);

		return word.toString();
	}

	public static final boolean isWindows() {
		String os = System.getProperty("os.name");
		return (null != os) && (-1 != os.indexOf("Windows"));
	}

	public static final String getShell() {
		return isWindows() ? "cmd" : "bash";
	}
}
