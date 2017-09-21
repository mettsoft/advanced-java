package com.ecc.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class DataTable {

	private List<Map<Key, String>> data;

	public DataTable() {
		data = new ArrayList<Map<Key, String>>();
	}

	public void setData(List<Map<Key, String>> data) {
		this.data = data;
	}

	public List<Map<Key, String>> getData() {
		return data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (Integer i = 0; i < data.size(); i++) {	
			for (Map.Entry<Key, String> entry : data.get(i).entrySet()) {
				builder.append(String.format("(%s, %s) ", entry.getKey().getValue(), entry.getValue()));
			}
			if (i < data.size() - 1)
				builder.append("\n");
		}

		return builder.toString();
	}
}