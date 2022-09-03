package edu.iss.nus.group1.studybuddy;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import edu.iss.nus.group1.studybuddy.dto.FileDTO;
import edu.iss.nus.group1.studybuddy.dto.UserDTO;
import edu.iss.nus.group1.studybuddy.utility.APICommunication;

public class ProfileActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> resultLauncherLogin;
    ActivityResultLauncher<Intent> resultLauncherChangePass;
    ActivityResultLauncher<Intent> resultLauncherChangeInterest;

    protected Button btnProfileLogout;
    protected Button btnProfileChangePass;
    protected Button btnProfileUpdateInterest;
    protected ImageView imgProfilePhoto;

    protected String token;
    protected int userId;
    protected String[] interestArr;

    protected int SELECT_PICTURE = 1204710;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Read
        SharedPreferences pref = getSharedPreferences("user_credentials", MODE_PRIVATE);
        userId = pref.getInt("userId", -1);
        token = pref.getString("token", null);

        // Register activity result
        resultLauncherLogin = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Receive result and process it
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();
                        token = data.getStringExtra("token");
                        userId = data.getIntExtra("userId", -1);
                        showProfileData();
                    }
                });

        // Register activity result
        resultLauncherChangePass = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Receive result and process it
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        // Logout user
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.apply();

                        // Call Login activity
                        Intent intent = new Intent(this, LoginActivity.class);
                        resultLauncherLogin.launch(intent);
                    }
                });

        // Register activity result
        resultLauncherChangeInterest = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Receive result and process it
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        //Update interests after it has been changed
                        showProfileData();
                    }
                });

        btnProfileLogout = findViewById(R.id.btnProfileLogout);
        btnProfileLogout.setOnClickListener(view -> {
            // Logout user
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();

            // Call Login activity
            Intent intent = new Intent(this, LoginActivity.class);
            resultLauncherLogin.launch(intent);
        });

        btnProfileChangePass = findViewById(R.id.btnProfileChangePass);
        btnProfileChangePass.setOnClickListener(view -> {
            // Call change password activity
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("token", token);
            resultLauncherChangePass.launch(intent);
        });

        btnProfileUpdateInterest = findViewById(R.id.btnProfileChangeInterest);
        btnProfileUpdateInterest.setOnClickListener(view -> {
            // Call change interest activity
            Intent intent = new Intent(this, ChangeInterestActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("token", token);
            intent.putExtra("interestArr", interestArr);
            resultLauncherChangeInterest.launch(intent);
        });

        imgProfilePhoto = findViewById(R.id.imgProfilePhoto);

        imgProfilePhoto.setOnClickListener(view -> imageChooser());

        showProfileData();
    }


    protected void showProfileData() {
        TextView txtProfileInterests = findViewById(R.id.txtProfileInterests);
        TextView txtProfileEmail = findViewById(R.id.txtProfileEmail);
        TextView txtProfileUsername = findViewById(R.id.txtProfileUsername);
        TextView txtProfileName = findViewById(R.id.txtProfileName);

        APICommunication apiComm = new APICommunication(token);
        apiComm.getUserDetails(userId);
        String result = apiComm.getResult();
        //Log.d(">> RESULT ::", result);

        if (result != null && result.length() > 10) {
            UserDTO userDetails = apiComm.jsonToObj(UserDTO.class);
            Log.d(">> RESULT OBJ ::", userDetails.toString());

            interestArr = userDetails.getInterest();

            txtProfileName.setText(userDetails.getName());
            txtProfileUsername.setText(userDetails.getUsername());
            txtProfileEmail.setText(userDetails.getEmail());
            String interests = String.join(", ", userDetails.getInterest());
            if (!interests.isEmpty()) {
                txtProfileInterests.setText(interests);
            } else {
                txtProfileInterests.setText("None");
            }

            if (userDetails.getPhoto() != null && userDetails.getPhoto().length() > 10) {
                Glide
                        .with(this)
                        .load(userDetails.getPhoto())
                        .into(imgProfilePhoto);
            }
        }
    }


    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }


    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();

                if (selectedImageUri != null) {

                    Toast.makeText(this, "Uploading image in progress...", Toast.LENGTH_LONG).show();

                    String logTag = ">>>>>>> TEST <<<<<<<<";
                    Log.d(logTag, selectedImageUri.toString());
                    Log.d(logTag, selectedImageUri.getPath());

                    new Thread(() -> {

                        String fileName = "upload." + getMimeType(selectedImageUri);
                        Log.d(logTag, fileName);

                        // update the preview image in the layout
                        //imgProfilePhoto.setImageURI(selectedImageUri);
                        Log.d(logTag, selectedImageUri.toString());

                        InputStream inputStream;
                        try {

                            inputStream = getContentResolver().openInputStream(selectedImageUri);

                            APICommunication apiComm = new APICommunication(token);
                            apiComm.uploadFile(inputStream, fileName);
                            String result = apiComm.getResult();

                            if (result != null && result.length() > 10) {
                                Log.d(">> RESULT ::", result);
                                FileDTO fileDTO = apiComm.jsonToObj(FileDTO.class);

                                Map<String, Object> postDataMap = new HashMap<>();
                                postDataMap.put("id", userId);
                                postDataMap.put("photo", fileDTO.getUrl());

                                apiComm.updateUserPhoto(postDataMap);

                                runOnUiThread(this::showProfileData);
                            }
                        } catch (Exception e) {
                            Log.d(logTag, "File not found: " + e);
                        }
                    }).start();

                }
            }
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
        return extension;
    }


}