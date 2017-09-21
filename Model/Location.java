package Model;

public final class Location {

	private final int rowIndex;
	private final int columnIndex;

	public Location(int rowIndex, int columnIndex) {
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	@Override
	public int hashCode() {
		// 2, 1 = (2 + 1) * 1 + 2 = 4
		// 1, 2 = (1 + 2) * 2 + 1 = 5
		return (rowIndex + columnIndex) * columnIndex + rowIndex;
	}	

	@Override
	public boolean equals(Object otherLocation) {
		
		return otherLocation instanceof Location && this.rowIndex == ((Location) otherLocation).getRowIndex() && 
			this.columnIndex == ((Location) otherLocation).getColumnIndex();
	}

	@Override
	public String toString() {
		return String.format("(%d, %d)", rowIndex, columnIndex);
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}