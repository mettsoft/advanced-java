package com.ecc.model;

public class Key {
	private Integer index;
	private String value;

	public Key(Integer index, String value) {
		this.index = index;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Integer getIndex() {
		return index;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	@Override
	public boolean equals(Object otherKey) {
		return otherKey != null && otherKey instanceof Key && 
			this.index == ((Key) otherKey).index;
	}

	@Override
	public int hashCode() {
		return index.hashCode();
	}	

	@Override
	public String toString() {
		return value;
	}
}