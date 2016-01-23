package component.space.map;

import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import component.space.map.SpaceObject.Type;

@Theme("mytheme")
@Widgetset("component.space.map.SpaceMapWidgetset")
public class SpaceMapUi extends UI{

	private final static Logger logger = Logger.getLogger(SpaceMapUi.class.getName());

	private static final long serialVersionUID = -4777033788925901490L;

	SpaceMapImpl spaceMap = new SpaceMapImpl();
	

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		
		final VerticalLayout top = new VerticalLayout();

		
		
//		final HorizontalLayout buttonBar = new HorizontalLayout();
//		buttonBar.setHeight("15px");
//		
//		final VerticalLayout visibilityMenu = new VerticalLayout();
//		visibilityMenu.setWidth("30px");
		
//		
//		//sideMenuContent.setSizeFull();
//		sideMenuContent.addComponent(visibilityMenu);
//		sideMenuContent.addComponent(SpaceMapLayout);
//		
//		SpaceMapLayout.setSizeFull();
//		
//		top.setSizeFull();
//		top.addComponent(buttonBar);
//		top.addComponent(sideMenuContent);
		
		
		
		
		
		SpaceObject sun = new SpaceObject(Type.STAR, 6, 0, 0, "Sun");
		spaceMap.addSpaceObject(sun);
		SpaceObject mars = new SpaceObject(Type.PLANET, 4, 250, 150, "Mars", sun);
		spaceMap.addSpaceObject(mars);
		spaceMap.addSpaceObject(new SpaceObject(Type.MOON, 3, 75, 25, "MarsMoon 1", mars));
		// addSpaceObject(new SpaceObject(Type.SHIP, 2, 300, 150, "SS.
		// Baksteen"));
		SpaceObject ship1 = new SpaceObject(Type.SHIP, 2, 0, 0, "Baksteen 1");
		SpaceObject ship2 = new SpaceObject(Type.SHIP, 2, 0, 0, "Baksteen 2");
		mars.dockShip(ship1);
		mars.dockShip(ship2);

		spaceMap.init();
		
		final HorizontalLayout leftMenuPlusSpaceMap = new HorizontalLayout();
		leftMenuPlusSpaceMap.setSizeFull();
		leftMenuPlusSpaceMap.setSpacing(false);
		leftMenuPlusSpaceMap.setMargin(false);
		leftMenuPlusSpaceMap.addComponent(spaceMap);
		
		top.addComponent(leftMenuPlusSpaceMap);
		top.setMargin(false);
		top.setSpacing(false);
		top.setSizeFull();
		
		setContent(top);	
		

//		Button button = new Button("Draw the map");
//		button.addClickListener(e -> drawMap());
//		buttonBar.addComponent(button);
//
//		Button left = new Button("move left");
//		left.addClickListener(e -> panMap(-10, 0));
//		buttonBar.addComponent(left);
//
//		Button right = new Button("Move  right");
//		right.addClickListener(e -> panMap(10, 0));
//		buttonBar.addComponent(right);
//
//		Button up = new Button("move up");
//		up.addClickListener(e -> panMap(0, -10));
//		buttonBar.addComponent(up);
//
//		Button down = new Button("Move  down");
//		down.addClickListener(e -> panMap(0, 10));
//		buttonBar.addComponent(down);

	

		// make the center of the canvas the 0,0 coord.
		// canvas.translate(centerX, centerY);


	}
	

	@WebServlet(urlPatterns = "/space/*", name = "SpaceMapUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = SpaceMapUi.class, productionMode = false)
	public static class SpaceMapUIServlet extends VaadinServlet {

		private static final long serialVersionUID = 8733063843465286735L;
	}

	

}
