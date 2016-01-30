package component.space.map;

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
		// FIXME
		addCombo(Type.MOON, 0);
		addFilter(Type.MOON, false);
	}

	protected boolean hasPermission(SpaceObject so) {
		return switchedOn.or(hasCombo).test(so);
	}

	protected Predicate<SpaceObject> hasCombo = so -> so.getIndicators().stream()
			.anyMatch(i -> comboMap.get(so.getType()).get(i));

	protected Predicate<SpaceObject> switchedOn = so -> {
		 logger.info(so.getName() + " is switched on");
		return filterSwitch.get(so.getType());
	};

	protected Predicate<SpaceObject> parentPermission = so -> {
		if (so.getOrbiting() == null) {
			logger.info("parentPermission is true");
			return true;
		}
		logger.info("parentPermission " + switchedOn.or(hasCombo).test(so.getOrbiting()));
		return switchedOn.or(hasCombo).test(so.getOrbiting());
	};

	protected void addFilter(Type type, boolean onOrOff) {
		
		notifyListeners();
		filterSwitch.merge(type, onOrOff, (old, newValue) -> newValue);
		logger.info("add filter for " + type + " " + onOrOff + " map " + filterSwitch.get(type));
	}

	protected void addCombo(Type type, int... indicators) {
		logger.info("add combo for " + type + " " + indicators);
		notifyListeners();
		for (int i : indicators) {
			if (i >= 6) {
				return;
			}
			comboMap.get(type).set(i, true);
		}

	}

	public void removeCombo(Type type, int... indicators) {
		notifyListeners();
		for (int i : indicators) {
			if (i >= 6) {
				return;
			}
			comboMap.get(type).set(i, false);
		}

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
