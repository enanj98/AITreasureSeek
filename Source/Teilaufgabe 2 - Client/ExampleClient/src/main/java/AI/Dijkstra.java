package AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import MessagesBase.EMove;
import MessagesGameState.GameState;
import client.model.Field;
import clientenums.Terrain;
import gameState.MyGameState;

public class Dijkstra {
	 private AiTargetFinder aihelper = new AiTargetFinder();
	 private Map<Field, FieldAtributs> distances = new HashMap<>();
	 
	 //https://www.baeldung.com/java-dijkstra  && Antonio Gasparevic 01506462
	 //start<23>//<end 49>
	public Map<Field, FieldAtributs> calculateShortestPath(MyGameState gameState,List<Field>visitedFields) {
		distances.clear();
		Field source = gameState.getMypos();
		
		Set<Field> unvisited = new HashSet<>();
		Set<Field> visited = new HashSet<>();
		
		distances.put(source, new FieldAtributs(0, source));
		for(Field field : gameState.getMap().getFields()) {
			distances.putIfAbsent(field,new FieldAtributs(1000,field));
		}
		unvisited.add(source);
		
		while(unvisited.size() != 0) {
			Field currentPos = getLowestDistanceField(unvisited);
			unvisited.remove(currentPos);
			for(Field nfield : getNeighbours(gameState,currentPos)) {
				Integer edgeWeight = calculateWeight(nfield, visitedFields);
				if(!visited.contains(nfield)) {
					calculateMinDistance(nfield,edgeWeight,currentPos);
					unvisited.add(nfield);
				}
			}
		 visited.add(currentPos);
		}
		return distances;
	}
	
	//Antonio Gasparevic 01506462
	//start<53> end<69>
	private Integer calculateWeight(Field neighbour ,List<Field>visited){
		int weight = 1000;
		
		switch(neighbour.getTerrain()) {
		case Grass:
			weight = 1;
			break;
		case Mountain:
			weight = 5;
			break;
        default:
        	break;
		
		}
		if(visited.contains(neighbour)) return weight = weight * 2;
		return weight;
	}							  //field         //currf
	                             //target        ////prev
	private EMove nextMove(Field pos, Field otherpos) {

				if(otherpos.getX() == pos.getX() + 1 && otherpos.getY() == pos.getY()) {
					return EMove.Left;
				}else if(otherpos.getX() == pos.getX() - 1  && otherpos.getY() == pos.getY()) {
					return EMove.Right;
				}else if(otherpos.getY() == pos.getY() + 1  && otherpos.getX() == pos.getX()) {
					return EMove.Up;
				}else {
					return EMove.Down;
				}
		
		
	}
	/*	Code taken from:https://www.baeldung.com/java-dijkstra
	 * Start<88> End<95>
	 */

  private void calculateMinDistance(Field nfield, Integer edgeWeight, Field currentPos) {
		Integer currentPosDistance = distances.get(currentPos).getDistance();
		if(currentPosDistance + edgeWeight < distances.get(nfield).getDistance()) {
			distances.get(nfield).setDistance(currentPosDistance + edgeWeight);
			distances.get(nfield).setPreviuosField(currentPos);
		}
		
	}
	/*	Code taken from:https://www.baeldung.com/java-dijkstra
	 * Start<100> End<113>
	 */

	private Field getLowestDistanceField(Set<Field> unsettledFields) {
		Field lowestdistancefield = null;
		int lowestDistance = 10000;
		
		for(Field field : unsettledFields) {
			int fieldDistance = distances.get(field).getDistance();
			if(fieldDistance < lowestDistance) {
				lowestDistance = fieldDistance;
                lowestdistancefield = field;
			}
		}
		return lowestdistancefield;
		
	}
	
