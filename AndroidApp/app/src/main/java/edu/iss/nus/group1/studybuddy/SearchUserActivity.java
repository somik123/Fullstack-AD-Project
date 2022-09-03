package edu.iss.nus.group1.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import edu.iss.nus.group1.studybuddy.adapters.UserListAdapter;
import edu.iss.nus.group1.studybuddy.dto.GroupDTO;
import edu.iss.nus.group1.studybuddy.dto.UserDTO;
import edu.iss.nus.group1.studybuddy.utility.APICommunication;

public class SearchUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    protected TextView searchBox;
    protected ImageView searchBtn;
    protected ListView listView;

    protected int userId;
    protected String token;
    protected String username;

    protected ArrayList<UserDTO> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        username = intent.getStringExtra("username");
        userId = intent.getIntExtra("userId", -1);

        searchBox = findViewById(R.id.txtUserSearch);
        searchBtn = findViewById(R.id.btnUserSearch);
        listView = findViewById(R.id.listViewUser);

        searchBtn.setOnClickListener(view -> {
            searchForUser();
        });

        searchBox.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press
                searchForUser();
                return true;
            }
            return false;
        });
    }

    protected void setUserList() {
        if (listView != null && users != null) {
            listView.setAdapter(new UserListAdapter(this, users));
            listView.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int participantId = users.get(i).getId();

        if (userId != participantId) {

            APICommunication apiComm = new APICommunication(token);
            apiComm.createPrivateGroups(userId, participantId);
            String reply = apiComm.getResult();
            Log.d(">><<>><< REPLY >><<>><<", reply);
            GroupDTO group = apiComm.jsonToObj(GroupDTO.class);

            if (group.getPrivate()) {
                String[] title = group.getName().split("\\|");
                if (username.equals(title[0].trim()))
                    group.setName(title[1].trim());
                else
                    group.setName(title[0].trim());
            }

            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("groupId", group.getId());
            intent.putExtra("groupName", group.getName());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }


    public void searchForUser() {
        String searchTxt = searchBox.getText().toString();
        APICommunication api = new APICommunication(token);
        api.searchUserByName(searchTxt);
        users = api.jsonToArrayList(UserDTO.class);
        setUserList();
    }
}