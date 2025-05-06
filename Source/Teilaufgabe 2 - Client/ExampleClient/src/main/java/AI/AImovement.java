package AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;



import MessagesBase.EMove;
import MessagesGameState.GameState;
import client.model.Field;
import client.model.Halfmap;
import clientenums.Terrain;
import gameState.MyGameState;

public class AImovement {

   private LinkedList<EMove> moves = new LinkedList<>();
  
   private AiTargetFinder aiHelper = new AiTargetFinder(); 
   private Dijkstra dijkstra = new Dijkstra();
   private List<Field> visitedFields = new ArrayList<>();
   private Field playerPosition; 
   private boolean gooldFirstTime = true;
   public EMove getMove(MyGameState gameState) {
	   
	   if(gooldFirstTime && aiHelper.checkGoldFound(gameState)) {
		   moves.clear();
		   gooldFirstTime = false;
	   }
	   Field fieldSource = aiHelper.findMyPosition(gameState); 
	   
	   if (playerPosition == null) {
		   playerPosition = fieldSource;
	   }
	 
	   if(!moves.isEmpty() && aiHelper.playerMoved(playerPosition, fieldSource)) {
		   if(!visitedFields.contains(fieldSource))
			   visitedFields.add(fieldSource);
		   playerPosition = fieldSource;		
		   return moves.pollFirst();
	   }
	   playerPosition = fieldSource;
	   
	   
	   if(moves.isEmpty()) {
			Field fieldTarget = aiHelper.findNewTarget(gameState, visitedFields,playerPosition); 
			Map<Field, FieldAtributs> mapTable = dijkstra.calculateShortestPath(gameState, visitedFields);
			moves = dijkstra.createListOfMoves(mapTable,playerPosition,fieldTarget);
			Collections.reverse(moves);
			EMove moveToReturn = moves.pollFirst();
			return moveToReturn;
	   }
	   return moves.pollFirst();

   }
   
	
}
