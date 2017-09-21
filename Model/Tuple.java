package Model;

public class Tuple <T, U> {

	private final T first;
	private final U second;

	public Tuple(T first, U second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public int hashCode() {
		// 2, 1 = (2 + 1) * 1 + 2 = 4
		// 1, 2 = (1 + 2) * 2 + 1 = 5
		return (first.hashCode() + second.hashCode()) * second.hashCode() + first.hashCode();
	}	

	@Override
	public boolean equals(Object otherTuple) {
		
		return otherTuple instanceof Tuple && this.first == ((Tuple) otherTuple).getFirst() && 
			this.second == ((Tuple) otherTuple).getSecond();
	}

	@Override
	public String toString() {
		return String.format("(%s, %s)", first, second);
	}

	public T getFirst() {
		return first;
	}

	public U getSecond() {
		return second;
	}
}