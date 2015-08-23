

import java.util.AbstractList;
import java.util.List;

public class ConvertListUtil {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Integer> intArrayAsList(final int[] a){   
        return new AbstractList(){   
  
            @Override   
            public Object get(int index)   
            {   
                return new Integer(a[index]);   
            }   
  
            @Override   
            public int size()   
            {   
                return a.length;   
            }   
               
            //排序所用到的方法   
            public Object set(int index, Object o){   
                int oldVal = a[index];   
                a[index] = ((Integer)o).intValue();   
                return new Integer(oldVal);   
            }

			@Override
			public String toString() {
				return super.toString().replace("[", "").replace("]", "").replace(", ", "\t");
			}
            
        };
    }   

}
