package converters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Map.Field;
import Map.GameMap;
import MessagesBase.HalfMap;
import MessagesGameState.EFortState;
import MessagesGameState.EPlayerPositionState;
import MessagesGameState.FullMapNode;


public class ServerNodesConverter {
	private TerrainConverter terrainconverter = new TerrainConverter();
	private TreasureConverter treasureconverter = new TreasureConverter();
	private PositionConverter positionconverter = new PositionConverter();
public Collection<FullMapNode> convertServerNodes(GameMap map,int fortx ,int forty){
		Collection<FullMapNode> nodes = new ArrayList<>();
		boolean randomenemyposset = false;
			for(Field field : map.getFields()) {
				if(field.getX() == fortx && field.getY() == forty) {
					nodes.add(new FullMapNode(terrainconverter.convertMyTerrain(field.getTerrain()),positionconverter.convertPosition(field.getplayerPresent()),treasureconverter.convertTreasureState(field.getTreasurestate()),EFortState.MyFortPresent,field.getX(),field.getY()));
				}else {
					if(!randomenemyposset && map.getMoves() <= 10) {
						nodes.add(new FullMapNode(terrainconverter.convertMyTerrain(field.getTerrain()),EPlayerPositionState.EnemyPlayerPosition,treasureconverter.convertTreasureState(field.getTreasurestate()),EFortState.NoOrUnknownFortState,field.getX(),field.getY()));
						randomenemyposset = true;
					}
					nodes.add(new FullMapNode(terrainconverter.convertMyTerrain(field.getTerrain()),positionconverter.convertPosition(field.getplayerPresent()),treasureconverter.convertTreasureState(field.getTreasurestate()),EFortState.NoOrUnknownFortState,field.getX(),field.getY()));
				}
				
			}
		
		return nodes;
	}
	public List<Field> convertNodes(HalfMap halfmap){
		List<Field> combinedfields = new ArrayList<>();
		halfmap.getNodes().forEach(node -> combinedfields.add(new Field(node.getX() ,node.getY(),node.isFortPresent(),terrainconverter.convertTerrain(node.getTerrain()))));
		return combinedfields;
	}
}
