package MapRules;

import MessagesBase.HalfMap;
import server.exceptions.FortNumberException;

public class CheckOneFort implements IRuleMap{

	@Override
	public void check(HalfMap map) {
		if(map.getNodes().stream().filter(node -> node.isFortPresent()).count() != 1) {
			logger.error("ID: " + map.getUniquePlayerID() + "had more than one fort on the halfmap"  );
			throw new FortNumberException("fort exception","excepted only one fort on the halfmap");
		}
		
	}

}
