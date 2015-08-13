package com.github.tsuyosh.installreferrertester.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.tsuyosh.installreferrertester.BuildConfig;
import com.github.tsuyosh.installreferrertester.R;
import com.github.tsuyosh.installreferrertester.view.TargetApplicationSpinnerAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = MainActivity.class.getSimpleName();

	private TargetApplicationSpinnerAdapter mAdapter;

	@Bind(R.id.targetApplicationSpinner)
	Spinner mTargetApplicationSpinner;

	@Bind(R.id.referrerDataEditText)
	EditText mReferrerDataEditText;

	@Bind(R.id.urlEncodedCheckBox)
	CheckBox mUrlEncodedCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		mAdapter = new TargetApplicationSpinnerAdapter(this);
		mTargetApplicationSpinner.setAdapter(mAdapter);
		loadTargetApplications();
	}

	@Override
	protected void onDestroy() {
		mTargetApplicationSpinner.setAdapter(null);
		ButterKnife.unbind(this);
		super.onDestroy();
	}

	@OnClick(R.id.sendButton)
	void onSendButtonClick() {
		String referrer = mReferrerDataEditText.getText().toString();
		if (mUrlEncodedCheckBox.isChecked()) {
			try {
				referrer = URLEncoder.encode(referrer, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// never happen
			}
		}
		if (BuildConfig.DEBUG) Log.d(TAG, "referrer=" + referrer);

		ResolveInfo info = (ResolveInfo) mTargetApplicationSpinner.getSelectedItem();
		ComponentName componentName = new ComponentName(info.activityInfo.packageName, info.activityInfo.name);
		if (BuildConfig.DEBUG) Log.d(TAG, "componentName=" + componentName);

		Intent intent = createIntent();
		intent.setComponent(componentName);
		intent.putExtra("referrer", referrer);
		sendBroadcast(intent);
		Toast.makeText(this, R.string.intentHasBeenSent, Toast.LENGTH_SHORT).show();
	}

	private void loadTargetApplications() {
		Intent intent = createIntent();
		List<ResolveInfo> list = getPackageManager().queryBroadcastReceivers(intent, 0);
		Collections.sort(list, new Comparator<ResolveInfo>() {
			@Override
			public int compare(ResolveInfo a, ResolveInfo b) {
				PackageManager pm = getPackageManager();
				String nameA = a.loadLabel(pm).toString();
				String nameB = b.loadLabel(pm).toString();
				return nameA.compareTo(nameB);
			}
		});
		mAdapter.addAll(list);
	}

	private Intent createIntent() {
		Intent intent = new Intent("com.android.vending.INSTALL_REFERRER");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		}
		return intent;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
