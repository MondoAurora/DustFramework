package dust.api;

import dust.api.components.DustEntity;
import dust.api.wrappers.DustIdentifier;

public interface DustConstants extends DustDeclarationConstants {
		enum VariantSetMode { set, addFirst, addLast, insert, remove, clear };
			
		enum EntityType { Temporal, Ghost, Persistent };
		enum EntityState { Creating, Changing, Steady };
		
		public interface DustDeclId {
			DustIdentifier getIdentifier();
		};
		
		public static interface InvokeResponseProcessor {
			void searchStarted();
			boolean entityFound(DustEntity entity); // return false if you do not want others to come
			void searchFinished();
		}

}
