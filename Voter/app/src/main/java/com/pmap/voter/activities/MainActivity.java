package com.pmap.voter.activities;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pmap.voter.R;

public class MainActivity extends AppCompatActivity {
    private Context context;
  //  private String el_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        Button btn_admin = (Button) findViewById(R.id.btn_admin);
        Button btn_user = (Button) findViewById(R.id.btn_user);
        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (!P.isValidEditText(edt_username, "Username")) return;
                if (!P.isValidEditText(edt_password, "Password")) return;*/


                Intent intent = new Intent(context, UserLoginActivity.class);
                intent.putExtra("who","admin");
                startActivity(intent);
            }

        });
        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (!P.isValidEditText(edt_username, "Username")) return;
                if (!P.isValidEditText(edt_password, "Password")) return;*/
                Intent intent = new Intent(context, UserLoginActivity.class);
                intent.putExtra("who","user");
               startActivity(intent);
            }

        });
     /*   Intent intent = new Intent(context, CandidatesActivity.class);
        startActivity(intent);*/
    }
}

