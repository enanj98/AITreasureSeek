package MapRules;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import server.exceptions.TooMuchWaterEdgesException;

public class CheckWateronSides implements IRuleMap{
	

	@Override
	public void check(HalfMap map) {
		
		int longsideup = (int)map.getNodes().stream().filter(field -> field.getY() == 0 && field.getTerrain() == ETerrain.Water).count();
		int longsidedown = (int)map.getNodes().stream().filter(field -> field.getY() == 3 && field.getTerrain() == ETerrain.Water).count();
	
		int shortsideleft = (int)map.getNodes().stream().filter(field -> field.getX() == 0 && field.getTerrain() == ETerrain.Water).count();
		int shortsideright = (int)map.getNodes().stream().filter(field -> field.getX() == 7 && field.getTerrain() == ETerrain.Water).count();
		
		if( longsideup > 3 || longsidedown > 3 ||  shortsideleft > 1 || shortsideright > 1) {
			logger.error("ID: " + map.getUniquePlayerID() + "had water on corners");
			throw new TooMuchWaterEdgesException("water check", "Too many waters on edges");
		}
		
		}
	
}
