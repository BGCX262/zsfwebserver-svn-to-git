package com.mina.data;


/**
 * 用户变量
 * @author zsf
 * 2011-4-12 上午10:28:00
 */
public class UserVar {
	
	private Object value;

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String getString() {
		return value.toString();
	}
	
	public int getInt() {
		return Integer.valueOf(value.toString()).intValue();
	}
	
	public double getDouble() {
		return Double.valueOf(value.toString()).doubleValue();
	}
	
	public float getFloat() {
		return Double.valueOf(value.toString()).floatValue();
	}
	
	public UserVar() {}
	
	public UserVar(Object value) {
		this.value = value;
	}

}
