package sandbox;

import sandbox.evaluator.DustEvaluator;
import sandbox.evaluator.DustEvaluatorField;
import sandbox.evaluator.DustEvaluatorField.AccessMode;
import sandbox.formatter.DustFormatter;
import sandbox.formatter.DustFormatterDefault;
import sandbox.stream.DustStream;
import sandbox.stream.DustStream.Indent;
import sandbox.stream.DustStreamWriter;
import sandbox.template.DustTemplate;
import sandbox.template.DustTemplateConstant;
import sandbox.template.DustTemplateEval;
import sandbox.template.DustTemplateOptional;
import sandbox.template.DustTemplateRepeat;
import sandbox.template.DustTemplateSequence;
import sandbox.template.DustTemplateWhitespace;
import dust.api.DustConstants;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;
import dust.api.components.DustWorld;
import dust.api.utils.DustUtils;
import dust.api.wrappers.DustIdentifier;
import dust.units.dust.common.v0_1.Common;
import dust.units.dust.kernel.v0_1.TypeManagement;

public class TestStreamExport extends Test.TestItem implements DustConstants {
	DustWorld world;
	
	DustDeclId idType;
	
	@Override
	public void test() throws Exception {
		world = DustUtils.getWorld();
		idType = world.getTypeId(TypeManagement.Type.class);
		
		DustDeclId idUnit = world.getTypeId(TypeManagement.Unit.class);
		DustDeclId idIdentified = world.getTypeId(Common.Identified.class);
		DustDeclId idNamed = world.getTypeId(Common.Named.class);
		DustDeclId idFldCnt = world.getTypeId(Common.FieldContainer.class);

		DustFormatter fmt = new DustFormatterDefault();
		DustEvaluator evalUnit = new DustEvaluatorField(idType, TypeManagement.Type.Fields.Unit);
		

		DustTemplate templFields = new DustTemplateSequence(new DustTemplate[]{
			new DustTemplateConstant("enum Fields implements FieldId {"),
			new DustTemplateWhitespace(Indent.inc),
			new DustTemplateRepeat(
				new DustEvaluatorField(idFldCnt, Common.FieldContainer.Fields.Fields),
				new DustTemplateEval(new DustEvaluatorField(idIdentified, Common.Identified.Fields.Identifier), fmt),
				new DustTemplateConstant(", ")
			),
			new DustTemplateWhitespace(Indent.dec),
			new DustTemplateConstant("};"),
			new DustTemplateWhitespace(Indent.keep, 1),
		});
		
		DustTemplate templShared = new DustTemplateSequence(new DustTemplate[]{
			new DustTemplateConstant("enum Shared implements SharedDef {"),
			new DustTemplateWhitespace(Indent.inc),
			new DustTemplateRepeat(
				new DustEvaluatorField(idType, TypeManagement.Type.Fields.Shared),
				new DustTemplateEval(new DustEvaluatorField(idIdentified, Common.Identified.Fields.Identifier), fmt),
				new DustTemplateConstant(", ")
			),
			new DustTemplateWhitespace(Indent.dec),
			new DustTemplateConstant("};"),
			new DustTemplateWhitespace(Indent.keep, 1),
		});
		
		DustTemplate templInterface = new DustTemplateSequence(new DustTemplate[]{
			new DustTemplateConstant("public interface "),
			new DustTemplateEval(new DustEvaluatorField(idNamed, Common.Named.Fields.Name), fmt),
			new DustTemplateConstant(" extends TypeDef {"),
			new DustTemplateWhitespace(Indent.inc),
			new DustTemplateOptional(new DustEvaluatorField(AccessMode.existence, null, idFldCnt, Common.FieldContainer.Fields.Fields), templFields),
			new DustTemplateOptional(new DustEvaluatorField(AccessMode.existence, null, idType, TypeManagement.Type.Fields.Shared), templShared),
			new DustTemplateWhitespace(Indent.dec),
			new DustTemplateConstant("}"),
		});
		
		DustTemplate templFile = new DustTemplateSequence(new DustTemplate[]{
			new DustTemplateConstant("package dust.units."),
			new DustTemplateEval(new DustEvaluatorField(evalUnit, idUnit, TypeManagement.Unit.Fields.Vendor), fmt),
			new DustTemplateConstant("."),
			new DustTemplateEval(new DustEvaluatorField(evalUnit, idUnit, TypeManagement.Unit.Fields.Domain), fmt),
			new DustTemplateConstant("."),
			new DustTemplateEval(new DustEvaluatorField(evalUnit, idUnit, TypeManagement.Unit.Fields.Version), fmt),
			new DustTemplateConstant("."),
			new DustTemplateEval(new DustEvaluatorField(evalUnit, idNamed, Common.Named.Fields.Name), fmt),
			new DustTemplateConstant(";"),
			new DustTemplateWhitespace(Indent.keep, 1),
			new DustTemplateConstant("import dust.api.DustDeclarationConstants;"),
			new DustTemplateWhitespace(Indent.keep, 1),
			templInterface
		});
		
		DustTemplate templSep = new DustTemplateWhitespace(Indent.keep, 3);
		
		DustStream ds = new DustStreamWriter();
		
		dumpType(idType.getIdentifier(), templFile, ds);
		
		templSep.writeInto(ds, null);
		
		dumpType(idUnit.getIdentifier(), templFile, ds);
		
		templSep.writeInto(ds, null);
		
		dumpType(idIdentified.getIdentifier(), templFile, ds);
		
	}
	
	void dumpType(DustIdentifier id, DustTemplate template, DustStream stream ) throws Exception {
		DustVariant[] knownFields;
		DustEntity e;
		
		knownFields = new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, id),
		};
		
		e = DustUtils.getEntity(idType, knownFields);

		template.writeInto(stream, e);
	}
}
