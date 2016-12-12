package bean;

import java.util.Comparator;

public class ComparatorMapStageDetail implements Comparator {

	public int compare(Object o1, Object o2) {
		MapStageDetail m1 = (MapStageDetail) o1;
		MapStageDetail m2 = (MapStageDetail) o2;		
		int flag = 0;
		//比较MapStageDetail大小
//		if(m2.getMapStageDetailId()>m1.getMapStageDetailId())
//			flag = 1;
//		else
//			flag = 0;
		return m2.getMapStageDetailId()-m1.getMapStageDetailId();
	}

}
