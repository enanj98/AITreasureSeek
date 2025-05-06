package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesGameState.EPlayerGameState;
import client.controller.PlayerController;
import client.model.Field;
import client.model.Halfmap;
import clientenums.EMapType;
import gameState.MyGameState;

public class CliView implements PropertyChangeListener{
	Logger logger = LoggerFactory.getLogger(PlayerController.class);
	private Halfmap mymap;
	public CliView(MyGameState gamestate) {
		gamestate.addListener(this);
	}

	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getNewValue() instanceof Halfmap) {
			Halfmap aftercast = (Halfmap)evt.getNewValue();
			mymap = aftercast;
			if(aftercast.getMaptype() == EMapType.eighteight && aftercast.getFields().size() == 64) {
			System.out.println("==================================================================================");
				int pos = 0;
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						aftercast.getFields().sort(new Comparator<Field>() {
							public int compare(Field o1, Field o2) {
								if(o1.getY() == o2.getY()) {
									return Integer.compare(o1.getX(), o2.getX());
								}else {
									return Integer.compare(o1.getY(), o2.getY());
								}
								
							};
						});
						aftercast.getFields().get(pos).display();
						pos++;
					}
					System.out.println();
				}
			System.out.println("==================================================================================");
			}else if(aftercast.getMaptype() == EMapType.foursixteen && aftercast.getFields().size() == 64) {
				System.out.println("==================================================================================================================================================================================");	
				int pos = 0;
				for(int i = 0; i < 4; i++) {
					for(int j = 0; j < 16; j++) {
						
						aftercast.getFields().sort(new Comparator<Field>() {
							public int compare(Field o1, Field o2) {
									if(o1.getY() == o2.getY() ) {
										return Integer.compare(o1.getX(), o2.getX());
									}else {
										return Integer.compare(o1.getY(), o2.getY());
									}
								
								};
							});
							
						aftercast.getFields().get(pos).display();	
						pos++;
					}
					System.out.println("\n");
					
				}
				System.out.println("==================================================================================================================================================================================");
			}
			
		}else if(evt.getNewValue() instanceof EPlayerGameState) {
			EPlayerGameState pstate = (EPlayerGameState)evt.getNewValue();
			if(pstate == EPlayerGameState.ShouldWait) {
				System.out.println("Client  waits on his turn");
			}
			else if(pstate == EPlayerGameState.ShouldActNext) {
				logger.info("Client  should  act now");
			}
		}else if(evt.getPropertyName().equals("TreasureFound")) {
			logger.info("Player has found his Treasure");
		}else if(evt.getNewValue() instanceof Field){
			Field newField = (Field)evt.getNewValue();
			logger.info("new Pos is : " + newField.getX() + " " + newField.getY());

		}
		
	}

	

}
