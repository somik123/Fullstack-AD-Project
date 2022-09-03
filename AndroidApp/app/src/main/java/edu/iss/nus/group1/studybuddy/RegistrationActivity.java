package edu.iss.nus.group1.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import edu.iss.nus.group1.studybuddy.utility.APICommunication;

public class RegistrationActivity extends AppCompatActivity {

    protected EditText txtUsername;
    protected EditText txtName;
    protected EditText txtEmail;
    protected EditText txtPassword;
    protected EditText txtRePassword;

    protected Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        txtUsername = findViewById(R.id.txtRegisterUserName);
        txtName = findViewById(R.id.txtRegisterName);
        txtEmail = findViewById(R.id.txtRegisterUserEmail);
        txtPassword = findViewById(R.id.txtRegisterUserPassword);
        txtRePassword = findViewById(R.id.txtRegisterUserPassword2);
        btnSubmit = findViewById(R.id.RegisterSubmit);

        btnSubmit.setOnClickListener(view -> {
            String username = txtUsername.getText().toString();
            String name = txtName.getText().toString();
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            String rePassword = txtRePassword.getText().toString();

            if (!rePassword.equals(password)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            } else {
                Map<String, Object> postDataMap = new HashMap<>();
                postDataMap.put("username", username);
                postDataMap.put("password", password);
                postDataMap.put("name", name);
                postDataMap.put("email", email);
                postDataMap.put("photo", "");

                APICommunication apiComm = new APICommunication(null);
                apiComm.registerUser(postDataMap);
                String reply = apiComm.getResult();

                if (reply != null && reply.contains(email)) {
                    // Return result from Login activity
                    Intent response = new Intent();
                    response.putExtra("username", username);
                    response.putExtra("password", password);
                    setResult(RESULT_OK, response);
                    finish();
                }
            }

        });

    }
}