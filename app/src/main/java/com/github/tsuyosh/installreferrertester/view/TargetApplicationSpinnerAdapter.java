package com.github.tsuyosh.installreferrertester.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.tsuyosh.installreferrertester.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tsuyosh on 2015/08/12.
 */
public class TargetApplicationSpinnerAdapter extends ArrayAdapter<ResolveInfo> {
	private PackageManager mPackageManager;
	private Resources mResources;

	public TargetApplicationSpinnerAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_item);
		mPackageManager = context.getPackageManager();
		mResources = context.getResources();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext())
					.inflate(android.R.layout.simple_spinner_item, parent, false);
			convertView.setTag(new ViewHolder(convertView));
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		ResolveInfo resolveInfo = getItem(position);
		holder.text.setText(resolveInfo.loadLabel(mPackageManager));
		holder.text.setCompoundDrawables(getIcon(resolveInfo), null, null, null);
		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext())
					.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
			convertView.setTag(new ViewHolder(convertView));
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		ResolveInfo resolveInfo = getItem(position);
		String label = resolveInfo.loadLabel(mPackageManager) + "(" + resolveInfo.activityInfo.name + ")";
		holder.text.setText(label);
		holder.text.setEllipsize(TextUtils.TruncateAt.MIDDLE);
		holder.text.setCompoundDrawables(getIcon(resolveInfo), null, null, null);
		return convertView;
	}

	private Drawable getIcon(ResolveInfo info) {
		Drawable d = info.loadIcon(mPackageManager);
		d.setBounds(
				0, 0,
				mResources.getDimensionPixelSize(R.dimen.targetApplicationIconWidth),
				mResources.getDimensionPixelSize(R.dimen.targetApplicationIconHeight)
		);
		return d;
	}

	static class ViewHolder {
		@Bind(android.R.id.text1)
		TextView text;

		ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
