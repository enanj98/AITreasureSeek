package Map;

import java.util.ArrayList;
import java.util.List;

import enums.EMapType;



public class GameMap {
		 private List<Field>fields = new ArrayList<>();
		 private EMapType maptype = EMapType.nomap;
		 private int moves = 0;
		public List<Field> getFields() {
			return fields;
		}
		public void incrementMoves() {
			moves++;
		}
		public int getMoves() {
			return moves;
		}

		public EMapType getMaptype() {
			return maptype;
		}

		public void setMaptype(EMapType maptype) {
			this.maptype = maptype;
		}

		public void setFields(List<Field> fields) {
			this.fields = fields;
		}

}
