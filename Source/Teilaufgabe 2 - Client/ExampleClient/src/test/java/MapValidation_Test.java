import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import MessagesBase.PlayerRegistration;
import client.controller.PlayerController;
import client.mapmodel.HalfMapValidator;
import client.model.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Assert;
import org.junit.Test;

import client.mapmodel.HalfMapGenerator;
import client.model.Halfmap;
import clientenums.Terrain;

import static org.hamcrest.Matchers.is;



public class MapValidation_Test {
	HalfMapGenerator mapgen = new HalfMapGenerator();
	@Test
	public void ValidateMapThatIsCorrect(){
		
		List<Field> emptyfields = mapgen.generateMap().getFields();
		HalfMapValidator mapvalidator = new HalfMapValidator();

		mapvalidator.setFields(emptyfields);
		
		assertThat(mapvalidator.validateMap(), is(equalTo(true)));
		
	}
	@Test
	public void ValidateMapThatHasMoreThan32Fields(){
		
		List<Field> emptyfields = mapgen.generateMap().getFields();
		emptyfields.add(new Field(9,9,Terrain.Grass));
		HalfMapValidator mapvalidator = new HalfMapValidator();

		mapvalidator.setFields(emptyfields);
		
		
		assertThat(mapvalidator.validateMap(), is(equalTo(false)));
		
	}

}
