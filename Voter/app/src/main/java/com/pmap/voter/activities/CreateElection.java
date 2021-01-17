package com.pmap.voter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.pmap.voter.R;
import com.pmap.voter.VoterCandidate;

public class CreateElection extends AppCompatActivity {


        private Context context;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.create_election);
            context = CreateElection.this;
            Button button = (Button) findViewById(R.id.btn_cre_ele);
            Button btn_viewresult = (Button) findViewById(R.id.btn_viewresult);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* if (!P.isValidEditText(edt_username, "Username")) return;
                if (!P.isValidEditText(edt_password, "Password")) return;*/


                    Intent intent = new Intent(context, VoterCandidate.class);
                    startActivity(intent);
                }

            });

            btn_viewresult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* if (!P.isValidEditText(edt_username, "Username")) return;
                if (!P.isValidEditText(edt_password, "Password")) return;*/


                    Intent intent = new Intent(context, ElectionListActivity.class);
                    startActivity(intent);
                }

            });


        }
    }



