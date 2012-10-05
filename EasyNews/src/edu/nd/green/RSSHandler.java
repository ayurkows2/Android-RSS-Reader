package edu.nd.green;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.widget.Toast;

public class RSSHandler extends DefaultHandler{
	
	Boolean inItem = false;
	Boolean inImage = false;
	final int stateUnknown = 0;
	final int stateTitle = 1;
	final int stateDescription = 2;
	final int stateDate = 3;
	final int stateLink = 4;
	final int stateImage = 5;
	final int stateURL = 6;
	
	int currentState = 0;
	
	final int MAX_ARTICLES = 30;
	int articles = 0;
	
	private String RSSimage;
	
	private StringBuffer chars;
	private Article currentArticle;
	private ArrayList<Article> articleList = new ArrayList<Article>();
	
	
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);

		if(articles<=MAX_ARTICLES){
			if(localName.equalsIgnoreCase("image"))
				inImage = true;
			if(localName.equalsIgnoreCase("item")){
				inItem = true;
			} else if(localName.equalsIgnoreCase("title")){
				currentState = stateTitle;
			} else if(localName.equalsIgnoreCase("description")){
				currentState = stateDescription;
			} else if(localName.equalsIgnoreCase("pubDate")){
				currentState = stateDate;
			} else if(localName.equalsIgnoreCase("link")){
				currentState = stateLink;
			} else if(localName.equalsIgnoreCase("url")){
				currentState = stateURL;
			}

		}
		chars = new StringBuffer();

	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		chars.append(new String(ch, start, length));
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);

		if(articles<=MAX_ARTICLES){
			if(localName.equalsIgnoreCase("item")){
				if(currentArticle!=null){
					articleList.add(currentArticle);
					articles++;
				}

				currentArticle = new Article();
			}
			

			if(currentState == stateURL){
				this.RSSimage = chars.toString();
			}

			if(inItem == true){
				if(currentArticle!=null){
					if(currentState == stateTitle){
						currentArticle.setTitle(chars.toString());
						currentArticle.setImage(RSSimage);
					}else if(currentState == stateDescription){
						try {
							currentArticle.setDescription(chars.toString());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(currentState == stateDate){
						try {
							currentArticle.setPubDate(chars.toString());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(currentState == stateLink){
						currentArticle.setLink(chars.toString());
					}
				}
			}

		}
		currentState = stateUnknown;
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}
	
	public ArrayList getArticleList(){
		return articleList;
	}
	
}
