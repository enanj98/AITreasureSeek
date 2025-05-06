import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import AI.AiTargetFinder;
import client.mapmodel.HalfMapGenerator;
import client.model.Field;
import client.model.Halfmap;
import clientenums.EMapType;
import clientenums.Terrain;
import gameState.MyGameState;

public class AImovement_Test {
	@Test
	public void RandomPositionFinderShouldBeGrass() {
		HalfMapGenerator mapgen = new HalfMapGenerator();
		List<Field> visited = new ArrayList<>();
		List<Field> emptyfields = mapgen.generateMap().getFields();
		Halfmap map = new Halfmap(emptyfields);
		map.setMaptype(EMapType.eighteight);
		MyGameState gamestate = new MyGameState();
		gamestate.setMap(map);
		gamestate.setMypos(emptyfields.stream().filter(field -> field.getTerrain() == Terrain.Grass).findAny().get());
		
		AiTargetFinder aihelper = new AiTargetFinder();
		Field expectedfield = aihelper.findNewTarget(gamestate, visited,gamestate.getMypos()); 
		assertThat(expectedfield.getTerrain(),is(equalTo(Terrain.Grass)));
	}
	
	
	
}
