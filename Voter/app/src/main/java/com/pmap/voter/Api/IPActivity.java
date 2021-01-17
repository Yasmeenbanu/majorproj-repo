package com.pmap.voter.Api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pmap.voter.R;
import com.pmap.voter.logic.P;

public class IPActivity  extends AppCompatActivity {


        EditText edt_ip;
        TextView txt_ip;
        Button btn_change;
        public static final String MyPREFERENCES = "AcademicBank" ;
        SharedPreferences sharedpreferences;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ip);
            edt_ip = (EditText) findViewById(R.id.edt_ip);
            txt_ip = (TextView) findViewById(R.id.txt_ip);
            btn_change = (Button) findViewById(R.id.btn_change);
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String url=sharedpreferences.getString("url", P.URL);
            txt_ip.setText(url);
            edt_ip.setText(url);
            btn_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("url", edt_ip.getText().toString().trim());
                    txt_ip.setText(edt_ip.getText().toString().trim());
                    P.URL=edt_ip.getText().toString().trim();
                    editor.commit();
                }
            });
            //http://192.168.43.60:81/academic_bank/
        }
    }


