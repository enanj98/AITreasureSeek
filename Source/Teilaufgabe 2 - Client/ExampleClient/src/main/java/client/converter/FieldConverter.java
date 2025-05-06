package client.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import client.mapmodel.HalfMapController;
import client.model.Field;
import client.model.Halfmap;
import clientenums.Terrain;
import MessagesBase.HalfMapNode;
import MessagesGameState.EFortState;
import MessagesGameState.EPlayerPositionState;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;

public  class FieldConverter {
private static List<Field> nodes = new ArrayList<>();


public static List<HalfMapNode> convertMap(Halfmap map){
	List<HalfMapNode> nodes = new ArrayList<>();
	for(Field node:map.getFields()) { 
		switch(node.getTerrain()) {
		case Grass:
			nodes.add(new HalfMapNode(node.getX(),node.getY(),node.getFortstate(),ETerrain.Grass));
			break;
		case Mountain:
			nodes.add(new HalfMapNode(node.getX(),node.getY(),node.getFortstate(),ETerrain.Mountain));
			break;
		case Water:
			nodes.add(new HalfMapNode(node.getX(),node.getY(),node.getFortstate(),ETerrain.Water));
			break;
		
		}
	}
	return nodes;
}
public static Halfmap convertServersMap(HalfMapController mapcontroller,FullMap map){
	for(FullMapNode node : map.getMapNodes()) {
		switch(node.getTerrain()) {
		case Grass:
			if(node.getFortState() == EFortState.MyFortPresent) {
			nodes.add(new Field(node.getX(),node.getY(),true,Terrain.Grass));
			nodes.stream().filter(p -> p.getX() == node.getX() && p.getY() == node.getY()).findFirst().get().setPlayerpositionstate(EPlayerPositionState.MyPosition);
			}
			else
			nodes.add(new Field(node.getX(),node.getY(),Terrain.Grass));	
		break;
		case Mountain:
			nodes.add(new Field(node.getX(),node.getY(),Terrain.Mountain));
		break;
		case Water:
			nodes.add(new Field(node.getX(),node.getY(),Terrain.Water));
		break;	
		}
	}
	
	return new Halfmap(nodes);
	
}



}
