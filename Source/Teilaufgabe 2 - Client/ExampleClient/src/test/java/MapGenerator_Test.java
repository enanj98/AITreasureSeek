import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Assert;
import org.junit.Test;

import client.mapmodel.HalfMapGenerator;
import client.model.Halfmap;
import clientenums.Terrain;

import static org.hamcrest.Matchers.is;

import org.hamcrest.core.IsNull;

import static org.hamcrest.Matchers.equalTo;
public class MapGenerator_Test {
	
	@Test
	public void GenerateTheMapWith32FieldsTest() {
		HalfMapGenerator mapgeneratortest = new HalfMapGenerator();
		int testmapsize = mapgeneratortest.generateMap().getFields().size();
		
		assertThat(testmapsize,is(equalTo(32)));
		
	}
	
	@Test
	public void ValidateHasAtleastOneFort() {
		HalfMapGenerator mapgeneratortest = new HalfMapGenerator();
		Halfmap testmap = mapgeneratortest.generateMap();
		
		assertThat(testmap.getFields().stream().anyMatch(field -> field.getFortstate()),is(equalTo(true)));
	}
	@Test
	public void ValidateHasAtleastThreeWaters() {
		HalfMapGenerator mapgeneratortest = new HalfMapGenerator();
		Halfmap testmap = mapgeneratortest.generateMap();
		
		assertThat(testmap.getFields().stream().filter(field -> field.getTerrain() == Terrain.Water).count() > 3,is(equalTo(true)));
	}
	@Test
	public void GeneratedMapIsNotNull() {
		HalfMapGenerator mapgeneratortest = new HalfMapGenerator();
		Halfmap testmap = mapgeneratortest.generateMap();
		
		assertThat(testmap,is(notNullValue()));
	}
	 
	
}
