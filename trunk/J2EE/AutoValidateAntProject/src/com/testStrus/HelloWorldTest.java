/**
 * 
 */
package com.testStrus;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author D-io
 *
 */
public class HelloWorldTest {

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.testStrus.HelloWorld#main(java.lang.String[])}.
	 */
	@Test
	public void testMain() {
		System.out.println("success~");
	}

	/**
	 * Test method for {@link com.testStrus.HelloWorld#getConn()}.
	 */
	@Test
	public void testGetConn() throws Exception {
		assertTrue(new HelloWorld().getConn() != null);
		System.out.println("ok~");
	}

}
