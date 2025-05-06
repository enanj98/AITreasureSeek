package gameState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Comparator;
import java.util.List;

import MessagesGameState.EPlayerGameState;
import MessagesGameState.EPlayerPositionState;
import client.model.Field;
import client.model.Halfmap;
import clientenums.EMapType;


public class MyGameState {
	private Halfmap map;
	private EPlayerGameState pstate;
	private boolean treasureFound = false;
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	private Field mypos;
	
	
	public Halfmap getMap() {
		return map;
	}
	
	

	public Field getMypos() {
		return mypos;
	}



	public void setMypos(Field mypos) {
		Field oldpos = this.mypos;
		this.mypos = mypos;
		changes.firePropertyChange("PosChange",oldpos,this.mypos);
	}



	public void setPlayerstate(EPlayerGameState state) {
		EPlayerGameState stateBeforeChange = this.pstate;
		this.pstate = state;
		changes.firePropertyChange("pchange",stateBeforeChange,this.pstate);
	}

	public void setMap(Halfmap map) {
		 Halfmap mapbeforechange = this.map;
		 this.map = map;
		 if(this.map.getFields().size() == 64) {
	 		 if(this.map.getFields().stream().anyMatch(p -> p.getX() > 8)) {
				 this.map.setMaptype(EMapType.foursixteen);
			 }else{
				 this.map.setMaptype(EMapType.eighteight);
			 }
	 	  }
		  
		changes.firePropertyChange("mapchange",mapbeforechange,this.map);
	}
	
	public void addListener(PropertyChangeListener view) {
		changes.addPropertyChangeListener(view);
	}
	public EPlayerGameState getPlayerGameState() {
		 return pstate;
	}
	public boolean getTreasureState() {
		return treasureFound;
	}
	public void setTreasureState(boolean treasure) {
		boolean treausurebeforechange = this.treasureFound;
		this.treasureFound = treasure;
		changes.firePropertyChange("TreasureFound", treausurebeforechange, treasure);
	}
	
}
