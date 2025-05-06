package MapRules;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import server.exceptions.NotEnoughFieldsException;

public class CheckTerrainCount implements IRuleMap{

	@Override
	public void check(HalfMap map) {
		int watercnt = (int)map.getNodes().stream().filter(p -> p.getTerrain() == ETerrain.Water).count();
		int grasscnt = (int)map.getNodes().stream().filter(p -> p.getTerrain() == ETerrain.Grass).count();
		int mountaincnt = (int)map.getNodes().stream().filter(p -> p.getTerrain() == ETerrain.Mountain).count();
		if( watercnt < 4 || grasscnt < 15 || mountaincnt < 3) {
			logger.error("ID: " + map.getUniquePlayerID() + "didnt create expected number of terrains");
			throw new NotEnoughFieldsException("fields amount", "less field generated that expected.Expected 4 waters , 15 grasses or atleast 3 mountains");
		}
		
	}

}
