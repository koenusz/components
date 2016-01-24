package test.canvas.data;

import java.util.List;

import com.google.gwt.thirdparty.guava.common.collect.ArrayListMultimap;
import com.google.gwt.thirdparty.guava.common.collect.Multimap;

public class CirclesCylinderDataImpl implements CiclesCylinderData{

	
	Multimap<Domain, Perspective> perspectives = ArrayListMultimap.create();
	
	Multimap<Perspective, Aspect> aspects = ArrayListMultimap.create();
	
	public void addPerspective(Domain domain, Perspective perspective)
	{
		perspectives.put(domain, perspective);
	}
	
	public void addAspect(Perspective perspective, Aspect aspect)
	{
		aspects.put(perspective, aspect);
	}
	
	@Override
	public List<Perspective> getPerspectives(Domain domain) {
	
		return (List<Perspective>) perspectives.get(domain);
	}

	@Override
	public List<Aspect> getAspects(Perspective perspective) {
		return (List<Aspect>) aspects.get(perspective);
	}

}
