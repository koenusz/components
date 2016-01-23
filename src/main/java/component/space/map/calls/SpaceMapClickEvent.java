package component.space.map.calls;

import java.util.ArrayList;
import java.util.List;

import component.space.map.SpaceObject;

public class SpaceMapClickEvent {

	int downX;
	int downY;

	int upX;
	int upY;
	
	/**
	 * The objects at the downLocation
	 */
	List<SpaceObject> downSpaceObjects = new ArrayList<>();
	
	/**
	 * The objects at the upLocation
	 */
	List<SpaceObject> upSpaceObjects = new ArrayList<>();

}
