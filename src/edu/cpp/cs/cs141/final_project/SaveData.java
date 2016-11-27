package edu.cpp.cs.cs141.final_project;

import java.util.ArrayList;

public class SaveData implements java.io.Serializable {

	/**
	 * Serial version identifier; confirms content is valid
	 */
	private static final long serialVersionUID = -6490327999606656237L;
	
	private transient Map map = null;
	private transient ArrayList<Ninja> ninjas = null;
	private transient Spy spy = null;
	private transient ArrayList<Room> rooms = null;
	
	private boolean loaded = false;
	
	private GamePiece[][] serialGamePieces = null;
	private Object[][] serialPowerups = null;
	
	public SaveData(Map m) {
		init(m);
	}
	
	/**
	 * initializes variables, not load data yet
	 * 
	 * @param m
	 */
	private void init(Map m) {
		map = m;
		ninjas = new ArrayList<Ninja>();
		rooms = new ArrayList<Room>();
		
		if (serialGamePieces == null) {
			serialGamePieces = new GamePiece[map.getLength()][map.getWidth()];
			serialPowerups = new Object[map.getLength()][map.getWidth()];
			
			serializeData();
		}
	}
	
	/**
	 * populates serial* variables
	 */
	private void serializeData() {
		for (int i = 0; i < map.getLength(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				serialGamePieces[i][j] = map.getAtLocation(i, j);
				serialPowerups[i][j] = map.getPowerUpAtLocation(i, j);
			}
		}
	}
	
	/**
	 * populates map and data variables by iterating through each "tile" 
	 * 
	 * @throws Exception usually a casting Exception
	 */
	private void loadData() throws Exception {
		map = new Map();
		init(map);
		
		GamePiece cursorPiece = null;
		PowerUp cursorPowerup = null;
		
		for (int i = 0; i < map.getLength(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				cursorPiece = (GamePiece)serialGamePieces[i][j];
				cursorPowerup = (PowerUp)serialPowerups[i][j];
				
				map.set(i, j, cursorPiece);
				map.set(i, j, cursorPowerup);
				
				switch (cursorPiece.getClass().getSimpleName()) {
				case("Ninja"):
					ninjas.add((Ninja) cursorPiece);
					break;
				case("Spy"):
					spy = (Spy) cursorPiece;
					break;
				case("Room"):
					rooms.add((Room) cursorPiece);
					break;
				default:
					break;
				}
			}
		}
		loaded = true;
	}

	/**
	 * Attempts to return the map 
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map getMap() throws Exception {
		if (!loaded) loadData();
		return map;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public Ninja[] getNinjas() throws Exception {
		if (!loaded) loadData();
		return ninjas.toArray(new Ninja[ninjas.size()]);
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public Spy getSpy() throws Exception {
		if (!loaded) loadData();
		return spy;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public Room[] getRooms() throws Exception {
		if (!loaded) loadData();
		return rooms.toArray(new Room[rooms.size()]);
	}
}
