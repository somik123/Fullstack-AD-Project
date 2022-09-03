package edu.iss.nus.group1.studybuddy.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import edu.iss.nus.group1.studybuddy.ChatActivity;
import edu.iss.nus.group1.studybuddy.MainActivity;
import edu.iss.nus.group1.studybuddy.R;
import edu.iss.nus.group1.studybuddy.dto.GroupDTO;
import edu.iss.nus.group1.studybuddy.dto.MessageDTO;
import edu.iss.nus.group1.studybuddy.utility.APICommunication;

public class PollingService extends Service {

    protected static final int autoRefreshInterval = 5000; //put here time 1000 milliseconds=1 second

    protected Integer processID;

    protected Integer userId;
    protected String token;

    protected APICommunication apiComm;

    protected ArrayList<GroupDTO> groups;
    protected Map<Integer, List<MessageDTO>> messages = new HashMap<>();

    protected static final String CHANNEL_ID = "StudyBuddyService";
    protected static final String CHANNEL_NAME = "StudyBuddy Notification Channel";
    protected static final String CHANNEL_DESCRIPTION = "This channel is for displaying messages from StudyBuddy";
    protected static final Integer SERVICE_NOTIFICATION_ID = 9769223;

    public PollingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {

        String action = intent.getAction();
        if (action != null) {
            token = intent.getStringExtra("token");
            userId = intent.getIntExtra("userId", -1);
            processID = intent.getIntExtra("processID", 0);

            if (processID != 0 && action.equals("run")) {

                // Create channel
                createNotificationChannel();
                Notification notification = createNotificationForService();

                System.out.println("notification.toString()");
                System.out.println(notification.toString());

                startForeground(SERVICE_NOTIFICATION_ID, notification);

                apiComm = new APICommunication(token);
                getGroups();
                if (groups != null) {
                    // Load current chat messages into list to reset notifications
                    for (GroupDTO g : groups) {
                        int groupId = g.getId();
                        getChats(groupId);
                    }

                    // Start background polling
                    timedUpdate();
                }
            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        processID = -1;
        super.onDestroy();
    }


    protected void getGroups() {
        apiComm.getGroups(userId);

        String reply = apiComm.getResult();

        if (reply == null) return;
        Log.d(">>>> FROM BG SERVICE <<<< ::", LocalDateTime.now().toString() + " -> " + reply);

        ArrayList<GroupDTO> groupList = apiComm.jsonToArrayList(GroupDTO.class);
        if (groupList != null) {
            for (GroupDTO g : groupList) {
                ArrayList<MessageDTO> messageList = new ArrayList<>();
                messages.put(g.getId(), messageList);
            }
        }

        groups = groupList;
    }


    protected void getChats(Integer groupId) {
        //Log.d(">> GROUP ID ::", String.valueOf(groupId));
        apiComm.getMessages(groupId);

        String reply = apiComm.getResult();
        //Log.d(">> REPLY ::", reply);

        if (reply != null && reply.length() > 10) {
            List<MessageDTO> messageList = apiComm.jsonToArrayList(MessageDTO.class);
            //Log.d(">> messageList ::", messageList.toString());

            // Only get messages by others
            List<MessageDTO> messageListFiltered = messageList.stream().filter(m -> {
                if (m != null && m.getUserId() != null && userId != null)
                    return !m.getUserId().equals(userId);
                else
                    return false;
            }).collect(Collectors.toList());
            //Log.d(">> messageListFiltered ::", messageListFiltered.toString());
            messages.replace(groupId, messageListFiltered);
        }
    }


    protected void timedUpdate() {

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (processID < 10) return;
                Map<Integer, List<MessageDTO>> messagesOld = new HashMap<>(messages);

                // update groups
                getGroups();

                if (groups != null) {
                    // Load current chat messages into list to reset notifications
                    for (GroupDTO g : groups) {
                        int groupId = g.getId();

                        // Update messages
                        getChats(groupId);

                        if (messagesOld != null && messages != null && messages.get(groupId) != null) {

                            String lastOldMessage = "";
                            if (messagesOld.containsKey(groupId) && messagesOld.get(groupId) != null && messagesOld.get(groupId).size() > 0) {
                                int lastIndex = messagesOld.get(groupId).size() - 1;
                                MessageDTO messageDTO = messagesOld.get(groupId).get(lastIndex);
                                lastOldMessage = messageDTO.getTime();
                            }

                            String message = null;
                            String lastNewMessage = "";
                            if (messages.containsKey(groupId) && messages.get(groupId) != null && messages.get(groupId).size() > 0) {
                                int lastIndex = messages.get(groupId).size() - 1;
                                MessageDTO messageDTO = messages.get(groupId).get(lastIndex);
                                lastNewMessage = messageDTO.getTime();
                                message = messageDTO.getMessage();
                            }

                            if (!lastNewMessage.equals(lastOldMessage)) {
                                String groupName = g.getName();
                                createNotificationForChat(groupId, groupName, message);
                                System.out.println("\n\nNOTIFICATION TRIGGERED!\n\n");
                            }
                        }
                    }
                }
            }
        }, autoRefreshInterval, autoRefreshInterval);
    }

    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
        channel.setDescription(CHANNEL_DESCRIPTION);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void createNotificationForChat(int groupId, String groupName, String message) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupName", groupName);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        String title = "New message in " + groupName;

        if (message == null) return;

        if (message.startsWith("[img"))
            message = "Sent a IMAGE";
        else if (message.startsWith("[url"))
            message = "Sent a FILE";
        else if (message.startsWith("[vid"))
            message = "Sent a VIDEO";
        else if (message.length() > 20)
            message = message.substring(0, 18) + "...";

        builder
                .setSmallIcon(R.drawable.chat_btn)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        int notificationId = 100000 + groupId;
        NotificationManagerCompat mgr = NotificationManagerCompat.from(this);
        Notification notification = builder.build();
        mgr.notify(notificationId, notification);
    }

    protected Notification createNotificationForService() {
        // the intent to invoke when our notification (on the status bar)
        // is touched. here, we want to display our Main Activity.
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Study Buddy Service")
                .setContentText("Checking for new messages")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pendingIntent)
                .build();
    }

}