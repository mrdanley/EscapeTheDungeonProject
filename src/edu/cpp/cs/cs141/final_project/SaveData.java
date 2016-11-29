package edu.cpp.cs.cs141.final_project;

import java.util.ArrayList;

public class SaveData implements java.io.Serializable {
	private static final long serialVersionUID = -6490327999606656237L;
	
	private transient Map map = null;
	private transient ArrayList<Ninja> ninjas = null;
	private transient Spy spy = null;
	private transient ArrayList<Room> rooms = null;
	
	/**
	 * Initializes {@code map} variable only. All variables are transient and
	 * should only be used after the object is de-serialized (which populates them).
	 * 
	 * @param m Active game map contains all relevant game data.
	 */
	public SaveData(Map m) {
		map = m;
	}

	/**
	 * Returns the map with all positions and content
	 * @return saved map data
	 */
	public Map getMap() {
		return map;
	}
	
	/**
	 * Returns an array of {@link Ninja}s loaded from the {@code map}.
	 * @return array of living {@link Ninja}s.
	 */
	public Ninja[] getNinjas() {
		return ninjas.toArray(new Ninja[ninjas.size()]);
	}

	/**
	 * Returns the spy player and relevant data.
	 * @return current player
	 */
	public Spy getSpy() {
		return spy;
	}

	/**
	 * Returns an array containing all the rooms loaded into the game.
	 * One of the rooms has a suitcase in it usually.
	 * @return array of {@link Room}s with data.
	 */
	public Room[] getRooms() {
		return rooms.toArray(new Room[rooms.size()]);
	}
	
	/**
	 * Custom serialization code. Creates a 2d array of {@link GamePiece}s and {@link PowerUp}s
	 * to write to file.
	 * 
	 * @param oos Stream to write datastream to. Usually a file.
	 * @throws java.io.IOException Problem while writing data
	 */
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
	
	/**
	 * Custom serialization code. Iterates through each position in the {@link map} and
	 * populates it with saved {@link ActiveAgent}s and/or {@link PowerUp}s.
	 * 
	 * @param ois Stream to load byte data from. Usually a file.
	 * @throws ClassNotFoundException Unknown or invalid class found
	 * @throws java.io.IOException Problem while reading data
	 */
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
