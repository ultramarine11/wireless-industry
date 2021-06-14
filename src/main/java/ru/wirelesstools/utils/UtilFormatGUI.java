package ru.wirelesstools.utils;

public class UtilFormatGUI {

	public static String formatNumber(double number) {
		if (number < 1000.0D) {
			return String.format("%.0f", number);
		} else if (number >= 1000.0D && number < 1000000.0D) {
			return String.format("%.2fK", number / 1000.0);
		} else if (number >= 1000000.0D && number < 1000000000.0D) {
			return String.format("%.2fM", number / 1000000.0);
		} else if (number >= 1000000000.0D && number < 1000000000000.0D) {
			return String.format("%.2fG", number / 1000000000.0);
		} else if (number >= 1000000000000.0D && number < 1000000000000000.0D) {
			return String.format("%.2fT", number / 1000000000000.0);
		}
		else {
			return String.valueOf(number);
		}
	}

	public static String formatNumberPanel(double number) {
		if (number < 1000.0D) {
			return String.format("%.0f", number);
		} else if (number >= 1000.0D && number < 1000000.0D) {
			return String.format("%.2fK", number / 1000.0);
		} else if (number >= 1000000.0D && number < 1000000000.0D) {
			return String.format("%.2fM", number / 1000000.0);
		} else if (number >= 1000000000.0D && number < 1000000000000.0D) {
			return String.format("%.3fG", number / 1000000000.0);
		} else if (number >= 1000000000000.0D && number < 1000000000000000.0D) {
			return String.format("%.3fT", number / 1000000000000.0);
		}
		else {
			return String.valueOf(number);
		}
	}

}
