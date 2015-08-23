package testenum;

import java.util.EnumSet;

public class ENUMTest {   
    /**enumҲ���Զ�����class����*/   
    public enum Color{RED,ORANGLE,YELLO,GREEN,CYAN,BLUE,PURPLE}   
       
    public static void main(String[] args){        
        for(Week w:Week.values()){   
         /**ordinal()�����������λ��,��һ����0*/   
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