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
import Exceptions.InvalidColumnSizeException;
import Exceptions.InvalidRowSizeException;
import Exceptions.ParseFailureException;
import Exceptions.SearchKeywordNotFoundException;

public final class DataTable {

	private interface WriteListener extends Consumer<String> {
	}

	private static final WriteListener onWrite;
	private static final String DEFAULT_FILE_PATH = "./build/default.DAT";
	private final Pattern PATTERN = Pattern.compile("(?:\\(([^()]+),([^()]+)\\))");
	private final List<List<Tuple<String, String>>> DATA;

	static {
		onWrite = new WriteListener() {
			@Override 
			public void accept(String data) {		
				try (FileWriter writer = new FileWriter(DEFAULT_FILE_PATH)) {
					writer.write(data);
				}
				catch (IOException exception) {
					System.out.println(String.format("ERROR: %s", exception));	
				}
			}
		};
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
	
			List<Tuple<String, String>> rowItem = new ArrayList<>();
			for (int j = 0; j < numberOfColumns; j++) {
				rowItem.add(new Tuple<String, String>(
					AsciiGenerator.createCharacterSequence(cellSize), 
					AsciiGenerator.createCharacterSequence(cellSize)
					)
				);
			}
			DATA.add(rowItem);
		}
		this.onWrite.accept(this.toString());
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

	public Map<Tuple<Integer, Integer>, Integer> search(String keyword, Function<Tuple<String, String>, String> elementChooser) throws SearchKeywordNotFoundException {

		Map<Tuple<Integer, Integer>, Integer> resultMap = new HashMap<>();

		if (!keyword.equals("")) {		
			for (int i = 0; i < DATA.size(); i++) {
				for (int j = 0; j < DATA.get(i).size(); j++) {

					int occurrences = 0;
					int fromIndex = 0;

					do {
						fromIndex = elementChooser.apply(DATA.get(i).get(j)).indexOf(keyword, fromIndex) + 1;
						if (fromIndex > 0)
							occurrences++;
					} while (fromIndex != 0);

					if (occurrences > 0)
						resultMap.put(new Tuple<Integer, Integer>(i, j), occurrences);
				}
			}
		}

		if (resultMap.isEmpty())
			throw new SearchKeywordNotFoundException(keyword);

		return resultMap;
	}

	public void add(String rowValues) throws InvalidColumnSizeException, ParseFailureException {

		Matcher matcher = PATTERN.matcher(rowValues);
		List<Tuple<String, String>> extractedValues = new ArrayList<>();

		while (matcher.find()) {
			extractedValues.add(new Tuple<String, String>(matcher.group(1), matcher.group(2)));
		}

		if (extractedValues.isEmpty())
			throw new ParseFailureException("(abc, bcd), (1sd, f3g)");

		if (!DATA.isEmpty() && extractedValues.size() != DATA.get(0).size())
			throw new InvalidColumnSizeException(DATA.get(0).size());

		DATA.add(extractedValues);
		this.onWrite.accept(this.toString());
	}

	public void replaceByKey(int rowIndex, int columnIndex, String replacement) {
		Tuple<String, String> previousTuple = DATA.get(rowIndex).get(columnIndex);
		DATA.get(rowIndex).set(columnIndex, new Tuple<String, String>(replacement, previousTuple.getSecond()));
		this.onWrite.accept(this.toString());
	}

	public void replaceByValue(int rowIndex, int columnIndex, String replacement) {
		Tuple<String, String> previousTuple = DATA.get(rowIndex).get(columnIndex);
		DATA.get(rowIndex).set(columnIndex, new Tuple<String, String>(previousTuple.getFirst(), replacement));
		this.onWrite.accept(this.toString());
	}

	public void sort() {
		DATA.sort((currentRow, nextRow) -> 
			currentRow
				.stream()
				.map(tuple -> tuple.getFirst() + tuple.getSecond())
				.collect(Collectors.joining(""))
				.compareTo(
					nextRow
						.stream()
						.map(tuple -> tuple.getFirst() + tuple.getSecond())
						.collect(Collectors.joining(""))
				)
		);
		this.onWrite.accept(this.toString());
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < DATA.size(); i++) {	
			for (int j = 0; j < DATA.get(i).size(); j++) {
				builder.append(String.format("%s ", DATA.get(i).get(j)));
			}
			if (i < DATA.size() - 1)
				builder.append("\n");
		}

		return builder.toString();
	}
}