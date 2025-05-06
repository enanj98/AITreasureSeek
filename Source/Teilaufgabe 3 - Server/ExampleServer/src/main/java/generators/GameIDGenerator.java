package generators;

import java.util.Random;

public class GameIDGenerator {

	public String generateGameID() {
		final String letters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		String randomGameID = "";
		Random random = new Random();
		int randompos = random.nextInt(letters.length());
		while(randomGameID.length() != 5) {
			randomGameID+=letters.charAt(randompos);
			randompos = random.nextInt(letters.length());
		}
		return randomGameID;
	}
}
