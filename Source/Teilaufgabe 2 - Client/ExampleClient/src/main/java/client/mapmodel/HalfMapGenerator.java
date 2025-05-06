package client.mapmodel;

import java.util.ArrayList;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import client.model.Field;
import client.model.Halfmap;
import clientenums.Terrain;

public class HalfMapGenerator {
	private HalfMapValidator mapvalidator;
	private List<Field>mapnodes;
	private LinkedList<Terrain> terrains;
	boolean fortressplaced;

    public HalfMapGenerator() {
      mapnodes = new ArrayList<Field>();
  	  terrains = new LinkedList<>();
  	  fortressplaced = false;
  	  mapvalidator = new HalfMapValidator();
		    
    }
	/*
	 * Generates 32 fields with coordinates with  terrain value
	 */
	public Halfmap generateMap(){
	  generateFields();
	  generateTerraintypes();
	  Collections.shuffle(terrains); 
	  setFields();
	  setFort();
	  mapvalidator.setFields(mapnodes);
	  mapvalidator.validateMap();
	  
	  if(mapvalidator.getisValidated()) {
		  return new Halfmap(mapnodes);
	  }
	  
	  do{
		unsetFort();
		unsetFields();
		generateTerraintypes();
		setFields();
		setFort();
		mapvalidator.validateMap();
		mapnodes = mapvalidator.getMap();
		}while(!mapvalidator.getisValidated());
	  
	  System.out.println("no islands were created\n");
	  
	  return new Halfmap(mapnodes);
	  
	}
	

	
	public void unsetFields() {
		for(Field field : mapnodes) {
			field.setTerrain(null);
		}
	}
	private void generateFields() {
		for (int x = 0; x < 8; x++) {
			 for (int y = 0; y < 4; y++) {	
				mapnodes.add(new Field(x,y,terrains.pollLast()));			
			 }				
		 }
		
	}
	public void setFields(){
		for(Field field : mapnodes) {
			field.setTerrain(terrains.pollLast());
		}
	}
	
	public void setFort() {
		Random random1 = new Random();
		int coordinate = random1.nextInt(14);
		mapnodes.stream().filter(p -> p.getTerrain() == Terrain.Grass).skip(coordinate).findAny().get().setFortstate(true);
		fortressplaced = true;
	}
	public void unsetFort() {
		mapnodes.stream().filter(p -> p.getFortstate()).findFirst().get().setFortstate(false);
	    fortressplaced = false;
	}
	
	public void generateTerraintypes() {
		 Random random = new Random();
		  int waternumber = 10;
		  int grassnumber = 15;
		  int mountainnumber = 3;
		  do {
				 int number =random.nextInt(3);
				 switch(number) {
				 case 0:
					 if(grassnumber != 0) {
						 terrains.add(Terrain.Grass);
						 grassnumber--;
					 }else if(mountainnumber !=0) {
						 terrains.add(Terrain.Mountain);
						 mountainnumber--;
					 }else if(waternumber !=0) {
						 terrains.add(Terrain.Water);
						 waternumber--;
					 }else {
						 terrains.add(Terrain.values()[number]);
					 }
					 break;
				 case 1:
					 if(mountainnumber != 0) {
						 terrains.add(Terrain.Mountain);
						 mountainnumber--;
					 }else if(grassnumber != 0) {
							 terrains.add(Terrain.Grass);
							 grassnumber--;
						 }else if(waternumber != 0) {
							 terrains.add(Terrain.Water);
							 waternumber--;
						 }else{
							 terrains.add(Terrain.values()[number]);
						 }
					 		
					 break;
				 case 2:
					 if(waternumber != 0) {
						 terrains.add(Terrain.Water);
						 waternumber--;
					 }else if(mountainnumber != 0) {
							 terrains.add(Terrain.Mountain);
							 mountainnumber--;
						 }else if(grassnumber != 0) {
							 terrains.add(Terrain.Grass);
							 grassnumber--;
						 }else{
							 terrains.add(Terrain.values()[number]);
						 }
					 break;
				 }
				 
			  }while(terrains.size() != 32);
	}
	public List<Field> getMap() {
		return mapnodes;
	}


	
}
