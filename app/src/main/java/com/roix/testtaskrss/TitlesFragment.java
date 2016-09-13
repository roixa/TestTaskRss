package com.roix.testtaskrss;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;

/**
 *this fragment also save list and current position in bundle,
 * it needs to restore data after screen reorientation
 * */
public class TitlesFragment extends ListFragment {
    boolean mDualPane;
    int mCurCheckPosition = 0;
    ArrayList<NewsItem> newsItems;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        final Context context=getActivity();

        new LoadFeedTask(getActivity(),new LoadFeedTask.TaskCallback() {
            @Override
            public void onLoad(ArrayList<NewsItem> items) {
                    if(items==null)return;
                    setListAdapter(new NewsAdapter(context, items));
                    newsItems=items;

            }
        }).execute("http://4pda.ru/feed/");

        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            Log.d("@@@","savedInstanceState != null");
            // Restore last state for checked position.
            newsItems=savedInstanceState.getParcelableArrayList("items");
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
            setListAdapter(new NewsAdapter(context, newsItems));
        }
        else             Log.d("@@@","savedInstanceState == null");


        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mCurCheckPosition);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("curChoice", mCurCheckPosition);
        outState.putParcelableArrayList("items", newsItems);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            DetailsFragment details = (DetailsFragment)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = DetailsFragment.newInstance(newsItems.get(index));

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);

                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra("item", (Parcelable)newsItems.get(index));
            startActivity(intent);
        }
    }

}