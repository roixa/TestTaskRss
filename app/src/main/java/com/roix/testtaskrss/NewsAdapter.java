package com.roix.testtaskrss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by u5 on 9/9/16.
 * rss feed list
 */
public class NewsAdapter extends BaseAdapter {
    private List<NewsItem> items;
    LayoutInflater lInflater;
    public NewsAdapter(Context context, List<NewsItem> items) {
        this.items=items;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if(items!=null) return items.size();
        else return 0;
    }

    @Override
    public Object getItem(int i) {
        if(items!=null)return items.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = lInflater.inflate(R.layout.news_item, viewGroup, false);
        }
        NewsItem item=items.get(i);
        ((TextView) view.findViewById(R.id.title)).setText(item.getTitle());
        //((TextView) view.findViewById(R.id.description)).setText(item.get());
        return view;
    }
}
