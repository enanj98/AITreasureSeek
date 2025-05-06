package AI;

import client.model.Field;

public class FieldAtributs {
	private Integer distance;
	private Field previuosField;
	
	public FieldAtributs(Integer distance, Field previuosField) {
		super();
		this.distance = distance;
		this.previuosField = previuosField;
	}
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	public Field getPreviuosField() {
		return previuosField;
	}
	public void setPreviuosField(Field previuosField) {
		this.previuosField = previuosField;
	}
	
	
}
