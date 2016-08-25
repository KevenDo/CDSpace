package com.u51.CDSpace.utils;

import java.util.Random;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class StringUtil {
	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
}
