package client.model;

import java.util.LinkedList;
import java.util.List;

import MessagesGameState.EPlayerPositionState;
import clientenums.Terrain;
import clientenums.TreasureState;

public class Field {
	private int x,y;
	private Terrain terrain;
	private TreasureState treasurestate;
	private boolean fortstate;
	private EPlayerPositionState playerpositionstate;


	public Field(int x ,int y,boolean fortstate,Terrain terrain) {
		this.x = x;
		this.y = y;
		this.fortstate = fortstate;
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
	
	public boolean getFortstate() {
		return fortstate;
	}
	public void setFortstate(boolean fortstate) {
		this.fortstate = fortstate;
	}
	public EPlayerPositionState getPlayerpositionstate() {
		return playerpositionstate;
	}
	public void setPlayerpositionstate(EPlayerPositionState state) {
		this.playerpositionstate = state;
	}
	
	public boolean checkIfExists(int x, int y) {
		return this.x == x && this.y == y;
	}
	public void display() {
		
		if(getTerrain() == Terrain.Grass) {
			if(getFortstate()) {
				if(getPlayerpositionstate() == EPlayerPositionState.MyPosition || getPlayerpositionstate() == EPlayerPositionState.BothPlayerPosition) {
				System.out.print( " || "+ getX() +"G" +getY() +" fp1|| ");
			}else {
				System.out.print( " || "+ getX() +"G" +getY() +" f|| ");
			}
		}
		else {
				System.out.print( " || "+ getX() +"G" +getY() +" || ");
		}
		}else if(getTerrain() == Terrain.Water) {
			if(getPlayerpositionstate() == EPlayerPositionState.MyPosition || getPlayerpositionstate() == EPlayerPositionState.BothPlayerPosition) {
				System.out.print( " || "+ getX() +"W" +getY() +" P1|| ");
			}else {
				System.out.print( " || "+ getX() +"W" +getY() +" || ");
			}
		}else if(getTerrain() == Terrain.Mountain) {
			if(getPlayerpositionstate() == EPlayerPositionState.MyPosition || getPlayerpositionstate() == EPlayerPositionState.BothPlayerPosition) {
				System.out.print( " || "+ getX() +"M" +getY() +" P1|| ");
			}else {
				System.out.print( " || "+ getX() +"M" +getY() +" || ");
			}
		}
		
	}


	
}
