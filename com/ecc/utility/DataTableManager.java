package com.ecc.utility;

import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.concurrent.ThreadLocalRandom;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import com.ecc.model.DataTable;
import com.ecc.model.Key;
import com.ecc.exception.InvalidColumnSizeException;
import com.ecc.exception.InvalidRowSizeException;

public class DataTableManager {

	private static final Pattern PATTERN = Pattern.compile("(?:\\(([^()]+),([^()]+)\\))");
	private static final String DEFAULT_FILE_PATH = "./build/default.DAT";

	private static final Integer ASCII_VALUE_OF_SPACE = 32;
	private static final Integer ASCII_VALUE_OF_DEL = 127;

	public static DataTable createFromFile(File inputFile) throws FileNotFoundException, ParseException, InvalidRowSizeException {

		DataTable dataTable = new DataTable();
		List<Map<Key, String>> data = dataTable.getData();

		if (inputFile == null || !inputFile.exists()) {
			inputFile = new File(DEFAULT_FILE_PATH);
		}

		Scanner	scanner = new Scanner(new FileReader(inputFile));

		while(scanner.hasNextLine()) {
			data.add(parse(scanner.nextLine()));
		}

		if (data.isEmpty()) {
			throw new InvalidRowSizeException("non-zero");			
		}

		saveToFile(dataTable);
		return dataTable;
	}

	public static Map<Key,String> parse(String rowValues) throws ParseException {
		
		Matcher matcher = PATTERN.matcher(rowValues);

		Map<Key, String> extractedValues = new HashMap<>();

		Integer i = 0;
		while (matcher.find()) {
			extractedValues.put(new Key(i++, matcher.group(1)), matcher.group(2));
		}

		if (extractedValues.isEmpty())
			throw new ParseException("Parsing failed! Conform to this pattern: \"(abc, bcd), (1sd, f3g)\".", 0);

		return extractedValues;

	}

	public static DataTable createRandomDataTable(Integer numberOfRows, Integer numberOfColumns, Integer cellSize) throws InvalidRowSizeException, InvalidColumnSizeException {

		if (numberOfRows <= 0) {
			throw new InvalidRowSizeException("non-zero");			
		}

		if (numberOfColumns <= 0) {
			throw new InvalidColumnSizeException("non-zero");
		}		

		DataTable dataTable = new DataTable();

		for (Integer i = 0; i < numberOfRows; i++) {
			Map<Key, String> rowItem = new HashMap<>();
			for (Integer j = 0; j < numberOfColumns; j++) {
				rowItem.put(
					new Key(j, createCharacterSequence(cellSize)),
					createCharacterSequence(cellSize)
				);
			}
			dataTable.getData().add(rowItem);
		}
		saveToFile(dataTable);
		return dataTable;
	}

	private static String createCharacterSequence(Integer length) {
		StringBuilder builder = new StringBuilder();
		for (Integer i = 0; i < length; i++) {
			builder.append((char) ThreadLocalRandom.current().nextInt(ASCII_VALUE_OF_SPACE, ASCII_VALUE_OF_DEL));
		}
		return builder.toString();
	}

	public static void saveToFile(DataTable dataTable) {
		try (FileWriter writer = new FileWriter(DEFAULT_FILE_PATH)) {
			writer.write(dataTable.toString());
		}
		catch (IOException exception) {
			System.out.println(String.format("Error: %s", exception.getMessage()));	
		}		
	}

}