package converters;

import MessagesGameState.ETreasureState;
import enums.TreasureState;

public class TreasureConverter {
    public ETreasureState convertTreasureState(TreasureState tstate) {
    	switch(tstate) {
    	case NoOrUnknownTreasureState:
    		return ETreasureState.NoOrUnknownTreasureState;
    	case MYTREASURESTATE:
    		return ETreasureState.MyTreasureIsPresent;
    	}
    	return null;
    }
}
