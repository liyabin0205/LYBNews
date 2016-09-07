package com.app.lybnews.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.lybnews.HomeActivity;
import com.app.lybnews.R;

/**
 * 自定义弹出框
 * 
 * @author 李亚彬 2016-08-26
 */
public class MyDialog {
	private Activity activity;
	private AlertDialog dialog;
	private EditText dialog_edit;

	public MyDialog(Activity activity) {
		this.activity = activity;
	}

	public void showDialog() {
		dialog = new AlertDialog.Builder(activity).create();
		// 点击外部区域不能取消dialog
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(keylistener);
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.my_dialog, null);
		dialog.setView(layout);
		dialog.show();

		Window window = dialog.getWindow();
		window.setContentView(R.layout.my_dialog);

		dialog_edit = (EditText) window.findViewById(R.id.dialog_edit);

		TextView tv_confirm = (TextView) window.findViewById(R.id.tv_confirm);
		TextView tv_cancel = (TextView) window.findViewById(R.id.tv_cance2);
		tv_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String inputCity = dialog_edit.getText().toString().trim();
				Intent intent = new Intent(activity, HomeActivity.class);
				intent.putExtra("extra", inputCity);
				((Activity) activity).startActivity(intent);
				dialog.dismiss();
			}
		});

		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);
	}

	public static OnKeyListener keylistener = new OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				return true;
			} else {
				return false;
			}
		}
	};
}