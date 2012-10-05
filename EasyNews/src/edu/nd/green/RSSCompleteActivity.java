package edu.nd.green;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class RSSCompleteActivity extends Activity implements OnClickListener, OnItemClickListener{
    /** Called when the activity is first created. */
	
	private Context context;
	private HorizontalScrollView HorizView;
	private LinearLayout layout;
	private ScrollView scrollView;
	private LinearLayout layout2;
	private ArrayList<String> feedList;
	private ArrayList<String> namesList;
	private ArrayList<String> feedsActivated;
	private int MaxArticles =15;
	private ArrayList<Article> fullArticleList;
	private static final int GO_TO_ARTICLE = 900;
	private static final int SHARE_ARTICLE = 800;
	
	
	private int CurrentIndex;
	private SharedPreferences shared;
	private static final String SHARED_PREFS_NAME = "shared";
	private static final String KEY = "feedlist";
	private static final String KEY2 = "namesList";
	
	private TextView textView;
	private ImageView imageView;
	private String Description;
	private CustomButton Button;
	private Button plus;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
		context = this.getApplicationContext();
		HorizView = (HorizontalScrollView) this.findViewById(R.id.horizScroll);
		layout = (LinearLayout)this.findViewById(R.id.layout1);
		fullArticleList = new ArrayList<Article>();
	    shared = this.getSharedPreferences(SHARED_PREFS_NAME, 0 );
		
	    if(feedList == null){
	    	String s = shared.getString(KEY, "");
	    	feedList = uncombineStrings(s);
	    	s = shared.getString(KEY2, "");
	    	namesList = uncombineStrings(s);
	    }
	    
		if(feedList==null){
			Intent i = this.getIntent();
			feedList=i.getStringArrayListExtra("feedList");
			namesList=i.getStringArrayListExtra("namesList");
		}

		if(feedList==null){
			feedList = new ArrayList<String>();
			namesList = new ArrayList<String>();
		}
		
		feedsActivated = (ArrayList<String>) namesList.clone();
		
		Button backButton = (Button)this.findViewById(R.id.backToStartButton);
		backButton.setOnClickListener(this);
		
		Button settings = (Button)this.findViewById(R.id.Settings);
		settings.setOnClickListener(this);
		
		Button refresh = (Button)this.findViewById(R.id.refresh);
		refresh.setOnClickListener(this);
		
		//feedList.add("http://gizmodo.com/vip.xml");
		//feedList.add("http://sports.espn.go.com/espn/rss/nfl/news");
		
		if(feedList.size()>0){
			for(String s: feedList){
				addRSS(s, fullArticleList);
			}
		}
		
		if(fullArticleList!=null){
			fullArticleList = sortByDate(fullArticleList);
		}
		
        /* RSS FEEDS
         * http://gizmodo.com/vip.xml
         * http://sports.espn.go.com/espn/rss/nfl/news
         * http://services.digg.com/2.0/story.getTopNews?type=rss
         * http://www.engadget.com/rss.xml
         * 
         */
		
		layout.removeAllViewsInLayout();

		addSideBar();
		for(CurrentIndex = 0; CurrentIndex<MaxArticles; CurrentIndex++){
			if(CurrentIndex<=fullArticleList.size()-1){
				Article r=fullArticleList.get(CurrentIndex);
				//r.setFeedName(namesList.get(feedList.indexOf(r)));
				addArticle(r, fullArticleList);
			}
		}
		addPlusButton();

    }
    
    private ArrayList<Article> sortByDate(ArrayList<Article> articleList){
    	Boolean listSorted = false;
    	int n = articleList.size();
    	while(!listSorted){
    		n--;
    		listSorted = true;
    		for(int i = 0; i<n; i++){
    			if(articleList.get(i).getDate().before(articleList.get(i+1).getDate())){
    				Article temp = articleList.get(i);
    				articleList.set(i, articleList.get(i+1));
    				articleList.set(i+1, temp);
    				listSorted = false;
    			}
    		}
    	}
    	return articleList;
    }
    
    private void addRSS(String url, ArrayList<Article>FullArticleList){
    	
    	ArrayList<Article> articleList = null;
        RSSFeed myRSSFeed = new RSSFeed();
        myRSSFeed.execute(url);
        
		try{
			articleList = myRSSFeed.get();
			for(Article i:articleList){
				i.setFeedName(namesList.get(feedList.indexOf(url)));
				FullArticleList.add(i);
			}
		}
		catch(Exception e){
			//Log.d("ERROR GETTING RSS FEED ARTICLE LIST", "ARY");
		}
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.backToStartButton){
			HorizView.scrollTo(0,0);	
		}
		else if(v.getId()==R.id.Settings){
			Intent i = new Intent(this.context, SettingsActivity.class);
			i.putStringArrayListExtra("feedList", this.feedList);
			i.putStringArrayListExtra("namesList", this.namesList);
			this.startActivity(i);
		}
		else if(v.getId()==9){
			layout.removeView(plus);
			int adjuster = 0;
			for(int j = CurrentIndex; CurrentIndex<j+5+adjuster;CurrentIndex++){
				if(CurrentIndex<fullArticleList.size()-1){
					if(feedsActivated.contains(fullArticleList.get(CurrentIndex).getFeedName())==true){
						addArticle(fullArticleList.get(CurrentIndex), fullArticleList);
					}
					else{
						adjuster++;
					}
				}
			}
			addPlusButton();
		}
		else if ((v.getId()==GO_TO_ARTICLE)){
			CustomButton b = (CustomButton)v;
			String s = (String) b.getButtonLink();
			if("Go To Article... ".compareTo((String)b.getText())==0){
				if(s!=""){
					Intent i = new Intent(this.context, WebViewActivity.class);
					i.putExtra("url", s);
					this.startActivity(i);
				}else{
					Toast.makeText(context, "Link not found", Toast.LENGTH_SHORT).show();
				}
			}
		}
		else if ((v.getId()==SHARE_ARTICLE)){
			
			CustomButton b = (CustomButton)v;
			String link = (String) b.getButtonLink();
			
			LinearLayout l = (LinearLayout)v.getParent();
			TextView titleTV = (TextView) l.getChildAt(0);
			String title = (String) titleTV.getText();
			
			
			ScrollView s = (ScrollView)l.getChildAt(2);
			TextView desTV = (TextView) s.getChildAt(0);
			String desc = (String) desTV.getText();
			
			if (desc.length() > 500) {
				
				desc = desc.substring(0, 500);
				desc += "...";
				
			}
			
			/* Create the Intent */
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

			/* Fill it with Data */
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"to@email.com"});
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I wanted to share this article:" + "\n" + "\n" + desc + "\n" + "\n" + link);

			/* Send it off to the Activity-Chooser */
			this.startActivity(emailIntent);
			//this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		}
		else if(v.getId()==R.id.refresh){
			Intent i = new Intent(this.context, RSSCompleteActivity.class);
			i.putExtra("feedList", feedList);
			i.putExtra("namesList", namesList);
			this.startActivity(i);
		}
	}
	
	
	private void addArticle(Article r, ArrayList<Article> fullArticleList){
		this.layout2 = new LinearLayout(context);
		int i = fullArticleList.indexOf(r);
		layout2.setMinimumHeight(layout.getHeight());
		layout2.setOrientation(1);
		layout2.setPadding(20, 10, 20, 10);
					
		/*imageView = new ImageView(context);
		if(fullArticleList.get(i).getImage()!=null){
			imageView = new ImageView(context);
			imageView.setImageBitmap(fullArticleList.get(i).getImage());
			layout2.addView(imageView);
		}
		*/ 
		textView = new TextView(context);
		textView.setText(r.getTitle());
		textView.setMaxWidth(400);
		textView.setHeight(130);
		textView.setTextSize(30);
		textView.setPadding(0, 0, 0, 15);
		layout2.addView(textView);
		
		textView = new TextView(context);
		textView.setText(r.getPubDate());
		textView.setMaxWidth(400);
		textView.setPadding(0, 0, 0, 10);
		layout2.addView(textView);
		
		ScrollView S = new ScrollView(context);
		textView = new TextView(context);
		Document doc = Jsoup.parse(r.getDescription());
		Description = doc.body().text();
		
		if (Description.length() > 750) {
			Description = Description.substring(0, 750);
			Description += "...";
		}
		
		textView.setText("\t"+Description);
		textView.setMaxWidth(400);
		textView.setHeight(320);
		textView.setTextSize(16);
		textView.setPadding(0, 0, 0, 10);
		S.addView(textView);
		layout2.addView(S);
		
		
		
		Button = new CustomButton(context);
		Button.setText("Go To Article... ");
		Button.setButtonLink(r.getLink());
		Button.setMaxWidth(420);
		Button.setHeight(40);
		Button.setPadding(0, 10, 0, 10);
		Button.setId(GO_TO_ARTICLE);
		Button.setOnClickListener(this);
		layout2.addView(Button);
		
		Button = new CustomButton(context);
		Button.setText("Email Article... ");
		Button.setButtonLink(r.getLink());
		Button.setMaxWidth(400);
		Button.setHeight(40);
		Button.setPadding(0, 10, 0, 10);
		Button.setId(SHARE_ARTICLE);
		Button.setOnClickListener(this);
		layout2.addView(Button);
		
		layout.addView(layout2);
	}
	
	private void addPlusButton(){
		plus = new Button(context);
		plus.setHeight(layout.getHeight());
		plus.setMinimumHeight(750);
		plus.setWidth(60);
		plus.setText("+");
		plus.setId(9);
		plus.setOnClickListener(this);
		layout.addView(plus);
	}

	private void addSideBar(){
		ListView listView = new ListView(context);
		LinearLayout l = new LinearLayout(context);
		l.setLayoutParams(new LayoutParams(200,LayoutParams.WRAP_CONTENT));
		l.setMinimumHeight(LayoutParams.FILL_PARENT);
		listView.setMinimumWidth(LayoutParams.WRAP_CONTENT);//LayoutParams.FILL_PARENT
		listView.setOnItemClickListener(this);
		ArrayAdapter<String> a = new ArrayAdapter<String>(this, R.layout.mylistitem, this.namesList);
		listView.setAdapter(a);
		listView.setLayoutParams(new LayoutParams(300,LayoutParams.FILL_PARENT));
		listView.setMinimumWidth(200);
		listView.setPadding(20, 10, 20, 10);
		listView.setDividerHeight(5);
		listView.setBackgroundColor(R.color.darkgrey);
		//listView.setBackgroundColor(color);
		l.addView(listView);
		layout.addView(l,0);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		String s = combineStrings(feedList);
		Editor e = shared.edit();
		e.putString(KEY, s);
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		// TODO Auto-generated method stub
		TextView t = (TextView) v;
		String feedName = t.getText().toString();
		if(feedsActivated.contains(feedName)==true){
			feedsActivated.remove(feedName);
			deleteFeedFromLayout(feedName);
		}
		else{
			feedsActivated.add(feedName);
			addFeedFromLayout(feedName);

		}
	}
	
	private void deleteFeedFromLayout(String feedName){
		for(Article r: fullArticleList){
			if(r.getFeedName().compareTo(feedName)==0){
				for(int i = 1; i<layout.getChildCount()-1; i++){
					LinearLayout l = (LinearLayout) layout.getChildAt(i);
					TextView titleView = (TextView) l.getChildAt(0);
					String title = titleView.getText().toString();
					if(title.compareTo(r.getTitle())==0){
						layout.removeViewAt(i);
					}
				}
			}
		}
	}
	
	private void addFeedFromLayout(String feedName){
		layout.removeAllViews();
		int adjuster = 0;
		//sortByDate(fullArticleList);
		addSideBar();
		for(CurrentIndex = 0; CurrentIndex<15+adjuster; CurrentIndex++){
			if(CurrentIndex<fullArticleList.size()-1){
				Article r=fullArticleList.get(CurrentIndex);
				if(feedsActivated.contains(r.getFeedName())==true){
					addArticle(r, fullArticleList);
				}
				else{
					adjuster++;
				}
			}
		}
		addPlusButton();
	}
}