package edu.cpp.cs.cs141.final_project;

import java.util.ArrayList;

public class SaveData implements java.io.Serializable {
	private static final long serialVersionUID = -6490327999606656237L;
	
	private transient Map map = null;
	private transient ArrayList<Ninja> ninjas = null;
	private transient Spy spy = null;
	private transient ArrayList<Room> rooms = null;
	
	public SaveData(Map m) {
		map = m;
	}

	/**
	 * Attempts to return the map 
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map getMap() throws Exception {
		return map;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public Ninja[] getNinjas() throws Exception {
		return ninjas.toArray(new Ninja[ninjas.size()]);
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public Spy getSpy() throws Exception {
		return spy;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public Room[] getRooms() throws Exception {
		return rooms.toArray(new Room[rooms.size()]);
	}
	
	private void writeObject(java.io.ObjectOutputStream oos) throws java.io.IOException {
		oos.defaultWriteObject();
		
		GamePiece[][] serialGamePieces = new GamePiece[map.getLength()][map.getWidth()];
		Object[][] serialPowerups = new Object[map.getLength()][map.getWidth()];
		
		for (int i = 0; i < map.getLength(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				serialGamePieces[i][j] = map.getAtLocation(i, j);
				serialPowerups[i][j] = map.getPowerUpAtLocation(i, j);
			}
		}
		oos.writeObject(serialGamePieces);
		oos.writeObject(serialPowerups);
	}
	
	private void readObject(java.io.ObjectInputStream ois) throws ClassNotFoundException, java.io.IOException {
		ois.defaultReadObject();
		
		GamePiece[][] serialGamePieces = (GamePiece[][]) ois.readObject();
		Object[][] serialPowerups = (Object[][]) ois.readObject();
		
		map = new Map();
		ninjas = new ArrayList<Ninja>();
		rooms = new ArrayList<Room>();
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
	}
}
