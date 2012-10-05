package edu.nd.green;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Article {

	private String Title;
	private String Description;
	private String PubDate;
	private String Link;
	private String Image;
	private String FeedName;
	private Bitmap ImageBitmap;
	private Date Date;
	
	public void setTitle(String string) {
		// TODO Auto-generated method stub
		this.Title=string;
	}

	public void setDescription(String string) throws Exception{
		// TODO Auto-generated method stub
		this.Description=string;
		
		
		/*if(this.Description.contains("<img ")){
			String image = this.Description.substring(this.Description.indexOf("<img "));
			String cleanup = image.substring(0, image.indexOf(">")+1);
			image = image.substring(image.indexOf("src=")+5);
			int indexOf = image.indexOf("'");
			if(indexOf==-1){
				indexOf = image.indexOf("\"");
			}
			this.Image = image.substring(0, indexOf);
			
			this.Description = this.Description.replace(cleanup, "");
			
		}
		
		/*
		if(this.Description.contains("<a href=")){
			String link = this.Description.substring(this.Description.indexOf("<a href="));
			String cleanup = link.substring(0, link.indexOf("</a>")+4);
			this.Description = this.Description.replace(cleanup, "");
			
		}
		*/

		
		if(this.Image!=null){
			String url = this.Image;
			URL feedImage = new URL(url);
		    HttpURLConnection conn= (HttpURLConnection)feedImage.openConnection(); 
		    InputStream is = conn.getInputStream();
		    this.ImageBitmap = BitmapFactory.decodeStream(is);
		}
				
		//Document doc = Jsoup.parse(this.Description);
		
		//this.Description = doc.body().text();
		
	}

	public void setPubDate(String string) throws Exception {
		// TODO Auto-generated method stub
		this.PubDate = string;
		DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
		this.setDate(formatter.parse(PubDate));
	}

	public void setLink(String string) {
		// TODO Auto-generated method stub
		this.Link=string;
	}

	public String getTitle(){
		return this.Title;
	}
	
	public String getDescription(){
		return this.Description;
	}
	
	public String getPubDate(){
		return this.PubDate;
	}
	
	public String getLink(){
		return this.Link;
	}
	
	public Bitmap getImage(){
		return this.ImageBitmap;	
	}
	
	public Date getDate(){
		return this.Date;
	}

	public void setDate(Date inDate){
		this.Date = inDate;
	}
	
	public void setImage(String string){
		this.Image = string;
	}
	
	public void setFeedName(String string){
		this.FeedName = string;
	}
	
	public String getFeedName(){
		return this.FeedName;
	}
}
