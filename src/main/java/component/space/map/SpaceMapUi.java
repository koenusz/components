package component.space.map;

import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import component.space.map.SpaceObject.Type;
import component.space.map.config.SpaceMapConfig;
import component.space.map.menu.VisibilityWindow;

@Theme("mytheme")
@Widgetset("component.space.map.SpaceMapWidgetset")
public class SpaceMapUi extends UI {

	private final static Logger logger = Logger.getLogger(SpaceMapUi.class.getName());

	private static final long serialVersionUID = -4777033788925901490L;

	SpaceMapImpl spaceMap = new SpaceMapImpl();

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		SpaceObject sun = new SpaceObject(Type.STAR, 6, 0, 0, 0, "Sun");
		spaceMap.addSpaceObject(sun);
		SpaceObject mars = new SpaceObject(Type.PLANET, 4, 24, 250, 150, "Mars", sun);
		spaceMap.addSpaceObject(mars);

		SpaceObject marsMoon1 = new SpaceObject(Type.MOON, 3, -10, 75, 25, "MarsMoon 1", mars);
		spaceMap.addSpaceObject(marsMoon1);
		spaceMap.addSpaceObject(new SpaceObject(Type.MOON, 3, 5, 1, 1, "Close Moon 2", mars));
		// addSpaceObject(new SpaceObject(Type.SHIP, 2, 300, 150, "SS.
		// Baksteen"));
		Fleet friend = spaceMap.newFleet(Fleet.Type.OWNED, mars, "Baksteen 1", "Heroes");
		Fleet enemy = spaceMap.newFleet(Fleet.Type.ENEMY, marsMoon1, "knallert", "Evildoers");
		Fleet enemy2 = spaceMap.newFleet(Fleet.Type.ENEMY, 100, 100, "beukert", "Evildoers");
		
		friend.setFleetInfo("This is a friend.");
		enemy.setFleetInfo("This is an enemy");
		enemy2.setFleetInfo("This is an other enemy");
		
		enemy2.mergeProperty("evildoers on board", "25");
		enemy2.mergeProperty("fuel", "25%");
		enemy2.mergeProperty("clowns on board", "25");
		enemy2.mergeProperty("shipclasses in fleet", "evil1, evil2");
		enemy2.mergeProperty("armaments", "missiles, lasers");
		

		SpaceMapConfig.activateIndicator("Has Minerals");
		SpaceMapConfig.activateIndicator("Has Colony");

		mars.setIndicatorsOn(0, 1, 2, 3, 4, 5);
		sun.setIndicatorsOn(0, 1, 3);
		marsMoon1.setIndicatorsOn(0);

		spaceMap.init();

		VerticalLayout leftMenu = new VerticalLayout();
		leftMenu.setWidthUndefined();

		Button arrow = new Button();
		final String arrowUp = "\u25B2";
		
		final String arrowDown = "\u25BC";
		arrow.setCaption(arrowDown);
		VisibilityWindow vis = new VisibilityWindow(spaceMap);
		vis.setVisible(false);
		UI.getCurrent().addWindow(vis);
		vis.setClosable(false);
		vis.setResizable(false);
		
		arrow.addClickListener( e -> {
		if(arrow.getCaption().equals(arrowDown))	
		{
		vis.setVisible(true);
		
		arrow.setCaption(arrowUp);
		} else 
		{
			arrow.setCaption(arrowDown);
			vis.setVisible(false);
		}
		});
		

//		BrowserWindowOpener popupOpener = new BrowserWindowOpener(VisibilityWindow.class);
//		popupOpener.setFeatures("height=300,width=300");
//		popupOpener.extend(arrow);
//
//		// Add a parameter
//		popupOpener.setParameter("foo", "bar");
//
//		// Set a fragment
//		popupOpener.setUriFragment("myfragment");

		Button button = new Button();
		button.setCaption("Cycle time");

		// HorizontalLayout leftMenuPlusSpaceMap = new HorizontalLayout();
		// leftMenuPlusSpaceMap.setSizeFull();
		// leftMenuPlusSpaceMap.setSpacing(false);
		// leftMenuPlusSpaceMap.setMargin(false);
		//
		// leftMenuPlusSpaceMap.addComponent(leftMenu);
		// leftMenuPlusSpaceMap.addComponent();
		//

		VerticalLayout topMenuContainer = new VerticalLayout();
		topMenuContainer.setSizeFull();

		HorizontalLayout topMenu = new HorizontalLayout();
		topMenu.addComponent(arrow);
		topMenu.addComponent(button);
		topMenu.setSpacing(true);

		topMenuContainer.addComponent(topMenu);
		topMenuContainer.setComponentAlignment(topMenu, Alignment.TOP_LEFT);
		topMenuContainer.addComponent(spaceMap);
		topMenuContainer.setExpandRatio(spaceMap, 1.0f);
		topMenuContainer.setMargin(false);
		topMenuContainer.setSpacing(false);

		setContent(topMenuContainer);

		// Button button = new Button("Draw the map");
		// button.addClickListener(e -> drawMap());
		// buttonBar.addComponent(button);
		//
		// Button left = new Button("move left");
		// left.addClickListener(e -> panMap(-10, 0));
		// buttonBar.addComponent(left);
		//
		// Button right = new Button("Move right");
		// right.addClickListener(e -> panMap(10, 0));
		// buttonBar.addComponent(right);
		//
		// Button up = new Button("move up");
		// up.addClickListener(e -> panMap(0, -10));
		// buttonBar.addComponent(up);
		//
		// Button down = new Button("Move down");
		// down.addClickListener(e -> panMap(0, 10));
		// buttonBar.addComponent(down);

		// make the center of the canvas the 0,0 coord.
		// canvas.translate(centerX, centerY);

	}

	@WebServlet(urlPatterns = "/space/*", name = "SpaceMapUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = SpaceMapUi.class, productionMode = false)
	public static class SpaceMapUIServlet extends VaadinServlet {

		private static final long serialVersionUID = 8733063843465286735L;
	}

}
