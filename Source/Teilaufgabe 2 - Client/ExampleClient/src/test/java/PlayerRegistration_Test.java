import java.lang.reflect.Executable;


import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import MessagesBase.PlayerRegistration;
import client.controller.PlayerController;
import exceptions.RegistrationException;

public class PlayerRegistration_Test {
	//negative test
	@Test
	public void RegisterGameIDThatIsNull() {
		PlayerRegistration player = new PlayerRegistration("enan", "jahic", "a11708481"); 
	    PlayerController gameController = new PlayerController("http://swe.wst.univie.ac.at:18235" , "null");
	    
		Assertions.assertThrows(RegistrationException.class,() -> gameController.registerPlayer(player));
	}

}
