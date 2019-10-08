package com.revature.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * This is a data object for the errors
 * 
 * @author Alonzo Muncy (190107 Java-Spark-USF)
 *
 */
public class UserErrorResponse implements Serializable {

	private static final long serialVersionUID = -780898122370684787L;
	
	private int status;
	private String message;
	private long timestamp;
	
	public UserErrorResponse() {}
	
	public UserErrorResponse(int status, String message, long timestamp) {
		super();
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(message, status, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserErrorResponse))
			return false;
		UserErrorResponse other = (UserErrorResponse) obj;
		return Objects.equals(message, other.message) && status == other.status && timestamp == other.timestamp;
	}

	@Override
	public String toString() {
		return "UserErrorResponse [status=" + status + ", message=" + message + ", timestamp=" + timestamp + "]";
	}
	
}
