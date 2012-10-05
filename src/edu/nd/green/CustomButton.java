package edu.nd.green;

import android.content.Context;
import android.widget.Button;

public class CustomButton extends Button{

	private String ButtonLink;
	
	public CustomButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}
	
	public void setButtonLink(String url){
		this.ButtonLink = url;
	}
	
	public String getButtonLink(){
		return this.ButtonLink;
	}
}
