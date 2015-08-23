package testenum;

import java.util.EnumSet;

public class ENUMTest {   
    /**enum也可以定义在class里面*/   
    public enum Color{RED,ORANGLE,YELLO,GREEN,CYAN,BLUE,PURPLE}   
       
    public static void main(String[] args){        
        for(Week w:Week.values()){   
         /**ordinal()可以输出索引位置,第一个是0*/   
            System.out.print(w+":"+w.ordinal()+", ");   
        }   
        System.out.println();   
        for(Week w:EnumSet.range(Week.MONDAY,Week.SATURDAY)){   
            System.out.print(w+":"+w.ordinal()+", ");   
        }   
        System.out.println();   
        for(Color c:Color.values()){   
            System.out.print(c+":"+c.ordinal()+", ");   
        }   
        System.out.println(Color.BLUE);   
    }   
}   