package com.revature.model;

public class Account {
	private int id;
	private double amount;
	private AccountType type;
	private AccountStatus status;
	/**
	 * @param id
	 * @param amount
	 * @param type
	 * @param status
	 */
	public Account(int id, double amount, AccountType type, AccountStatus status) {
		super();
		this.id = id;
		this.amount = amount;
		this.type = type;
		this.status = status;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}
	/**
	 * @return the type
	 */
	public AccountType getType() {
		return type;
	}
	/**
	 * @return the status
	 */
	public AccountStatus getStatus() {
		return status;
	}
	@Override
	public String toString() {
		return "Account [id=" + id + ", amount=" + amount + ", type=" + type + ", status=" + status + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + id;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Account other = (Account) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (id != other.id)
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	
}
