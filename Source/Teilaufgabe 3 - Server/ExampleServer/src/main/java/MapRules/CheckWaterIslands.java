package MapRules;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import Map.Field;
import Map.GameMap;
import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import converters.ServerNodesConverter;
import enums.Terrain;
import server.exceptions.IslandCreatedException;

public class CheckWaterIslands implements IRuleMap {
	private	List<Field>visited = new ArrayList<>();
	private List<Field> map = new ArrayList<>();
	private ServerNodesConverter nodesconverter = new ServerNodesConverter();
	private long accesiblefields;
	private boolean validated = false;
	@Override
	public void check(HalfMap map) {
		this.map = nodesconverter.convertNodes(map);
		accesiblefields = map.getNodes().stream().filter(node -> node.getTerrain() != ETerrain.Water).count();
		HalfMapNode start = map.getNodes().stream().filter(node -> node.getTerrain() != ETerrain.Water).findFirst().get();
		validateReachable(start.getX(),start.getY());
		if(!validated) {
			logger.info("accesible:"+ accesiblefields);
			logger.info("visited:"+ visited.size());
			visited.clear();
			logger.error("ID: " + map.getUniquePlayerID() + "created an island.");
			throw new IslandCreatedException("island created", "Playerid: " + map.getUniquePlayerID() + " has created an island!" );
		}
		
		System.out.println("okaccesible:"+ accesiblefields);
		System.out.println("visited:"+ visited.size());
		validated = false;
		visited.clear();
	}
	public Terrain getTerrain(int x,int y) {
		return map.stream().filter(p -> p.getX() == x && p.getY() == y).findFirst().get().getTerrain();
	}
	public Field getField(int x , int y) {
		return map.stream().filter(p -> p.getX() == x && p.getY() == y).findFirst().get();
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
	private boolean hasBeenVisited(int x,int y) {
		return this.visited.stream().anyMatch(p -> p.getX() == x && p.getY() == y);
	}

	private boolean checkBounds(int x , int y) {
		final int lowerxybound = 0;
		final int upperxbound = 7;
		final int upperybound = 3;
		return x < lowerxybound || x > upperxbound ||  y < lowerxybound || y > upperybound;
	}

	
}
