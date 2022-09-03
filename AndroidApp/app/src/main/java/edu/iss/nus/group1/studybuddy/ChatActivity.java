package edu.iss.nus.group1.studybuddy;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import edu.iss.nus.group1.studybuddy.adapters.MessageListAdapterNew;
import edu.iss.nus.group1.studybuddy.dto.FileDTO;
import edu.iss.nus.group1.studybuddy.dto.MessageDTO;
import edu.iss.nus.group1.studybuddy.utility.APICommunication;
import edu.iss.nus.group1.studybuddy.widget.GifEditText;

public class ChatActivity extends AppCompatActivity implements GifEditText.ImageSelectedCallback {

    protected static final int autoRefreshInterval = 3000; //put here time 1000 milliseconds=1 second

    protected ArrayList<MessageDTO> messages = new ArrayList<>();
    protected RecyclerView mMessageRecycler;
    protected MessageListAdapterNew mMessageAdapter;

    protected PopupWindow popupWindow;

    protected GifEditText txtChatMsg;
    protected ImageView imgChatAttach;
    protected ImageView imgChatSend;
    protected TextView txtChatGroupName;

    protected Integer groupId;
    public Integer userId;
    public String username;
    public String token;


    protected int SELECT_IMAGE = 1204710;
    protected int SELECT_VIDEO = 7896651;
    protected int SELECT_FILE = 6546465;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Receive intent
        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupId", -1);
        String groupName = intent.getStringExtra("groupName");

        // Read
        SharedPreferences pref = getSharedPreferences("user_credentials", MODE_PRIVATE);
        userId = pref.getInt("userId", -1);
        username = pref.getString("username", null);
        token = pref.getString("token", null);

        // First load
        getChats();
        if (mMessageAdapter == null) {
            initChatList();
        } else {
            mMessageAdapter.setMessageList(messages);
            mMessageAdapter.notifyDataSetChanged();
        }
        scrollToBottom();


        txtChatMsg = findViewById(R.id.txtChatMsg);
        txtChatMsg.setImageSelectedCallback(this);

        imgChatAttach = findViewById(R.id.imgChatAttach);
        imgChatSend = findViewById(R.id.imgChatSend);
        txtChatGroupName = findViewById(R.id.txtChatGroupName);

        txtChatGroupName.setText(String.format("%s  %s", getString(R.string.group), groupName));

