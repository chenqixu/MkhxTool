package com.frame;

import client.Http360ClientThread;
import client.HttpDuokuClientThread;
import client.HttpMysticalcardClientThread;

public class MyCheckBox {
	private boolean bolValue = false;
	private String value = null;
    private Http360ClientThread thread1 = null;
    private HttpDuokuClientThread thread2 = null;
    private HttpMysticalcardClientThread thread3 = null;
    public MyCheckBox() {
    }
    public MyCheckBox(boolean bolValue, String value) {
        this.bolValue = bolValue;
        this.value = value;
    }
	public Http360ClientThread getThread1() {
		return thread1;
	}
	public void setThread1(Http360ClientThread thread1) {
		this.thread1 = thread1;
	}
	public HttpDuokuClientThread getThread2() {
		return thread2;
	}
	public void setThread2(HttpDuokuClientThread thread2) {
		this.thread2 = thread2;
	}
	public HttpMysticalcardClientThread getThread3() {
		return thread3;
	}
	public void setThread3(HttpMysticalcardClientThread thread3) {
		this.thread3 = thread3;
	}
	public boolean isBolValue() {
		return bolValue;
	}
	public void setBolValue(boolean bolValue) {
		this.bolValue = bolValue;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
