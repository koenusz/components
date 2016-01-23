package component.space.map.extension;

import org.vaadin.hezamu.canvas.Canvas;

import com.vaadin.server.AbstractExtension;

public class SpaceMapExtension extends AbstractExtension {

	private static final long serialVersionUID = -8180000295948529898L;
	
	 protected SpaceMapExtension(Canvas canvas) {
	        // Non-public constructor to discourage direct instantiation
	        extend(canvas);
	    }

}
