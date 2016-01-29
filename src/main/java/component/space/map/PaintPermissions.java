package component.space.map;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;

import component.space.map.SpaceObject.Type;

public class PaintPermissions {

	private final static Logger logger = Logger.getLogger(SpaceMapUi.class.getName());

	
	private Map<Type, Boolean> filterSwitch = new HashMap<>();

	private Map<Type, BitSet> comboMap = new HashMap<>();

	public PaintPermissions() {
		for (Type type : SpaceObject.Type.values()) {
			filterSwitch.put(type, true);
			BitSet set = new BitSet();
			comboMap.put(type, set);
		}
		//FIXME
		addCombo(Type.MOON,0);
		addFilter(Type.MOON, false);
	}

	public Predicate<SpaceObject> hasCombo = so -> so.getIndicators().stream()
			.anyMatch(i -> comboMap.get(so.getType()).get(i));

	public Predicate<SpaceObject> switchedOn = so -> {
		//logger.info(so.getName() + " is switched on");
		return filterSwitch.get(so.getType());
	};

	public void addFilter(Type type, boolean onOrOff) {
		filterSwitch.merge(type, onOrOff, (old, newValue) -> newValue);
	}

	public void addCombo(Type type, int... indicators) {
		for (int i : indicators) {
			if (i >= 6) {
				return;
			}
			comboMap.get(type).set(i, true);
		}

	}

	public void removeCombo(Type type, int... indicators) {
		for (int i : indicators) {
			if (i >= 6) {
				return;
			}
			comboMap.get(type).set(i, false);
		}

	}

}
