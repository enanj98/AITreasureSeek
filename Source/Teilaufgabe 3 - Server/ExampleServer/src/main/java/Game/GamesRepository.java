package Game;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GamesRepository {
 private Map<String,Game>games = new HashMap<>();
 private List<String>gamespos = new ArrayList<>();
 Logger logger = LoggerFactory.getLogger(getClass());
 public void addNewGame(String gameID,Game game) {
	 removeExpiredPos();
	 if(games.size() >=1000) {
		 games.remove(gamespos.get(0));
		 gamespos.remove(0);
	 }
	 	logger.info("Gameid: " + gameID + " has been added to the server");
		 games.put(gameID, game);
		 gamespos.add(gameID);
		 
 }

public Map<String, Game> getGames() {
	return games;
}
public Game getGameByID(String id) {
	return games.get(id);
}

/*
 * removes games positions of games that the server doesnt contain anymore
 */
private void removeExpiredPos() {
	gamespos = gamespos.stream().filter(position -> games.containsKey(position)).collect(Collectors.toList());
}
public void removeOlderGames(Instant timeNow) {
	long gameexpireTime = 10;
	var iterator = games.entrySet().iterator();
	while (iterator.hasNext()) {
		var entry = iterator.next();
		if(Duration.between(timeNow, entry.getValue().getCreationTime()).abs().toMinutes()  > gameexpireTime) {
			iterator.remove();
		}
	}
	logger.info("checking if there are games that have expired...");
	logger.info("Server contains:" + games.size() + "  games");
}

public void setGameStateID(String gameID, String gamestateID) {
	games.get(gameID).setgameStateID(gamestateID);
}

}
