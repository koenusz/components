package component.space.map;

import static component.space.map.SpaceObject.Type.MOON;
import static component.space.map.SpaceObject.Type.PLANET;
import static component.space.map.SpaceObject.Type.STAR;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;

import component.space.map.SpaceObject.Type;

public class PaintPermissions {

	private final static Logger logger = Logger.getLogger(SpaceMapUi.class.getName());

	private List<PermissionsChangedListener> listeners = new ArrayList<>();

	private Map<Type, Boolean> filterSwitch = new HashMap<>();

	private Map<Type, BitSet> comboMap = new HashMap<>();

	protected PaintPermissions() {
		for (Type type : SpaceObject.Type.values()) {
			filterSwitch.put(type, true);
			BitSet set = new BitSet();
			comboMap.put(type, set);
		}
	}

	public boolean findPermissionForType(Type type) {
		return filterSwitch.get(type);
	}

	public boolean findComboForType(Type type, int indicator) {
		return comboMap.get(type).get(indicator);
	}

	public boolean hasPermission(SpaceObject so) {
		return switchedOn.or(hasCombo).and(parentPermission).test(so);
	}

	protected Predicate<SpaceObject> hasCombo = so -> so.getIndicators().stream()
			.anyMatch(i -> comboMap.get(so.getType()).get(i));

	protected Predicate<SpaceObject> switchedOn = so -> {
		// logger.info(so.getName() + " is switched " +
		// filterSwitch.get(so.getType()));
		return filterSwitch.get(so.getType());
	};

	protected Predicate<SpaceObject> parentPermission = so -> {
		if (so.getOrbiting() == null) {
			return true;
		} else if (so.getType() == MOON) {
			return filterSwitch.get(PLANET) && filterSwitch.get(STAR);
		} else if (so.getType() == PLANET) {
			return filterSwitch.get(STAR);
		}
		return false;
	};

	public void addFilter(Type type, boolean onOrOff) {

		filterSwitch.merge(type, onOrOff, (old, newValue) -> newValue);
		notifyListeners();
		logger.info("add filter for " + type + " " + onOrOff + " map " + filterSwitch.get(type));
	}

	public void addCombo(Type type, int... indicators) {
		logger.info("add combo for " + type + " " + indicators);
		for (int i : indicators) {
			if (i >= 6) {
				return;
			}
			comboMap.get(type).set(i, true);

		}
		notifyListeners();
	}

	public void removeCombo(Type type, int... indicators) {

		for (int i : indicators) {
			if (i >= 6) {
				return;
			}
			comboMap.get(type).set(i, false);

		}
		notifyListeners();
	}

	private void notifyListeners() {
		for (PermissionsChangedListener listener : listeners) {
			listener.onPermissionChange();
		}
	}

	protected void addListener(PermissionsChangedListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	protected void removeListener(PermissionsChangedListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

}