	private List<Field> getNeighbours(MyGameState gamestate , Field currentPos) {
		// TODO Auto-generated method stub
		List<Field> neighbours = new ArrayList<>();
		if(gamestate.getMap().getFields().contains(currentPos) ) {
			if(gamestate.getMap().getFields().stream().anyMatch( field -> field.getX() == currentPos.getX()+1 && field.getY() == currentPos.getY())){
				if(gamestate.getMap().getFields().stream().filter( field -> field.getX() == currentPos.getX()+1 && field.getY() == currentPos.getY()).findFirst().get().getTerrain() != Terrain.Water) {
					neighbours.add(gamestate.getMap().getFields().stream().filter( field -> field.getX() == currentPos.getX()+1 && field.getY() == currentPos.getY()).findFirst().get());
				}
			}
		}
		if(gamestate.getMap().getFields().contains(currentPos) ) {
			if(gamestate.getMap().getFields().stream().anyMatch( field -> field.getX() == currentPos.getX()-1 && field.getY() == currentPos.getY())){
				if(gamestate.getMap().getFields().stream().filter( field -> field.getX() == currentPos.getX()-1 && field.getY() == currentPos.getY()).findFirst().get().getTerrain() != Terrain.Water) {
					neighbours.add(gamestate.getMap().getFields().stream().filter( field -> field.getX() == currentPos.getX()-1 && field.getY() == currentPos.getY()).findFirst().get());
				}
			}
		}
		if(gamestate.getMap().getFields().contains(currentPos) ) {
			if(gamestate.getMap().getFields().stream().anyMatch( field -> field.getX() == currentPos.getX() && field.getY() == currentPos.getY()+1)){
				if(gamestate.getMap().getFields().stream().filter( field -> field.getX() == currentPos.getX() && field.getY() == currentPos.getY()+1).findFirst().get().getTerrain() != Terrain.Water) {
					neighbours.add(gamestate.getMap().getFields().stream().filter( field -> field.getX() == currentPos.getX() && field.getY() == currentPos.getY()+1).findFirst().get());
				}
			}
		}
		if(gamestate.getMap().getFields().contains(currentPos) ) {
			if(gamestate.getMap().getFields().stream().anyMatch( field -> field.getX() == currentPos.getX() && field.getY() == currentPos.getY()-1)){
				if(gamestate.getMap().getFields().stream().filter( field -> field.getX() == currentPos.getX() && field.getY() == currentPos.getY()-1).findFirst().get().getTerrain() != Terrain.Water) {
					neighbours.add(gamestate.getMap().getFields().stream().filter( field -> field.getX() == currentPos.getX() && field.getY() == currentPos.getY()-1).findFirst().get());
				}
			}
		}
		return neighbours;
	} 
         																		   //playerpos          target
	public LinkedList<EMove> createListOfMoves(Map<Field, FieldAtributs> mapTable, Field fieldSource, Field fieldTarget) {
		LinkedList<EMove>moves = new LinkedList<>();
		Field currfield = fieldTarget;
		Field field = mapTable.get(fieldTarget).getPreviuosField();

		while(field != fieldSource) {
			if(currfield.getTerrain() == Terrain.Mountain) {
				if(field.getTerrain() == Terrain.Mountain) {
					moves.add(nextMove(currfield, field));
					moves.add(nextMove(currfield, field));
					moves.add(nextMove(currfield, field));
					moves.add(nextMove(currfield, field));
				}else {
					moves.add(nextMove(currfield, field));
					moves.add(nextMove(currfield, field));
					moves.add(nextMove(currfield, field));
				}
			}
			else {
				if(field.getTerrain() == Terrain.Grass) {
					moves.add(nextMove(currfield, field));
				    moves.add(nextMove(currfield, field));
				}else {
					moves.add(nextMove(currfield, field));
				    moves.add(nextMove(currfield, field));
				    moves.add(nextMove(currfield, field));
				}
				
				
			}
			currfield = mapTable.get(currfield).getPreviuosField();
			field = mapTable.get(field).getPreviuosField();
		}
		
		if(field == fieldSource) {
			if(currfield.getTerrain() == Terrain.Mountain ) {
				if(field.getTerrain() == Terrain.Mountain) {
					moves.add(nextMove(currfield, field));
					moves.add(nextMove(currfield, field));
					moves.add(nextMove(currfield, field));
					moves.add(nextMove(currfield, field));
				}else {
					moves.add(nextMove(currfield, field));
					moves.add(nextMove(currfield, field));
					moves.add(nextMove(currfield, field));
				}
			}else {
				if(field.getTerrain() == Terrain.Grass) {
					moves.add(nextMove(currfield, field));
				    moves.add(nextMove(currfield, field));
				}else {
					moves.add(nextMove(currfield, field));
				    moves.add(nextMove(currfield, field));
				    moves.add(nextMove(currfield, field));
				}
			}
		}
		return moves;
	}
	
}
