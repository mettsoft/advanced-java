package com.ecc.service;

import java.util.Map;
import com.ecc.model.Tuple;

public interface DataTableService {

	void printDataTable();

	Map<Tuple<Integer, Integer>, Integer> search(String keyword);

	void replaceByKey(Integer rowIndex, Integer columnIndex, String replacement);

	void replaceByValue(Integer rowIndex, Integer columnIndex, String replacement);

	void reset(Integer numberOfRows, Integer numberOfColumns, Integer cellSize) throws Exception;

	void add(String rowValues) throws Exception;

	void sort();
}