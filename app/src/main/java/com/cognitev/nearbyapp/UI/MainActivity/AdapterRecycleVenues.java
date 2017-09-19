package com.cognitev.nearbyapp.UI.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cognitev.nearbyapp.R;
import com.cognitev.nearbyapp.UI.MainActivity.ViewModels.VMHolderRecyclerVenue;
import com.cognitev.nearbyapp.Utilities.UtilityConversions;

public class AdapterRecycleVenues extends RecyclerView.Adapter<HolderRecycleVenue> {

    public CursorAdapter mCursorAdapter;
    private Activity mActivity;

    public AdapterRecycleVenues(Activity activity, Cursor cursor) {
        mActivity = activity;
        initCursorAdapter(cursor);
    }

    public void initCursorAdapter(Cursor cursor) {
        mCursorAdapter = new CursorAdapter(mActivity, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return DataBindingUtil.inflate(
                        LayoutInflater.from(mActivity),
                        R.layout.item_recycler_venues,
                        parent, false).getRoot();
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                ((HolderRecycleVenue) view.getTag()).binding.getVenue()
                        .changeVenue(UtilityConversions.getVenueFromCursor(cursor));
            }
        };
    }

    @Override
    public HolderRecycleVenue onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mCursorAdapter.newView(mActivity, mCursorAdapter.getCursor(), parent);
        HolderRecycleVenue holder = new HolderRecycleVenue(v, new VMHolderRecyclerVenue());
        v.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(HolderRecycleVenue holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mActivity, mCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }
}
