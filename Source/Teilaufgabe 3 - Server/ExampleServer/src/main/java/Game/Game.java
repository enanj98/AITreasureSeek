package Game;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Map.Field;
import Map.GameMap;
import Map.MapHandler;
import MessagesGameState.EPlayerGameState;
import server.exceptions.MaxPlayersRegisteredException;

public class Game {
	private List<Player>players = new ArrayList<>();
	private GameMap map = new GameMap();
	private Instant creationTime = Instant.now();
	private String gameStateID;
	private Logger logger = LoggerFactory.getLogger(getClass());

	public void addPlayer(Player player) {
		if(players.size() == 2) {
			throw new MaxPlayersRegisteredException("max players exception","there are already two players registered");
		}
		players.add(player);
		logger.info("Player with id:" + player.getUniqueplayerid() + " has registered in a game.");
	}
	
	public Game() {
		super();
	}
	public Player getPlayerByID(String id) {
		return players.stream().filter(player -> player.getUniqueplayerid().equals(id)).findFirst().get();
	}

	public List<Player> getPlayers(){
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public GameMap getMap() {
		return map;
	}

	public String getgameStateID() {
		return gameStateID;
	}
	public void setgameStateID(String gamestateid) {
		this.gameStateID = gamestateid;
	}
	public Instant getCreationTime() {
		return creationTime;
	}
	public void setMapFields(List<Field> fields) {
		map.setFields(fields);
	}
	public void ChoosePlayerToAct() {
		if(players.size() != 2) return;
		if(players.stream().allMatch(player -> player.getState() == EPlayerGameState.ShouldWait) ) {
			Random random = new Random();
			int playersamount = 2;
			players.get(random.nextInt(playersamount)).setState(EPlayerGameState.ShouldActNext);
			players.stream().filter( player -> player.getState() == EPlayerGameState.ShouldActNext).findFirst().get().setTimer(Instant.now());
		}else {			
			for(Player eachPlayer : players) {
				if(eachPlayer.getState() == EPlayerGameState.ShouldActNext) {
					eachPlayer.setState(EPlayerGameState.ShouldWait);
				}else if(eachPlayer.getState() == EPlayerGameState.ShouldWait) {
					eachPlayer.setState(EPlayerGameState.ShouldActNext);
					eachPlayer.setTimer(Instant.now());
				}
			}
			
		}
	}

	public void setLoser(String uniquePlayerID) {
		for(Player eachPlayer : players) {
			if(eachPlayer.getUniqueplayerid().equals(uniquePlayerID)) {
				eachPlayer.setState(EPlayerGameState.Lost);
				logger.info("PlayerID: " + uniquePlayerID + " has lost the the game!");
			}else {
				eachPlayer.setState(EPlayerGameState.Won);
				logger.info("PlayerID: " + eachPlayer.getUniqueplayerid() + " has won the the game!");
			}
		}
		
	}
	
	
}
