package com.tg.iweibo.utils;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.tg.iweibo.domain.UpdateInfo;

public class XmlInfoParser {
	 public static UpdateInfo getUpdateInfo(InputStream is) throws XmlPullParserException, IOException {
	        XmlPullParser parser = Xml.newPullParser();
	        UpdateInfo info = new UpdateInfo();
	        parser.setInput(is,"utf-8");
	        int type = parser.getEventType();
	        while(type != XmlPullParser.END_DOCUMENT){
	            switch(type){
	                case XmlPullParser.START_TAG:
	                    if("version".equals(parser.getName())){
	                        info.setVersion(parser.nextText());
	                    }else if ("description".equals(parser.getName())){
	                        info.setDescription(parser.nextText());
	                    }else if("apkurl".equals(parser.getName())){
	                        info.setApkurl(parser.nextText());
	                    }
	                    break;
	            }
	            type = parser.next();
	        }

	        return info;
	    }
}
