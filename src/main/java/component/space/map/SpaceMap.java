package component.space.map;

import com.vaadin.shared.ui.colorpicker.Color;

import component.space.map.calls.SpaceMapListener;

//TODO: use sprites
public interface SpaceMap {
	
	
	/**
	 * Set the size of this square shaped solar system
	 * 
	 * @param size in millons of kilometers, default is 30.000 
	 */
	public void setSize(int size);

	public void drawBackGround(Color color);

	/**
	 * Location is always 0,0 (center of the canvas).
	 * 
	 * @param color
	 * @param name
	 */
	public void drawSun(Color color, String name);
	
	
	/**
	 * Draw a planet in the solar system.
	 * 
	 * <p> the planet will always orbit clockwise.
	 * 
	 * @param color
	 * @param x coordinate position
	 * @param y coordinate position
	 * @param speed the orbit speed in km/s
	 */
	public  void drawPlanet(Color color, String name, int x, int y, int speed);
	
	/**
	 * Draw a planet in the solar system.
	 * 
	 * <p> the planet will always orbit clockwise.
	 * 
	 * @param color
	 * @param x coordinate position
	 * @param y coordinate position
	 * @param speed the orbit speed in km/s 
	 */
	public void drawAsteroid(Color color, String name, int x, int y, int speed);
	
	/**
	 * Add a listener that listens to user interactions with the map.
	 */
	public void addListener(SpaceMapListener listener);
	
	

}
