package sandbox;

import dust.api.DustConstants;
import dust.api.components.DustVariantStructure;

public class Test implements DustConstants {

	static class P implements TypeDef {
		enum Fields implements FieldId {
			Title
		};

	}
	/**
	 * @param args
	 */
	@SuppressWarnings({ "null", "unused" })
	public static void main(String[] args) {
		DustVariantStructure vs = null;
		
		Object o = vs.getField(P.Fields.Title);
	}
}
