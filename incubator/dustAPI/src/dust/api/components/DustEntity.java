package dust.api.components;

import dust.api.DustConstants;

public interface DustEntity extends DustConstants {
	DustVariantStructure getPrimaryAspect();
	DustVariantStructure getAspect(Class <? extends TypeDef> aspect);
	
	DustInstanceId getInstanceId();
}
