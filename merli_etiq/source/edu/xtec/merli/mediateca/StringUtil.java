package edu.xtec.merli.mediateca;

public class StringUtil {

	public static String shorten(String s) {
		return shorten(s, 20);
	}

	public static String shorten(String s, int length) {
		return shorten(s, length, "...");
	}

	public static String shorten(String s, int length, String suffix) {
		if ((s == null) || (suffix == null)) {
			return null;
		}

		if (s.length() > length) {
			for (int j = length; j >= 0; j--) {
				if (Character.isWhitespace(s.charAt(j))) {
					length = j;

					break;
				}
			}

			String temp = s.substring(0, length);

			s = temp.concat(suffix);
		}

		return s;
	}

	public static int length(String s) {
		if (s == null) {
			return 0;
		}
		return s.length();
	}

}
