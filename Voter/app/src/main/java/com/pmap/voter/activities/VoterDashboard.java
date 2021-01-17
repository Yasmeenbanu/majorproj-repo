package com.pmap.voter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.pmap.voter.R;
import com.pmap.voter.VoterCandidate;

public class VoterDashboard  extends AppCompatActivity{


        private Context context;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.voter_dashboard);
            context = VoterDashboard.this;
            Button btn_caste_vote = (Button) findViewById(R.id.btn_caste_vote);
            Button btn_viewresult1 = (Button) findViewById(R.id.btn_viewresult1);
            btn_caste_vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* if (!P.isValidEditText(edt_username, "Username")) return;
                if (!P.isValidEditText(edt_password, "Password")) return;*/


                    Intent intent = new Intent(context, CandidateListActivity.class);
                    startActivity(intent);
                }

            });

            btn_viewresult1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* if (!P.isValidEditText(edt_username, "Username")) return;
                if (!P.isValidEditText(edt_password, "Password")) return;*/


                    Intent intent = new Intent(context, ElectionResultActivity.class);
                    startActivity(intent);
                }

            });


        }
    }





