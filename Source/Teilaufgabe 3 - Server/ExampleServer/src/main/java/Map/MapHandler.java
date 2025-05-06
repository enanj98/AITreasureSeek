package Map;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Game.Game;
import Game.GamesRepository;
import Game.Player;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesGameState.EPlayerGameState;
import converters.FortConverter;
import converters.TerrainConverter;
import enums.EMapType;
import enums.FortState;
import generators.GameStateIDGenerator;
import server.exceptions.GameNotExistingException;
import server.exceptions.NotPlayersTurnException;
import server.exceptions.PlayerNotExistingException;
import server.exceptions.ThreeMapsSentException;
import server.exceptions.ThreeSecondsException;
import server.exceptions.TwoPlayersException;

public class MapHandler {
	private TerrainConverter terrainconverter = new TerrainConverter();
	private GameStateIDGenerator gamestategen = new GameStateIDGenerator();
	private FortConverter fortconverter = new FortConverter();
	private Logger logger = LoggerFactory.getLogger(getClass());
	public List<Field> addMap(String gameID, HalfMap halfmap,Game game) {
		String uniquePlayerID = halfmap.getUniquePlayerID();
		if(game.getPlayers().stream().noneMatch(p -> p.getUniqueplayerid().toString().equals(uniquePlayerID))) {
			logger.error("ID:" + uniquePlayerID + "does not  exist!");
			throw new PlayerNotExistingException("wrong playerid","player with id:" + uniquePlayerID + "does not  exist!");	
		}
		if(game.getMap().getMaptype() == EMapType.eighteight || game.getMap().getMaptype() == EMapType.foursixtteen) {
			logger.error("ID:" + uniquePlayerID + "sent a map more than once!");
			throw new ThreeMapsSentException("two maps already transfered","player with id:" + uniquePlayerID + "sent his map for second time!");	
		}
		if(game.getPlayers().size() != 2) {
			logger.error("ID:" + uniquePlayerID + "game didnt contain two players before map was sent!");
			throw new TwoPlayersException("not two players","game didnt contain two players before map was sent");	
		}
		if(game.getPlayerByID(uniquePlayerID).getState() == EPlayerGameState.ShouldWait) {
			game.setLoser(uniquePlayerID);
			logger.error("ID:" + uniquePlayerID + "sent a map while it wasnt his turn!");
			throw new NotPlayersTurnException(gameID,"Playerid: " + uniquePlayerID + "sent a map while it wasnt his turn");
		}
		if(Duration.between(game.getPlayerByID(uniquePlayerID).getsendCommandTime(),Instant.now()).toSeconds() >= Duration.ofSeconds(3).toSeconds()) {
			game.setLoser(uniquePlayerID);
			logger.error("ID:" + uniquePlayerID + "didnt send map in 3 seconds!");
			throw new ThreeSecondsException("3 seconds exception","3 seconds have passed and the client didnt send his map");
		}
		logger.info("player with id: " + uniquePlayerID + "has sent a map");
		if(game.getMap().getMaptype() == EMapType.halfmap) {
			chooseRandomMapType(game.getMap());
			game.setMapFields(convertFullMapFields(halfmap,game));
			
			game.ChoosePlayerToAct();
			game.setgameStateID(gamestategen.generateGameStateID());
			
		}
		if(game.getMap().getMaptype() == EMapType.nomap) {
			game.setMapFields(convertHalfMapFields(game,halfmap));
			game.getMap().setMaptype(EMapType.halfmap);
			game.ChoosePlayerToAct();
			game.setgameStateID(gamestategen.generateGameStateID());			
		}
		return game.getMap().getFields();
		
	}
	
	
	
	private void chooseRandomMapType(GameMap map) {
		Random random = new Random();
		if(random.nextBoolean()) {
			map.setMaptype(EMapType.eighteight);
		}else {
			map.setMaptype(EMapType.foursixtteen);
		}
	}
	
