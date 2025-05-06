package client.main;


import MessagesBase.PlayerRegistration;

import client.controller.PlayerController;
import exceptions.RegistrationException;

public class MainClient {

	public static void main(String[] args) {
				
		PlayerRegistration player = new PlayerRegistration("enan", "jahic", "a11708481"); 
		
		PlayerController gameController = new PlayerController(args[1], args[2]);	
		try {
			gameController.registerPlayer(player);
		} catch (RegistrationException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
		}
		try {
			gameController.playGame();
		} catch (Exception e) {
			e.printStackTrace();
		}	

	}
		

}
