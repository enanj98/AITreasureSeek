package generators;

import java.util.UUID;

public class GameStateIDGenerator {
	public String generateGameStateID() {
		return UUID.randomUUID().toString();
	}
	
}
