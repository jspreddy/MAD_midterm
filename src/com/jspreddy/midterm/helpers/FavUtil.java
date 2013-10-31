package com.jspreddy.midterm.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;
import android.util.Xml;

public class FavUtil{
	
	static public class FavSAXParser extends DefaultHandler{
		
		ErrorObject error;
		Boolean errorStart = false;
		StringBuilder xmlInnerText;
		
		public static ErrorObject getError(InputStream in) throws IOException, SAXException
		{
			FavSAXParser parser = new FavSAXParser();
			Xml.parse(in, Xml.Encoding.UTF_8, parser);
			return parser.error;
		}
		
		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			xmlInnerText= new StringBuilder();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (localName.equals("error")) {
				error = new ErrorObject();
				errorStart=true;
			}
		
			xmlInnerText.setLength(0);
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			super.endElement(uri, localName, qName);
			if (localName.equals("error")){
				errorStart=false;
			}
			
			if(errorStart == true && localName.equals("id"))
			{
				error.setId(xmlInnerText.toString().trim());
			}
			if(errorStart == true && localName.equals("message"))
			{
				error.setMessage(xmlInnerText.toString().trim());
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

