package org.mondoaurora.frame.tools;

import org.mondoaurora.frame.eval.MAFEval;
import org.mondoaurora.frame.kernel.MAFKernelAspect;
import org.mondoaurora.frame.kernel.MAFKernelConnector;
import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.kernel.MAFKernelIdentifier;
import org.mondoaurora.frame.kernel.MAFKernelVariant;
import org.mondoaurora.frame.shared.MAFConnector;
import org.mondoaurora.frame.shared.MAFStream;
import org.mondoaurora.frame.shared.MAFStream.Out;
import org.mondoaurora.frame.template.MAFTemplate;
import org.mondoaurora.frame.template.MAFTemplateConstant;
import org.mondoaurora.frame.template.MAFTemplateConsts;
import org.mondoaurora.frame.template.MAFTemplateEval;
import org.mondoaurora.frame.template.MAFTemplateRef;
import org.mondoaurora.frame.template.MAFTemplateRepeat;
import org.mondoaurora.frame.template.MAFTemplateSequence;
import org.mondoaurora.frame.template.MAFTemplateSwitch;
import org.mondoaurora.frame.template.MAFTemplateSyntax;
import org.mondoaurora.frame.template.MAFTemplateWhitespace;

public class MAFToolsJsonRelay implements MAFTemplateConsts {
	public static final String RULE_OBJECT = "object";
	public static final String RULE_MEMBERS = "members";
	public static final String RULE_ASSIGNMENT = "assignment";
	public static final String RULE_ARRAY = "array";
	public static final String RULE_ELEMENTS = "elements";
	public static final String RULE_VALUE = "value";

	// object : sequence( const("{"), ref("members"), const("}") )
	// members: repeat( ref("assignment"), ",")
	// assignment: sequence ( const("\""), eval(name), const("\""), const(":"),
	// ref("value") )
	// array: sequence( const("["), ref("elements"), const("]") )
	// elements: repeat( ref("value"), ",")
	// value: switch( ref("object"), ref("array"), eval(value))

	MAFTemplateSyntax syntax;
	
	private class RelayWriteEntity extends MAFKernelEntity {
		MAFKernelEntity wrappedEntity;
		MAFKernelAspect currentAspect;
	}
	
	private class RelayEval implements MAFEval {

		@Override
		public MAFKernelVariant getVariant(MAFKernelEntity currentEntity) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void writeContent(Out target, MAFKernelEntity currentEntity) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private class EvalMemberRepeat extends RelayEval {
		@Override
		public MAFKernelVariant getVariant(MAFKernelEntity currentEntity) {
			// TODO Auto-generated method stub
			return super.getVariant(currentEntity);
		}
	}
	private class EvalName extends RelayEval {	}
	private class EvalValueSwitch extends RelayEval {	}
	private class EvalValue extends RelayEval {	}

	public MAFToolsJsonRelay() {
		MAFTemplate tSpace = new MAFTemplateWhitespace(" ", MAFStream.Indent.keep, 0);
		MAFTemplate tLineFeed = new MAFTemplateWhitespace(" ", MAFStream.Indent.keep, 1);
		MAFTemplate tIndentInc = new MAFTemplateWhitespace(" ", MAFStream.Indent.inc, 1);
		MAFTemplate tIndentDec = new MAFTemplateWhitespace(" ", MAFStream.Indent.dec, 1);

		MAFTemplate tConstComma = new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant(","), tLineFeed });
		
		
		new MAFTemplateConstant(",");
		
		syntax = new MAFTemplateSyntax(RULE_OBJECT, new Initer[] {
				new Initer(RULE_OBJECT, new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant("{"), tIndentInc,
						new MAFTemplateRef(RULE_MEMBERS), tIndentDec, new MAFTemplateConstant("}"), })),
						
				new Initer(RULE_MEMBERS, new MAFTemplateRepeat(new EvalMemberRepeat(), new MAFTemplateRef(RULE_ASSIGNMENT), tConstComma)),
						
				new Initer(RULE_ASSIGNMENT, new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant("\""),
						new MAFTemplateEval(new EvalName()), new MAFTemplateConstant("\""), tSpace, new MAFTemplateConstant(":"), tSpace, new MAFTemplateRef(RULE_VALUE), })),
						
				new Initer(RULE_ARRAY, new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant("["), tIndentInc,
						new MAFTemplateRef(RULE_ELEMENTS), tIndentDec, new MAFTemplateConstant("]"), })),
						
				new Initer(RULE_ELEMENTS, new MAFTemplateRepeat(new EvalMemberRepeat(), new MAFTemplateRef(RULE_VALUE), tConstComma)),
						
				new Initer(RULE_VALUE, new MAFTemplateSwitch(new EvalValueSwitch(), new Initer[] {
					new Initer(RULE_OBJECT, new MAFTemplateRef(RULE_OBJECT)),
					new Initer(RULE_ARRAY, new MAFTemplateRef(RULE_ARRAY)),
					new Initer(RULE_VALUE, new MAFTemplateEval(new EvalValue())),
				})), 
		});
	}
	
	public void write(MAFStream.Out stream, MAFConnector conn) {
		syntax.write(((MAFKernelConnector)conn).getEntity(), stream);
	}
}
