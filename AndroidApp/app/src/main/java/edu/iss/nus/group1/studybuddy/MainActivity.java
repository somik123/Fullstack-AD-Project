package edu.iss.nus.group1.studybuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.iss.nus.group1.studybuddy.adapters.GroupListAdapter;
import edu.iss.nus.group1.studybuddy.dto.GroupDTO;
import edu.iss.nus.group1.studybuddy.service.PollingService;
import edu.iss.nus.group1.studybuddy.utility.APICommunication;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    protected static final int autoRefreshInterval = 6000; //put here time 1000 milliseconds=1 second

    ActivityResultLauncher<Intent> resultLauncherLogin;

    protected ArrayList<GroupDTO> groups = new ArrayList<>();

    SharedPreferences pref;

    protected int userId;
    protected String token;
    protected String username;

    APICommunication apiComm;

    protected ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("tabLayout", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("tabId", 0);
        editor.apply();
        setContentView(R.layout.activity_main);

        // Register activity result
        resultLauncherLogin = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Receive result and process it
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            token = data.getStringExtra("token");
                            username = data.getStringExtra("username");
                            userId = data.getIntExtra("userId", -1);
                            getGroups();
                            initGroupList();
                        }
                    }
                });

        // Fist load
        verifyUserDetails();
        initGroupList();

        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchUserActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("username", username);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getGroups();
                runOnUiThread(() -> initGroupList());
            }
        }, autoRefreshInterval, autoRefreshInterval);//put here time 1000 milliseconds=1 second

    }


    protected void verifyUserDetails() {
        // Read
        if (token == null || username == null || userId == -1) {
            pref = getSharedPreferences("user_credentials", MODE_PRIVATE);
            userId = pref.getInt("userId", -1);
            username = pref.getString("username", null);
            token = pref.getString("token", null);

            Log.d(">> LOCATION ::", "Read prefs");
        }

        if (token != null && token.length() > 10) {
            apiComm = new APICommunication(token);
            apiComm.verifyToken();
            String reply = apiComm.getResult();

            Log.d(">> LOCATION ::", "Verify token: " + reply);
            Log.d(">> LOCATION ::", username);

            if (reply != null && reply.trim().equals(username)) {
                getGroups();
                startPollService();
                return;
            }
        }

        // If come to here, user not logged in
        showLogin();
    }


    protected void getGroups() {
        if (token != null && userId > -1) {
            apiComm = new APICommunication(token);
            apiComm.getGroups(userId);
            String reply = apiComm.getResult();
            //Log.d(">> LOCATION ::", "Get groups");
            if (reply == null) return;

            ArrayList<GroupDTO> groupList = apiComm.jsonToArrayList(GroupDTO.class);

            for (int i = 0; i < groupList.size(); i++) {
                GroupDTO g = groupList.get(i);
                if (g.getPrivate()) {
                    String[] title = g.getName().split("\\|");
                    if (username.equals(title[0].trim()))
                        g.setName(title[1].trim());
                    else
                        g.setName(title[0].trim());
                    groupList.set(i, g);
                }
            }

            groups = groupList;
        }
    }


    protected void showLogin() {
        // Call Login activity
        Intent intent = new Intent(this, LoginActivity.class);
        resultLauncherLogin.launch(intent);
    }


    protected void initGroupList() {
        listView = findViewById(R.id.listViewGroup);
        if (listView != null && groups != null) {
            listView.setAdapter(new GroupListAdapter(this, groups));
            listView.setOnItemClickListener(this);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
        GroupDTO group = groups.get(pos);

        Log.d(">> GROUP ::", group.toString());

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("groupId", group.getId());
        intent.putExtra("groupName", group.getName());
        startActivity(intent);
    }


    protected void startPollService() {
        // Generate new process ID
        Integer processID = new Random().nextInt(999999);

        // Create new intent
        Intent intent = new Intent(MainActivity.this, PollingService.class);

        // Stop the running service (if any)
        stopService(intent);

        // Initiate the service
        intent.setAction("run");
        intent.putExtra("userId", userId);
        intent.putExtra("token", token);
        intent.putExtra("processID", processID);

        // Start the service
        getApplicationContext().startForegroundService(intent);
    }

}