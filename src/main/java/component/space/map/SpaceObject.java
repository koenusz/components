package component.space.map;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Image;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(exclude = {"dockedAt", "orbitingThis"}) // prevent circularLoop in tostring
@EqualsAndHashCode(exclude = {"dockedAt", "orbitingThis"})
public class SpaceObject {

	public enum Type {
		MOON, PLANET, STAR, SHIP, ASTEROID, JUMP, LAGRANGE
	}

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

	/**
	 * in km/s
	 */
	@NotNull
	private double speed;

	private boolean selected = false;

	/**
	 * toggles visual indicators
	 */
	private BitSet indicators = new BitSet(6);

	// the body this spaceobject is orbiting around
	private SpaceObject orbiting;
	
	private List<SpaceObject> orbitingThis = new ArrayList<>();

	private List<SpaceObject> docked = new ArrayList<>();

	private SpaceObject dockedAt;

	private String mouseOverInfo;

	private Image image;

	private Color color = null;

	public SpaceObject(Type type, int size, double speed, int x, int y, String name) {
		this.type = type;
		this.size = size;
		this.x = x;
		this.y = y;
		this.name = name;
		if (name.length() > 15) {
			throw new RuntimeException("Name is longer than 15 characters");
		}
	}

	/**
	 * When a spaceobject is orbiting this spaceObjet, call this.
	 * 
	 * @param so
	 */
	public void callbackOrbiting(SpaceObject so)
	{
		orbitingThis.add(so);
	}
	
	/**
	 * 
	 * Creates a Spaceobject relative to the object it is orbiting.
	 * 
	 * @param type
	 * @param size
	 * @param x
	 * @param y
	 * @param name
	 *            max 15 chars
	 * @param orbiting
	 */
	public SpaceObject(Type type, int size, double speed, int x, int y, String name, SpaceObject orbiting) {
		this(type, size, speed, orbiting.getX() + x, orbiting.getY() + y, name);
		this.orbiting = orbiting;
		orbiting.callbackOrbiting(this);
	}

	public void dockShip(SpaceObject ship) {
		if (ship.getType() == Type.SHIP) {
			ship.setX(x);
			ship.setY(y);
			ship.setDockedAt(this);
			docked.add(ship);
		}
	}

	public void undockShip(SpaceObject ship) {
		if (ship.getType() == Type.SHIP) {
			ship.setDockedAt(null);
			docked.remove(ship);
		}
	}

	public List<String> getMapInfo() {
		List<String> mapInfo = new ArrayList<>();
		// always ad your own name first
		mapInfo.add(name);
		for (SpaceObject ship : docked) {
			mapInfo.add(ship.getName());

		}
		return mapInfo;
	}

	public boolean getIndicator(int index) {
		return indicators.get(index);
	}

	public boolean matchesIndicators(int... index) {
		boolean result = false;
		for (int i : index) {
			if (indicators.get(i)) {
				result = true;
			} else {
				return false;
			}
		}
		return result;
	}

	public void setIndicatorsOn(int... index) {
		for (int i : index) {
			if (i >= 6) {
				return;
			}
			indicators.set(i, true);
		}
	}

	public void setIndicatorsOff(int... index) {
		for (int i : index) {
			if (i >= 6) {
				return;
			}
			indicators.set(i, false);
		}
	}

	public double getOrbitRadius() {
		if (orbiting == null) {
			return 0;
		}
		double a2 = Math.pow((x - this.orbiting.getX()), 2);
		double b2 = Math.pow((y - this.orbiting.getY()), 2);
		// hint: pythagoras
		return Math.sqrt(a2 + b2);
	}

	public Location getGameLocation() {
		return new Location(x, y);
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
		case ASTEROID:
			return ColorConverter.convert(new Color(65, 70, 20));// brown
		case STAR:
			return ColorConverter.convert(new Color(240, 245, 70));// yellow
		case PLANET:
			return ColorConverter.convert(new Color(15, 220, 60));// green
		case SHIP:
			return ColorConverter.convert(new Color(150, 240, 140));
		// light green
		case MOON:
			return ColorConverter.convert(new Color(215, 240, 245));
		// white blue
		case JUMP:
			return ColorConverter.convert(new Color(255, 255, 255));// white
		case LAGRANGE:
			return ColorConverter.convert(new Color(240, 245, 70));// yellow
		default:
			return null;
		}
	}

}
