package com.pmap.voter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pmap.voter.Api.Api;
import com.pmap.voter.Api.WebServices;
import com.pmap.voter.logic.P;
import com.pmap.voter.models.ElectionNameInput;
import com.pmap.voter.models.ElectionNameOutput;
import com.pmap.voter.models.VoterRegisterInput;
import com.pmap.voter.models.VoterRegisterOutput;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VoterCandidate extends AppCompatActivity{
    @BindView(R.id.edt_ele_name)
    public
    EditText edt_ele_name;

    @BindView(R.id.btn_submit)
    public
    Button btn_submit;

    @BindView(R.id.btn_voters)
    public
    Button btn_voters;
    @BindView(R.id.btn_candidates)
    public
    Button btn_candidates;


    private Context context;

    private String el_id="";
    private SweetAlertDialog dialog;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.votercandidate);
            ButterKnife.bind(this);
            context = VoterCandidate.this;



            btn_voters.setVisibility(View.GONE);
            btn_candidates.setVisibility(View.GONE);
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!P.isValidEditText(edt_ele_name, "ElectionName")) return;

                    createelection();


/* Intent intent = new Intent(VoterCandidate.this, SecondActivity.class);
                    startActivity(intent);*/

                }

            });
            btn_voters.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VoterActivity.class);
                    intent.putExtra("el_id",el_id);
                    startActivity(intent);
                }
            });
            btn_candidates.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CandidatesActivity.class);
                    intent.putExtra("el_id",el_id);
                    startActivity(intent);
                }
            });


        }
    private void createelection() {
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        ElectionNameInput createelectionInput = new ElectionNameInput(
                edt_ele_name.getText().toString().trim()

        );
        dialog = P.showBufferDialog(context, "Processing...");
        //btn_login.setProgress(1);
        btn_submit.setEnabled(false);
        P.hideSoftKeyboard(VoterCandidate.this);


        //CALL NOW
        webServices.createelection(createelectionInput)
                .enqueue(new Callback<ElectionNameOutput>() {
                    @Override
                    public void onResponse(Call<ElectionNameOutput> call, Response<ElectionNameOutput> response) {
                        if (dialog.isShowing()) dialog.dismiss();
                        if (!P.analyseResponse(response)) {
                            //btn_login.setProgress(0);
                            btn_submit.setEnabled(true);
                            //Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                            P.ShowErrorDialogAndBeHere(context, "Error", "Server Error");


                            return;
                        }
                        ElectionNameOutput result = response.body();
                        if (result.is_success) {
                            P.ShowSuccessDialogAndBeHere(context, "Success", result.msg);
                            btn_voters .setVisibility(View.VISIBLE);
                            btn_candidates .setVisibility(View.VISIBLE);
                            el_id=result.el_id;
                        } else {
                            // btn_login.setProgress(0);
                            btn_submit.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, "Error", result.msg);
                            //Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ElectionNameOutput> call, Throwable t) {
                        P.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();
                        if (dialog.isShowing()) dialog.dismiss();
                        // btn_login.setProgress(0);

                        btn_submit.setEnabled(true);
                        P.ShowErrorDialogAndBeHere(context, "Error", "Check Internet Connection");
                    }
                });
    }
}