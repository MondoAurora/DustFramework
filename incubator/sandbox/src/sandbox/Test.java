package sandbox;

import dust.api.DustConstants;
import dust.api.boot.DustBootAPI;
import dust.api.components.DustAPI;
import dust.api.components.DustVariant;
import dust.api.components.DustVariantStructure;
import dust.api.utils.DustUtils;

import dust.units.dust.common.v0_1.Common.Identified;
import dust.units.dust.kernel.v0_1.TypeManagement.Type;

public class Test implements DustConstants {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DustAPI api = new DustBootAPI();
		
		DustUtils.setApi(api, true);
		
		DustDeclId idType = api.getTypeId(Type.class);
		
		DustVariantStructure vs = api.getVarStruct(idType, new DustVariant[] {
			api.getVar(Identified.Fields.Identifier, FieldType.String, "Ahoy!"),
			api.getVar(Type.Fields.AutoInit, FieldType.Boolean, false),
		});
		
		System.out.print(vs.getField(Identified.Fields.Identifier).getValueString());
	}
}
