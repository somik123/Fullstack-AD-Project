package edu.iss.nus.group1.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iss.nus.group1.studybuddy.dto.InterestDTO;
import edu.iss.nus.group1.studybuddy.utility.APICommunication;

public class ChangeInterestActivity extends AppCompatActivity {

    protected Integer userId;
    protected String token;
    protected String[] interestArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_interest);

        // Receive intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        token = intent.getStringExtra("token");
        interestArr = intent.getStringArrayExtra("interestArr");

        APICommunication apiCommunication = new APICommunication(token);
        apiCommunication.getAllInterest();
        ArrayList<InterestDTO> interests = apiCommunication.jsonToArrayList(InterestDTO.class);
        String result = apiCommunication.getResult();

        Button button = findViewById(R.id.btnChangeInterest);
        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        if (result != null) {
            listAllInterest(interests, rootLayout);
        } else {
            Log.d("ERROR", "onCreate: no interest found");
        }

        button.setOnClickListener(view -> {
            Log.d("userId", userId.toString());
            if (userId != -1) {
                List<Integer> interestSelected = new ArrayList<>();
                ViewGroup viewgroup = (ViewGroup) rootLayout;
                for (int i = 0; i < viewgroup.getChildCount(); i++) {
                    View v1 = viewgroup.getChildAt(i);
                    if (v1 instanceof CheckBox) {
                        CheckBox box = (CheckBox) v1;
                        if (box.isChecked()) {
                            interestSelected.add(box.getId());
                        }
                    }
                }
                Log.d("selected", interestSelected.toString());

                Map<String, Object> postDataMap = new HashMap<>();
                postDataMap.put("userId", userId);
                postDataMap.put("interests", interestSelected);
                APICommunication api = new APICommunication(token);
                api.updateInterest(postDataMap);
            }
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    protected void listAllInterest(ArrayList<InterestDTO> interests, LinearLayout rootLayout) {
        for (InterestDTO interest : interests) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(interest.getName());
            checkBox.setTextSize(20);
            checkBox.setId(interest.getId());
            if (ArrayUtils.contains(interestArr, interest.getName()))
                checkBox.setChecked(true);
            rootLayout.addView(checkBox);
        }
    }
}