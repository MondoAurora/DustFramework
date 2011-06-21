package sandbox;

import java.util.ArrayList;

import sandbox.Test.TestItem;
import dust.api.components.DustEntity;
import dust.api.components.DustVariant;
import dust.api.components.DustWorld;
import dust.api.utils.DustUtilInitValue;
import dust.api.utils.DustUtils;
import dust.api.wrappers.DustIdentifier;
import dust.units.dust.common.v0_1.Common;
import dust.units.dust.kernel.v0_1.TypeManagement;

public class TestInitTypeManagement extends TestItem {

	@Override
	public void test() throws Exception {
		DustWorld world = DustUtils.getWorld();
		
		DustDeclId idIdentified = world.getTypeId(Common.Identified.class);		
		DustDeclId idNamed = world.getTypeId(Common.Named.class);		
		DustDeclId idFieldContainer = world.getTypeId(Common.FieldContainer.class);
		
		DustDeclId idType = world.getTypeId(TypeManagement.Type.class);
		DustDeclId idField = world.getTypeId(TypeManagement.Field.class);
		DustDeclId idFieldValue = world.getTypeId(TypeManagement.FieldValue.class);
		DustDeclId idShared = world.getTypeId(TypeManagement.Shared.class);
		DustDeclId idResource = world.getTypeId(TypeManagement.Resource.class);
		DustDeclId idMessage = world.getTypeId(TypeManagement.Message.class);
		DustDeclId idUnit = world.getTypeId(TypeManagement.Unit.class);
		
		DustEntity fld1, fld2, fld3, fld4, fld5;
		DustEntity shared1;
		
		DustEntity e;
		ArrayList<DustEntity> arrTypes = new ArrayList<DustEntity>();
		
		// Common
		
		fld1 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(Common.Identified.Fields.Identifier.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.Identifier),
		});
		
		e = DustUtils.getEntity(idType, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, idIdentified.getIdentifier()),
			world.getVar(null, Common.FieldContainer.Fields.Fields, new DustUtilInitValue[] {
				new DustUtilInitValue(fld1),
			}),
		});
		arrTypes.add(e);

		fld1 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(Common.Named.Fields.Name.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.String),
		});
		
		e = DustUtils.getEntity(idType, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, idNamed.getIdentifier()),
			world.getVar(null, Common.FieldContainer.Fields.Fields, new DustUtilInitValue[] {
				new DustUtilInitValue(fld1),
			}),
		});
		arrTypes.add(e);

		fld1 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(Common.FieldContainer.Fields.Fields.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.ObSet),
			world.getVar(null, TypeManagement.Field.Fields.ObType, idField.getIdentifier()),
		});
		
		e = DustUtils.getEntity(idType, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, idFieldContainer.getIdentifier()),
			world.getVar(null, Common.FieldContainer.Fields.Fields, new DustUtilInitValue[] {
				new DustUtilInitValue(fld1),
			}),
		});
		arrTypes.add(e);
				
		
		// TypeManagement 
		fld1 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.FieldValue.Fields.Ordinal.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.Integer),
		});
		
		e = DustUtils.getEntity(idType, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, idFieldValue.getIdentifier()),
			world.getVar(null, Common.FieldContainer.Fields.Fields, new DustUtilInitValue[] {
				new DustUtilInitValue(fld1),
			}),
		});
		arrTypes.add(e);

		
		fld1 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Shared.Fields.ObType.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.ObSingle),
			world.getVar(null, TypeManagement.Field.Fields.ObType, idType.getIdentifier()),
		});
		
		e = DustUtils.getEntity(idType, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, idShared.getIdentifier()),
			world.getVar(null, Common.FieldContainer.Fields.Fields, new DustUtilInitValue[] {
				new DustUtilInitValue(fld1),
			}),
		});
		arrTypes.add(e);

		
		e = DustUtils.getEntity(idType, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, idResource.getIdentifier()),
		});
		arrTypes.add(e);

		
		
		fld1 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Fields.FieldType.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.ValueSet),
			world.getVar(null, TypeManagement.Field.Fields.Values, new DustUtilInitValue[] {
				new DustUtilInitValue(DustUtils.getEntity(idFieldValue, new DustVariant[] {
					world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Values.FieldType.Identifier.name())),
					world.getVar(null, TypeManagement.FieldValue.Fields.Ordinal, 0),
				})), new DustUtilInitValue(DustUtils.getEntity(idFieldValue, new DustVariant[] {
					world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Values.FieldType.String.name())),
					world.getVar(null, TypeManagement.FieldValue.Fields.Ordinal, 1),
				})), new DustUtilInitValue(DustUtils.getEntity(idFieldValue, new DustVariant[] {
					world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Values.FieldType.Integer.name())),
					world.getVar(null, TypeManagement.FieldValue.Fields.Ordinal, 2),
				})), new DustUtilInitValue(DustUtils.getEntity(idFieldValue, new DustVariant[] {
					world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Values.FieldType.Double.name())),
					world.getVar(null, TypeManagement.FieldValue.Fields.Ordinal, 3),
				})), new DustUtilInitValue(DustUtils.getEntity(idFieldValue, new DustVariant[] {
					world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Values.FieldType.ImmutableDate.name())),
					world.getVar(null, TypeManagement.FieldValue.Fields.Ordinal, 4),
				})), new DustUtilInitValue(DustUtils.getEntity(idFieldValue, new DustVariant[] {
					world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Values.FieldType.Boolean.name())),
					world.getVar(null, TypeManagement.FieldValue.Fields.Ordinal, 5),
				})), new DustUtilInitValue(DustUtils.getEntity(idFieldValue, new DustVariant[] {
					world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Values.FieldType.ValueSet.name())),
					world.getVar(null, TypeManagement.FieldValue.Fields.Ordinal, 6),
				})), new DustUtilInitValue(DustUtils.getEntity(idFieldValue, new DustVariant[] {
					world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Values.FieldType.ByteArray.name())),
					world.getVar(null, TypeManagement.FieldValue.Fields.Ordinal, 7),
				})), new DustUtilInitValue(DustUtils.getEntity(idFieldValue, new DustVariant[] {
					world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Values.FieldType.ObType.name())),
					world.getVar(null, TypeManagement.FieldValue.Fields.Ordinal, 8),
				})), new DustUtilInitValue(DustUtils.getEntity(idFieldValue, new DustVariant[] {
					world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Values.FieldType.ObSingle.name())),
					world.getVar(null, TypeManagement.FieldValue.Fields.Ordinal, 9),
				})), new DustUtilInitValue(DustUtils.getEntity(idFieldValue, new DustVariant[] {
					world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Values.FieldType.ObSet.name())),
					world.getVar(null, TypeManagement.FieldValue.Fields.Ordinal, 10),
				})), new DustUtilInitValue(DustUtils.getEntity(idFieldValue, new DustVariant[] {
					world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Values.FieldType.ObArray.name())),
					world.getVar(null, TypeManagement.FieldValue.Fields.Ordinal, 11),
				})), new DustUtilInitValue(DustUtils.getEntity(idFieldValue, new DustVariant[] {
					world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Values.FieldType.ObMap.name())),
					world.getVar(null, TypeManagement.FieldValue.Fields.Ordinal, 12),
				})), 
			}),
		});
		
		fld2 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Fields.ObType.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.Identifier),
		});

		fld3 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Field.Fields.Values.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.ObSet),
			world.getVar(null, TypeManagement.Field.Fields.ObType, idFieldValue.getIdentifier()),
		});

		
		e = DustUtils.getEntity(idType, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, idField.getIdentifier()),
			world.getVar(null, Common.FieldContainer.Fields.Fields, new DustUtilInitValue[] {
				new DustUtilInitValue(fld1),
				new DustUtilInitValue(fld2),
				new DustUtilInitValue(fld3),
			}),
		});
		arrTypes.add(e);

			
		e = DustUtils.getEntity(idType, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, idMessage.getIdentifier()),
		});
		arrTypes.add(e);

		
		fld1 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Unit.Fields.Vendor.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.Identifier),
		});

		fld2 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Unit.Fields.Domain.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.Identifier),
		});

		fld3 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Unit.Fields.Version.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.Identifier),
		});

		shared1 = DustUtils.getEntity(idShared, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Unit.Shared.Kernel.name())),
			world.getVar(null, TypeManagement.Shared.Fields.ObType, idUnit.getIdentifier()),
		});

		e = DustUtils.getEntity(idType, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, idUnit.getIdentifier()),
			world.getVar(null, Common.FieldContainer.Fields.Fields, new DustUtilInitValue[] {
				new DustUtilInitValue(fld1),
				new DustUtilInitValue(fld2),
				new DustUtilInitValue(fld3),
			}),
			world.getVar(null, TypeManagement.Type.Fields.Shared, new DustUtilInitValue[] {
				new DustUtilInitValue(shared1),
			}),
		});
		arrTypes.add(e);

		
		fld1 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Type.Fields.Unit.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.ObSingle),
			world.getVar(null, TypeManagement.Field.Fields.ObType, idUnit.getIdentifier()),
		});
		fld2 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Type.Fields.isGlobal.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.Boolean),
		});
		fld3 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Type.Fields.Messages.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.ObSet),
			world.getVar(null, TypeManagement.Field.Fields.ObType, idMessage.getIdentifier()),
		});
		fld4 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Type.Fields.Shared.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.ObSet),
			world.getVar(null, TypeManagement.Field.Fields.ObType, idShared.getIdentifier()),
		});
		fld5 = DustUtils.getEntity(idField, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Type.Fields.Resource.name())),
			world.getVar(null, TypeManagement.Field.Fields.FieldType, TypeManagement.Field.Values.FieldType.ObSet),
			world.getVar(null, TypeManagement.Field.Fields.ObType, idResource.getIdentifier()),
		});
		shared1 = DustUtils.getEntity(idShared, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, new DustIdentifier(TypeManagement.Type.Shared.Self.name())),
			world.getVar(null, TypeManagement.Shared.Fields.ObType, idType.getIdentifier()),
		});

		e = DustUtils.getEntity(idType, new DustVariant[] {
			world.getVar(null, Common.Identified.Fields.Identifier, idType.getIdentifier()),
			world.getVar(null, Common.FieldContainer.Fields.Fields, new DustUtilInitValue[] {
				new DustUtilInitValue(fld1),
				new DustUtilInitValue(fld2),
				new DustUtilInitValue(fld3),
				new DustUtilInitValue(fld4),
				new DustUtilInitValue(fld5),
			}),
			world.getVar(null, TypeManagement.Type.Fields.Shared, new DustUtilInitValue[] {
				new DustUtilInitValue(shared1),
			}),
		});
		arrTypes.add(e);

		DustEntity eUnitKernel = DustUtils.getEntity(idUnit, new DustVariant[] {
			world.getVar(idNamed,Common.Named.Fields.Name, new DustIdentifier("TypeManagement")),
			world.getVar(idUnit,TypeManagement.Unit.Fields.Vendor, new DustIdentifier("dust")),
			world.getVar(idUnit,TypeManagement.Unit.Fields.Domain, new DustIdentifier("kernel")),
			world.getVar(idUnit,TypeManagement.Unit.Fields.Version, new DustIdentifier("v0_1")),
		});

		for ( DustEntity eType : arrTypes ) {
			eType.getAspect(idType, true).getField(TypeManagement.Type.Fields.Unit).setData(eUnitKernel, VariantSetMode.set, null);
			
			String id = eType.getAspect(idIdentified, false).getField(Common.Identified.Fields.Identifier).getValueIdentifier().toString();
			String name = id.substring(id.lastIndexOf('$')+1);
			eType.getAspect(idNamed, true).getField(Common.Named.Fields.Name).setValueString(name);
		}

/*		
		System.out.println(eTypeIdentified);
		System.out.println(eTypeFieldContainer);
		
		System.out.println(eTypeFieldValue);
		System.out.println(eTypeShared);
		System.out.println(eTypeResource);
		System.out.println(eTypeField);
		System.out.println(eTypeMessage);
		System.out.println(eTypeModule);
		System.out.println(eTypeType);
*/
		
	}
}
