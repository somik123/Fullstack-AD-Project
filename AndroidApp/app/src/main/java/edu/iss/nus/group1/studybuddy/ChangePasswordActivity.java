package edu.iss.nus.group1.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import edu.iss.nus.group1.studybuddy.utility.APICommunication;

public class ChangePasswordActivity extends AppCompatActivity {

    protected EditText txtPassword;
    protected EditText txtNewPassword;
    protected EditText txtReNewPassword;
    protected Button btnChangePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        txtPassword = findViewById(R.id.txtPasswordResetPassword);
        txtNewPassword = findViewById(R.id.txtPasswordResetPasswordNew);
        txtReNewPassword = findViewById(R.id.txtPasswordResetPasswordNew2);

        btnChangePass = findViewById(R.id.btnChangePass);

        btnChangePass.setOnClickListener(view -> {
            String password = txtPassword.getText().toString();
            String newPassword = txtNewPassword.getText().toString();
            String reNewPassword = txtReNewPassword.getText().toString();
            //TBD

            if (!newPassword.equals(reNewPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            } else {
                // Receive intent
                Intent intent = getIntent();
                Integer userId = intent.getIntExtra("userId", -1);
                String token = intent.getStringExtra("token");

                if (userId != -1) {
                    Map<String, Object> postDataMap = new HashMap<>();
                    postDataMap.put("userId", userId);
                    postDataMap.put("currentPassword", password);
                    postDataMap.put("updatePassword", newPassword);

                    APICommunication apiComm = new APICommunication(token);
                    apiComm.changePassword(postDataMap);
                    String reply = apiComm.getResult();

                    if (reply != null && reply.contains("User password updated")) {
                        // Return result from Login activity
                        Intent response = new Intent();
                        setResult(RESULT_OK, response);
                        finish();
                    }
                } else {
                    Log.d(">> ERROR ::", "NO USER ID");
                }
            }
        });

    }
}