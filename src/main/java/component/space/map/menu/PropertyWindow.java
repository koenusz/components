package component.space.map.menu;

import java.util.Map;
import java.util.logging.Logger;

import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class PropertyWindow extends Window {

	private static final long serialVersionUID = -7337727292351145515L;
	private final static Logger logger = Logger.getLogger(PropertyWindow.class.getName());
	
	Table propertyTable;
	
	public PropertyWindow()
	{
		VerticalLayout content = new VerticalLayout();
		this.setContent(content);
		this.setVisible(true);
		
		this.setWidth("300px");
		this.setContent(content);
		
		
		propertyTable = new Table();
		content.addComponent(propertyTable);
		propertyTable.addContainerProperty("Property", String.class, null);
		propertyTable.addContainerProperty("Value", String.class, null);
		
		
	}
	
	private void PositionWindow()
	{
		this.setPosition(10, 200);
	}
	
	public void populateTable(Map<String, String> properties)
	{
		propertyTable.removeAllItems();
		for(String key : properties.keySet())
		{
			propertyTable.addItem(new Object[]{key,properties.get(key)}, key);
			
			
		}
		propertyTable.setPageLength(propertyTable.size());	
		
		PositionWindow();
		
	}
	
}
