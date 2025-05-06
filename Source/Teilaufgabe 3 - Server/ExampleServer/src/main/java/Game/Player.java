package Game;

import java.time.Instant;

import Map.Field;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import enums.PlayerState;

public class Player {
	private String studentFirstName, studentLastName,studentID;
	private String uniqueplayerid;
	private EPlayerGameState state = EPlayerGameState.ShouldWait;
	private boolean treasureCollected = false;
	private int fortx,forty; 
	private Instant sendcommandTime;
	public Player(String studentFirstName, String studentLastName, String studentID) {
		super();
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.studentID = studentID;
	}

	public void setUniquePlayerID(String id) {
		this.uniqueplayerid = id;
		
	}

	public String getStudentFirstName() {
		return studentFirstName;
	}

	public String getStudentLastName() {
		return studentLastName;
	}

	public String getStudentID() {
		return studentID;
	}

	public String getUniqueplayerid() {
		return uniqueplayerid;
	}

	public EPlayerGameState getState() {
		return state;
	}
	public boolean getTreasureState() {
		return treasureCollected;
	}
	public void setTreasureState(boolean treasure) {
		this.treasureCollected = treasure;
	}
	

	public void setState(EPlayerGameState state) {
		this.state = state;
	}
	
	public int getfortx() {
		return fortx;
	}
	public int getforty() {
		return forty;
	}


	public void setFortpos(int x , int y) {
		this.fortx = x;
		this.forty = y;
	}
	
	public void setTimer(Instant instant) {
		this.sendcommandTime = instant;
	}
	public Instant getsendCommandTime() {
		return this.sendcommandTime;
	}
	
	
}
