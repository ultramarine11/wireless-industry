package ru.wirelesstools.facetoside;

public class FacingToSide {

	// [face][side], iconID: 6:head, 7:tail, 8:side, 9:top, 10:btm (0~5 save for
	// vanilla icon method)
	public static final int[][] face2iconID = { { 6, 7, 8, 8, 8, 8 }, // face btm(0)
			{ 7, 6, 8, 8, 8, 8 }, // face top(1)
			{ 10, 9, 6, 7, 8, 8 }, // face N(2)
			{ 10, 9, 7, 6, 8, 8 }, // face S(3)
			{ 10, 9, 8, 8, 6, 7 }, // face W(4)
			{ 10, 9, 8, 8, 7, 6 } };// face E(5)

}
