package dust.api;

public interface DustConstants extends DustDeclarationConstants {
	enum FieldType { Identifier, String, JavaClass, Long, Double, ImmutableDate, Boolean, ValueSet, ByteArray, 
		ObSingle, ObSet, ObArray, ObMap };

		enum VariantSetMode { set, addFirst, addLast, insert, remove, clear };

		enum FieldAccessHint { wait, forUpdate };
		
		enum EntityAccessHint { lazyCreate, allowSingle, allowMultiple };
		
		public interface DustDeclId {};
		public interface DustInstanceId {};
		
}
