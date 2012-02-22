package org.mondoaurora.frame.tools;

import java.util.HashSet;
import java.util.Set;

import org.mondoaurora.frame.eval.MAFEval;
import org.mondoaurora.frame.eval.MAFEvalBase;
import org.mondoaurora.frame.kernel.*;
import org.mondoaurora.frame.process.MAFProcessEventSource;
import org.mondoaurora.frame.process.MAFProcessManager;
import org.mondoaurora.frame.shared.*;
import org.mondoaurora.frame.shared.MAFStream.Out;
import org.mondoaurora.frame.template.*;

public class MAFToolsJsonRelay implements MAFTemplateConsts {
	public static final String RULE_OBJECT = "object";
	public static final String RULE_MEMBERS = "members";
	public static final String RULE_ASSIGNMENT = "assignment";
	public static final String RULE_ARRAY = "array";
	public static final String RULE_ELEMENTS = "elements";
	public static final String RULE_VALUE = "value";
	public static final String RULE_VAR = "var";

	public static final String KEY_REF = "!ref";

	// object : sequence( const("{"), ref("members"), const("}") )
	// members: repeat( ref("assignment"), ",")
	// assignment: sequence ( const("\""), eval(name), const("\""), const(":"),
	// ref("value") )
	// array: sequence( const("["), ref("elements"), const("]") )
	// elements: repeat( ref("value"), ",")
	// value: switch( ref("object"), ref("array"), ref("var"), )
	// var: sequence( const("\""), eval(value), const("\"") )

	private static final MAFVariant VAR_STR_OBJECT = new MAFToolsVariantWrapper.ConstString(RULE_OBJECT);
	private static final MAFVariant VAR_STR_ARRAY = new MAFToolsVariantWrapper.ConstString(RULE_ARRAY);
	private static final MAFVariant VAR_STR_VALUE = new MAFToolsVariantWrapper.ConstString(RULE_VALUE);

	MAFTemplateSyntax syntax;

	Set<MAFKernelEntity> setEntities = new HashSet<>();
	
	private static String ESC_KEYS = "/bfnrt";
	private static String ESC_VALUES = "/\b\f\n\r\t";

	private abstract class RelayEval extends MAFEvalBase {

		@Override
		public MAFVariant getVariant(MAFVariant var) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void writeContent(Out target, MAFVariant var) {
			// TODO Auto-generated method stub

		}

		class Ctx {
			StringBuilder content = new StringBuilder();
			boolean esc = false;
			int unicodeCountdown;
			int unicodeValue;
			
			boolean appendChar(char c) {
				switch (c) {
				case '\\':
					if (!esc) {
						esc = true;
						return true;
					}
					break;
				case '\"':
					if ( !esc ) {
						return false;
					}
				}
				
				if ( esc ) {
					if ( 0 < unicodeCountdown ) {
						unicodeValue = 16*unicodeValue + Integer.parseInt(String.valueOf(c), 16);
						if ( 0 == --unicodeCountdown ) {
							content.appendCodePoint(unicodeValue);
							esc = false;
						}
						
					}
					if ( 'u' == c ) {
						unicodeCountdown = 4;
						return true;
					} else {
						int ek = ESC_KEYS.indexOf(c);
						if ( -1 == ek ) {
							throw new MAFRuntimeException("JSONRelay", "Invalid escape char: \'" + c + "\'");
						} else {
							content.append(ESC_VALUES.charAt(ek));
						}
					}
					esc = false;
				}
				
				content.append(c);
				return true;
			}
		}

		@Override
		public Object createContextObject(Object msg) {
			return new Ctx();
		}

		@Override
		protected Return processChar(char c, Object ctx) {
			Ctx context = (Ctx) ctx;
			
			return context.appendChar(c) ? CONTINUE : new Return(ReturnType.Success, context.content.toString(), false);
		}
	}

	private class EvalName extends RelayEval {
		@Override
		public void writeContent(Out target, MAFVariant var) {
			target.put(var.getKey());
		}
		
		@Override
		protected Return processChar(char c, Object ctx) {
			Return ret = super.processChar(c, ctx);
			
			if ( ReturnType.Continue != ret.getType() ) {
				System.out.println("Name: " + ret.getOb());
			}
			
			return ret;
		}
	}

