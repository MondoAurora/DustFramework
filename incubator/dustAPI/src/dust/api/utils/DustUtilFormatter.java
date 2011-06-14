package dust.api.utils;

import java.text.FieldPosition;
import java.text.MessageFormat;

public class DustUtilFormatter {
	public static class TemplateDef {
		public final String pattern;
		public final int valCount;
		
		public TemplateDef(String pattern, int valCount) {
			this.pattern = pattern;
			this.valCount = valCount;
		}		
		
		public TemplateDef(String pattern) {
			this(pattern, 1);
		}
		
		public DustUtilFormatter getFormatter() {
			return new DustUtilFormatter(this);
		}
	}
	
	MessageFormat fmt;
	Object[] values;
	
	FieldPosition fp = new FieldPosition(0);
	StringBuffer sb = new StringBuffer();
	
	public DustUtilFormatter(TemplateDef def) {
		this(def.pattern, def.valCount);
	}
	
	public DustUtilFormatter(String pattern, int valCount) {
		fmt = new MessageFormat(pattern);
		values = new Object[valCount];
	}
	
	public Object getValue(int idx) { 
		return values[idx];
	}
	
	public void setValue(int idx, Object value) { 
		values[idx] = value;
	}
	
	public String useFormat() {
		return useFormat(values);
	}
	
	public String useFormat(Object[] values) {
		fp.setBeginIndex(0);
		DustUtils.delSB(sb);

		fmt.format(values, sb, fp);

		return sb.toString();
	}
}
