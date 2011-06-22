package sandbox;

import java.io.*;

import dust.api.DustConstants;
import dust.api.components.DustEntity;
import dust.api.components.DustWorld;
import dust.api.utils.DustUtils;

import dust.units.dust.common.v0_1.Common;
import dust.units.dust.kernel.v0_1.TypeManagement;

import sandbox.evaluator.*;
import sandbox.evaluator.DustEvaluatorField.AccessMode;
import sandbox.formatter.DustFormatter;
import sandbox.formatter.DustFormatterDefault;
import sandbox.stream.*;
import sandbox.stream.DustStream.Indent;
import sandbox.template.*;

public class TestStreamExport extends Test.TestItem implements DustConstants {
	DustWorld world;
	
	DustDeclId idType;
	DustDeclId idIdentified;
	
	DustTemplate templPath;
	
	DustTemplate templFileName;
	DustTemplate templSource;
	
	String pathRoot = "generated";
	
	int expCount;
	
	InvokeResponseProcessor irProc = new InvokeResponseProcessor() {
		@Override
		public void searchStarted() {
			expCount = 0;
			System.out.println("Start searching types");
		}
		
		@Override
		public void searchFinished() {
			System.out.println("Export finished successfully, generated " + expCount + " sources.");
		}
		
		@Override
		public boolean entityFound(DustEntity entity) {
			System.out.println("Exporting " + entity.getAspect(idIdentified, false).getField(Common.Identified.Fields.Identifier).getValueIdentifier());
			try {
				dumpEntity(entity);
				++expCount;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return true;
		}
	};

	
	@Override
	public void test() throws Exception {
		world = DustUtils.getWorld();
		idIdentified = world.getTypeId(Common.Identified.class);
		idType = world.getTypeId(TypeManagement.Type.class);
		
		DustDeclId idUnit = world.getTypeId(TypeManagement.Unit.class);
		DustDeclId idField = world.getTypeId(TypeManagement.Field.class);
		DustDeclId idNamed = world.getTypeId(Common.Named.class);
		DustDeclId idFldCnt = world.getTypeId(Common.FieldContainer.class);

		DustFormatter fmt = new DustFormatterDefault();
		DustEvaluator evalUnit = new DustEvaluatorField(idType, TypeManagement.Type.Fields.Unit);

		DustEvaluator evalVal = new DustEvaluatorField(AccessMode.existence, idField, TypeManagement.Field.Fields.Values);
		

		DustTemplate templValues = new DustTemplateOptional(
			evalVal, 
			new DustTemplateSequence(new DustTemplate[]{
				new DustTemplateConstant("enum "),
				new DustTemplateEval(new DustEvaluatorField(idIdentified, Common.Identified.Fields.Identifier), fmt),
				new DustTemplateConstant(" {"),
				new DustTemplateWhitespace(Indent.inc),
				new DustTemplateRepeat(
					new DustEvaluatorField(idField, TypeManagement.Field.Fields.Values),
					new DustTemplateEval(new DustEvaluatorField(idIdentified, Common.Identified.Fields.Identifier), fmt),
					new DustTemplateConstant(", ")
				),
				new DustTemplateWhitespace(Indent.dec),
				new DustTemplateConstant("};"),
				new DustTemplateWhitespace(Indent.keep, 1),
			})
		);


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
			
			new DustTemplateOptional(
				new DustEvaluatorField(AccessMode.childExistence, null, evalVal, idFldCnt, Common.FieldContainer.Fields.Fields), 
				new DustTemplateSequence(new DustTemplate[]{
					new DustTemplateConstant("public static interface Values {"),
					new DustTemplateWhitespace(Indent.inc),
					new DustTemplateRepeat(
						new DustEvaluatorField(idFldCnt, Common.FieldContainer.Fields.Fields),
						templValues,
						null
					),
					new DustTemplateWhitespace(Indent.dec),
					new DustTemplateConstant("};"),
					new DustTemplateWhitespace(Indent.keep, 1),
				})
			),			
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
		});
		
		DustTemplate templInterface = new DustTemplateSequence(new DustTemplate[]{
			new DustTemplateConstant("public interface "),
			new DustTemplateEval(new DustEvaluatorField(idNamed, Common.Named.Fields.Name), fmt),
			new DustTemplateConstant(" extends TypeDef {"),
			new DustTemplateWhitespace(Indent.inc),
			new DustTemplateOptional(new DustEvaluatorField(AccessMode.existence, idFldCnt, Common.FieldContainer.Fields.Fields), templFields),
			new DustTemplateOptional(new DustEvaluatorField(AccessMode.existence, idType, TypeManagement.Type.Fields.Shared), templShared),
			new DustTemplateWhitespace(Indent.dec),
			new DustTemplateConstant("}"),
		});
		
		templPath = new DustTemplateSequence(new DustTemplate[]{
			new DustTemplateConstant("dust_units."),
			new DustTemplateEval(new DustEvaluatorField(evalUnit, idUnit, TypeManagement.Unit.Fields.Vendor), fmt),
			new DustTemplateConstant("."),
			new DustTemplateEval(new DustEvaluatorField(evalUnit, idUnit, TypeManagement.Unit.Fields.Domain), fmt),
			new DustTemplateConstant("."),
			new DustTemplateEval(new DustEvaluatorField(evalUnit, idUnit, TypeManagement.Unit.Fields.Version), fmt),
			new DustTemplateConstant("."),
			new DustTemplateEval(new DustEvaluatorField(evalUnit, idNamed, Common.Named.Fields.Name), fmt),
		});
		
		templSource = new DustTemplateSequence(new DustTemplate[]{
			new DustTemplateConstant("package "),
			templPath,
			new DustTemplateConstant(";"),
			new DustTemplateWhitespace(Indent.keep, 1),
			new DustTemplateConstant("import dust.api.DustDeclarationConstants.TypeDef;"),
			new DustTemplateWhitespace(Indent.keep, 1),
			templInterface
		});
		
		templFileName = new DustTemplateSequence(new DustTemplate[]{
			new DustTemplateConstant(pathRoot),
			new DustTemplateConstant("."),
			templPath,
			new DustTemplateConstant("."),
			new DustTemplateEval(new DustEvaluatorField(idNamed, Common.Named.Fields.Name), fmt),
		});
				
		world.invoke(irProc , idType, null, false, null, null);		
	}
	
	File getFile(String name) throws Exception {
		File f = new File(name);
		
		if ( !f.exists() ) {
			char c = File.separatorChar;
			String pName = name.substring(0, name.lastIndexOf(c));
			new File(pName).mkdirs();
			f.createNewFile();
		}
		
		return f;
	}
		
	void dumpEntity(DustEntity e) throws Exception {		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bos);
		DustStream ds = new DustStreamWriter(ps, null);
	
		templFileName.writeInto(ds, e);
		
		ps.flush();
		ps.close();
		
		StringBuilder sb = new StringBuilder(bos.toString().replace('.', File.separatorChar));
		sb.append(".java");
		
		ps = new PrintStream(getFile(sb.toString()));
		ds = new DustStreamWriter(ps, "  ");
		
		templSource.writeInto(ds, e);
		
		ps.flush();
		ps.close();
	}
}
