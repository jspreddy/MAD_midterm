package com.jspreddy.midterm.helpers;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Xml;

public class FavUtil{
	
	static public class FavSAXParser extends DefaultHandler{
		
		ErrorObject error;
		UserObject user;
		FavoriteObject fav;
		FavApiObject favApi;
		Boolean errorStart = false;
		Boolean userStart = false;
		Boolean favListStart = false;
		Boolean favItemStart = false;
		StringBuilder xmlInnerText;
		
		public static FavApiObject getFavApiObject(InputStream in) throws IOException, SAXException
		{
			FavSAXParser parser = new FavSAXParser();
			Xml.parse(in, Xml.Encoding.UTF_8, parser);
			return parser.favApi;
		}
		
		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			xmlInnerText= new StringBuilder();
			this.favApi = new FavApiObject();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (localName.equals("error")) {
				error = new ErrorObject();
				errorStart=true;
			}
			if(localName.equals("user")){
				userStart = true;
				user = new UserObject();
			}
			if(localName.equals("favorites")){
				favListStart = true;
			}
			if(localName.equals("favorite")){
				favItemStart = true;
			}
		
			xmlInnerText.setLength(0);
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			super.endElement(uri, localName, qName);
			if (localName.equals("error")){
				errorStart=false;
				this.favApi.setError(error);
			}
			
			if(errorStart == true && localName.equals("id"))
			{
				error.setId(xmlInnerText.toString().trim());
			}
			if(errorStart == true && localName.equals("message"))
			{
				error.setMessage(xmlInnerText.toString().trim());
			}
			
			if(localName.equals("user")){
				userStart = false;
				this.favApi.setUser(user);
			}
			if(userStart == true && localName.equals("id")){
				user.setId(xmlInnerText.toString().trim());
			}
			
			if(localName.equals("favorites")){
				favListStart = false;
			}
			
			if(localName.equals("favorite")){
				favItemStart = false;
				this.favApi.addFavorite(fav);
			}
			
			if(favListStart && favItemStart && localName.equals("id")){
				this.fav.setId(xmlInnerText.toString().trim());
			}
			if(favListStart && favItemStart && localName.equals("count")){
				this.fav.setCount(xmlInnerText.toString().trim());
			}
			if(favListStart && favItemStart && localName.equals("isFavorite")){
				this.fav.setIsFav(xmlInnerText.toString().trim());
			}
			
			
			xmlInnerText.setLength(0);
		}
		
		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			xmlInnerText.append(ch,start,length);
		}
	}
	
}

