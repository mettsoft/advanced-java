package Model;

import java.util.concurrent.ThreadLocalRandom;

public final class AsciiGenerator {

	private static final int ASCII_VALUE_OF_SPACE = 32;
	private static final int ASCII_VALUE_OF_DEL = 127;

	public static String createCharacterSequence(int length) {

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			builder.append((char) ThreadLocalRandom.current().nextInt(ASCII_VALUE_OF_SPACE, ASCII_VALUE_OF_DEL));
		}

		return builder.toString();
	}
}