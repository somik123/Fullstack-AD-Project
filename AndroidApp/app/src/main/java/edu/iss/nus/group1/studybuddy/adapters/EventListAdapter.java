package edu.iss.nus.group1.studybuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.iss.nus.group1.studybuddy.R;
import edu.iss.nus.group1.studybuddy.dto.EventDTO;

public class EventListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_TOP = 0;
    private static final int VIEW_TYPE_MIDDLE = 1;
    private static final int VIEW_TYPE_BOTTOM = 2;

    private static ClickListener clickListener;

    private Context mContext;
    private ArrayList<EventDTO> mEventList;

    public EventListAdapter(Context context, ArrayList<EventDTO> eventList) {
        mContext = context;
        mEventList = eventList;
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return VIEW_TYPE_TOP;
        } else if (position == mEventList.size() - 1) {
            return VIEW_TYPE_BOTTOM;
        }
        return VIEW_TYPE_MIDDLE;
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        EventListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EventDTO event = mEventList.get(position);

        // Populate views...
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_TOP:
                // The top of the line has to be rounded
                ((ViewHolder) holder).bind(event, VIEW_TYPE_TOP);
                break;
            case VIEW_TYPE_MIDDLE:
                // Only the color could be enough
                // but a drawable can be used to make the cap rounded also here
                ((ViewHolder) holder).bind(event, VIEW_TYPE_MIDDLE);
                break;
            case VIEW_TYPE_BOTTOM:
                ((ViewHolder) holder).bind(event, VIEW_TYPE_BOTTOM);
                break;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView mItemTitle;
        TextView mItemSubtitle;
        TextView mItemDate;
        FrameLayout mItemLine;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mItemTitle = (TextView) itemView.findViewById(R.id.item_title);
            mItemSubtitle = (TextView) itemView.findViewById(R.id.item_subtitle);
            mItemDate = (TextView) itemView.findViewById(R.id.item_date);
            mItemLine = (FrameLayout) itemView.findViewById(R.id.item_line);
        }

        void bind(EventDTO event, int view_type) {
            mItemTitle.setText(event.getName());
            mItemSubtitle.setText(event.getDescription());
            mItemDate.setText(event.getEventTime().replace('T', ' '));
            if (view_type == VIEW_TYPE_TOP)
                mItemLine.setBackgroundResource(R.drawable.line_bg_top);
            else if (view_type == VIEW_TYPE_MIDDLE)
                mItemLine.setBackgroundResource(R.drawable.line_bg_middle);
            else
                mItemLine.setBackgroundResource(R.drawable.line_bg_bottom);
            // Format the stored timestamp into a readable String using method.
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

}
