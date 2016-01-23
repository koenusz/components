package component.space.map.extension.client;

import org.vaadin.hezamu.canvas.client.ui.CanvasConnector;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.Connect;
import com.vaadin.ui.Label;

import component.space.map.Location;
import component.space.map.extension.client.SpaceMapClientRpc.Unit;
import lombok.Getter;

@Connect(component.space.map.extension.SpaceMapExtension.class)
public class SpaceMapConnector extends AbstractExtensionConnector {

	private static final long serialVersionUID = 293398092547287598L;

	Canvas canvas;

	double ratio;

	Context2d ctx;

	Unit unit;
	
	Label cursorTextLabel;

	@Override
	protected void extend(ServerConnector target) {
		canvas = ((CanvasConnector) target).getWidget();
		ctx = canvas.getContext2d();
	}

	private void drawLine() {
		ctx.beginPath();
		ctx.moveTo(startLocation.getX(), startLocation.getY());
		ctx.lineTo(currentLocation.getX(), currentLocation.getY());
		ctx.stroke();
	}

	private double distanceSelected() {
		if (unit == Unit.AU) {
			return getDraggingDistance() / 100 * ratio;
		} else if (unit == Unit.KM) {
			return getDraggingDistance() / 100 * 149598000 * ratio;
		}
		return 0.0;
	}

	private void writeTextAtCursor() {
		if(cursorTextLabel == null)
		{
			cursorTextLabel = new Label();
		}
		
		String text = distanceSelected() + " " + unit.toString();
	
	}

	private boolean isDragging = false;

	@Getter
	private Location startLocation;

	@Getter
	private Location currentLocation;

	public void onMouseDown(MouseEventDetails med) {
		ctx.save();
		isDragging = true;
		currentLocation = new Location(med.getRelativeX(), med.getRelativeY());
		startLocation = new Location(med.getRelativeX(), med.getRelativeY());
		drawLine();
	}

	public void onMouseMove(MouseEventDetails med) {

		if (isDragging) {
			// System.out.println("dragging");
			currentLocation.setX(med.getRelativeX());
			currentLocation.setY(med.getRelativeY());
			drawLine();
		}
	}

	public void onMouseUp(MouseEventDetails med) {
		currentLocation.setX(med.getRelativeX());
		currentLocation.setY(med.getRelativeY());
		isDragging = false;
		drawLine();
		ctx.restore();

	}

	public double getDraggingDistance() {
		return Math.sqrt(Math.pow(currentLocation.getX() - startLocation.getX(), 2)
				+ Math.pow(currentLocation.getY() - startLocation.getY(), 2));
	}

}
