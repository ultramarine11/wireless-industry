package ru.wirelesstools.utils;

public class UtilFormatGUI {

	public static String formatNumber(double number) {

		String formattednumber = "0";

		if (number < 1000.0D) {

			formattednumber = String.format("%.0f", number);
		} else if (number >= 1000.0D && number < 1000000.0D) {

			formattednumber = String.format("%.2fK", number / 1000.0);
		} else if (number >= 1000000.0D && number < 1000000000.0D) {

			formattednumber = String.format("%.2fM", number / 1000000.0);
		} else if (number >= 1000000000.0D && number < 1000000000000.0D) {

			formattednumber = String.format("%.2fG", number / 1000000000.0);
		} else if (number >= 1000000000000.0D && number < 1000000000000000.0D) {

			formattednumber = String.format("%.2fT", number / 1000000000000.0);
		}

		return formattednumber;
	}

}
