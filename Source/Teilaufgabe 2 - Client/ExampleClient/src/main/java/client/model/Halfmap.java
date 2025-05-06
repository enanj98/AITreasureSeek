package client.model;

import java.util.Collection;
import java.util.List;

import MessagesGameState.EPlayerPositionState;
import clientenums.EMapType;

public class Halfmap {
 private List<Field>fields;
 private EMapType maptype;

public Halfmap(List<Field> fields) {
	super();
	this.fields = fields;
}

public List<Field> getFields() {
	return fields;
}


public EMapType getMaptype() {
	return maptype;
}

public void setMaptype(EMapType maptype) {
	this.maptype = maptype;
}

public void setFields(List<Field> fields) {
	this.fields = fields;
}




 
}
