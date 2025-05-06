package converters;

import MessagesGameState.EFortState;
import enums.FortState;

public class FortConverter {

	public boolean convertFortState(FortState state) {
		switch(state) {
		case MyFortPresent:
			return true;
		case NoOrUnknownFortState:
			return false;
		case EnemyFortPresent:
			return true;
		}
		return false;
	}
	
	public EFortState convertMyFortState(FortState value) {
		switch(value) {
		case MyFortPresent:
			return EFortState.MyFortPresent;
		case NoOrUnknownFortState:
			return EFortState.NoOrUnknownFortState;
		case EnemyFortPresent:
			return EFortState.EnemyFortPresent;
		}
		return null;
		}

}
	

