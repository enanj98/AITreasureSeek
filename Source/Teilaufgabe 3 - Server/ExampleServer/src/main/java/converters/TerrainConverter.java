package converters;

import MessagesBase.ETerrain;
import enums.Terrain;

public class TerrainConverter {
	public Terrain convertTerrain(ETerrain terrain) {
		Terrain returnTerrain = null;
		switch(terrain) {
		case Grass:
			returnTerrain =  Terrain.Grass;
			break;
		case Mountain:
			returnTerrain =  Terrain.Mountain;
			break;
		case Water:
			returnTerrain = Terrain.Water;
			break;
		}
		return returnTerrain;
		}		
	
	public ETerrain convertMyTerrain(Terrain terrain) {
		ETerrain returnTerrain = null;
		switch(terrain) {
		case Grass:
			returnTerrain =  ETerrain.Grass;
			break;
		case Mountain:
			returnTerrain =  ETerrain.Mountain;
			break;
		case Water:
			returnTerrain = ETerrain.Water;
			break;
		}
		return returnTerrain;
	
	}
}
