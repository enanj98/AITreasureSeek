package Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import Map.Field;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import converters.ServerNodesConverter;
import generators.GameStateIDGenerator;
import generators.PlayerIDGenerator;
import server.exceptions.GameNotExistingException;
import server.exceptions.GenericExampleException;
import server.exceptions.PlayerNotExistingException;
public class PlayerHandler {
	private ServerNodesConverter fieldconverter = new ServerNodesConverter();
	private GameStateIDGenerator gamestategen = new GameStateIDGenerator();
	public PlayerHandler() {
		super();		
	}

	public UniquePlayerIdentifier registerPlayer(String gameID, PlayerRegistration playerRegistration, Game game) {
		
		PlayerIDGenerator playeridgen = new PlayerIDGenerator();
		UniquePlayerIdentifier playerid = playeridgen.generatePlayerID();
		Player player = new Player(playerRegistration.getStudentFirstName(),playerRegistration.getStudentLastName(),playerRegistration.getStudentID());
		player.setUniquePlayerID(playerid.getUniquePlayerID());	
		game.addPlayer(player);
		
		///added newly
		game.ChoosePlayerToAct();
		game.setgameStateID(gamestategen.generateGameStateID());
		return playerid;
	}
	
	public GameState getMyGameState(String gameID,String playerid , Game game) {
		
		if(game.getPlayers().stream().noneMatch(player -> player.getUniqueplayerid().equals(playerid))) {
			throw new PlayerNotExistingException("gameState","Player with the given id does not exist");
		}
		Collection<PlayerState> states = retrievePlayerStates(gameID, playerid, game);
		int fortfieldx = game.getPlayerByID(playerid).getfortx();
		int fortfieldy = game.getPlayerByID(playerid).getforty();
		Collection<FullMapNode> nodes = fieldconverter.convertServerNodes(game.getMap(),fortfieldx,fortfieldy);
		
		
		Optional<FullMap> fullmap = Optional.ofNullable(new FullMap(nodes));
		return new GameState(fullmap,states,game.getgameStateID());
		
	}
	private Collection<PlayerState> retrievePlayerStates(String gameID,String playerID,Game game){
		Collection<PlayerState> playerStates = new ArrayList<>();
		
		game.getPlayers().stream().filter(player -> player.getUniqueplayerid().equals(playerID)).forEach(player ->
			playerStates.add(new PlayerState(player.getStudentFirstName(),player.getStudentLastName(),player.getStudentID(),player.getState(),new UniquePlayerIdentifier(player.getUniqueplayerid()),player.getTreasureState()))
		);
		game.getPlayers().stream().filter(player -> !player.getUniqueplayerid().equals(playerID)).forEach(player ->
			playerStates.add(new PlayerState(player.getStudentFirstName(),player.getStudentLastName(),player.getStudentID(),player.getState(),new UniquePlayerIdentifier("hidden"),player.getTreasureState()))
		);
		
		return playerStates;	
	}
	public Player getPlayerByID(String uniqueID,Game game) {
		return game.getPlayers().stream().filter(player -> player.getUniqueplayerid().equals(uniqueID)).findFirst().get();
	}
    
}
