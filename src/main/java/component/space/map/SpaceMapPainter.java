package component.space.map;

import java.util.List;
import java.util.logging.Logger;

import org.vaadin.hezamu.canvas.Canvas;

import com.vaadin.shared.ui.colorpicker.Color;

import lombok.Getter;

public class SpaceMapPainter {

	private final static Logger logger = Logger.getLogger(SpaceMapUi.class.getName());

	private SpaceMapImpl spaceMap;

	@Getter
	private PaintPermissions permisisons = new PaintPermissions();

	public SpaceMapPainter(SpaceMapImpl spaceMap) {
		super();

		this.spaceMap = spaceMap;
	}

	protected void drawMap(Canvas canvas, List<SpaceObject> spaceObjects, double scale) {
		canvas.clear();

		drawBackground(canvas, new Color(35, 20, 180));
		// TODO maybe filter spaceobject outside the painting coordinates of the
		// canvas
		logger.info("##### drawing map");
		spaceObjects.stream().filter(permisisons.switchedOn.or(permisisons.hasCombo).and(permisisons.parentPermission))
				.forEach(so -> {
					logger.info("saceObject " + so.getName() + "switch/combo/parent " + permisisons.switchedOn.test(so) + "/"
							+ permisisons.hasCombo.test(so) + "/" + permisisons.parentPermission.test(so));
					drawSpaceObject(canvas, so, scale);
				});
	}

	private void drawInfoText(Canvas canvas, SpaceObject so) {
		Location drawLocation = spaceMap.gameLocationToCameraLocation(so.getGameLocation());

		canvas.saveContext();

		// canvas.translate(drawLocation.getX() + 15 , drawLocation.getY());
		canvas.setFillStyle(so.getColor());

		for (int i = 0; i < so.getMapInfo().size(); i++) {

			canvas.fillText(so.getMapInfo().get(i), drawLocation.getX() + 15, drawLocation.getY() + i * 10, 60);
		}

		canvas.restoreContext();

	}

	private void drawBackground(Canvas canvas, Color color) {
		canvas.saveContext();
		canvas.setFillStyle(ColorConverter.convert(color));
		canvas.fillRect(0, 0, spaceMap.getAbsoluteWidth(), spaceMap.getAbsoluteHeight());
		canvas.restoreContext();
	}

	protected void drawSpaceObject(Canvas canvas, SpaceObject so, double scale) {

		Location drawLocation = spaceMap.gameLocationToCameraLocation(so.getGameLocation());

		double drawRadius = so.getOrbitRadius() / scale;

		// dont draw if to close to the star (zoomed in), except ofc for the
		// star itself.
		if (so.getType() != SpaceObject.Type.STAR && drawRadius < 10) {
			return;
		}

		// logger.info("\n\tdrawing " + so.getName() + " at game " +
		// so.getGameLocation().toString() + "\n\tdrawing "
		// + so.getName() + " at camera " + drawLocation.toString());

		canvas.saveContext();
		// centerMap();
		canvas.translate(drawLocation.getX(), drawLocation.getY());
		canvas.setFillStyle(so.getColor());
		drawCircle(canvas, so.getSize(), true);
		canvas.restoreContext();

		drawOrbit(canvas, so, scale);
		drawCircleSelection(canvas, so);
		drawInfoText(canvas, so);
		drawIndicators(canvas, so);
	}

	// should input an object
	private void drawCircleSelection(Canvas canvas, SpaceObject so) {
		if (so.isSelected() == false) {
			return;
		}
		Location drawLocation = spaceMap.gameLocationToCameraLocation(so.getGameLocation());

		canvas.saveContext();

		// logger.info("drawing selection for " + so.getName() + " at " +
		// drawLocation.toString());
		canvas.translate(drawLocation.getX(), drawLocation.getY());
		// selection is always white
		canvas.setStrokeStyle(ColorConverter.convert(new Color(255, 255, 255)));
		drawCircle(canvas, so.getSize() + 2, false);
		canvas.restoreContext();
	}

	private void drawIndicators(Canvas canvas, SpaceObject so) {

		Location drawLocation = spaceMap.gameLocationToCameraLocation(so.getGameLocation());
		canvas.saveContext();
		for (int i = 0; i < 6; i++) {
			if (so.getIndicator(i)) {
				// selection is always white
				canvas.setStrokeStyle(so.getColor());
				canvas.setLineWidth(2);
				canvas.beginPath();

				double start = ((Math.PI * 1 / 3) * i) + (Math.PI * 2 / 3);
				double end = start + Math.PI * 0.3;
				canvas.arc(drawLocation.getX(), drawLocation.getY(), so.getSize() + 5, start, end, true);
				canvas.stroke();
				canvas.restoreContext();
			}
		}
	}

	private void drawOrbit(Canvas canvas, SpaceObject so, double scale) {
		if (so.getOrbiting() == null) {
			return;
		}
		Location drawLocation = spaceMap.gameLocationToCameraLocation(so.getOrbiting().getGameLocation());
		double drawRadius = so.getOrbitRadius() / scale;
		canvas.saveContext();
		canvas.translate(drawLocation.getX(), drawLocation.getY());
		canvas.setStrokeStyle(so.getColor());
		drawCircle(canvas, drawRadius, false);
		canvas.restoreContext();

	}

	private void drawCircle(Canvas canvas, double radius, boolean fill) {
		canvas.saveContext();
		canvas.beginPath();
		canvas.arc(0, 0, radius, 0, Math.PI * 2d, true);
		canvas.setLineWidth(1);
		canvas.stroke();
		if (fill) {
			canvas.fill();
		}
		canvas.restoreContext();
	}

}
