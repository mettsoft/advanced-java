package com.ecc;

import java.util.Map;
import java.util.List;
import java.io.File;

import com.ecc.utility.InputHandler;
import com.ecc.service.DataTableService;
import com.ecc.service.DataTableServiceImpl;

public class Main {

	private static final Integer DEFAULT_ROW_SIZE = 3;
	private static final Integer DEFAULT_COLUMN_SIZE = 3;
	private static final Integer DEFAULT_CELL_SIZE = 3;

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

		DataTableService dataTableService = null;
		File inputFile = args.length == 1? new File(args[0]): null;

		try {
			dataTableService = new DataTableServiceImpl(inputFile);		
		}
		catch(Exception exception) {
			System.err.println("Error: " + exception);
			return;
		}

		System.out.println(MAIN_HEADER);

		while (true) {

			try {

				dataTableService.printDataTable();

				Integer selectedOption = null;
				Integer selectedOperation = InputHandler.getNextLine(MAIN_PROMPT, 
					input -> Integer.valueOf(input) - 1);

				switch (selectedOperation) {

					// Search
					case 0: 						

						String searchKeyword = InputHandler.getNextLine(SEARCH_PROMPT);
							
						Map resultMap = dataTableService.search(searchKeyword);

						if (resultMap.isEmpty())
							System.out.println("No search found!\n");
						else 
							System.out.println(String.format("Search Result:\n%s\n", resultMap));		

						break;

					// Edit
					case 1: 

						selectedOption = InputHandler.getNextLine(OPTION_PROMPT, 
								input -> Integer.valueOf(input) - 1);

						Integer row = InputHandler.getNextLine(EDIT_PROMPT_ROW_INDEX, Integer::valueOf);
						Integer column = InputHandler.getNextLine(EDIT_PROMPT_COLUMN_INDEX, Integer::valueOf);

						// Both or key.
						if (selectedOption == 2 || selectedOption == 0) {

							String replaceByKey = InputHandler.getNextLine(EDIT_PROMPT_BY_KEY);
							dataTableService.replaceByKey(row, column, replaceByKey);				
						}

						// Both or value.
						if (selectedOption == 2 || selectedOption == 1) {

							String replaceByValue = InputHandler.getNextLine(EDIT_PROMPT_BY_VALUE);
							dataTableService.replaceByValue(row, column, replaceByValue);				
						}

						break;

					// Reset
					case 2:

						Integer numberOfRows = InputHandler.getNextLine(RESET_PROMPT_NUMBER_OF_ROWS, Integer::valueOf);
						Integer numberOfColumns = InputHandler.getNextLine(RESET_PROMPT_NUMBER_OF_COLUMNS, Integer::valueOf);

						dataTableService.reset(numberOfRows, numberOfColumns, DEFAULT_CELL_SIZE);

						break;

					// Add
					case 3: 					

						Integer rowsToBeAdded = InputHandler.getNextLine(ADD_PROMPT_NUMBER_OF_ROWS, Integer::valueOf);

						for (Integer i = 0; i < rowsToBeAdded; i++) {
							String rowValues = InputHandler.getNextLine(ADD_PROMPT_ROW_VALUES);
							dataTableService.add(rowValues);
						}

						break;

					// Sort
					case 4: 
					 	dataTableService.sort();
						break;

					// Exit
					case 5: 
						System.exit(0);
				}
			}
			catch (Exception exception) {
				System.out.println(String.format("Error: %s", exception.getMessage()));
			}
		}
	}
}