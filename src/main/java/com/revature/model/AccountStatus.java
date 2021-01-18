package com.revature.model;

public class AccountStatus {
	private int id;
	private String status;
	/**
	 * @param id
	 * @param status
	 */
	public AccountStatus(int id, String status) {
		super();
		this.id = id;
		this.status = status;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountStatus other = (AccountStatus) obj;
		if (id != other.id)
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AccountStatus [id=" + id + ", status=" + status + "]";
	}
}
