/**
 * Copyright (C) futuretek AG 2016
 * All Rights Reserved
 *
 * @author Artan Veliju
 */
package survey.android.futuretek.ch.ft_survey;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class FinishActivity extends BaseActivity {
    private Button nextBtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));


        nextBtn = (Button) findViewById(R.id.nextBtn);
        nextBtn.setText("Finish!");
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finishAffinity();

                System.exit(0);
            }
        });
    }

    protected void onResume() {
        super.onResume();

        List<String> textArray;

        List<String> skills = getDatabase().getAllSkills();

        int count = skills.size();

        textArray = new ArrayList<>();
        textArray.add("Thank you for participation in Futuretek survey!");
        textArray.add("At Futuretek we are facing multiple challenges in order to bring the best, innovative solutions.");
        textArray.add("This requites talented people who want to be a part of our mission.");
        textArray.add("It looks that you have added " + skills.size() + " skills!");
        if (count < 2) {
            textArray.add("Do you have what it takes to join us?");
        } else if (count > 5) {
            textArray.add("You are a wizard!");
        } else {
            textArray.add("You certainly look like a promising developer!");
        }

        textArray.add("We will get back in touch with you shortly!");

        animateText(textArray, new AnimationListDone() {
            public void done() {
                Button nextBtn = ((Button) findViewById(R.id.nextBtn));
                nextBtn.setTextColor(Color.GREEN);
                nextBtn.setEnabled(true);
            }
        });
    }
}