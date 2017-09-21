package Model;

public enum Option {
	KEY,
	VALUE,
	BOTH;

	public static Option getOptionFor(int enumValue) {
		return values()[enumValue];
	}
}