package ru.santaev.utils;

public class Check {

	public static <T> T notNull(String message, T object) {
		if (object == null) {
			throw new NullPointerException(message);
		}
		return object;
	}
}
