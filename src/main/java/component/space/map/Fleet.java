package component.space.map;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.google.gwt.dev.util.collect.HashMap;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Image;

import lombok.Data;

@Data
public class Fleet {

	
	enum Type{OWNED, ALLIED, ENEMY, NEUTRAL, CIVILIAN}
	
	@NotNull
	private Type type;
	@NotNull
	private int size;
	@NotNull
	private int x;
	@NotNull
	private int y;
	@NotNull
	private String name;
	
	private String faction;

	/**
	 * in km/s
	 */
	private double speed;
	
	/**
	 * in radians North = 0, East is 1/2PI
	 */
	private double direction;
	
	private SpaceObject dockedAt;

	private String fleetInfo;

	private Image image;

	private Color color = null;
	
	private Map<String, String> properties = new HashMap<>();
	
	
	public Fleet(Type type, int x, int y, String name, String faction) {
		super();
		this.type = type;
		this.x = x;
		this.y = y;
		this.name = name;
		this.faction = faction;
	}
	
	public Fleet(Type type, SpaceObject dockedAt , String name, String faction) {
		this(type, dockedAt.getX(), dockedAt.getY(),name, faction);
		this.dockedAt = dockedAt;
	}
	
	public void mergeProperty(String key, String value)
	{
		properties.merge(key, value, (old, newValue) -> newValue);
	}
	
	
	protected Location getLocation()
	{
		return new Location(x,y);
	}
	
	
	
	/**
	 * Returns the set color and if null a default color for each Type.
	 * 
	 * @return
	 */
	public String getColor() {
		if (color != null) {
			return ColorConverter.convert(color);
		}
		// the default colors for objects
		switch (type) {
		case OWNED:
			return ColorConverter.convert(new Color(150, 240, 140));// green
		case CIVILIAN:
			return ColorConverter.convert(new Color(0, 220, 230));// Cyan
		case ALLIED:
			return ColorConverter.convert(new Color(208, 227, 36));// yellow
		case NEUTRAL:
			return ColorConverter.convert(new Color(139, 0, 139)); //Magenta
		// light green
		case ENEMY:
			return ColorConverter.convert(new Color(230, 0, 25));
		// red
		default:
			return null;
		}
	}




}
