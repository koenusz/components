package component.space.map;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.vaadin.hezamu.canvas.Canvas;
import org.vaadin.hezamu.canvas.client.mousewheel.MouseWheelEventDetails;

import com.ejt.vaadin.sizereporter.ComponentResizeEvent;
import com.ejt.vaadin.sizereporter.SizeReporter;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;

import component.space.map.SpaceObject.Type;
import component.space.map.calls.SpaceMapListener;
import component.space.map.mouse.DragListener;
import component.space.map.mouse.MouseController;
import lombok.Getter;

public class SpaceMapImpl extends Panel implements DragListener {

	private static final long serialVersionUID = 1817025638490555374L;

	List<SpaceMapListener> subscribers = new ArrayList<>();

	private final static Logger logger = Logger.getLogger(SpaceMapImpl.class.getName());

	Canvas canvas;

	// the bigger the scale the more zoomed out. So objects on the screen,
	// orbits for instance, get smaller.
	private double scale = 1;

	private int mouseZoomX = 0;

	private int mouseZoomY = 0;

	private int panOffsetX = 0;

	private int panOffsetY = 0;

	private List<SpaceObject> spaceObjects = new ArrayList<>();

	private MouseController mouse = new MouseController();

	private SpaceMapPainter painter = new SpaceMapPainter(this);

	private SizeReporter sizeReporter;

	@Getter
	private int absoluteHeight = 0;
	@Getter
	private int absoluteWidth = 0;

	public void init() {

		addCanvas(this, 100, 100, "%");

		addMouseListeners(canvas, this);
		sizeReporter = new SizeReporter(this);
		sizeReporter.addResizeListener(this::updateSize);

		painter.drawMap(canvas, spaceObjects, scale);
	}

	private void updateSize(ComponentResizeEvent event) {
		this.absoluteHeight = event.getHeight();
		this.absoluteWidth = event.getWidth();
		logger.info("Resizing server canvas " + absoluteHeight + "," + absoluteWidth);
		painter.drawMap(canvas, spaceObjects, scale);
	}

	private void listenerCallback() {
		for (SpaceMapListener listener : subscribers) {
			listener.callback();
		}
	}

	private void addMouseListeners(Canvas canvas, SpaceMapImpl space) {
		canvas.addMouseUpListener(e -> selectObjectAt(new Location(e.getRelativeX(), e.getRelativeY())));
		canvas.addMouseWheelListener(space::zoom);
		canvas.addMouseDownListener(mouse::onMouseDown);
		canvas.addMouseUpListener(mouse::onMouseUp);

		mouse.register(this);
	}

	private void panMap(int amountX, int amountY) {
		panOffsetX = panOffsetX + amountX;
		panOffsetY = panOffsetY + amountY;
		logger.info("Panning: " + amountX + "," + amountY + " to " + panOffsetX);
		painter.drawMap(canvas, spaceObjects, scale);
	}

	public SpaceObject getMainStar() {
		for (SpaceObject so : spaceObjects) {
			if (so.getType() == Type.STAR && so.getOrbiting() == null) {
				return so;
			}
		}
		// should never happen
		throw new RuntimeException("No main star found");
	}

	public SpaceObject getSelectedObject() {
		for (SpaceObject so : spaceObjects) {
			if (so.isSelected()) {
				return so;
			}
		}
		return null;
	}

	public Location gameLocationToCameraLocation(final Location game) {

		Location cameraLocation = game;

		SpaceObject so = getSelectedObject();
		// focus on selected object
		if (so != null) {
			cameraLocation.setX(cameraLocation.getX() - so.getX());
			cameraLocation.setY(cameraLocation.getY() - so.getY());
		} else {
			//focus on mouse cursor
			cameraLocation.setX(cameraLocation.getX() - mouseZoomX);
			cameraLocation.setY(cameraLocation.getY() - mouseZoomY);
		}

		// adjust for zooming
		// only the game coordinates need to be adjusted for zooming.
		cameraLocation.setX((int) (cameraLocation.getX() / scale));
		cameraLocation.setY((int) (cameraLocation.getY() / scale));

		// adjust for panning
		cameraLocation.setX(cameraLocation.getX() + panOffsetX);
		cameraLocation.setY(cameraLocation.getY() + panOffsetY);

		// canvas starts counting top left and game starts counting center (0,0)
		// converting to camera should be done lastsince it is just an offset.
		// adjust for selected objects
		cameraLocation.setX((int) (cameraLocation.getX() + absoluteWidth / 2));
		cameraLocation.setY((int) (cameraLocation.getY() + absoluteHeight / 2));

		// logger.info("converting game " + game.toString() + " -> camera " +
		// cameraLocation.toString());

		return cameraLocation;
	}

