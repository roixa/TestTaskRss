package com.roix.testtaskrss;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by u5 on 9/7/16.
 * this acync task needs to load, parse, and cache rss feed
 */
public class LoadFeedTask extends AsyncTask<String,Void,ArrayList<NewsItem>> {

    private TaskCallback callback;
    private Context context;
    public LoadFeedTask(Context context,TaskCallback callback){
        this.callback=callback;
        this.context=context;
    }
    @Override
    protected ArrayList<NewsItem> doInBackground(String... strings) {
        InputStream stream=getInputStream(strings[0]);
        if(stream!=null){
            ArrayList<NewsItem> items=parseXMLFeed(stream);
            try {
                InternalStorage.writeObject(context,items);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return items;
        }
        else {
            ArrayList<NewsItem> items=null;

            try {
                items=(ArrayList<NewsItem>)InternalStorage.readObject(context);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return items;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<NewsItem> newsItems) {
        super.onPostExecute(newsItems);
        callback.onLoad(newsItems);
    }

    public static ArrayList<NewsItem> parseXMLFeed(InputStream inputStream){
        // Initializing instance variables

        ArrayList<NewsItem> ret=new ArrayList<>();
        try {
            Log.i("@@@", "url");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            Log.i("@@@", "setInput");

            // We will get the XML from an input stream
            xpp.setInput(inputStream, "windows-1251");//"UTF_8");
            Log.i("@@@", "endsetInput");

        /* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
         * However, we should take in consideration that the rss feed name also is enclosed in a "<title>" tag.
         * As we know, every feed begins with these lines: "<channel><title>Feed_Name</title>...."
         * so we should skip the "<title>" tag which is a child of "<channel>" tag,
         * and take in consideration only "<title>" tag which is a child of "<item>"
         *
         * In order to achieve this, we will make use of a boolean variable.
         */
            boolean insideItem = false;

            // Returns the type of current event: START_TAG, END_TAG, etc..
            int eventType = xpp.getEventType();
            String title="";
            String link="";
            String comments="";
            String pubDate="";
            String description="";

            Log.i("@@@","eventType");

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {

                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (insideItem)
                            title=xpp.nextText();
                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                        if (insideItem)
                            link=xpp.nextText();
                    } else if (xpp.getName().equalsIgnoreCase("comments")) {
                        if (insideItem)
                            comments=xpp.nextText();
                    } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                        if (insideItem)
                            pubDate=xpp.nextText();
                    } else if (xpp.getName().equalsIgnoreCase("description")) {
                        if (insideItem)
                            description=xpp.nextText();
                    }
                }else if(eventType== XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                    insideItem=false;
                    ret.add(new NewsItem(title,comments,link,description,pubDate));
                }

                eventType = xpp.next(); //move to next element
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("@@@","return");

        return ret;
    }

    public static InputStream getInputStream(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public interface TaskCallback{
        void onLoad(ArrayList<NewsItem> items);
    }
}
