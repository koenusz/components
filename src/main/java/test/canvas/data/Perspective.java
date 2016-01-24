package test.canvas.data;

import java.util.List;

public class Perspective {
	
	String name;
	
	List<Aspect> aspects;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Aspect> getAspects() {
		return aspects;
	}

	public void setAspects(List<Aspect> aspects) {
		this.aspects = aspects;
	}

}
