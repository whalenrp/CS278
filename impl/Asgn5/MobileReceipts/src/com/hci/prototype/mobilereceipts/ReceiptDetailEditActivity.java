package com.hci.prototype.mobilereceipts;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class ReceiptDetailEditActivity extends Activity {

	/*
	 * This class is responsible for asyncronously retrieving a cursor to the receipts
	 * database. Once it receives a new cursor, it will update the UI thread's list with
	 * the new data.
	 */
	private class AsyncCursor extends AsyncTask<ContentValues,Void,Void>{

		private final ReceiptDbAdapter mDb;
		private final long rowId;
		public AsyncCursor(final Context ctxt, final long id){
			mDb = new ReceiptDbAdapter(ctxt);
			rowId = id;
			mDb.open();
		}

		@Override
		protected Void doInBackground(final ContentValues... arg0) {
			mDb.updateReceipt(rowId, arg0[0]);
			return null;
		}

		@Override
		protected void onPostExecute(final Void nothing){
			mDb.close();
			finish();
		}
	}
	private int key_id;
	private AspectRatioImage receiptImg;
	private EditText receiptTitle;
	private EditText receiptCost;
	private Spinner receiptCategory;
	private Spinner receiptType;


	private DatePicker receiptDate;

	@Override
	protected void onCreate(final Bundle savedInstance){
		super.onCreate(savedInstance);
		setContentView(R.layout.receipt_detail_edit_activity);

		receiptImg = (AspectRatioImage)findViewById(R.id.receiptImage);
		receiptTitle = (EditText)findViewById(R.id.receiptTitle);
		receiptCost = (EditText)findViewById(R.id.receiptTotal);
		receiptCategory = (Spinner)findViewById(R.id.receiptCategory);
		receiptType = (Spinner)findViewById(R.id.receiptType);
		receiptDate = (DatePicker)findViewById(R.id.receiptDate);

		//mDb = new ReceiptDbAdapter(getApplicationContext());

		final Intent i = getIntent();
		key_id = Integer.parseInt(i.getStringExtra("key_id"));
		receiptTitle.setHint(i.getStringExtra("label"));
		receiptCost.setHint(i.getStringExtra("cost"));
		//receiptCategory.setSelection(position, animate)i.getStringExtra("category"));
		//receiptDate.set....("date", date.getText().toString());

		final Bitmap image = FileDatabaseController.getBitmapFromFile(i.getStringExtra("filename"));
		Log.e("ReceiptDetailEditActivity",i.getStringExtra("filename"));

		receiptImg.setImageBitmap(image);

		// set up the spinner with the categories resources array
		// Create an ArrayAdapter using the string array and a default spinner layout
		final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.categories, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		receiptCategory.setAdapter(adapter);

		final ArrayAdapter<CharSequence> busAdapter = ArrayAdapter.createFromResource(this,
				R.array.business, android.R.layout.simple_spinner_item);

		busAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		receiptType.setAdapter(busAdapter);
	}

	/*
	 * This method will be called when the user clicks "Save" at the bottom of the activity.
	 * It will commit the changes currently stored in the activity fields to the database
	 * and return to the main activity.
	 */
	public void onSubmit(final View v){
		final int   day  = receiptDate.getDayOfMonth();
		final int   month= receiptDate.getMonth();
		final int   year = receiptDate.getYear();

		final SimpleDateFormat sdf = new SimpleDateFormat(ReceiptDbAdapter.DATE_FORMAT);
		final String formattedDate = sdf.format(new Date(year, month, day));
		// Send data to AsyncCursor
		final ContentValues cv = new ContentValues();
		cv.put(ReceiptDbAdapter.KEY_TITLE, receiptTitle.getText().toString());
		cv.put(ReceiptDbAdapter.KEY_AMOUNT, receiptCost.getText().toString());
		cv.put(ReceiptDbAdapter.KEY_TIME, formattedDate);
		cv.put(ReceiptDbAdapter.KEY_CATEGORY, receiptCategory.getSelectedItem().toString());
		cv.put(ReceiptDbAdapter.KEY_TYPE, receiptType.getSelectedItem().toString());
		new AsyncCursor(this, key_id).execute(cv);
	}

}
