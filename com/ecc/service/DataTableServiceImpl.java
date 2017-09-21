package com.ecc.service;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.stream.Collectors;

import com.ecc.utility.DataTableManager;
import com.ecc.model.Tuple;
import com.ecc.model.Key;
import com.ecc.model.DataTable;
import com.ecc.exception.InvalidColumnSizeException;
import com.ecc.exception.InvalidRowSizeException;

public class DataTableServiceImpl implements DataTableService {

	private DataTable dataTable;

	public DataTableServiceImpl(File inputFile) throws FileNotFoundException, ParseException, InvalidRowSizeException {
		dataTable = DataTableManager.createFromFile(inputFile);
	}

	@Override
	public void printDataTable() {
		System.out.println(dataTable);
	}

	@Override
	public Map<Tuple<Integer, Integer>, Integer> search(String keyword) {

		Map<Tuple<Integer, Integer>, Integer> resultMap = new HashMap<>();

		if (!keyword.equals("")) {		
			for (Integer i = 0; i < dataTable.getData().size(); i++) {
				Integer j = 0;
				for (Map.Entry<Key, String> entry : dataTable.getData().get(i).entrySet()) {

					Integer occurrences = 0;
					Integer fromIndex = 0;

					String concatenatedString = entry.getKey().getValue() + entry.getValue();

					do {
						fromIndex = concatenatedString.indexOf(keyword, fromIndex) + 1;
						if (fromIndex > 0) {
							occurrences++;
						}
					} while (fromIndex != 0);

					if (occurrences > 0) {
						resultMap.put(new Tuple<Integer, Integer>(i, j), occurrences);
					}

					j++;
				}
			}
		}

		return resultMap;
	}

	@Override
	public void replaceByKey(Integer rowIndex, Integer columnIndex, String replacement) {
		String previousValue = dataTable.getData().get(rowIndex).remove(new Key(columnIndex, null));
		dataTable.getData().get(rowIndex).put(new Key(columnIndex, replacement), previousValue);
		DataTableManager.saveToFile(dataTable);
	}

	@Override
	public void replaceByValue(Integer rowIndex, Integer columnIndex, String replacement) {
		dataTable.getData().get(rowIndex).replace(new Key(columnIndex, null), replacement);
		DataTableManager.saveToFile(dataTable);
	}

	@Override
	public void reset(Integer numberOfRows, Integer numberOfColumns, Integer cellSize) throws InvalidRowSizeException, InvalidColumnSizeException {
		dataTable = DataTableManager.createRandomDataTable(numberOfRows, numberOfColumns, cellSize);
	}

	@Override
	public void add(String rowValues) throws InvalidColumnSizeException, ParseException {
		Map<Key, String> dataRow = DataTableManager.parse(rowValues);

		if (!dataTable.getData().isEmpty() && dataRow.size() != dataTable.getData().get(0).size())
			throw new InvalidColumnSizeException(dataTable.getData().get(0).size());

		dataTable.getData().add(dataRow);
		DataTableManager.saveToFile(dataTable);
	}

	@Override
	public void sort() {
		dataTable.getData().sort((currentRow, nextRow) -> 
			currentRow
				.entrySet()
				.stream()
				.map(entry -> entry.getKey().getValue() + entry.getValue())
				.collect(Collectors.joining(""))
				.compareTo(
					nextRow
						.entrySet()
						.stream()
						.map(entry -> entry.getKey().getValue() + entry.getValue())
						.collect(Collectors.joining(""))
				)
		);
		DataTableManager.saveToFile(dataTable);
	}
}