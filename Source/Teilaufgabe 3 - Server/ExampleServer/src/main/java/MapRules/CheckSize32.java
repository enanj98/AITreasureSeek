package MapRules;

import MessagesBase.HalfMap;
import server.exceptions.LessFieldsException;

public class CheckSize32 implements IRuleMap {

	@Override
	public void check(HalfMap map) {
		if(map.getNodes().size() != 32) {
			logger.error("ID: " + map.getUniquePlayerID() + "map cointained" + map.getNodes().size() + " fields.Expected was 32");
			throw new LessFieldsException("fields", "map cointained" + map.getNodes().size() + " fields.Expected was 32");
		}
		
	}


	

}
