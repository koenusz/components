package canvas;

import test.canvas.data.Aspect;
import test.canvas.data.CiclesCylinderData;
import test.canvas.data.CirclesCylinderDataImpl;
import test.canvas.data.Domain;
import test.canvas.data.Perspective;

public class CitclesCylinderDataTest {

	CirclesCylinderDataImpl data;

	private void initDataFull() {
		int sizeP = 7;
		int sizeA = 7;

		data = new CirclesCylinderDataImpl();
		
		for(Domain domain : Domain.values())
		{
			for(int per = 0; per < sizeP; per++)
			{
				Perspective p  = new Perspective();
				p.setName("per: " + per);
				data.addPerspective(domain, p);
				
				for(int asp = 0; asp < sizeA; asp++)
				{
					Aspect a = new Aspect();
					a.setName("asp: " + asp);
					a.setValue(1);
					data.addAspect(p, a);
					
				}
			}
		}
		
	}

	public CiclesCylinderData getData() {
		if (data == null) {
			//initData();

		}

		return data;
	}
}
