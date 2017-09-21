import java.util.Scanner;
import java.util.Map;
import java.io.File;

import Utility.InputHandler;
import Model.DataTable;
import Model.Tuple;
import Model.Operation;
import Model.Option;

// New Features:
// 1. [Data] Each cell must contain a key-value pair.
// 2. [Bootstrap] Import data table from the specified filename extracted from String args, otherwise import the data table from a default file.
// - Format: 
// (3t4, LjP), (3t4, LjP), (3t4, LjP), (3t4, LjP)
// (3t4, LjP), (3t4, LjP), (3t4, LjP), (,,,, LjP)
// 3. [Print] Must display both key and value.
// 4. [Search/Edit] By key, value, and key & value pair.
// 5. [Edge case] It must handle the edge case that contains ',' for search, edit and other operations.
// 6. [Add Command]: Add N rows and manually enter of new values.
// 7. [Sort Command]: Concatenate each cell of keys and values in every row and sort the rows per table. 
// 8. [Data] Collection instead of array.
// 9. [Changes] All changes must be saved to the default file. 
// 10. [Search] Must display as follows => (row, col) key: N, value: M 
// 11. [Add/Edit/Import] No character limit.

public class Main {

	private static final int DEFAULT_ROW_SIZE = 3;
	private static final int DEFAULT_COLUMN_SIZE = 3;
	private static final int DEFAULT_CELL_SIZE = 3;

	private static final String MAIN_HEADER = "----------\nExercise 2 - Advanced Java\nBy Emmett Young";
	private static final String MAIN_PROMPT = "Please choose what operation you want to perform:\n1.Search\n2.Edit\n3.Reset\n4.Add\n5.Sort\n6.Exit\nOperation: ";

	private static final String SEARCH_PROMPT = "Please enter the search keyword: ";	
	private static final String OPTION_PROMPT = "Please select an option:\n1. Key\n2. Value\n3. Both\nOption: ";	
	private static final String SEARCH_PROMPT_BY_KEY = "Please enter the search keyword for key: ";
	private static final String SEARCH_PROMPT_BY_VALUE = "Please enter the search keyword for value: ";

	private static final String EDIT_PROMPT_ROW_INDEX = "Please enter the row index: ";
	private static final String EDIT_PROMPT_COLUMN_INDEX = "Please enter the column index: ";
	private static final String EDIT_PROMPT_BY_KEY = "Please enter the replacement string for key: ";
	private static final String EDIT_PROMPT_BY_VALUE = "Please enter the replacement string for value: ";
	
	private static final String RESET_PROMPT_NUMBER_OF_ROWS = "Please enter the desired number of rows: ";
	private static final String RESET_PROMPT_NUMBER_OF_COLUMNS = "Please enter the desired number of columns: ";	

	private static final String ADD_PROMPT_NUMBER_OF_ROWS = "Please enter the desired number of rows to be added: ";	
	private static final String ADD_PROMPT_ROW_VALUES = "Please enter the row values (e.g. (abc,eeef), (abqe, asdf)): ";

	public static void main(String[] args) {

		DataTable dataTable = null;
		File inputFile = null;
		if (args.length == 1) {
			inputFile = new File(args[0]);
		}
		
		Scanner scanner = new Scanner(System.in);
		System.out.println(MAIN_HEADER);

		while (true) {

			try {
				if (dataTable == null) {
					dataTable = new DataTable(inputFile);
				}

				System.out.println(dataTable);
				System.out.print(MAIN_PROMPT);
	
				int operationIndex = InputHandler.getIntegerInput(scanner) - 1;
				Operation selectedOperation = Operation.getOperationFor(operationIndex);
				Option selectedOption = null;
				int optionIndex = 0;

				switch (selectedOperation) {
					case SEARCH: 
						System.out.print(OPTION_PROMPT);
						optionIndex = InputHandler.getIntegerInput(scanner) - 1; 
						selectedOption = Option.getOptionFor(optionIndex);

						StringBuilder builder = new StringBuilder();

						if (selectedOption == Option.BOTH || selectedOption == Option.KEY) {
							System.out.print(SEARCH_PROMPT_BY_KEY);
							String searchByKey = scanner.nextLine();
							Map<Tuple<Integer, Integer>, Integer> resultMapByKey = dataTable.search(searchByKey, tuple -> tuple.getFirst());
							builder.append(("By Key:\n" + resultMapByKey + "\n"));		
						}

						if (selectedOption == Option.BOTH || selectedOption == Option.VALUE) {
							System.out.print(SEARCH_PROMPT_BY_VALUE);
							String searchByValue = scanner.nextLine();
							Map<Tuple<Integer, Integer>, Integer> resultMapByValue = dataTable.search(searchByValue, tuple -> tuple.getSecond());
							builder.append(("By Value:\n" + resultMapByValue));		
						}

						System.out.println(builder);
						break;

					case EDIT: 
						System.out.print(OPTION_PROMPT);
						optionIndex = InputHandler.getIntegerInput(scanner) - 1; 
						selectedOption = Option.getOptionFor(optionIndex);

						System.out.print(EDIT_PROMPT_ROW_INDEX);
						int row = InputHandler.getIntegerInput(scanner);

						System.out.print(EDIT_PROMPT_COLUMN_INDEX);
						int column = InputHandler.getIntegerInput(scanner);


						if (selectedOption == Option.BOTH || selectedOption == Option.KEY) {
							System.out.print(EDIT_PROMPT_BY_KEY);
							String replaceByKey = scanner.nextLine();
							dataTable.replaceByKey(row, column, replaceByKey);				
						}

						if (selectedOption == Option.BOTH || selectedOption == Option.VALUE) {
							System.out.print(EDIT_PROMPT_BY_VALUE);
							String replaceByValue = scanner.nextLine();
							dataTable.replaceByValue(row, column, replaceByValue);				
						}

						break;

					case RESET:
						System.out.print(RESET_PROMPT_NUMBER_OF_ROWS);
						int numberOfRows = InputHandler.getIntegerInput(scanner);

						System.out.print(RESET_PROMPT_NUMBER_OF_COLUMNS);
						int numberOfColumns = InputHandler.getIntegerInput(scanner);

					 	dataTable = new DataTable(numberOfRows, numberOfColumns, DEFAULT_CELL_SIZE);
						break;

					case ADD: 						
						System.out.print(ADD_PROMPT_NUMBER_OF_ROWS);
						int rowsToBeAdded = InputHandler.getIntegerInput(scanner);

						for (int i = 0; i < rowsToBeAdded; i++) {
							System.out.print(ADD_PROMPT_ROW_VALUES);
							String rowValues = scanner.nextLine();
							dataTable.add(rowValues);
						}

						break;

					case SORT: 
					 	dataTable.sort();
						break;

					case EXIT: 
						System.exit(0);
				}
			}
			catch (Exception exception) {
				System.out.println(String.format("ERROR: %s", exception));
			}
		}
	}
}