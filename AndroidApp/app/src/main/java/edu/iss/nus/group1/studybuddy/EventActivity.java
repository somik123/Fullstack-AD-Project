package edu.iss.nus.group1.studybuddy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.iss.nus.group1.studybuddy.adapters.EventListAdapter;
import edu.iss.nus.group1.studybuddy.dto.EventDTO;
import edu.iss.nus.group1.studybuddy.utility.APICommunication;

public class EventActivity extends AppCompatActivity {

    protected static final int autoRefreshInterval = 10000; //put here time 1000 milliseconds=1 second

    protected ArrayList<EventDTO> events = new ArrayList<>();
    protected RecyclerView eventRecycler;
    protected EventListAdapter eventListAdapter;
    protected MaterialAlertDialogBuilder materialAlertDialogBuilder;
    protected View customAlertDialogView;
    protected TextInputEditText editEventName, editDescription, editGroupName, editEventDate, editEventTime;
    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    protected int userId;
    protected String token;
    protected String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        SharedPreferences pref = getSharedPreferences("user_credentials", MODE_PRIVATE);
        userId = pref.getInt("userId", 0);
        username = pref.getString("username", null);
        token = pref.getString("token", null);


        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            customAlertDialogView = LayoutInflater.from(EventActivity.this)
                    .inflate(R.layout.create_event, null, false);

            // Launching the custom alert dialog
            launchCreateEventDialog();

            // Update list of events/groups
            getEvents();
            initEventList();
        });

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);

        getEvents();
        initEventList();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ArrayList<EventDTO> oldEvents = new ArrayList<>(events);
                getEvents();

                if (events != null && events.size() > oldEvents.size()) {
                    runOnUiThread(() -> {
                        initEventList();
                    });
                }
            }
        }, autoRefreshInterval, autoRefreshInterval);
    }

    protected void getEvents() {

        APICommunication apiComm = new APICommunication(token);
        apiComm.getAllEvents();
        String reply = apiComm.getResult();

        Log.d(">>>> EVENTS <<<<", reply);

        if (reply == null) return;

        ArrayList<EventDTO> eventList = apiComm.jsonToArrayList(EventDTO.class);

        if (eventList.size() > 0) {
            events = eventList;
        }
    }

    protected void initEventList() {

        eventRecycler = findViewById(R.id.recycler_event);
        if (eventRecycler != null) {
            eventListAdapter = new EventListAdapter(this, events);
            eventListAdapter.setOnItemClickListener(new EventListAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    EventDTO event = events.get(position);
                    customAlertDialogView = LayoutInflater.from(EventActivity.this)
                            .inflate(R.layout.view_event, null, false);

                    // Launching the custom alert dialog
                    launchViewEventDialog(event, userId);
                }

                @Override
                public void onItemLongClick(int position, View v) {
                    EventDTO event = events.get(position);
                    Toast toast = Toast.makeText(EventActivity.this, event.getName(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            eventRecycler.setLayoutManager(new LinearLayoutManager(this));
            eventRecycler.setAdapter(eventListAdapter);

        }
    }

    private void launchCreateEventDialog() {
        editEventName = customAlertDialogView.findViewById(R.id.event_name);
        editDescription = customAlertDialogView.findViewById(R.id.description);
        editGroupName = customAlertDialogView.findViewById(R.id.group_name);
        editEventDate = customAlertDialogView.findViewById(R.id.event_date);
        editEventTime = customAlertDialogView.findViewById(R.id.event_time);

        editEventDate.setOnClickListener(view1 -> {
            MaterialDatePicker picker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select date")
                            .build();
            picker.show(getSupportFragmentManager(), "tag");

            picker.addOnPositiveButtonClickListener(selection -> {
                String date = sdf.format(picker.getSelection());
                Log.d("Date", date.substring(0, 10));
                // if the user clicks on the positive
                // button that is ok button update the
                // selected date
                editEventDate.setText(date.substring(0, 10));
                // in the above statement, getHeaderText
                // is the selected date preview from the
                // dialog
            });
        });

        editEventTime.setOnClickListener(view1 -> {
            MaterialTimePicker picker =
                    new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_24H)
                            .setTitleText("Select Event time")
                            .build();
            picker.show(getSupportFragmentManager(), "tag");

            picker.addOnPositiveButtonClickListener(selection -> {
                int hour = picker.getHour();
                int minute = picker.getMinute();
                String time = (hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour)) + ":" + (minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute));
                Log.d("Time", time);
                editEventTime.setText(time);
            });
        });

        // Building the Alert dialog using materialAlertDialogBuilder instance
        materialAlertDialogBuilder.setView(customAlertDialogView)
                .setTitle("Create Event")
                .setMessage("Enter basic details")
                .setPositiveButton("Create", (dialogInterface, i) -> {
                    String sEventName = editEventName.getText().toString();
                    String sDescription = editDescription.getText().toString();
                    String sGroupName = editGroupName.getText().toString();
                    String sEventDate = editEventDate.getText().toString();
                    String sEventTime = editEventTime.getText().toString();
                    //check validation before submitting

                    Map<String, Object> postDataMap = new HashMap<>();
                    postDataMap.put("name", sEventName);
                    postDataMap.put("description", sDescription);
                    postDataMap.put("username", username);
                    postDataMap.put("createTime", LocalDateTime.now().toString());
                    postDataMap.put("groupName", sGroupName);
                    postDataMap.put("eventTime", sEventDate + "T" + sEventTime + ":00");

                    Log.d(">> Event Map ::", postDataMap.toString());

                    // EventDTO event = new EventDTO(0, sEventName, sDescription, "somik", sGroupName, LocalDateTime.now().toString(), sEventDate);
                    APICommunication apiComm = new APICommunication(token);
                    apiComm.createEvent(postDataMap);

                    String reply = apiComm.getResult();
                    Log.d(">>> EVENT CREATED REPLY CHANGED <<<", reply);
                    if (reply != null && reply.length() > 10) {
                        EventDTO eventDTO = apiComm.jsonToObj(EventDTO.class);
                        if (eventDTO.getName().equals(sEventName)) {
                            dialogInterface.dismiss();
                            getEvents();
                            initEventList();
                            Log.d(">>> EVENT CREATED <<<", reply);
                            displayMessage("Event created");
                        } else
                            displayMessage(reply);
                    } else
                        displayMessage("No reply from server");
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void launchViewEventDialog(@NonNull EventDTO event, int userId) {
        editEventName = customAlertDialogView.findViewById(R.id.event_name);
        editDescription = customAlertDialogView.findViewById(R.id.description);
        editGroupName = customAlertDialogView.findViewById(R.id.group_name);
        editEventDate = customAlertDialogView.findViewById(R.id.event_date);

        editEventName.setText(event.getName());
        editDescription.setText(event.getDescription());
        editGroupName.setText(event.getGroupName());
        editEventDate.setText(event.getEventTime().replace('T', ' '));

        String joinBtnTxt = "Join Event";
        List<String> participants = Arrays.asList(event.getParticipantName());
        if (participants.contains(username))
            joinBtnTxt = "Already Joined";

        // Building the Alert dialog using materialAlertDialogBuilder instance
        materialAlertDialogBuilder.setView(customAlertDialogView)
                .setTitle("Event Details")
                .setMessage("You can join this event if you haven't done so.")
                .setPositiveButton(joinBtnTxt, (dialogInterface, i) -> {
                    APICommunication api = new APICommunication(token);
                    api.userAddEvent(userId, event.getId());
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void displayMessage(String message) {
        Toast.makeText(EventActivity.this, message, Toast.LENGTH_LONG).show();
    }
}