package sandbox.lab;

import dust.api.DustConstants.DustDeclId;
import dust.api.components.*;
import dust.api.utils.DustUtils;

import dust.units.dust.common.v0_1.Common;

public class LabEntity implements Comparable<LabEntity> {
	String displName;
	
	DustEntity entity;
	
	public LabEntity(DustEntity e) {
		final DustWorld world = DustUtils.getWorld();

		final DustDeclId idIdentified = world.getTypeId(Common.Identified.class);
		final DustDeclId idNamed = world.getTypeId(Common.Named.class);

		this.entity = e;
		
		String s = entity.getPrimaryTypeId().getIdentifier().toString();
		
		StringBuilder b = new StringBuilder(s);
		b.append(": ");
		s = null;
		
		DustAspect asp = entity.getAspect(idNamed, false);
		if ( null != asp ) {
			s = asp.getField(Common.Named.Fields.Name).getValueString();
		} else {
			asp = entity.getAspect(idIdentified, false);
			if ( null != asp ) {
				s = asp.getField(Common.Identified.Fields.Identifier).getValueIdentifier().toString();
			} 
		}
		
		if ( null == s ) {
			s = "Unidentified - " + entity.hashCode();
		}

		b.append(s);
		
		displName = b.toString();
	}

	@Override
	public int compareTo(LabEntity arg0) {
		return displName.compareTo(arg0.displName);
	}

	@Override
	public String toString() {
		return displName;
	}
}
