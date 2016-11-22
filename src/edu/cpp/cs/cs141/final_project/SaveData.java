package edu.cpp.cs.cs141.final_project;

import java.io.Serializable;
import java.util.ArrayList;

public class SaveData implements Serializable {

	/**
	 * Serial version identifier; confirms content is valid
	 */
	private static final long serialVersionUID = -6490327999606656237L;
	
	private transient Map map = null;
	private transient ArrayList<Ninja> ninjas = null;
//	private transient ArrayList<PowerUp> powerups;
	private transient Spy spy = null;
	private transient ArrayList<Room> rooms = null;
	
	private GamePiece[][] serialGamePieces;
	private Object[][] serialPowerups;
	
	
	public SaveData(Map m) {
		init(m);
	}
	
	private void init(Map m) {
		map = m;
		ninjas = new ArrayList<Ninja>();
		rooms = new ArrayList<Room>();
		spy = null;
		
		serialGamePieces = new GamePiece[map.getLength()][map.getWidth()];
		serialPowerups = new Object[map.getLength()][map.getWidth()];
		
		serializeData();
	}
			
	
	
	
	private void serializeData() {
		for (int i = 0; i < map.getLength(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				serialGamePieces[i][j] = map.getAtLocation(i, j);
				serialPowerups[i][j] = map.getPowerUpAtLocation(i, j);
			}
		}
	}
	
	private void loadData() throws Exception {
		map = new Map();
		init(map);
		
		GamePiece cursorPiece = null;
		PowerUp cursorPowerup = null;
		
		for (int i = 0; i < map.getLength(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				
				cursorPiece = (GamePiece)serialGamePieces[i][j];
				cursorPowerup = (PowerUp)serialPowerups[i][j];
				
				System.out.println(i+":"+j+"; and "+cursorPiece.getClass().getSimpleName());
				
				
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
				
//				switch (cursorPowerup.getClass().getSimpleName()) {
//				case:
//				}
				
			}
		}
		
	}
	
	
	
	

	
	
	public GamePiece[] findInMap(Object o) {
		ArrayList<GamePiece> items = new ArrayList<GamePiece>();
		
		GamePiece cursor;
		
		for (int i = 0; i < map.getLength(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				
				cursor = map.getAtLocation(i, j);
				
				if (cursor.getClass().equals(o.getClass())) {
					items.add(cursor); //extremely dangerous; but do it anyway
				}
			}
		}
		
		return (ActiveAgent[]) items.toArray();
	}
	

	public Map getMap() throws Exception {
		if (map == null) loadData();
		return map;
	}
	
	public Ninja[] getNinjas() throws Exception {
		if (map == null) loadData();
		return ninjas.toArray(new Ninja[ninjas.size()]);
	}
	
//	public PowerUp[] getPowerups() {
//		if (map == null) loadData();
//		return 
//	}

	public Spy getSpy() throws Exception {
		if (map == null) loadData();
		return spy;
	}

	public Room[] getRooms() throws Exception {
		if (map == null) loadData();
		return rooms.toArray(new Room[rooms.size()]);
	}
}
