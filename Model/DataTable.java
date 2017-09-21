package Model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.function.Function;

import java.util.Scanner;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import Exception.InvalidColumnSizeException;
import Exception.InvalidRowSizeException;
import Exception.ParseFailureException;
import Utility.AsciiGenerator;

public final class DataTable {

	private static final String DEFAULT_FILE_PATH = "./build/default.DAT";
	private final Pattern PATTERN = Pattern.compile("(?:\\(([^()]+),([^()]+)\\))");

	private final List<Map<Key, String>> DATA;

	private void writeToFile(String data) {
		try (FileWriter writer = new FileWriter(DEFAULT_FILE_PATH)) {
			writer.write(data);
		}
		catch (IOException exception) {
			System.out.println(String.format("ERROR: %s", exception));	
		}		
	}

	public DataTable(int numberOfRows, int numberOfColumns, int cellSize) throws InvalidRowSizeException, InvalidColumnSizeException {

		if (numberOfRows <= 0) {
			throw new InvalidRowSizeException("non-zero");			
		}

		if (numberOfColumns <= 0) {
			throw new InvalidColumnSizeException("non-zero");
		}		

		DATA = new ArrayList<>(); 

		for (int i = 0; i < numberOfRows; i++) {
			Map<Key, String> rowItem = new HashMap<>();
			for (int j = 0; j < numberOfColumns; j++) {
				rowItem.put(
					new Key(j, AsciiGenerator.createCharacterSequence(cellSize)),
					AsciiGenerator.createCharacterSequence(cellSize)
				);
			}
			DATA.add(rowItem);
		}
		writeToFile(this.toString());
	}

	public DataTable(File inputFile) throws InvalidRowSizeException, FileNotFoundException, InvalidColumnSizeException, ParseFailureException {

		DATA = new ArrayList<>(); 

		Scanner scanner = null;

		if (inputFile != null && inputFile.exists() && !inputFile.isDirectory()) {
			scanner = new Scanner(new FileReader(inputFile));
		}
		else {
			scanner = new Scanner(new FileReader(DEFAULT_FILE_PATH));
		}

		while(scanner.hasNextLine())
			add(scanner.nextLine());

		if (DATA.isEmpty()) {
			throw new InvalidRowSizeException("non-zero");			
		}
	}

	public Map<Tuple<Integer, Integer>, Integer> search(String keyword) {

		Map<Tuple<Integer, Integer>, Integer> resultMap = new HashMap<>();

		if (!keyword.equals("")) {		
			for (int i = 0; i < DATA.size(); i++) {
				int j = 0;
				for (Map.Entry<Key, String> entry : DATA.get(i).entrySet()) {

					int occurrences = 0;
					int fromIndex = 0;

					String concatenatedString = entry.getKey().getValue() + entry.getValue();

					do {
						fromIndex = concatenatedString.indexOf(keyword, fromIndex) + 1;
						if (fromIndex > 0)
							occurrences++;
					} while (fromIndex != 0);

					if (occurrences > 0)
						resultMap.put(new Tuple<Integer, Integer>(i, j++), occurrences);
				}
			}
		}

		return resultMap;
	}

	public void add(String rowValues) throws InvalidColumnSizeException, ParseFailureException {

		Matcher matcher = PATTERN.matcher(rowValues);

		Map<Key, String> extractedValues = new HashMap<>();

		int i = 0;
		while (matcher.find()) {
			extractedValues.put(new Key(i++, matcher.group(1)), matcher.group(2));
		}

		if (extractedValues.isEmpty())
			throw new ParseFailureException("(abc, bcd), (1sd, f3g)");

		if (!DATA.isEmpty() && extractedValues.size() != DATA.get(0).size())
			throw new InvalidColumnSizeException(DATA.get(0).size());

		DATA.add(extractedValues);
		writeToFile(this.toString());
	}

	public void replaceByKey(int rowIndex, int columnIndex, String replacement) {
		String previousValue = DATA.get(rowIndex).remove(new Key(columnIndex, null));
		DATA.get(rowIndex).put(new Key(columnIndex, replacement), previousValue);
		writeToFile(this.toString());
	}

	public void replaceByValue(int rowIndex, int columnIndex, String replacement) {
		DATA.get(rowIndex).replace(new Key(columnIndex, null), replacement);
		writeToFile(this.toString());
	}

	public void sort() {
		DATA.sort((currentRow, nextRow) -> 
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
		writeToFile(this.toString());
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < DATA.size(); i++) {	
			for (Map.Entry<Key, String> entry : DATA.get(i).entrySet()) {
				builder.append(String.format("(%s, %s) ", entry.getKey().getValue(), entry.getValue()));
			}
			if (i < DATA.size() - 1)
				builder.append("\n");
		}

		return builder.toString();
	}
}