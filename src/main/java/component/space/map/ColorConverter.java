package component.space.map;

import com.vaadin.shared.ui.colorpicker.Color;

public class ColorConverter {

	/**
	 * Convert a {@link Color} to the string representation that the canvas addon can understand.
	 * 
	 * <p>Example output: "rgb(0,0,0)"
	 * 
	 * @param rgb
	 * @return 
	 */
	static public String convert (Color rgb) {
		return "rgb(" + rgb.getRed() + "," + rgb.getGreen() + "," + rgb.getBlue() + ")";
	}

}
