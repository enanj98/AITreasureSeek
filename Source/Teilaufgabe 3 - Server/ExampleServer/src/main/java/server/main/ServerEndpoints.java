package server.main;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import Game.Game;
import Game.GamesRepository;
import Game.PlayerHandler;
import Map.MapHandler;
import MapRules.CheckOneFort;
import MapRules.CheckTerrainCount;
import MapRules.CheckWaterIslands;
import MapRules.CheckWateronSides;
import MapRules.CheckSize32;
import MapRules.IRuleMap;
import MapRules.IsFortonGrass;
import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.FullMap;
import MessagesGameState.GameState;
import converters.ServerNodesConverter;
import enums.PlayerState;
import generators.GameIDGenerator;
import generators.GameStateIDGenerator;
import server.exceptions.GameNotExistingException;
import server.exceptions.GenericExampleException;
@EnableScheduling
@Controller
@RequestMapping(value = "/games")
public class ServerEndpoints {
	private GamesRepository  gamerepository = new GamesRepository();
	private GameIDGenerator idgen = new GameIDGenerator();
	private PlayerHandler playerhandler = new PlayerHandler();
	private MapHandler maphandler = new MapHandler();
	private GameStateIDGenerator gamestategen = new GameStateIDGenerator();

	private ArrayList<IRuleMap> irulemap = new ArrayList<>() {

		{
			add(new CheckWaterIslands());
			add(new CheckSize32());
			add(new CheckTerrainCount());
			add(new CheckWateronSides());
			add(new CheckOneFort());
			add(new IsFortonGrass());
		}
	};
	
	@Scheduled(initialDelay = 600000,fixedDelay = 15000)
	public void removeExpiredGamesTask() {
		gamerepository.removeOlderGames(Instant.now());
	}
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame() {		
		String gameid = idgen.generateGameID();
		//If key exists in a gamerepo , we try to regenerate new one
		while(gamerepository.getGames().containsKey(gameid)) {
			gameid = idgen.generateGameID();
		}
		UniqueGameIdentifier gameIdentifier = new UniqueGameIdentifier(gameid);
		gamerepository.addNewGame(gameid, new Game());
		gamerepository.setGameStateID(gameid,gamestategen.generateGameStateID());
		return gameIdentifier;

		// note you will need to include additional logic, e.g., additional classes
		// which create, store, validate, etc. game ids
	}
	
	@RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<?> transferMap(@PathVariable String gameID,
			@Validated @RequestBody HalfMap halfmap) {
		if(!gamerepository.getGames().containsKey(gameID)) {
			throw new GameNotExistingException("wrong gameid","game with id:" + gameID + "does not exist!");		
		}
		try {
			irulemap.forEach(eachRule -> eachRule.check(halfmap));
		}catch(GenericExampleException e) {
			gamerepository.getGameByID(gameID).setLoser(halfmap.getUniquePlayerID());
			return new ResponseEnvelope<>(e);
		}
		
		
		maphandler.addMap(gameID,halfmap,gamerepository.getGameByID(gameID));
		
		return new ResponseEnvelope<>();

		// note you will need to include additional logic, e.g., additional classes
		// which create, store, validate, etc. game ids
	}
	

	// example for a POST endpoint based on games/{gameID}/players
	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(@PathVariable String gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {
		if(!gamerepository.getGames().containsKey(gameID)) {
			throw new GameNotExistingException("game doesnt exist", "Game with game id: " + gameID + "does not exist on the server");
		}
		UniquePlayerIdentifier playeridentifier = playerhandler.registerPlayer(gameID,playerRegistration,gamerepository.getGameByID(gameID));
		ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(new UniquePlayerIdentifier(playeridentifier));
		
		return playerIDMessage;
	}
	@RequestMapping(value = "/{gameID}/states/{playerID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> retrieveGameState(@PathVariable String gameID,
			@PathVariable String playerID) {
		if(!gamerepository.getGames().containsKey(gameID)) {
			throw new GameNotExistingException("gameState","Game with the given id does not exist");
		}
		GameState gamestate = playerhandler.getMyGameState(gameID, playerID, gamerepository.getGameByID(gameID));		
		return new ResponseEnvelope<GameState>(gamestate);
	}


	@ExceptionHandler({ GenericExampleException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(GenericExampleException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());

		// reply with 200 OK as defined in the network documentation
		// Side note: We only do this here for simplicity reasons. For future projects,
		// you should check out HTTP status codes and
		// what they can be used for. Note, the WebClient used on the client can react
		// to them using the .onStatus(...) method.
		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
	

	
}
