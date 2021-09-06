package ru.wirelesstools.utils;

public class UtilFormatNumber {

	public static String formatNumber(double number) {
		double logarithm = Math.log10(number);
		if(logarithm < 3.0)
			return String.format("%.0f", number);
		else if(logarithm >= 3.0 && logarithm < 6.0)
			return String.format("%.2fK", number / 1E3);
		else if(logarithm >= 6.0 && logarithm < 9.0)
			return String.format("%.2fM", number / 1E6);
		else if(logarithm >= 9.0 && logarithm < 12.0)
			return String.format("%.2fG", number / 1E9);
		else if(logarithm >= 12.0 && logarithm < 15.0)
			return String.format("%.2fT", number / 1E12);
		else if(logarithm >= 15.0 && logarithm < 18.0)
			return String.format("%.2fP", number / 1E15);
		else if(logarithm >= 18.0 && logarithm < 21.0)
			return String.format("%.2fE", number / 1E18);
		else if(logarithm >= 21.0 && logarithm < 24.0)
			return String.format("%.2fZ", number / 1E21);
		else if(logarithm >= 24.0 && logarithm < 27.0)
			return String.format("%.2fY", number / 1E24);
		else
			return String.valueOf(number);

		/*if (number < 1000.0D) {
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
		}*/
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
