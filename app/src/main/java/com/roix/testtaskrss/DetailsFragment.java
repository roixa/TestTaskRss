package com.roix.testtaskrss;

import android.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by u5 on 9/9/16.
 * this class show one item info
 */
public class DetailsFragment extends Fragment {
    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static DetailsFragment newInstance(NewsItem item) {
        DetailsFragment f = new DetailsFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("item",item);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        NewsItem item=getArguments().getParcelable("item");
        if(item==null)return view;
        TextView title=(TextView)view.findViewById(R.id.title);
        TextView description=(TextView)view.findViewById(R.id.description);
        TextView link=(TextView)view.findViewById(R.id.link);

        title.setText(item.getTitle());
        description.setText(item.getDescription());
        link.setText(item.getLink());
        return view;
    }
}
