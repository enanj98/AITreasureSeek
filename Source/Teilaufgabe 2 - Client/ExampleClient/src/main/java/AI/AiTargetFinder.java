package AI;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import MessagesGameState.EPlayerPositionState;
import MessagesGameState.GameState;
import client.model.Field;
import clientenums.EMapType;
import clientenums.Terrain;
import gameState.MyGameState;
import io.netty.util.internal.ThreadLocalRandom;

public class AiTargetFinder {

    private Field myFortPosition = null;
    
    
	public boolean checkGoldFound(MyGameState gameState) {
		 
		return gameState.getTreasureState();
	}

	// returns clients position in the given moment
	public Field findMyPosition(MyGameState gameState) {		
		return gameState.getMypos();
	}
    /*
     * Depending on the final maps dimensions calculates random grass
     *  field on the right half of map 
     */
	
	/*
	 * Logic taken from Antonio Gasparevic 01506462 start<40>
	 * end <99>
	 */
	public Field findNewTarget(MyGameState gameState, List<Field> visitedFields,Field myPosition) {
	
		List<Field> fields = new ArrayList<>();
	    
		myFortPosition = gameState.getMap().getFields().stream().filter(field -> field.getFortstate() == true).findFirst().get();
	
		
		if(!gameState.getTreasureState()) {
			if(gameState.getMap().getMaptype() == EMapType.foursixteen) {
				if(myFortPosition.getX() > 7) {
					fields = gameState.getMap().getFields().stream().filter( field -> field.getX() > 7).collect(Collectors.toList());				
				}
				else {
					fields = gameState.getMap().getFields().stream().filter( field -> field.getX() < 8).collect(Collectors.toList());					
				}			
			}else{
				if(myFortPosition.getY() > 3) {
					fields = gameState.getMap().getFields().stream().filter( field -> field.getY() > 3).collect(Collectors.toList());
				}
				else{
					fields = gameState.getMap().getFields().stream().filter( field -> field.getY() < 4).collect(Collectors.toList());
				}
				
			}
		}else {
			if(gameState.getMap().getMaptype() == EMapType.foursixteen) {
				if(myFortPosition.getX() > 7) {
					fields = gameState.getMap().getFields().stream().filter( field -> field.getX() < 8).collect(Collectors.toList());
				}
				else {
					fields = gameState.getMap().getFields().stream().filter( field -> field.getX() > 7).collect(Collectors.toList());
				}			
			}else {
				if(myFortPosition.getY() > 3) {
					fields = gameState.getMap().getFields().stream().filter( field -> field.getY() < 4).collect(Collectors.toList());
				}
				else {
					fields = gameState.getMap().getFields().stream().filter( field -> field.getY() > 3).collect(Collectors.toList());
				}
				
			}
			Field retField;
			do {
				retField = fields.get(ThreadLocalRandom.current().nextInt(0,fields.size()-1));
			}while(visitedFields.contains(retField) || retField.getTerrain() != Terrain.Grass || (retField.getX() == myPosition.getX() && retField.getY() == myPosition.getY()));
			
			return retField;	
		}
		

		Field randomField = fields.get(ThreadLocalRandom.current().nextInt(0,fields.size()-1));
		
		do {
			randomField = fields.get(ThreadLocalRandom.current().nextInt(0,fields.size()-1));
		}while(visitedFields.contains(randomField) || randomField.getTerrain() != Terrain.Grass || (randomField.getX() == myPosition.getX() && randomField.getY() == myPosition.getY()));
		
		return randomField;
	}

	public boolean playerMoved(Field playerPosition, Field fieldSource) {
		return playerPosition.getX() != fieldSource.getX()  || fieldSource.getY() != playerPosition.getY();
	}

}