	private Location cameraLocationToGameLocation(Location camera) {
		Location gameLocation = camera;
		// canvas starts counting top left and game starts counting center (0,0)
		gameLocation.setX((int) (gameLocation.getX() - absoluteWidth / 2));
		gameLocation.setY((int) (gameLocation.getY() - absoluteHeight / 2));

		// adjust for zooming
		gameLocation.setX((int) (gameLocation.getX() * scale));
		gameLocation.setY((int) (gameLocation.getY() * scale));

		// adjust for panning
		gameLocation.setX(gameLocation.getX() - panOffsetX);
		gameLocation.setY(gameLocation.getY() - panOffsetY);

		// logger.info("converting camera " + gameLocation.toString() + " ->
		// game " + camera.toString());

		SpaceObject so = getSelectedObject();
		// focus on selected object
		if (so != null) {
			gameLocation.setX(gameLocation.getX() + so.getX());
			gameLocation.setY(gameLocation.getY() + so.getY());
		} else {
			//focus on mouse cursor
			gameLocation.setX(gameLocation.getX() + mouseZoomX);
			gameLocation.setY(gameLocation.getY() + mouseZoomY);
		}
		return gameLocation;
	}

	// TODO refactor this code to be more clean.
	private void selectObjectAt(Location clickLocation) {

		// logger.info("selecting at " + clickLocation.toString());

		Location gameLocation = cameraLocationToGameLocation(clickLocation);

		// logger.info("selecting at gamelocation: {" + gameLocation.getX() + ",
		// " + gameLocation.getY() + "}");

		boolean redraw = false;
		// int threshhold = 2;
		for (SpaceObject so : spaceObjects) {

			// in order to not have a single pixel to select we will create a
			// selectbox roughtly equivalent with the drawing of the object.
			// Bit of extra valid pixels to make selection easier.
			// The collision (size) of the mouseclicks need tobe adjusted for
			// the zoom.
			double adjustedSize = (so.getSize() + 2) * scale;
			double selectBoxLeft = so.getX() - adjustedSize;
			double selectBoxRight = so.getX() + adjustedSize;
			double selectBoxUp = so.getY() - adjustedSize;
			double selectBoxDown = so.getY() + adjustedSize;

			// logger.info("ClickBox: { x:[" + selectBoxLeft + ", " +
			// selectBoxRight + "], y:[ " + selectBoxUp + ", "
			// + selectBoxDown + "]}");

			if (selectBoxLeft < gameLocation.getX() && gameLocation.getX() < selectBoxRight
					&& selectBoxUp < gameLocation.getY() && gameLocation.getY() < selectBoxDown) {
				so.setSelected(true);
				// only redraw if something changed
				redraw = true;
			} else {
				so.setSelected(false);
			}
		}
		if (redraw) {
			painter.drawMap(canvas, spaceObjects, scale);
		}

	}

	private void zoom(MouseWheelEventDetails med) {

		logger.info("zooming at x " + med.getRelativeX() + " y " + med.getRelativeY());
		
		if (med.isNorth()) {
			scale = scale * 2; // * med.getScrollspeed()/3;
		} else if (med.isSouth()) {
			scale = scale * 0.5;
		}
		//TODO figure out how to zoom on mouse cursor
//		mouseZoomX = (int) (med.getRelativeX()/scale);
//		mouseZoomY = (int) (med.getRelativeY()/scale);

		
		painter.drawMap(canvas, spaceObjects, scale);
	}

	public void addSpaceObject(SpaceObject spaceObject) {
		for (SpaceObject so : spaceObjects) {
			if (spaceObject.getName() == null || so.getName().equals(spaceObject.getName())) {
				throw new RuntimeException("SpaceObject with name " + spaceObject.getName()
						+ " is invalid, either null or already exists on the map.");
			}

		}
		spaceObjects.add(spaceObject);
	}

	// remove from the map
	public boolean removeSpaceObject(String name) {
		for (SpaceObject so : spaceObjects) {
			if (so.getName().equals(name)) {
				return spaceObjects.remove(so);
			}
		}
		return false;
	}

	// Mode can be px or %.
	private void addCanvas(Panel content, int height, int width, String mode) {
		content.setContent(canvas = new Canvas());
		canvas.setWidth(width + mode);
		canvas.setHeight(height + mode);
	}

	@Override
	public void onDrag(MouseController mouse) {
		// if the startlocation is empty: pan

		this.panMap(mouse.getCurrentLocation().getX() - mouse.getStartLocation().getX(),
				mouse.getCurrentLocation().getY() - mouse.getStartLocation().getY());

		// if the startlocation is occupied, behaviour based on object. //TODO
		// We probally should not make the spacemap aware of what that behaviour
		// should be besides not panning the map.

	}
}
