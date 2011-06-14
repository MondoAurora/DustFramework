package sandbox.persistence.db;

import dust.api.utils.DustUtilFormatter;

public interface DBConstants {
	DustUtilFormatter.TemplateDef TD_SELECT_ENTITY_ASPECT = new DustUtilFormatter.TemplateDef("SELECT EntityID, AspectID, TypeID FROM aspects WHERE EntityID = {0}", 2);
	DustUtilFormatter.TemplateDef TD_SELECT_ENTITY_VALUES = new DustUtilFormatter.TemplateDef("SELECT a0.TypeID, v0.FieldID, v0.ValueStr FROM aspects a0, aspValues v0 WHERE a0.EntityID = {0} AND a0.AspectID = v0.AspectID");
	DustUtilFormatter.TemplateDef TD_SELECT_ENTITY_LINKS = new DustUtilFormatter.TemplateDef("SELECT a0.TypeID, l0.LinkID, l0.ToAspectID, e0.PrimaryAspectTypeID, l0.LinkData FROM aspects a0, links l0, entities e0 WHERE  a0.EntityID = {0} AND a0.AspectID = l0.FromAspectID AND e0.EntityID = l0.ToAspectID");
	
	DustUtilFormatter.TemplateDef TD_FIND_ENTITY = new DustUtilFormatter.TemplateDef("SELECT e.EntityID, e.PrimaryAspectTypeID from entities e {0} WHERE {1}", 2);
	DustUtilFormatter.TemplateDef TD_FIND_ENTITY_TYPE = new DustUtilFormatter.TemplateDef("e.PrimaryAspectTypeID = ''{0}''");
	DustUtilFormatter.TemplateDef TD_FIND_ENTITY_FROM = new DustUtilFormatter.TemplateDef(", aspects a{0}, aspValues v{0}");
	DustUtilFormatter.TemplateDef TD_FIND_ENTITY_WHERE = new DustUtilFormatter.TemplateDef(" AND e.EntityID = a{0}.EntityID AND a{0}.TypeID = ''{1}'' AND a{0}.aspectID = v{0}.aspectID and v{0}.FieldID = ''{2}'' and v{0}.ValueStr = ''{3}''", 4);
	
	
	DustUtilFormatter.TemplateDef TD_INSERT_ENTITY = new DustUtilFormatter.TemplateDef("INSERT INTO entities (EntityID, PrimaryAspectTypeID) VALUES ({0}, ''{1}'')", 2);
	DustUtilFormatter.TemplateDef TD_INSERT_ASPECT = new DustUtilFormatter.TemplateDef("INSERT INTO aspects (EntityID, TypeID) VALUES ({0}, ''{1}'')", 2);
	DustUtilFormatter.TemplateDef TD_INSERT_VALUE = new DustUtilFormatter.TemplateDef("INSERT INTO aspValues (AspectID, FieldID, ValueStr) VALUES ({0}, ''{1}'', ''{2}'')", 3);
	DustUtilFormatter.TemplateDef TD_INSERT_LINK = new DustUtilFormatter.TemplateDef("INSERT INTO links (FromAspectID, ToAspectID, LinkID, LinkData) VALUES ({0}, {1}, ''{2}'', ''{3}'')", 4);
	
	DustUtilFormatter.TemplateDef TD_DELETE_ASPECT = new DustUtilFormatter.TemplateDef("DELETE FROM aspects WHERE AspectID = {0}");
	DustUtilFormatter.TemplateDef TD_DELETE_ASPECT_VALUES = new DustUtilFormatter.TemplateDef("DELETE FROM aspValues WHERE AspectID = {0}");
	DustUtilFormatter.TemplateDef TD_DELETE_ASPECT_LINKS = new DustUtilFormatter.TemplateDef("DELETE FROM links WHERE FromAspectID = {0}");
	
	DustUtilFormatter.TemplateDef TD_UPDATE_ASPECT_ENTITYID = new DustUtilFormatter.TemplateDef("UPDATE aspects SET EntityID = {0} WHERE AspectID = {0}");
}
