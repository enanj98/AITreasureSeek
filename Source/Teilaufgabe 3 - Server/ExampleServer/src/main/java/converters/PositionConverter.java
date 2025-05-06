package converters;

import MessagesGameState.EPlayerPositionState;
import enums.PlayerPositionState;

public class PositionConverter {
	public EPlayerPositionState convertPosition(PlayerPositionState position) {
		switch(position) {
			case BothPlayerPositon:
				return EPlayerPositionState.BothPlayerPosition;
			case EnemyPlayerPosition:
				return EPlayerPositionState.EnemyPlayerPosition;
			case MyPosition:
				return EPlayerPositionState.MyPosition;
			case NoPlayerPresent:
				return EPlayerPositionState.NoPlayerPresent;
		}
		return null;
	}
}
