package edu.nd.green;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener{

	// DONT FORGET TO OVERRIDE THE BACK BUTTON OR SAVE ARTICLE LIST EVERY TIME THEY CHANGE IT.
	
	/**
	 * 
	 */
	private ArrayList<String> feedList;
	private ArrayList<String> namesList;
	private Context context;
	private Button back;
	private LinearLayout layout;
	private EditText editText;
	private EditText editText2;
	private static final int ADD_ADDITION_ID = 300;
	private static final int ADD_BUTTON_ID = 400;
	private static final int DELETE_ID = 500;
	
	private SharedPreferences shared;
	private static final String SHARED_PREFS_NAME = "shared";
	private static final String KEY = "feedlist";
	private static final String KEY2 = "namesList";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.settings);
		
		context = this.getApplicationContext();
		this.layout = (LinearLayout)findViewById(R.id.linearLayout1);
		
	    shared = this.getSharedPreferences(SHARED_PREFS_NAME, 0 );
	    
		Intent i = this.getIntent();
		feedList=i.getStringArrayListExtra("feedList");
		namesList = i.getStringArrayListExtra("namesList");
		
		if(feedList==null){
			feedList=new ArrayList<String>();
		}
		if(namesList==null){
			namesList=new ArrayList<String>();
		}
		
		for(int k = 0; k<feedList.size(); k++){
			addOption(feedList.get(k), namesList.get(k));
		}
		addAddition();
		
		
		
		back = (Button)findViewById(R.id.BackButton);
		back.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.BackButton){
			Intent i = new Intent(this.context, RSSCompleteActivity.class);
			i.putExtra("feedList", feedList);
			i.putExtra("namesList", namesList);
			this.startActivity(i);
		}
		if(v.getId()==ADD_BUTTON_ID){
			String s = editText.getText().toString();
			String s2 = editText2.getText().toString();
			removeAddition();
			addOption(s, s2);
			feedList.add(s);
			namesList.add(s2);
			addAddition();
		}
		if(v.getId()==DELETE_ID){
			LinearLayout l = (LinearLayout)v.getParent();
			Button b = (Button)v;
			TextView t = (TextView) l.getChildAt(0);
			String s = (String) t.getText();
			feedList.remove(s);
			t=(TextView) l.getChildAt(1);
			s = t.getText().toString();
			namesList.remove(s);
			
			l.removeView(v);
			LinearLayout l2 = (LinearLayout)l.getParent();
			l2.removeView(l);
		}
	}
	

	private void addOption(String feed, String name){
		LinearLayout layout2 = new LinearLayout(context);
		layout2.setOrientation(0);
		layout2.setMinimumHeight(50);
		layout2.setGravity(1);
		layout.addView(layout2);
		
		TextView t = new TextView(context);
		t.setText(name);
		t.setWidth(400);
		t.setTextColor(R.color.black);
		t.setTextSize(28);
		layout2.addView(t);
		
		t = new TextView(context);
		t.setText(feed);
		t.setWidth(300);
		t.setTextColor(R.color.black);
		layout2.addView(t);
		
		
		
		Button b = new Button(context);
		b.setText("Delete");
		b.setWidth(200);
		b.setOnClickListener(this);
		b.setId(DELETE_ID);
		layout2.addView(b);
		
		
	}
	
	private void addAddition(){
		LinearLayout layout2 = new LinearLayout(context);
		layout2.setOrientation(0);
		layout2.setId(ADD_ADDITION_ID);
		layout2.setPadding(100, 50, 20, 10);
		layout.addView(layout2);
		
		TextView t;

		t = new TextView(context);
		t.setText("Feed Name:");
		t.setTextColor(R.color.black);
		t.setTextSize(18);
		t.setPadding(0, 0, 10, 0);
		layout2.addView(t);
		
		editText2 = new EditText(context);
		editText2.setWidth(350);
		layout2.addView(editText2);
		
		t = new TextView(context);
		t.setText("Feed url:");
		t.setTextColor(R.color.black);
		t.setTextSize(18);
		t.setPadding(20, 0, 10, 0);
		layout2.addView(t);
		
		editText = new EditText(context);
		layout2.addView(editText);
		editText.setWidth(350);
		
		
		
		Button add = new Button(context);
		add.setText("Add Feed");
		add.setId(ADD_BUTTON_ID);
		add.setHeight(50);
		add.setWidth(200);
		add.setOnClickListener(this);
		layout2.addView(add);
		
		
	}
	
	private void removeAddition(){
		LinearLayout layout2 = (LinearLayout)findViewById(ADD_ADDITION_ID);
		layout.removeView(layout2);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		String s = combineStrings(feedList);
		Editor e = shared.edit();
		e.putString(KEY, s);
		s = combineStrings(namesList);
		e.putString(KEY2, s);
		e.commit();
	}
	private String combineStrings(ArrayList<String> aList){
		String s="";
		for(String n:aList){
			s+=" "+n;
		}
		return s;
	}
	
	private ArrayList<String> uncombineStrings(String s){
		String[] array = s.split(" ");
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 1; i<array.length; i++){
			if(array[i].compareTo(" ")!=0){
				list.add(array[i]);
			}
		}
		return list;
	}
}
