package edu.iss.nus.group1.studybuddy.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.iss.nus.group1.studybuddy.R;
import edu.iss.nus.group1.studybuddy.dto.UserDTO;

public class UserListAdapter extends ArrayAdapter<Object> {
    private final Context context;
    private final ArrayList<UserDTO> users;


    public UserListAdapter(Context context, ArrayList<UserDTO> users) {
        super(context, R.layout.user_search_row);
        this.context = context;
        this.users = users;
        Log.i("TAG", "UserListAdapter A: " + users.size());
        addAll(new Object[users.size()]);
    }

    @NonNull
    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        Log.i("TAG", "UserListAdapter B: " + users.size());
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.user_search_row, parent, false);
        }

        UserDTO user = users.get(pos);
        // set the image for ImageView
        TextView titleView = view.findViewById(R.id.txtUserSearchUsername);
        titleView.setText(user.getUsername());

        // set the text for TextView
        TextView descView = view.findViewById(R.id.txtUserSearchEmail);
        String description = user.getEmail();
        descView.setText(description);

        if (user.getPhoto() != null && user.getPhoto().length() > 10) {
            ImageView img = view.findViewById(R.id.imgUserIcon);
            Glide
                    .with(view)
                    .load(user.getPhoto())
                    .into(img);
        }
        return view;

    }
}
