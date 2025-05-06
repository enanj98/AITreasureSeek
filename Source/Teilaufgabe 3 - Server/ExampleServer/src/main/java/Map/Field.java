package Map;

import MessagesGameState.EPlayerPositionState;
import enums.FortState;
import enums.PlayerPositionState;
import enums.Terrain;
import enums.TreasureState;

public class Field {
	private int x,y;
	private Terrain terrain;
	private TreasureState treasurestate = TreasureState.NoOrUnknownTreasureState;
	private FortState fortstate;
	private PlayerPositionState playerpositionstate = PlayerPositionState.NoPlayerPresent;
	public Field(int x ,int y,boolean fortstate,Terrain terrain) {
		this.x = x;
		this.y = y;
		if(fortstate) {
			this.fortstate = FortState.MyFortPresent;
			this.playerpositionstate = PlayerPositionState.MyPosition;
		}else {
			this.fortstate = FortState.NoOrUnknownFortState;	
		}
		this.terrain = terrain;
		
	}
	public Field(int x, int y, Terrain terrain) {
		this(x,y,false,terrain);
	}

	public Terrain getTerrain() {
		return terrain;
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}
	public TreasureState getTreasurestate() {
		return treasurestate;
	}
	
	public FortState getFortstate() {
		return fortstate;
	}
	public void setFortstate(FortState fortstate) {
		this.fortstate = fortstate;
	}
	public PlayerPositionState getplayerPresent() {
		return playerpositionstate;
	}
	public PlayerPositionState setPlayerPresetn(PlayerPositionState playerpositionstate) {
		return this.playerpositionstate = playerpositionstate;
	}

}
