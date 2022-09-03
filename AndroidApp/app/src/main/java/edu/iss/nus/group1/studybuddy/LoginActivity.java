package edu.iss.nus.group1.studybuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import edu.iss.nus.group1.studybuddy.dto.TokenDTO;
import edu.iss.nus.group1.studybuddy.utility.APICommunication;

public class LoginActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> resultLauncherRegister;

    protected EditText txtLoginUser;
    protected EditText txtLoginPass;
    protected Button btnLogin;
    protected Button btnLoginRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtLoginUser = findViewById(R.id.txtLoginUser);
        txtLoginPass = findViewById(R.id.txtLoginPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginRegister = findViewById(R.id.btnLoginRegister);

// Register activity result
        resultLauncherRegister = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Receive result and process it
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();

                        String username = data.getStringExtra("username");
                        String password = data.getStringExtra("password");

                        txtLoginUser.setText(username);
                        txtLoginPass.setText(password);
                    }
                });

        btnLoginRegister.setOnClickListener(view -> {
            // Call Register activity
            Intent intent = new Intent(this, RegistrationActivity.class);
            resultLauncherRegister.launch(intent);
        });

        btnLogin.setOnClickListener(view -> {
            String user = txtLoginUser.getText().toString();
            String pass = txtLoginPass.getText().toString();

            if (user.length() > 0 && pass.length() > 0) {
                // Do login action here and set API key
                APICommunication apiComm = new APICommunication(null);
                apiComm.login(user, pass);

                String reply = apiComm.getResult();

                if (reply == null) {
                    Toast.makeText(this, "Wrong username or password"
                            , Toast.LENGTH_LONG).show();
                    return;
                }

                Log.d(">> REPLY ::", reply);

                TokenDTO tokenDTO = apiComm.jsonToObj(TokenDTO.class);
                String token = tokenDTO.getToken();

                Log.d(">> TOKEN ::", token);
                Log.d(">> TOKEN ::", tokenDTO.toString());

                if (token != null && token.length() > 10) {

                    // Write
                    SharedPreferences pref = getSharedPreferences("user_credentials", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("name", tokenDTO.getDisplayName());
                    editor.putInt("userId", tokenDTO.getUserId());
                    editor.putString("username", tokenDTO.getUserName());
                    editor.putString("photo", tokenDTO.getPhoto());
                    editor.putString("token", token);
                    editor.apply();

                    // Return result from Login activity
                    Intent response = new Intent();
                    response.putExtra("username", tokenDTO.getUserName());
                    response.putExtra("userId", tokenDTO.getUserId());
                    response.putExtra("token", token);
                    setResult(RESULT_OK, response);
                    finish();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, FinalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}