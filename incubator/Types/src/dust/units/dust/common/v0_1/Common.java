package dust.units.dust.common.v0_1;

import dust.api.DustDeclarationConstants;

public interface Common extends DustDeclarationConstants {
	public interface ValIdentifier extends TypeDef {}
	public interface ValString extends TypeDef {}
	public interface ValJavaClass extends TypeDef {}
	public interface ValLong extends TypeDef {}
	public interface ValDouble extends TypeDef {}
	public interface ValImmutableDate extends TypeDef {}
	public interface ValBoolean extends TypeDef {}
	public interface ValValueSet extends TypeDef {}
	public interface ValByteArray extends TypeDef {}
	
	public interface Identified extends TypeDef {
		enum Fields implements FieldId {
			Identifier
		};
	}

}
