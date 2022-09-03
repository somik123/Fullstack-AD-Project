package edu.iss.nus.group1.studybuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import edu.iss.nus.group1.studybuddy.R;
import edu.iss.nus.group1.studybuddy.dto.GroupDTO;

public class GroupListAdapter extends ArrayAdapter<Object> {
    private final Context context;

    private final ArrayList<GroupDTO> groups;

    public GroupListAdapter(Context context, ArrayList<GroupDTO> groups) {
        super(context, R.layout.chat_group_row);
        this.context = context;
        this.groups = groups;

        addAll(new Object[groups.size()]);
    }

    @androidx.annotation.NonNull
    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_group_row, parent, false);
        }

        GroupDTO group = groups.get(pos);

        // set the image for ImageView
        TextView titleView = view.findViewById(R.id.txtGroupTitle);
        titleView.setText(group.getName());

        // set the text for TextView
        TextView descView = view.findViewById(R.id.txtGroupDesc);

        String lastMsgTime = group.getLastActiveTime();
        if (lastMsgTime != null && lastMsgTime.length() > 10)
            lastMsgTime = "Latest message: " + lastMsgTime.replace('T', ' ') + "\n";
        else
            lastMsgTime = "No messages found.\n";

        String description = lastMsgTime + "Created By: " + group.getCreatorName();
        descView.setText(description);

        return view;
    }

}
