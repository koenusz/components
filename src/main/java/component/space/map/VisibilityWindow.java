package component.space.map;


import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import component.space.map.SpaceObject.Type;

public class VisibilityWindow extends Window {

	private static final long serialVersionUID = -6779786309470946282L;
	
	
	private SpaceMapImpl spaceMap;
	
	VerticalLayout content = new VerticalLayout();
	
	TabSheet tabsheet = new TabSheet();
	
	Tree tree;
	
	
	public VisibilityWindow(SpaceMapImpl spaceMap)
	{
		this.spaceMap = spaceMap;
		this.setWidth("300px");
		
		this.setPosition(10, 40);
		
		//TODO fix alignment of the window
		Component parent =  this.getParent();
		//parent.setComponentAlignment()
		
		this.setContent(content);
		
		TextField searchBox = new TextField("What  object are you looking for?" );
		searchBox.setImmediate(true);
		searchBox.setSelectionRange(0, searchBox.getMaxLength());
		//searchBox.addTextChangeListener(e -> (System.out::print) );
		
		content.addComponent(searchBox);
		content.addComponent(tabsheet);
		
		
		addVisibilityTab();
		addOverviewTreeTab();
	}
	
	
	
	private void addVisibilityTab()
	{
		VerticalLayout visibilityTab = new VerticalLayout();
		tabsheet.addTab(visibilityTab, "Visibility");
		
	Accordion visibilityAccordion = new Accordion();
	visibilityTab.addComponent(visibilityAccordion);	
		for(Type type : SpaceObject.Type.values())
		{
			VerticalLayout typeTab = new VerticalLayout();
			visibilityAccordion.addTab(typeTab, type.toString());
			
			typeTab.addComponent(new CheckBox("Show " + type.toString()));
			
			for(String config : SpaceMapConfig.getIndicators())
			{
				CheckBox indicatorBox = new CheckBox(config);
				typeTab.addComponent(indicatorBox);
				indicatorBox.setEnabled(false);
			}
			
		}
		
	}
	
	private void addOverviewTreeTab()
	{
		VerticalLayout overviewTreeTab = new VerticalLayout();
		tabsheet.addTab(overviewTreeTab, "Solarsystem overview");
		
		tree = new Tree(SpaceMapConfig.getSolarSystemName());
		overviewTreeTab.addComponent(tree);
		SpaceObject mainStar = spaceMap.getMainStar();
		tree.addItem(mainStar.getName());
		//addChildren(mainStar, tree);
		
		
	

		
	}
	
	private void addChildren(SpaceObject so)
	{
		for(SpaceObject child : so.getOrbitingThis())
		{
			tree.addItem(child.getName());
			tree.setParent(child.getName(), so.getName());
			addChildren(so);
		}
	}
	

/*	@Override
	protected void init(VaadinRequest request) {
		
		
		
		setContent(new Label("This is a popup where parameter foo="
                + request.getParameter("foo") + " and fragment is set to "
                + getPage().getUriFragment()));
		
		// Create a sub-window and set the content
        Window subWindow = new Window("Sub-window");
        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        subWindow.setContent(subContent);
        
        // Put some components in it
        subContent.addComponent(new Label("Meatball sub"));
        subContent.addComponent(new Button("Awlright"));

        // Center it in the browser window
        subWindow.center();

        // Open it in the UI
        addWindow(subWindow);
	}*/

}
