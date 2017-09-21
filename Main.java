import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import Utility.PromptBuilder;
import Model.DataTable;
import Model.Tuple;
import Enumeration.Option;

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

// Revision Finished:
// 1. Concatenate key&value then search.
// 2. Use map.

public class Main {

	private static final int DEFAULT_ROW_SIZE = 3;
	private static final int DEFAULT_COLUMN_SIZE = 3;
	private static final int DEFAULT_CELL_SIZE = 3;

	private static final String MAIN_HEADER = "----------\nExercise 2 - Advanced Java\nBy Emmett Young";
	private static final String MAIN_PROMPT = "Please choose what operation you want to perform:\n1.Search\n2.Edit\n3.Reset\n4.Add\n5.Sort\n6.Exit\nOperation: ";

	private static final String SEARCH_PROMPT = "Please enter the search keyword: ";	
	private static final String OPTION_PROMPT = "Please select an option:\n1. Key\n2. Value\n3. Both\nOption: ";	

	private static final String EDIT_PROMPT_ROW_INDEX = "Please enter the row index: ";
	private static final String EDIT_PROMPT_COLUMN_INDEX = "Please enter the column index: ";
	private static final String EDIT_PROMPT_BY_KEY = "Please enter the replacement string for key: ";
	private static final String EDIT_PROMPT_BY_VALUE = "Please enter the replacement string for value: ";
	
	private static final String RESET_PROMPT_NUMBER_OF_ROWS = "Please enter the desired number of rows: ";
	private static final String RESET_PROMPT_NUMBER_OF_COLUMNS = "Please enter the desired number of columns: ";	

	private static final String ADD_PROMPT_NUMBER_OF_ROWS = "Please enter the desired number of rows to be added: ";	
	private static final String ADD_PROMPT_ROW_VALUES = "Please enter the row values (e.g. (abc,eeef), (abqe, asdf)): ";

	public static void main(String[] args) {

		PromptBuilder promptBuilder = new PromptBuilder(System.in, System.out);
		DataTable dataTable = null;
		File inputFile = args.length == 1? new File(args[0]): null;
		System.out.println(MAIN_HEADER);

		while (true) {

			try {
				if (dataTable == null) {
					dataTable = new DataTable(inputFile);
				}

				System.out.println(dataTable);

				Option selectedOption = null;
				int selectedOperation = promptBuilder.prompt(MAIN_PROMPT)
					.getNextLine(input -> Integer.valueOf(input) - 1);

				switch (selectedOperation) {

					// Search
					case 0: 						

						String searchKeyword = promptBuilder
							.prompt(SEARCH_PROMPT)
							.getNextLine();

						Map resultMap = dataTable.search(searchKeyword);

						if (resultMap.isEmpty())
							System.out.println("No search found!\n");
						else 
							System.out.println(String.format("Search Result:\n%s\n", resultMap));		

						break;

					// Edit
					case 1: 

						selectedOption = promptBuilder
							.prompt(OPTION_PROMPT)
							.getNextLine(input -> Option.getOptionFor(Integer.valueOf(input) - 1));

						int row = promptBuilder
							.prompt(EDIT_PROMPT_ROW_INDEX)
							.getNextLine(input -> Integer.valueOf(input));

						int column = promptBuilder
							.prompt(EDIT_PROMPT_COLUMN_INDEX)
							.getNextLine(input -> Integer.valueOf(input));

						if (selectedOption == Option.BOTH || selectedOption == Option.KEY) {

							String replaceByKey = promptBuilder
								.prompt(EDIT_PROMPT_BY_KEY)
								.getNextLine();

							dataTable.replaceByKey(row, column, replaceByKey);				
						}

						if (selectedOption == Option.BOTH || selectedOption == Option.VALUE) {

							String replaceByValue = promptBuilder
								.prompt(EDIT_PROMPT_BY_VALUE)
								.getNextLine();

							dataTable.replaceByValue(row, column, replaceByValue);				
						}

						break;

					// Reset
					case 2:

						int numberOfRows = promptBuilder
							.prompt(RESET_PROMPT_NUMBER_OF_ROWS)
							.getNextLine(input -> Integer.valueOf(input));

						int numberOfColumns = promptBuilder
							.prompt(RESET_PROMPT_NUMBER_OF_COLUMNS)
							.getNextLine(input -> Integer.valueOf(input));

					 	dataTable = new DataTable(numberOfRows, numberOfColumns, DEFAULT_CELL_SIZE);

						break;

					// Add
					case 3: 					

						int rowsToBeAdded = promptBuilder
							.prompt(ADD_PROMPT_NUMBER_OF_ROWS)
							.getNextLine(input -> Integer.valueOf(input));

						for (int i = 0; i < rowsToBeAdded; i++) {

							String rowValues = promptBuilder
								.prompt(ADD_PROMPT_ROW_VALUES)
								.getNextLine();

							dataTable.add(rowValues);
						}

						break;

					// Sort
					case 4: 
					 	dataTable.sort();
						break;

					// Exit
					case 5: 
						System.exit(0);
				}
			}
			catch (Exception exception) {
				System.out.println(String.format("ERROR: %s", exception));
			}
		}
	}
}