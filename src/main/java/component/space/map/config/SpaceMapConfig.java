package component.space.map.config;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SpaceMapConfig {
	
	
	/**
	 * the name of the current solar system.
	 */
	@Getter @Setter private static String solarSystemName = "sol";

	private static List<Button> buttons = new ArrayList<>();

	private static List<String> indicators = new ArrayList<>(6);
	
	/**
	 * Adds a button to the top button bar
	 * @param button
	 */
	public static void addButton(Button button)
	{
		buttons.add(button);
	}
	
	public static List<Button> getButtons()
	{
		return buttons;
	}

	
	/**
	 * Makes an entry into the indicator list. This makes them appear in the ui.
	 * 
	 * @return false if there are more than 5.
	 */
	public static boolean activateIndicator(String indicatorName) {
		if (indicators.size() > 5 || indicators.contains(indicatorName)) {
			return false;
		}
		return indicators.add(indicatorName);

	}

	/**
	 * Returns the name of the indicator at the respective index.
	 * 
	 * @param index
	 *            between 0 and 5
	 * @return
	 */
	public static String getIndicator(int index) {
		return indicators.get(index);
	}
	
	/**
	 * Returns a list of indicator names, max 6.
	 * 
	 * @return
	 */
	public static List<String> getIndicators() {
		return indicators;}
	
	public static int findIndicatorIndex(String indicatorName)
	{
		return indicators.indexOf(indicatorName);
	}
}

