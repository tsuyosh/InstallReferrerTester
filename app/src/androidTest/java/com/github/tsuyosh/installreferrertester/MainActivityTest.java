package com.github.tsuyosh.installreferrertester;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.github.tsuyosh.installreferrertester.activity.MainActivity;
import com.squareup.spoon.Spoon;

/**
 * Created by tsuyosh on 2015/08/14.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	public MainActivityTest() {
		super(MainActivity.class);
	}

	@SmallTest
	public void testSmallTest() throws Throwable {
		MainActivity activity = getActivity();
		Spoon.screenshot(activity, "startup-smallTest");
	}

	@MediumTest
	public void testMediumTest() throws Throwable {
		MainActivity activity = getActivity();
		Spoon.screenshot(activity, "startup-mediumTest");
	}

	@LargeTest
	public void testLargeTest() throws Throwable {
		MainActivity activity = getActivity();
		Spoon.screenshot(activity, "startup-largeTest");
	}
}
