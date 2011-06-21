package sandbox;

import sandbox.evaluator.DustEvaluator;
import sandbox.evaluator.DustEvaluatorField;
import sandbox.formatter.DustFormatter;
import sandbox.formatter.DustFormatterDefault;
import sandbox.stream.DustStream.Indent;
import sandbox.template.DustTemplate;
import sandbox.template.DustTemplateConstant;
import sandbox.template.DustTemplateEval;
import sandbox.template.DustTemplateSequence;
import sandbox.template.DustTemplateWhitespace;
import dust.api.DustConstants;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;
import dust.api.components.DustWorld;
import dust.api.utils.DustUtils;
import dust.units.dust.common.v0_1.Common;
import dust.units.dust.kernel.v0_1.TypeManagement;

public class TestStreamExport extends Test.TestItem implements DustConstants {
	@Override
	public void test() throws Exception {
		DustWorld world = DustUtils.getWorld();
		
		DustDeclId idUnit = world.getTypeId(TypeManagement.Unit.class);
		DustDeclId idType = world.getTypeId(TypeManagement.Type.class);
		DustDeclId idField = world.getTypeId(TypeManagement.Field.class);

		DustVariant[] knownFields = new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, idField.getIdentifier()),
		};
		
		DustEntity e = DustUtils.getEntity(idType, knownFields);

//		package dust.units.dust.kernel.v0_1;
//
//		import dust.api.DustDeclarationConstants;
//

		DustFormatter fmt = new DustFormatterDefault();
		DustEvaluator evalUnit = new DustEvaluatorField(idType, TypeManagement.Type.Fields.Unit);
		
		DustTemplate t = new DustTemplateSequence(new DustTemplate[]{
			new DustTemplateConstant("package dust.units."),
			new DustTemplateEval(new DustEvaluatorField(evalUnit, idUnit, TypeManagement.Unit.Fields.Vendor), fmt),
			new DustTemplateConstant("."),
			new DustTemplateEval(new DustEvaluatorField(evalUnit, idUnit, TypeManagement.Unit.Fields.Domain), fmt),
			new DustTemplateConstant("."),
			new DustTemplateEval(new DustEvaluatorField(evalUnit, idUnit, TypeManagement.Unit.Fields.Version), fmt),
			new DustTemplateConstant(";"),
			new DustTemplateWhitespace(Indent.keep, 2),
			new DustTemplateConstant("import dust.api.DustDeclarationConstants;"),
			new DustTemplateWhitespace(Indent.keep, 2),
		});
		
		
	}
}
