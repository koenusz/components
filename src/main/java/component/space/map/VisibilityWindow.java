package component.space.map;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
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

	private final static Logger logger = Logger.getLogger(VisibilityWindow.class.getName());

	private static final long serialVersionUID = -6779786309470946282L;

	private SpaceMapImpl spaceMap;

	VerticalLayout content = new VerticalLayout();

	TabSheet tabsheet = new TabSheet();

	Tree tree;
	
	Tree fleetTree;

	SimpleStringFilter filter = null;

	public VisibilityWindow(SpaceMapImpl spaceMap) {
		this.spaceMap = spaceMap;
		this.setWidth("300px");
		this.setPosition(10, 40);
		this.setContent(content);

		TextField searchBox = new TextField("What  object are you looking for?");
		searchBox.setImmediate(true);
		searchBox.setSelectionRange(0, searchBox.getMaxLength());
		searchBox.addTextChangeListener(e -> textChange(e));

		content.addComponent(searchBox);
		content.addComponent(tabsheet);

		addVisibilityTab();
		addOverviewTreeTab();
		addFleetTab();
	}

	private void textChange(TextChangeEvent event) {
		Filterable f = (Filterable) tree.getContainerDataSource();

		// Remove old filter
		if (filter != null)
			f.removeContainerFilter(filter);

		// Set new filter for the "caption" property
		filter = new SimpleStringFilter("name", event.getText(), true, false);
		f.addContainerFilter(filter);
	}
	
	private void addFleetTab()
	{
		VerticalLayout fleetTab = new VerticalLayout();
		tabsheet.addTab(fleetTab, "Fleets");
		fleetTree = new Tree(SpaceMapConfig.getSolarSystemName());
		fleetTab.addComponent(fleetTree);
		fleetTree.setImmediate(true);
		
		fleetTree.addContainerProperty("name", String.class, null);
		fleetTree.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		fleetTree.setItemCaptionPropertyId("name");
		
		for(Fleet fleet: spaceMap.getFleets())
		{
			Item factionItem = fleetTree.getItem(fleet.getFaction());
			if( factionItem == null)
			{
				factionItem = fleetTree.addItem(fleet.getFaction());
				factionItem.getItemProperty("name").setValue(fleet.getFaction());
				
			}
			Item fleetItem = fleetTree.addItem(fleet);
			fleetItem.getItemProperty("name").setValue(fleet.getName());
			fleetTree.setParent(fleetItem, factionItem);
			
		}
		
		
	}

	private void addVisibilityTab() {
		VerticalLayout visibilityTab = new VerticalLayout();
		tabsheet.addTab(visibilityTab, "Visibility");

		Accordion visibilityAccordion = new Accordion();
		visibilityTab.addComponent(visibilityAccordion);
		for (Type type : SpaceObject.Type.values()) {
			VerticalLayout typeTab = new VerticalLayout();
			visibilityAccordion.addTab(typeTab, type.toString());

			CheckBox showType = new CheckBox("Show " + type.toString());
			showType.setValue(spaceMap.getPaintPermissions().findPermissionForType(type));

			showType.addValueChangeListener(e -> {
				spaceMap.getPaintPermissions().addFilter(type, (boolean) e.getProperty().getValue());
			});
			showType.setImmediate(true);

			typeTab.addComponent(showType);

			for (String config : SpaceMapConfig.getIndicators()) {

				CheckBox indicatorBox = new CheckBox(config);
				showType.addValueChangeListener(e -> indicatorBox.setEnabled(!(Boolean) e.getProperty().getValue()));
				indicatorBox.setValue(spaceMap.getPaintPermissions().findComboForType(type,
						SpaceMapConfig.findIndicatorIndex(config)));
				indicatorBox.addValueChangeListener(e -> {
					if ((Boolean) e.getProperty().getValue()) {
						spaceMap.getPaintPermissions().addCombo(type, SpaceMapConfig.findIndicatorIndex(config));
					} else {
						spaceMap.getPaintPermissions().removeCombo(type, SpaceMapConfig.findIndicatorIndex(config));
					}
				});

				typeTab.addComponent(indicatorBox);
				indicatorBox.setEnabled(false);

			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addOverviewTreeTab() {
		VerticalLayout overviewTreeTab = new VerticalLayout();
		tabsheet.addTab(overviewTreeTab, "Solarsystem");
		tree = new Tree(SpaceMapConfig.getSolarSystemName());
		overviewTreeTab.addComponent(tree);
		SpaceObject mainStar = spaceMap.getMainStar();
		tree.setImmediate(true);

		tree.addContainerProperty("name", String.class, null);
		tree.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		tree.setItemCaptionPropertyId("name");

		Item item = tree.addItem(mainStar);

		item.getItemProperty("name").setValue(mainStar.getName());
		addChildren(mainStar);
		tree.expandItemsRecursively(mainStar);
		tree.addItemClickListener(e -> selectListener(e));

	}

	private void selectListener(ItemClickEvent event) {
		SpaceObject so = (SpaceObject) event.getItemId();
		logger.info("selecting " + so.getName());
		spaceMap.selectSpaceObject(so);
	}

	@SuppressWarnings("unchecked")
	private void addSpaceObjectToTree(SpaceObject so) {

		Item item = tree.addItem(so);
		item.getItemProperty("name").setValue(so.getName());

	}

	private void addChildren(SpaceObject parent) {
		for (SpaceObject child : parent.getOrbitingThis()) {
			if (spaceMap.getPaintPermissions().hasPermission(child)) {
				addSpaceObjectToTree(child);
				tree.setParent(child, parent);
				addChildren(child);
			}
		}
	}

}
