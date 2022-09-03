package edu.iss.nus.group1.studybuddy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class FinalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        this.finish();
        System.exit(0);
    }
}