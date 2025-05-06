package MapRules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import converters.ServerNodesConverter;

public interface IRuleMap {
	Logger logger = LoggerFactory.getLogger(IRuleMap.class);
	public void check(HalfMap map);
}
