package com.walkindeep.teammanagerpreview.Controller;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    /**
     * @param xmlData API接口
     * @return dataAssignSpinner的数据
     */
    public static void parseXMLWithPull(String xmlData, List<String> dataAssignSpinner) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            //由于对应的API接口只有ID,PROJECT,USER这几个参数,所以建立对应字符串对象
            String id = "";
            String project = "";
            String user = "";
            List<String> role = new ArrayList<String>();
            while (eventType != XmlPullParser.END_DOCUMENT) {//XmlPullParser.END_DOCUMENT是指XML结尾
                String nodeName = xmlPullParser.getName();

                switch (eventType) {
                    //开始解析某个结点
                    case XmlPullParser.START_TAG: {
                        if ("id".equals(nodeName)) {//判断是不是ID所对应的数据
                            id = xmlPullParser.nextText();
                        } else if ("user".equals(nodeName)) {//判断是不是user所对应的属性
                            String temp = xmlPullParser.getAttributeValue(null, "name");
                            dataAssignSpinner.add(temp);
                            Log.d("Debug", temp);
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {

                        break;
                    }
                }
                eventType = xmlPullParser.next();//获取一个名字后,进行下一个解析
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
