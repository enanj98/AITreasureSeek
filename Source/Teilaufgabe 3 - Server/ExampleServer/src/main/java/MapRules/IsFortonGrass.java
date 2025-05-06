package MapRules;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import enums.Terrain;
import server.exceptions.FortNotGrassException;

public class IsFortonGrass implements IRuleMap{

	@Override
	public void check(HalfMap map) {
		if(map.getNodes()
				.stream()
				.filter(field -> field.isFortPresent()).findFirst().get().getTerrain() != ETerrain.Grass) {
			logger.error("ID: " + "has placed fort on non grass field!");
			throw new FortNotGrassException("fort not on grass", "Playerid: " + map.getUniquePlayerID() + "map contains fort placed on non-grass field!" );
		}
	}

	
}
