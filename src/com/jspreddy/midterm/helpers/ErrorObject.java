package com.jspreddy.midterm.helpers;

public class ErrorObject {
	String id;
	String message;

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}	
	
	public String toString()
	{
		return "["+id+":"+message+"]";
		
	}
}
