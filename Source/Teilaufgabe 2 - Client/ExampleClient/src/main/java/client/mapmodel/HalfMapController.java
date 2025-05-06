package client.mapmodel;



import client.model.Halfmap;

public class HalfMapController {
	
	private HalfMapGenerator mapgenerator;
	private Halfmap myhalfmap;

	
	public HalfMapController() {
		mapgenerator = new HalfMapGenerator();
		myhalfmap = mapgenerator.generateMap();
	}
	
	public Halfmap getMyhalfmap() {
		return myhalfmap;
	}
	public void setMyHalfMap(Halfmap fields) {
		myhalfmap = fields;
	}


	

}

