package Model;

public enum Operation {
	SEARCH,
	EDIT,
	RESET,
	ADD,
	SORT,
	EXIT;

	public static Operation getOperationFor(int enumValue) {
		return values()[enumValue];
	}
}