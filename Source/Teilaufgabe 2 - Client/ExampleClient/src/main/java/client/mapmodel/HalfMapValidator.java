package client.mapmodel;

import java.util.ArrayList;
import java.util.List;

import client.model.Field;
import clientenums.Terrain;

public class HalfMapValidator {
	private List<Field> mapfields = new ArrayList<>();
    private List<Field>visited = new ArrayList<>();
    boolean validated = false;
    boolean isvalidated = false;
    private int accesiblefields;
    
    public HalfMapValidator() {
    
    }
    public List<Field> getMap(){
    	return mapfields;
    }
    public void setFields(List<Field> fields){

    	this.mapfields = fields;
    }
    /*
     * returns if all the rules are satisfied , and starts the flood fill algorithm from field that is not  water
     * 
     */
	public  boolean validateMap() {
		
		accesiblefields = (int)mapfields.stream().filter(field -> field.getTerrain() == Terrain.Mountain || field.getTerrain() == Terrain.Grass).count();
		Field startPos = mapfields.stream().filter(field -> field.getTerrain() != Terrain.Water).findFirst().get();
		validateReachable(startPos.getX(),startPos.getY());

		if(validated && checkWaterOnSides() && checkNumberofFields() ) {
			isvalidated = true;
			return true;
		}else {
			validated = false;
			visited.clear();
			return false;
		}		
	}
	
	/*
	 * Checks number of water field on all of the possible halfmap edges
	 */
	private boolean checkWaterOnSides() {
		int longsideup = (int)mapfields.stream().filter(field -> field.getY() == 0 && field.getTerrain() == Terrain.Water).count();
		int longsidedown = (int)mapfields.stream().filter(field -> field.getY() == 3 && field.getTerrain() == Terrain.Water).count();
	
		int shortsideleft = (int)mapfields.stream().filter(field -> field.getX() == 0 && field.getTerrain() == Terrain.Water).count();
		int shortsideright = (int)mapfields.stream().filter(field -> field.getX() == 7 && field.getTerrain() == Terrain.Water).count();
	
		return longsideup <= 3 && longsidedown <=3 &&  shortsideleft <= 1 && shortsideright <=1;
	}
    
    
	/*
	 * Recursive method that RETURNS TRUE if number of reachable fields is equal to number of grass and mountain fields genereted by the AI
	 * Algorithm from : https://www.geeksforgeeks.org/flood-fill-algorithm-implement-fill-paint/ 
	 */
private void validateReachable(int x,int y) {	
		
		switch(getTerrain(x, y)) {
		case Grass:
				visited.add(getField(x, y));
				break;

		case Mountain:
				visited.add(getField(x, y));
				break;
		case Water:
			break;
		}
		
		if(!checkBounds(x+1,y) && getTerrain(x+1, y) != Terrain.Water && !hasBeenVisited(x+1,y)) {
			validateReachable(x+1,y);
		}
		
		if(!checkBounds(x-1,y) && getTerrain(x-1, y) != Terrain.Water && !hasBeenVisited(x-1,y)) {
			validateReachable(x-1,y);
		}
		
		if(!checkBounds(x,y-1) && getTerrain(x, y-1) != Terrain.Water && !hasBeenVisited(x,y-1)) {
			validateReachable(x,y-1);
		}
		
		if(!checkBounds(x,y+1) && getTerrain(x, y+1) != Terrain.Water  && !hasBeenVisited(x,y+1)) {
			validateReachable(x,y+1);
		}
		
		if(visited.size() == accesiblefields) {
			validated = true;	
		}
		return;
			
	}
	private boolean checkNumberofFields() {
		int watercnt = (int)mapfields.stream().filter(p -> p.getTerrain() == Terrain.Water).count();
		int grasscnt = (int)mapfields.stream().filter(p -> p.getTerrain() == Terrain.Grass).count();
		int mountaincnt = (int)mapfields.stream().filter(p -> p.getTerrain() == Terrain.Mountain).count();
		return watercnt >=4 && grasscnt >= 15 && mountaincnt >= 3;
	}

	private boolean hasBeenVisited(int x,int y) {
		return visited.stream().anyMatch(p -> p.getX() == x && p.getY() == y);
	}
	
	private boolean checkBounds(int x , int y) {
		final int lowerxybound = 0;
		final int upperxbound = 7;
		final int upperybound = 3;
		return x < lowerxybound || x > upperxbound ||  y < lowerxybound || y > upperybound;
	}
	
	public boolean getisValidated() {
		return isvalidated;
	}

	public Terrain getTerrain(int x,int y) {
		return mapfields.stream().filter(p -> p.getX() == x && p.getY() == y).findFirst().get().getTerrain();
	}
	public Field getField(int x , int y) {
		return mapfields.stream().filter(p -> p.getX() == x && p.getY() == y).findFirst().get();
	}
	
}
