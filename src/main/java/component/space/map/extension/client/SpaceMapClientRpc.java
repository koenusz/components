package component.space.map.extension.client;

import com.vaadin.shared.communication.ClientRpc;

public interface SpaceMapClientRpc extends ClientRpc {
	
	public enum Unit{AU, KM};
	
	/**
	 * Set the units of the displayed distance measurement.
	 * 
	 * <p> Example values are KM or AU.
	 * 
	 * <p> Default is KM
	 * @param unit
	 */
	public void setDistanceUnit(Unit unit);
	
	/**
	 * The ratio between a pixel length and game distance.
	 * 
	 * <p> The is 100px for 1AU or 149.598.000Km for a ratio of 1.
	 * 
	 * <p> Example: a ratio of 2 will make 100px 2 AU long.
	 * @param multiplier
	 */
	public void adjustRatio(double multiplier);

}
