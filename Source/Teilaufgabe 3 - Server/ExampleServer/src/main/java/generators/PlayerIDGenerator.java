package generators;

import java.util.UUID;

import MessagesBase.UniquePlayerIdentifier;

public class PlayerIDGenerator {
  public UniquePlayerIdentifier generatePlayerID() {
	 return new UniquePlayerIdentifier(UUID.randomUUID().toString());
  }
}
