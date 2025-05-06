package client.controller;





import org.slf4j.LoggerFactory;



import AI.AImovement;
import MessagesBase.EMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.EPlayerPositionState;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import MessagesGameState.PlayerState;
import client.converter.FieldConverter;
import client.mapmodel.HalfMapController;
import client.model.Halfmap;
import communication.Network;
import exceptions.IncorrectGameStateException;
import exceptions.RegistrationException;
import gameState.MyGameState;
import view.CliView;

import org.slf4j.Logger;



public class PlayerController {

	Logger logger = LoggerFactory.getLogger(PlayerController.class);
	
	private HalfMapController mapcontroller  = new HalfMapController();;
	private Network network;
	private MyGameState mygamestate;
	private CliView view;
	private AImovement aimovement = new AImovement();
	private int myroundcounter;
	
	public PlayerController(String serverBaseUrl,String gameID) {
		this.network = new Network(serverBaseUrl,gameID);
		this.mygamestate = new MyGameState();
		this.view = new CliView(mygamestate);
		
	}
	
	@SuppressWarnings("unused")
	public void registerPlayer(PlayerRegistration player) throws RegistrationException {
		 network.registerPlayersID(network.getGameId(), player);
	}
	
	/*
	 * Client runs the loop as long as its not his turn
	 * 
	 */
	private boolean checkTurn() {
		do {
			for(PlayerState p : network.getGameState().getPlayers()){
			   if(p.getUniquePlayerID().equals(network.getplayerId())) {
				   mygamestate.setPlayerstate(p.getState());	
			   }
			}
			try{
			  Thread.sleep(100);
			}catch (InterruptedException e) {
			  e.printStackTrace();
			}
		}while(mygamestate.getPlayerGameState() != EPlayerGameState.ShouldActNext);
		return true;
    }
	
	private void sendHalfmap() {
		Halfmap map = mapcontroller.getMyhalfmap();
	    checkTurn();
	    	
		@SuppressWarnings("unused")
		ResponseEnvelope<Halfmap> resultReg = network.sendMap(network.getGameId(),map);
			mapcontroller.getMyhalfmap().getFields().stream().count();
			mygamestate.setMap(map);
		    
	
	}
	/*
	 * Main game Looop which run untill either one player has won the game or number of moves that client sent is 100
	 * 
	 */

	private void runGame()  {
		boolean fortSet = false;
		myroundcounter = 1;
		 do {
			 
			 for(PlayerState playerState : network.getGameState().getPlayers()){
				 if(playerState.getUniquePlayerID().equals(network.getplayerId())) {
					 if(mygamestate.getPlayerGameState() != playerState.getState()) {					
						 mygamestate.setPlayerstate(playerState.getState());
						 mygamestate.setTreasureState(playerState.hasCollectedTreasure());
					 }
				 }
			 }
			 
			 
			 switch(mygamestate.getPlayerGameState()) {
			 	case ShouldWait:
			 		if(mygamestate.getMap().getMaptype() == null && network.getGameState().getMap().get().getMapNodes().size() == 64) {
						mygamestate.setMap(FieldConverter.convertServersMap(mapcontroller,network.getGameState().getMap().get()));
					}
			 					 		
			 		break;
			 	case ShouldActNext:
					if(mygamestate.getMap().getMaptype() == null && network.getGameState().getMap().get().getMapNodes().size() == 64) {
						mygamestate.setMap(FieldConverter.convertServersMap(mapcontroller,network.getGameState().getMap().get()));
					}
					if(!fortSet) {
					mygamestate.setMypos(mygamestate.getMap().getFields().stream().filter(field -> field.getFortstate()).findFirst().get());
					fortSet = true;
					}
					checkTurn();
					changeofPos();
					network.sendMove(aimovement.getMove(mygamestate),network.getplayerId());
					System.out.println(" move number: " + myroundcounter++);
					break;
			 	case Won:
			 		 logger.info("Player with the Player ID :" + network.getplayerId() + " has won the game!!!");
			 		 break;
			 		
			 	case Lost:
			 		 logger.info("Player with the Player ID :" + network.getplayerId() + " has lost the game!!!");
			 		 break;
			 		 
			 	    default:
			 		throw new IncorrectGameStateException("gamestate is unknown");
			 		 
			 		
			 }
			 
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		 }while(mygamestate.getPlayerGameState() != EPlayerGameState.Won && mygamestate.getPlayerGameState() != EPlayerGameState.Lost);
	  
	}
	/*
	 *  method that calls other  methods in the order that the game should run
	 */
	public void playGame() {
		sendHalfmap();
		runGame();
	}
	/*
	 * Checking and changing the position on the client , that the server has of a player
	 */
	private void changeofPos() {
	 for(FullMapNode node : network.getGameState().getMap().get().getMapNodes()) {
		 if(node.getPlayerPositionState() == EPlayerPositionState.BothPlayerPosition || node.getPlayerPositionState() == EPlayerPositionState.MyPosition) {
			 mygamestate.setMypos(mygamestate.getMap().getFields()
					 .stream().filter(p -> p.getX() == node.getX() && p.getY() == node.getY())
					 .findFirst()
					 .get());
	
		 mygamestate.getMap().getFields().stream().filter(field -> field.getX() == node.getX() && field.getY() == node.getY()).findFirst().get().setPlayerpositionstate(EPlayerPositionState.MyPosition);
		 
		 }
	 }
	
	}
    


}
