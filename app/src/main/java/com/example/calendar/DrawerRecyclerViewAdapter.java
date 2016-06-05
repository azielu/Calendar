package com.example.calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Adas on 24.03.2016.
 */
public class DrawerRecyclerViewAdapter extends RecyclerView.Adapter<DrawerRecyclerViewAdapter.Holder> {
    private LayoutInflater mInflater;
    private List<String> mCalendars = Collections.emptyList();


    private Context mContext;

    public DrawerRecyclerViewAdapter(Context context, List<String> calendars) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mCalendars = calendars;
    }

    // Called when the RecyclerView needs a new RecyclerView.ViewHolder (NoteHolder)
    // to represent an item.  We inflate the XML layout and return our view (NoteHolder)
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.drawer_list_item, parent, false);
        return new Holder(view);
    }

    // Called by RecyclerView to display the data (a note) at the specified position.
    // This method needs to update the contents of the view to reflect the item at the
    // given position e.g. we are updating the view here with the data
    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.mName.setText(mCalendars.get(position));

        ScreenSlidePagerActivity activity = (ScreenSlidePagerActivity) mContext;
        ScreenSlidePageFragment fragment = (ScreenSlidePageFragment) activity.getSupportFragmentManager()
                .getFragments().get((position+1)% activity.getSupportFragmentManager().getFragments().size());

        GradientDrawable bgShape = (GradientDrawable) holder.mCircle.getBackground();
        bgShape.setColor(fragment.todaysColor);
        bgShape.setStroke(2, Color.BLACK);
        if (position == activity.mPager.getCurrentItem()) {
            holder.mBackground.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryBright));
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorTextPrimary));
        }
        else {
            holder.mBackground.setBackgroundColor(mContext.getResources().getColor(R.color.colorBackground));
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorTextSecondary));
        }


    }

    @Override
    public int getItemCount() {
        return mCalendars.size();
    }

    public void setData(List<String> calendars) {

        this.mCalendars = calendars;

    }

    public void delete(int position) {
        mCalendars.remove(position);
        notifyItemRemoved(position);
    }


    public void notifyImageObtained() {
        notifyDataSetChanged();
    }

    // Simple nested class that holds the various view components for the adapter
    // and as specified in custom_notes_adapter_layout.xml which we just created
    public class Holder extends RecyclerView.ViewHolder {

        TextView mName;
        ImageView mCircle;
        LinearLayout mBackground;


        public Holder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.drawer_text_view);
            mCircle = (ImageView) itemView.findViewById(R.id.drawer_circle);
            mBackground = (LinearLayout) itemView.findViewById(R.id.drawer_item_background);


        }

    }
}
