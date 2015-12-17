package org.hazehorizon.restaurantPicker;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class TestUtils {
	public static Long randomId() {
		return RandomUtils.nextLong(1, 1000);
	}
	
	public static String randomString() {
		return RandomStringUtils.random(42);
	}
}
