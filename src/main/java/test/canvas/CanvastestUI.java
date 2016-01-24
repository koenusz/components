package test.canvas;

import javax.servlet.annotation.WebServlet;

import org.vaadin.hezamu.canvas.Canvas;
import org.vaadin.hezamu.canvas.Canvas.CanvasMouseDownListener;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
@Theme("mytheme")
@Widgetset("component.space.map.SpaceMapWidgetset")
public class CanvastestUI extends UI {

	private Canvas canvas;

	private Canvas canvas2;

	private Canvas canvas3;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);
		addCanvas(layout);

		Button button = new Button("Click Me");
		button.addClickListener(e -> layout.addComponent(new Label("Thank you for clicking")));

		int centerX = 150;
		int centerY = 150;

		canvas.translate(centerX, centerY);

		// some mouse



		canvas.addMouseDownListener(null);

		// drawInitialPattern();
		drawCircle(100);
		drawCircle(75);
		drawCircle(50);
		drawCircle(25);

		drawPerspectiveLines();

		drawChart();

		drawInitialPattern();

		layout.addComponent(button);

	}

	private void drawCircle(double radius) {
		canvas.saveContext();
		double scale = 1.0;
		canvas.scale(scale, scale);
		canvas.beginPath();
		canvas.arc(0, 0, radius, 0, Math.PI * 2d, true);
		canvas.setLineWidth(1);
		canvas.stroke();
		canvas.restoreContext();
	}

	private void drawPerspectiveLines() {
		canvas.saveContext();

		int numberOfLines = 8;
		int x = -100;
		int y = 0;

		// canvas.beginPath();
		// canvas.moveTo(x, y);
		// canvas.lineTo(x + 200, y);
		// canvas.stroke();
		//
		for (int line = 0; line < numberOfLines; line++) {

			canvas.beginPath();
			canvas.moveTo(x, y);
			canvas.lineTo(x + 200, y);
			canvas.stroke();
			canvas.rotate(Math.PI * 2 / numberOfLines);

		}
		canvas.restoreContext();
	}

	private void drawChart() {

		// Get the dimensions of the canvas.
		int width = (int) canvas2.getWidth();
		int height = (int) canvas2.getHeight();
		double radius = Math.min(width, height) / 2.0;
		double cx = width / 2.0;
		double cy = height / 2.0;

		canvas2.setFillStyle(makeColor(255, 211, 25));
		canvas2.beginPath();
		canvas2.moveTo(cx, cy);
		canvas2.arc(cx, cy, radius, 0, Math.PI * 0.15, true);
		canvas2.fill();
		canvas2.setFillStyle(makeColor(255, 0, 25));
		canvas2.beginPath();
		canvas2.moveTo(cx, cy);
		canvas2.arc(cx, cy, radius, Math.PI * 0.15, Math.PI * 0.45, true);
		canvas2.fill();

		canvas2.setFillStyle(makeColor(0, 220, 200));
		canvas2.beginPath();
		canvas2.moveTo(cx, cy);
		canvas2.arc(cx, cy, radius, Math.PI * 0.45, Math.PI * 0.5, true);
		canvas2.fill();
		canvas2.setFillStyle(makeColor(25, 255, 25));
		canvas2.beginPath();
		canvas2.moveTo(cx, cy);
		canvas2.arc(cx, cy, radius, Math.PI * 0.5, Math.PI * 1.2, true);
		canvas2.fill();
		canvas2.setFillStyle(makeColor(25, 0, 255));
		canvas2.beginPath();
		canvas2.moveTo(cx, cy);
		canvas2.arc(cx, cy, radius, Math.PI * 1.2, Math.PI * 2, true);
		canvas2.fill();

	}

	private String makeColor(int red, int green, int blue) {
		return "rgb(" + red + "," + green + "," + blue + ")";
	}

	private void drawInitialPattern() {
		canvas3.saveContext();
		canvas3.translate(175d, 175d);
		canvas3.scale(1.6d, 1.6d);

		for (int i = 1; i < 6; ++i) {
			canvas3.saveContext();
			canvas3.setFillStyle("rgb(" + (51 * i) + "," + (255 - 51 * i) + ",255)");

			for (int j = 0; j < i * 6; ++j) {
				canvas3.rotate((Math.PI * 2d / (i * 6)));
				canvas3.beginPath();
				canvas3.arc(0d, i * 12.5d, 5d, 0d, Math.PI * 2d, true);
				canvas3.closePath();
				canvas3.fill();
			}

			canvas3.restoreContext();
		}

		canvas3.closePath();

		canvas3.restoreContext();
	}

	private int[][] getMatrix() {
		int[][] matrix = { { 1, 1, 1, 1 }, { 1, 1, 1, 0 }, { 1, 1, 0, 0 }, { 1, 0, 0, 0 } };
		return matrix;
	}

	private void addCanvas(VerticalLayout content) {
		content.addComponent(canvas = new Canvas());
		canvas.setWidth("300px");
		canvas.setHeight("300px");

		content.addComponent(canvas2 = new Canvas());
		canvas2.setWidth("300px");
		canvas2.setHeight("300px");

		content.addComponent(canvas3 = new Canvas());
		canvas3.setWidth("300px");
		canvas3.setHeight("300px");
	}

	@WebServlet(urlPatterns = "/canvastest", name = "CanvastestUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = CanvastestUI.class, productionMode = false)
	public static class CanvastestUIServlet extends VaadinServlet {
	}
}