	private void setPlayersFort(Field field,Player player) {
		player.setFortpos(field.getX(),field.getY());
	}

	
	private List<Field> convertHalfMapFields(Game game,HalfMap halfmap){
		Collection<HalfMapNode> nodes = halfmap.getNodes();
		
		List<Field> fields = new ArrayList<>();
		
		nodes.forEach(node -> fields.add(new Field(node.getX(),node.getY(),node.isFortPresent(),terrainconverter.convertTerrain(node.getTerrain()))));
		Field field = fields.stream().filter(node -> node.getFortstate() == FortState.MyFortPresent).findFirst().get();
		if(game.getMap().getMaptype() == EMapType.nomap) {
			setPlayersFort(field,game.getPlayerByID(halfmap.getUniquePlayerID()));
		}
		return fields;
	}
	private List<Field> convertFullMapFields(HalfMap halfmap,Game game){	
		List<Field> fields = new ArrayList<>();
			
		if(game.getMap().getMaptype() == EMapType.eighteight) {
			fields = combineEightEight(convertHalfMapFields(game,halfmap),game.getMap().getFields(),game,game.getPlayerByID(halfmap.getUniquePlayerID()));			
		}else if(game.getMap().getMaptype() == EMapType.foursixtteen){
			fields = combineFourSixteen(convertHalfMapFields(game,halfmap),game.getMap().getFields(),game,game.getPlayerByID(halfmap.getUniquePlayerID()));
		}
		return fields;
	}
	private List<Field> combineEightEight(List<Field>nodes,List<Field>fields,Game game,Player player){
		List<Field> combinedfields = new ArrayList<>();
		
		final int xcoordtop = 8;
		Random random = new Random();
		Field castlepos;
		Field castleposenemy;
		if(random.nextBoolean()) {
			combinedfields.addAll(fields);
			nodes.forEach(node -> combinedfields.add(new Field(node.getX() + xcoordtop,node.getY(),fortconverter.convertFortState(node.getFortstate()),node.getTerrain())));
			castlepos = combinedfields.stream().filter(pos -> pos.getX() > 7  && pos.getFortstate() == FortState.MyFortPresent).findFirst().get();
			setPlayersFort(castlepos,player);
			castleposenemy = combinedfields.stream().filter(pos -> pos.getX() < 8 && pos.getFortstate() == FortState.MyFortPresent).findFirst().get();
			game.getPlayers().stream().filter(eachPlayer -> !eachPlayer.getUniqueplayerid().equals(player.getUniqueplayerid())).findFirst().get().setFortpos(castleposenemy.getX(), castleposenemy.getY());
		}else {
			fields.forEach(field -> combinedfields.add(new Field(field.getX() + xcoordtop,field.getY(),fortconverter.convertFortState(field.getFortstate()),field.getTerrain())));
			nodes.forEach(node -> combinedfields.add(node));
			castlepos = combinedfields.stream().filter(pos -> pos.getX() < 8 && pos.getFortstate() == FortState.MyFortPresent).findFirst().get();
			setPlayersFort(castlepos,player);
			castleposenemy = combinedfields.stream().filter(pos -> pos.getX() > 7 && pos.getFortstate() == FortState.MyFortPresent).findFirst().get();
			game.getPlayers().stream().filter(eachPlayer -> !eachPlayer.getUniqueplayerid().equals(player.getUniqueplayerid())).findFirst().get().setFortpos(castleposenemy.getX(), castleposenemy.getY());
		}
		
		return combinedfields;
		}
	private List<Field> combineFourSixteen(List<Field>nodes,List<Field>fields,Game game,Player player){
		List<Field> combinedfields = new ArrayList<>();
		final int ycoorddown = 4;
		Random random = new Random();
		Field castlepos;
		Field castleposenemy;
		if(random.nextBoolean()) {
			combinedfields.addAll(fields);
			nodes.forEach(node -> combinedfields.add(new Field(node.getX(),node.getY() + ycoorddown,fortconverter.convertFortState(node.getFortstate()),node.getTerrain())));
			castlepos = combinedfields.stream().filter(pos -> pos.getY() > 3 && pos.getFortstate() == FortState.MyFortPresent).findFirst().get();
			castleposenemy = combinedfields.stream().filter(pos -> pos.getY() < 4 && pos.getFortstate() == FortState.MyFortPresent).findFirst().get();
			game.getPlayers().stream().filter(eachPlayer -> !eachPlayer.getUniqueplayerid().equals(player.getUniqueplayerid())).findFirst().get().setFortpos(castleposenemy.getX(), castleposenemy.getY());
			setPlayersFort(castlepos,player);
		}else {
			fields.forEach(field -> combinedfields.add(new Field(field.getX(),field.getY() + ycoorddown,fortconverter.convertFortState(field.getFortstate()),field.getTerrain())));
			nodes.forEach(node -> combinedfields.add(new Field(node.getX(),node.getY(),fortconverter.convertFortState(node.getFortstate()),node.getTerrain())));
			castlepos = combinedfields.stream().filter(pos -> pos.getY() < 4 && pos.getFortstate() == FortState.MyFortPresent).findFirst().get();
			setPlayersFort(castlepos,player);
			castleposenemy = combinedfields.stream().filter(pos -> pos.getY() > 3 && pos.getFortstate() == FortState.MyFortPresent).findFirst().get();
			game.getPlayers().stream().filter(eachPlayer -> !eachPlayer.getUniqueplayerid().equals(player.getUniqueplayerid())).findFirst().get().setFortpos(castleposenemy.getX(), castleposenemy.getY());
		}
		
		return combinedfields;
		}
	
}
