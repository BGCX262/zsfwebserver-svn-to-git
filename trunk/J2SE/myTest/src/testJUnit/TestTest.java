package testJUnit;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TestTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("begin........");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("end........");
	}

	@Test(expected=ArithmeticException.class)
	public void testDivide() {
		for (int i = 1; i < 100; i++) {
			for (int j = 1; j < 100; j++) {
				System.out.println(i + ":" + j);
				assertEquals(i / j, TestJUnit.divide(i, j));
			}
		}
		assertEquals(0, TestJUnit.divide(10, 0));
	}

	@Ignore("忽略乘法测试")
	@Test  
	public void testMultiple() {
		fail("尚未实现");
	}

}
