package org.mondoaurora.frame.tools;

import org.mondoaurora.frame.eval.MAFEval;
import org.mondoaurora.frame.kernel.MAFKernelEntity;
import org.mondoaurora.frame.kernel.MAFKernelVariant;
import org.mondoaurora.frame.shared.MAFStream.Out;
import org.mondoaurora.frame.template.MAFTemplate;
import org.mondoaurora.frame.template.MAFTemplateConstant;
import org.mondoaurora.frame.template.MAFTemplateConsts;
import org.mondoaurora.frame.template.MAFTemplateRef;
import org.mondoaurora.frame.template.MAFTemplateRepeat;
import org.mondoaurora.frame.template.MAFTemplateSequence;
import org.mondoaurora.frame.template.MAFTemplateSyntax;

public class MAFJsonRelay implements MAFTemplateConsts {
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
	
	private class EvalMemberRepeat implements MAFEval {

		@Override
		public MAFKernelVariant getVariant(MAFKernelEntity currentEntity) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void writeContent(Out target, MAFKernelEntity currentEntity) throws Exception {
			// TODO Auto-generated method stub
			
		}
		
	}

	public MAFJsonRelay() {
		syntax = new MAFTemplateSyntax(RULE_OBJECT, new Initer[] {
				new Initer(RULE_OBJECT, new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant("{"),
						new MAFTemplateRef(RULE_MEMBERS), new MAFTemplateConstant("}"), })),
						
				new Initer(RULE_MEMBERS, new MAFTemplateRepeat(new EvalMemberRepeat(), new MAFTemplateRef(RULE_ASSIGNMENT), new MAFTemplateConstant(","))),
						
				new Initer(RULE_ASSIGNMENT, new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant("{"),
						new MAFTemplateRef(RULE_MEMBERS), new MAFTemplateConstant("}"), })),
						
				new Initer(RULE_ARRAY, new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant("{"),
						new MAFTemplateRef(RULE_MEMBERS), new MAFTemplateConstant("}"), })),
						
				new Initer(RULE_ELEMENTS, new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant("{"),
						new MAFTemplateRef(RULE_MEMBERS), new MAFTemplateConstant("}"), })), 
						
				new Initer(RULE_VALUE, new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant("{"),
						new MAFTemplateRef(RULE_MEMBERS), new MAFTemplateConstant("}"), })), 
		});
	}
}
