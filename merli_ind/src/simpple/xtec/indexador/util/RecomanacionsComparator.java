package simpple.xtec.indexador.util;

import java.util.Comparator;

public class RecomanacionsComparator implements Comparator
{
	public int compare(Object o1, Object o2)
	{
		RecomanacioObject a = (RecomanacioObject ) o1;
		RecomanacioObject b = (RecomanacioObject ) o2;
		
        if (a.repeticions > b.repeticions) {
        	return 1;
            }
		return -1;
	}
}
