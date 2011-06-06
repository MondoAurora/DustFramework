package dust.api;

import dust.api.components.DustEntity;

public interface DustConstants extends DustDeclarationConstants {
	enum FieldType { Identifier, String, Long, Double, ImmutableDate, Boolean, ValueSet, ByteArray, 
		ObSingle, ObSet, ObArray, ObMap };

		enum VariantSetMode { set, addFirst, addLast, insert, remove, clear };
			
		enum EntityType { Temporal, Ghost, Persistent };
		enum EntityState { Creating, Changing, Steady };
		
		public interface DustDeclId {};
		
		public static interface InvokeResponseProcessor {
			void searchStarted();
			boolean entityFound(DustEntity entity); // return false if you do not want others to come
			void searchFinished();
		}

}
