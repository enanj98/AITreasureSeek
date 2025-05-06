package communication;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.EMove;
import MessagesBase.ERequestState;
import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import client.converter.FieldConverter;
import client.model.Halfmap;
import exceptions.RegistrationException;

/**
 *  class for networking communication with the Server
 * @author enanj98
 *
 */

public class Network {
	private  Logger networklogger = LoggerFactory.getLogger(Network.class);
	  private  WebClient baseWebClient;
	  private  String gameID,playerID;

	  
	public Network (String serverBaseUrl,String gameID) {
		this.gameID = gameID;
		this.baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
					.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) //the network protocol uses XML
				    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
				    .build();
		    
	  }
	  /*
	   * Registers player onto the server that corresponds to the given gameId
	   */
	  public  void registerPlayersID(String gameId, PlayerRegistration player) throws RegistrationException{	
		if(gameId == null || gameId.length() != 5) {throw new RegistrationException("gameid is not valid"); }
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
					.uri("/" + gameId + "/players")
					.body(BodyInserters.fromValue(player)) // specify the data which is set to the server
					.retrieve()
					.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server
		  	
		
			ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();
			if(resultReg.getState() == ERequestState.Okay) {
				this.playerID = resultReg.getData().get().getUniquePlayerID();
				this.gameID = gameId;
				networklogger.info("Player with player Id:" + playerID + "has registered succesfully");	
			}else {
			 	throw new RegistrationException("Cant connect to the server");
			}
		
	  }

	  /*
	   * Generated map gets translated into the Servers map and gets sent
	   */
	  @SuppressWarnings({ "rawtypes", "unchecked" })
	public  ResponseEnvelope<Halfmap> sendMap(String gameId,Halfmap map){
		HalfMap serversmap = new HalfMap(playerID,FieldConverter.convertMap(map));
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
					.uri("/" + gameId + "/halfmaps")
					.body(BodyInserters.fromValue(serversmap)) // specify the data which is set to the server
					.retrieve()
					.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server
		  
		ResponseEnvelope<Halfmap> resultReg = webAccess.block();
		  if(resultReg.getState() == ERequestState.Error) {
				networklogger.error("Client error, errormessage: " + resultReg.getExceptionMessage());
		  }
		  else{
				networklogger.info("Player with id: " + playerID + "has sent a valid map");			
		  }
			return resultReg;
	  }
	  public  GameState getGameState() {
		  
			@SuppressWarnings("rawtypes")
			Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
					.uri("/" + gameID + "/states/" + playerID)
					.retrieve()
					.bodyToMono(ResponseEnvelope.class);
			
			@SuppressWarnings("unchecked")
			ResponseEnvelope<GameState> requestResult = webAccess.block();
			
			if(requestResult.getState() == ERequestState.Error) {
				networklogger.error(requestResult.getExceptionMessage());
			}else{		
				return requestResult.getData().get();
			}
			return null;
	  }
	  
	  public void sendMove(EMove move,String playerID) {
		  PlayerMove pMove = PlayerMove.of(playerID, move);
		  Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
					.uri("/" + gameID + "/moves")
					.body(BodyInserters.fromValue(pMove)) // specify the data which is set to the server
					.retrieve()
					.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server
		 
		  ResponseEnvelope<Halfmap> resultReg = webAccess.block();
		  if(resultReg.getState() == ERequestState.Error) {
				networklogger.error("Client error, errormessage: " + resultReg.getExceptionMessage());
		  }
		  else {
				networklogger.info("Player : " + playerID + " has sent the move");			
		  }
	  }
	  public String getGameId() {
		  return gameID;
	  }
	  public String getplayerId() {
		  return playerID;
	  }
	  
}
