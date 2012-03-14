package org.mondoaurora.frame.tools;

import java.util.*;

import org.mondoaurora.frame.eval.MAFEval;
import org.mondoaurora.frame.eval.MAFEvalBase;
import org.mondoaurora.frame.kernel.*;
import org.mondoaurora.frame.process.MAFProcess.Return;
import org.mondoaurora.frame.process.MAFProcess.ReturnType;
import org.mondoaurora.frame.process.*;
import org.mondoaurora.frame.shared.*;
import org.mondoaurora.frame.shared.MAFStream.Out;
import org.mondoaurora.frame.template.*;

public class MAFToolsJsonRelay implements MAFTemplate.Connector, MAFTemplateConsts {
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
					if (!esc) {
						return false;
					}
				}

				if (esc) {
					if (0 < unicodeCountdown) {
						unicodeValue = 16 * unicodeValue + Integer.parseInt(String.valueOf(c), 16);
						if (0 == --unicodeCountdown) {
							content.appendCodePoint(unicodeValue);
							esc = false;
						}

					}
					if ('u' == c) {
						unicodeCountdown = 4;
						return true;
					} else {
						int ek = ESC_KEYS.indexOf(c);
						if (-1 == ek) {
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

		@Override
		public String toString() {
			return "MAFToolsJsonRelay.RelayEval";
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

			if (ReturnType.Continue != ret.getType()) {
				// System.out.println("Name: " + ret.getOb());
			}

			return ret;
		}
	}

	private class EvalValueSwitch extends RelayEval {
		@Override
		public MAFVariant getVariant(MAFVariant var) {
			if (var instanceof MAFKernelAspect.Variant) {
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

			if (ReturnType.Continue != ret.getType()) {
				// System.out.println("Value: " + ret.getOb());
			}

			return ret;
		}

		@Override
		public String toString() {
			return "MAFToolsJsonRelay.EvalValue";
		}
	}

	MAFEval evalName = new EvalName();
	MAFEval evalValue = new EvalValue();

	public MAFToolsJsonRelay() {

		MAFTemplate tConstComma = new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant(","), TEMPL_WS_LINEFEED });
		MAFTemplate tConstQuot = new MAFTemplateConstant("\"");

		syntax = new MAFTemplateSyntax(RULE_OBJECT, new Initer[] {
				new Initer(RULE_OBJECT, new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant("{"), TEMPL_WS_INDENT_INC,
						new MAFTemplateRef(RULE_MEMBERS), TEMPL_WS_INDENT_DEC, new MAFTemplateConstant("}"), })),

				new Initer(RULE_MEMBERS, new MAFTemplateRepeat(null, new MAFTemplateRef(RULE_ASSIGNMENT), tConstComma)),

				new Initer(RULE_ASSIGNMENT, new MAFTemplateSequence(new MAFTemplate[] { tConstQuot,
						new MAFTemplateEval(evalName), tConstQuot, TEMPL_WS_SPACE, new MAFTemplateConstant(":"), TEMPL_WS_SPACE,
						new MAFTemplateRef(RULE_VALUE), })),

				new Initer(RULE_ARRAY, new MAFTemplateSequence(new MAFTemplate[] { new MAFTemplateConstant("["), TEMPL_WS_INDENT_INC,
						new MAFTemplateRef(RULE_ELEMENTS), TEMPL_WS_INDENT_DEC, new MAFTemplateConstant("]"), })),

				new Initer(RULE_ELEMENTS, new MAFTemplateRepeat(null, new MAFTemplateRef(RULE_VALUE), tConstComma)),

				new Initer(RULE_VALUE, new MAFTemplateSwitch(new EvalValueSwitch(), evalValue, new Initer[] {
						new Initer(RULE_OBJECT, new MAFTemplateRef(RULE_OBJECT)),
						new Initer(RULE_ARRAY, new MAFTemplateRef(RULE_ARRAY)),
						new Initer(RULE_VALUE, new MAFTemplateRef(RULE_VAR)), })),

				new Initer(RULE_VAR, new MAFTemplateSequence(new MAFTemplate[] { tConstQuot, new MAFTemplateEval(evalValue),
						tConstQuot })),

		}, this);
	}

	MAFVariant wrapEntity(MAFKernelEntity e) {
		return new MAFKernelEntity.Variant(e, KEY_REF, !setEntities.add(e));
	}

	public void write(MAFStream.Out stream, MAFConnector conn) {
		MAFKernelEntity e = ((MAFKernelConnector) conn).getEntity();
		syntax.write(wrapEntity(e), stream);
	}

	private static final String[] RULE_WATCH = new String[] { RULE_OBJECT };

	LinkedList<MAFKernelEntity.Variant> readStack = new LinkedList<>();
	String readingKeyName;
	MAFKernelEntity.Variant read;
	
	public MAFKernelEntity.Variant read(MAFProcessEventSource src) {
		syntax.read(src);
		return read;
	}

	@Override
	public void templateBegin(MAFTemplate template, Object context) {
		String tName = template.getId();
		MAFKernelAspect.Variant va = null;

		switch (MAFUtils.indexOf(RULE_WATCH, tName)) {
		case 0:
			if (readStack.isEmpty()) {
				readStack.addFirst(new MAFKernelEntity.Variant(va));
			} else {
				MAFKernelEntity.Variant ve = readStack.getFirst();
				va = ve.getCurrAspect();

				if ((null == va)) {
					ve.startAspect(readingKeyName);
				} else {
					readStack.addFirst(new MAFKernelEntity.Variant(va));
				}
			}
			break;
		case -1:
			return;
		}
	}

	@Override
	public void templateEnd(MAFTemplate template, Object context, Return ret) {
		String tName = template.getId();
		String s = ((ret.getType() == ReturnType.Success) ? "Success " : "Failure ") + template;

		if (-1 != MAFUtils.indexOf(RULE_WATCH, tName)) {
			MAFKernelEntity.Variant ve = readStack.getFirst();

			if (null == ve) {
				throw new MAFRuntimeException("MAFToolsJsonRelay", "templateEnd called with no opened Object");
			} else {
				if (ret.getType() == ReturnType.Success) {
					if ((null == ve.getCurrAspect())) {
						readStack.removeFirst();
						if ( readStack.isEmpty() ) {
							read = ve;
						} else {
							readStack.getFirst().getCurrAspect().setFromVariant(ve);
						}
					} else {
						ve.endCurrentAspect();
					}
				} else {
					readStack.removeFirst();
					System.out.println(s);
				}
			}
		} else if (template instanceof MAFTemplateEval) {
			if (template.getId().startsWith(RULE_ASSIGNMENT)) {
				readingKeyName = (String) ret.getOb();

				if (!KEY_REF.equals(readingKeyName)) {
					MAFKernelEntity.Variant ve = readStack.getFirst();
					if (null != ve) {
						MAFKernelAspect.Variant va = ve.getCurrAspect();
						if (null != va) {
							va.select(readingKeyName);
						}
					}
				}
			} else {
				String value = (String) ret.getOb();

				MAFKernelEntity.Variant ve = readStack.getFirst();
				if (KEY_REF.equals(readingKeyName)) {
					ve.setRef(value);
				} else {
					MAFKernelAspect.Variant va = ve.getCurrAspect();

					if ((null == va)) {
						throw new MAFRuntimeException("MAFToolsJsonRelay", "Value set without current aspect");
					} else {
						va.setFromString(value);
					}
				}
			}
		}
	}
}