        imgChatAttach.setOnClickListener(view -> {
            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.chat_popup, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            popupWindow = new PopupWindow(popupView, width, height, focusable);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window token
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, -200);

            Button btnChatPopupImage = popupView.findViewById(R.id.btnChatPopupImage);
            Button btnChatPopupVideo = popupView.findViewById(R.id.btnChatPopupVideo);
            Button btnChatPopupFile = popupView.findViewById(R.id.btnChatPopupFile);

            btnChatPopupImage.setOnClickListener(view1 -> {
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(Intent.createChooser(intent1, "Select Picture"), SELECT_IMAGE);
            });

            btnChatPopupVideo.setOnClickListener(view1 -> {
                Intent intent1 = new Intent();
                intent1.setType("video/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(Intent.createChooser(intent1, "Select Video"), SELECT_VIDEO);
            });

            btnChatPopupFile.setOnClickListener(view1 -> {
                Intent intent1 = new Intent();
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                intent1.setType("*/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(Intent.createChooser(intent1, "Select File"), SELECT_FILE);
            });

        });

        imgChatSend.setOnClickListener(view -> submitMessage());

        txtChatMsg.setOnClickListener(view -> {
            System.out.println("EDIT VIEW CLICKED");
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                //Do something here
                System.out.println("SCROLL TO BOTTOM");
                scrollToBottom();
            }, 1500);
        });


        txtChatMsg.setOnFocusChangeListener((view, b) -> {
            System.out.println("EDIT VIEW FOCUSED");
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                //Do something here
                System.out.println("SCROLL TO BOTTOM");
                scrollToBottom();
            }, 1500);
        });

        txtChatMsg.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press
                submitMessage();
                return true;
            }
            return false;
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ArrayList<MessageDTO> messagesOld = new ArrayList<>(messages);
                getChats();

                if (messages != null && messages.size() > messagesOld.size()) {
                    runOnUiThread(() -> {
                        //update TextView here
                        if (mMessageAdapter == null) {
                            initChatList();
                        } else {
                            mMessageAdapter.setMessageList(messages);
                            System.out.println("REFRESH VIEW IN PROGRESS");
                            mMessageAdapter.notifyDataSetChanged();
                        }
                        scrollToBottom();
                    });
                }
            }
        }, autoRefreshInterval, autoRefreshInterval);


        /* Not needed with fixed webview height
        // Delayed scroll to bottom
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            //Do something here
            System.out.println("SCROLL TO BOTTOM");
            scrollToBottom();
        }, 5000);

        */
    }

    private void submitMessage() {
        // Read
        String message = txtChatMsg.getText().toString();

        new Thread(() -> {
            int replyTo = -1; // Disabled
            if (message.trim().length() == 0) return;

            APICommunication apiComm = new APICommunication(token);
            apiComm.sendMessage(message, userId, groupId, replyTo);
            String reply = apiComm.getResult();
            //Log.d(">> REPLY ::", reply);
            if (reply.equals("Message Saved")) {

                runOnUiThread(() -> {
                    txtChatMsg.setText("");

                    getChats();
                    if (mMessageAdapter == null) {
                        initChatList();
                    } else {
                        mMessageAdapter.setMessageList(messages);
                        mMessageAdapter.notifyDataSetChanged();
                    }
                    scrollToBottom();
                });
            }
        }).start();


    }

    protected void getChats() {

        //Log.d(">> GROUP ID ::", String.valueOf(groupId));

        APICommunication apiComm = new APICommunication(token);
        apiComm.getMessages(groupId);

        String reply = apiComm.getResult();
        //Log.d(">> REPLY ::", reply);

        if (reply != null && reply.length() > 10) {
            //Log.d(">> TOKEN ::", messageList.toString());
            messages = apiComm.jsonToArrayList(MessageDTO.class);
        }
    }

    protected void initChatList() {

        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        if (mMessageRecycler != null) {
            mMessageAdapter = new MessageListAdapterNew(this, messages, userId);
            mMessageAdapter.setOnItemClickListener(new MessageListAdapterNew.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    // Do something
                    MessageDTO message = messages.get(position);

                    if (message.getMessage().length() > 5) {
                        String type = message.getMessage().substring(1, 4);
                        if (type.equals("url")) {
                            String fileUrl = message.getMessage().substring(5, message.getMessage().length() - 1);

                            Uri uri = Uri.parse(fileUrl);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onItemLongClick(int position, View v) {
                    MessageDTO message = messages.get(position);
                    setClipboard(message.getMessage());
                    Toast toast = Toast.makeText(ChatActivity.this, "Message copied", Toast.LENGTH_SHORT);
                    toast.show();

                }
            });
            mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
            mMessageRecycler.setAdapter(mMessageAdapter);

        }
    }

    // Scroll to bottom. To do on refresh
    protected void scrollToBottom() {
        if (mMessageAdapter != null && mMessageAdapter.getItemCount() > 0) {
            mMessageRecycler.post(() -> {
                //mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                mMessageRecycler.scrollToPosition(mMessageAdapter.getItemCount() - 1);
            });
        }
    }


    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                Uri selectedImageUri = data.getData();
                uploadFileAndGetLink(selectedImageUri, "img");
            } else if (requestCode == SELECT_VIDEO) {
                Uri selectedImageUri = data.getData();
                uploadFileAndGetLink(selectedImageUri, "vid");
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                uploadFileAndGetLink(selectedImageUri, "url");
            }
        }
    }

    protected void uploadFileAndGetLink(Uri uri, String type) {
        String logTag = ">>>>>>> TEST <<<<<<<<";
        if (null != uri) {
            Log.d(logTag, uri.toString());
            Log.d(logTag, uri.getPath());

            popupWindow.dismiss();
            Toast.makeText(this, "Uploading file in progress...", Toast.LENGTH_LONG).show();

            new Thread(() -> {
                String fileName = "upload." + getMimeType(uri);
                Log.d(logTag, fileName);

                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(uri);

                    APICommunication apiComm = new APICommunication(token);
                    apiComm.uploadFile(inputStream, fileName);
                    String result = apiComm.getResult();

                    if (result != null && result.length() > 10) {
                        Log.d(">> RESULT ::", result);
                        FileDTO fileDTO = apiComm.jsonToObj(FileDTO.class);

                        String message = "[" + type + "=" + fileDTO.getUrl() + "]";

                        apiComm.sendMessage(message, userId, groupId, 0);
                        String reply = apiComm.getResult();
                        Log.d(logTag, reply);

                        runOnUiThread(() -> {
                            Toast.makeText(ChatActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                        });
                    }

                } catch (Exception e) {
                    Log.d(logTag, "File not found: " + e);
                }
            }).start();


        }
    }

    public String getMimeType(Uri uri) {
        String extension;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(this.getContentResolver().getType(uri));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        if (extension == null) return "mp4";

        return extension;
    }


    private void setClipboard(String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onImageSelected(String message, String mimeType) {
        submitMessage();
    }
}