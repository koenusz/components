package component.space.map;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * The location represents the coordinate pair in the gameworld. This is
 * different form the canvas (camera) coordinates. It provides a means to
 * translate between the two.
 * 
 * 
 */

@Data
@AllArgsConstructor
public class Location {

	@NotNull
	private int x;
	@NotNull
	private int y;

	boolean isAt(int x, int y) {
		return this.x == x && this.y == y;
	}

}