	private class EvalValueSwitch extends RelayEval {
		@Override
		public MAFVariant getVariant(MAFVariant var) {
			if (var instanceof MAFToolsVariantWrapper.Aspect) {
				return VAR_STR_OBJECT;
			} else if (var instanceof MAFKernelVariant) {
				switch (((MAFKernelVariant) var).getType()) {
				case REFERENCE:
					return VAR_STR_OBJECT;
				case ARRAY:
				case SET:
					return VAR_STR_ARRAY;
				default:
					return VAR_STR_VALUE;
				}
			} else {
				return VAR_STR_VALUE;
			}
		}
	}

	private class EvalValue extends RelayEval {
		@Override
		public MAFVariant getVariant(MAFVariant var) {
			if (var instanceof MAFKernelVariant) {
				switch (((MAFKernelVariant) var).getType()) {
				case REFERENCE:
					MAFKernelEntity e = ((MAFKernelConnector) var.getReference(MAFUtils.EMPTYARR)).getEntity();
					return wrapEntity(e);
				}
			}

			return var;
		}

		@Override
		public void writeContent(Out target, MAFVariant var) {
			target.put(var.toString());
		}
		
		@Override
		protected Return processChar(char c, Object ctx) {
			Return ret = super.processChar(c, ctx);
			
			if ( ReturnType.Continue != ret.getType() ) {
				System.out.println("Value: " + ret.getOb());
			}
			
			return ret;
		}

	}

	public MAFToolsJsonRelay() {
		MAFTemplate tSpace = new MAFTemplateWhitespace();
		MAFTemplate tLineFeed = new MAFTemplateWhitespace(MAFStream.Indent.keep);
		MAFTemplate tIndentInc = new MAFTemplateWhitespace(MAFStream.Indent.inc);
		MAFTemplate tIndentDec = new MAFTemplateWhitespace(MAFStream.Indent.dec);

		MAFTemplate tConstComma = new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant(","), tLineFeed });
		MAFTemplate tConstQuot = new MAFTemplateConstant("\"");

		MAFEval evalValue = new EvalValue();

		new MAFTemplateConstant(",");

		syntax = new MAFTemplateSyntax(RULE_OBJECT, new Initer[] {
				new Initer(RULE_OBJECT, new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant("{"), tIndentInc,
						new MAFTemplateRef(RULE_MEMBERS), tIndentDec, new MAFTemplateConstant("}"), })),

				new Initer(RULE_MEMBERS, new MAFTemplateRepeat(null, new MAFTemplateRef(RULE_ASSIGNMENT), tConstComma)),

				new Initer(RULE_ASSIGNMENT, new MAFTemplateSequence(new MAFTemplate[] { tConstQuot,
						new MAFTemplateEval(new EvalName()), tConstQuot, tSpace, new MAFTemplateConstant(":"), tSpace,
						new MAFTemplateRef(RULE_VALUE), })),

				new Initer(RULE_ARRAY, new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant("["), tIndentInc,
						new MAFTemplateRef(RULE_ELEMENTS), tIndentDec, new MAFTemplateConstant("]"), })),

				new Initer(RULE_ELEMENTS, new MAFTemplateRepeat(null, new MAFTemplateRef(RULE_VALUE), tConstComma)),

				new Initer(RULE_VALUE, new MAFTemplateSwitch(new EvalValueSwitch(), evalValue, new Initer[] {
						new Initer(RULE_OBJECT, new MAFTemplateRef(RULE_OBJECT)),
						new Initer(RULE_ARRAY, new MAFTemplateRef(RULE_ARRAY)),
						new Initer(RULE_VALUE, new MAFTemplateRef(RULE_VAR)), })),

				new Initer(RULE_VAR, new MAFTemplateSequence(new MAFTemplate[] { tConstQuot, new MAFTemplateEval(evalValue),
						tConstQuot })),

		});
	}

	MAFVariant wrapEntity(MAFKernelEntity e) {
		return new MAFToolsVariantWrapper.Entity(e, KEY_REF, !setEntities.add(e));
	}

	public void write(MAFStream.Out stream, MAFConnector conn) {
		MAFKernelEntity e = ((MAFKernelConnector) conn).getEntity();
		syntax.write(wrapEntity(e), stream);
	}
	
	public void read(MAFProcessEventSource src) {
		MAFProcessManager mgr = new MAFProcessManager();
		
		mgr.process(syntax.getStartRule(), src);
	}
}
