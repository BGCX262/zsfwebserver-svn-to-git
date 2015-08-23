import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class MyTestEndoce {
	
	public static ConcurrentSkipListMap<Character , Character> encMap = new ConcurrentSkipListMap<Character, Character>();
	public static ConcurrentSkipListMap<Character , Character> decMap = new ConcurrentSkipListMap<Character, Character>();
	
	public static void init() {
		encMap.put('a', 'q');
		encMap.put('b', 'a');
		encMap.put('c', 'z');
		encMap.put('d', 'x');
		encMap.put('e', 's');
		encMap.put('f', 'w');
		encMap.put('g', 'e');
		encMap.put('h', 'd');
		encMap.put('i', 'c');
		encMap.put('j', 'v');
		encMap.put('k', 'f');
		encMap.put('l', 'r');
		encMap.put('m', 't');
		encMap.put('n', 'g');
		encMap.put('o', 'b');
		encMap.put('p', 'n');
		encMap.put('q', 'h');
		encMap.put('r', 'y');
		encMap.put('s', 'u');
		encMap.put('t', 'j');
		encMap.put('u', 'm');
		encMap.put('v', 'k');
		encMap.put('w', 'i');
		encMap.put('x', 'o');
		encMap.put('y', 'l');
		encMap.put('z', 'p');
		encMap.put('0', '7');
		encMap.put('1', '4');
		encMap.put('2', '1');
		encMap.put('3', '0');
		encMap.put('4', '2');
		encMap.put('5', '5');
		encMap.put('6', '8');
		encMap.put('7', '9');
		encMap.put('8', '6');
		encMap.put('9', '3');
		
		Set<Character> keys = encMap.keySet();
		for (Iterator<Character> iter = keys.iterator(); iter.hasNext(); ) {
			char key = iter.next();
			char value = encMap.get(key);
			decMap.put(value, key);
		}
	}
	
	public static void main(String[] args) {
		init();
		Date begin = new Date();
		String str = "Hello, Jetty, This is the Test Msg, Anf my id is 13545197547, my card number is 421003198706132633!!";
		
		for (int i = 0; i < 10000; i++) {
			String rtnVal = enCode(str);
			
			//System.out.println(rtnVal);
			

			String decVal = deCode(rtnVal);
			//System.out.println(decVal);
			
			if (!decVal.equals(str))
				System.out.println("error!");
		}
		
		Date end = new Date();
		
		System.out.println("耗时：-- " + (end.getTime() - begin.getTime()));
	}
	
	/**
	 * 加密
	 * @param str
	 * @return
	 */
	public static String enCode(String str) {
		String rtnVal = "";
		char[] charArr = str.toCharArray();
		for (int i = 0; i < charArr.length; i++) {
			if (encMap.containsKey(charArr[i])) {
				charArr[i] = encMap.get(charArr[i]);
			}
			rtnVal += charArr[i];
		}
		return rtnVal;
	}
	
	/**
	 * 解密
	 * @param str
	 * @return
	 */
	public static String deCode(String str) {

		String decVal = "";
		char[] decArr = str.toCharArray();
		for (int i = 0; i < decArr.length; i++) {
			if (decMap.containsKey(decArr[i])) {
				decArr[i] = decMap.get(decArr[i]);
			}
			decVal += decArr[i];
		}
		return decVal;
	}

}
