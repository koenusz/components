package component.space.map.mouse;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.vaadin.hezamu.canvas.Canvas;

import com.vaadin.shared.MouseEventDetails;

import component.space.map.Location;
import component.space.map.SpaceMapImpl;
import lombok.Getter;

public class MouseController {
	
	private final static Logger logger = Logger.getLogger(MouseController.class.getName());


	private List<DragListener> listeners = new ArrayList<>();
	
	private boolean isDragging = false;

	@Getter
	private Location startLocation;

	@Getter
	private Location currentLocation;
	
	private void notifyListeners(){
		for(DragListener listener : listeners)
		{
			listener.onDrag(this);
		}
	}
	
	

	public void onMouseDown(MouseEventDetails med) {
		
		isDragging = true;
		currentLocation = new Location(med.getRelativeX(), med.getRelativeY());
		startLocation = new Location(med.getRelativeX(), med.getRelativeY());
		logger.info("Start drag at "+startLocation.toString());
		notifyListeners();
	}

	public void onMouseUp(MouseEventDetails med) {
		currentLocation.setX(med.getRelativeX());
		currentLocation.setY(med.getRelativeY());
		isDragging = false;
		logger.info("StopDrag at " + currentLocation.toString());
		notifyListeners();
	}

	public double getDraggingDistance() {
		return Math.sqrt(Math.pow(currentLocation.getX() - startLocation.getX(), 2)
				+ Math.pow(currentLocation.getY() - startLocation.getY(), 2));
	}
	
	public void register(DragListener listener)
	{
		listeners.add(listener);
	}
	
	public void unRegister(DragListener listener)
	{
		listeners.remove(listener);
	}

}
