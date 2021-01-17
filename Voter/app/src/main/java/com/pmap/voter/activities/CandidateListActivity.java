package com.pmap.voter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pmap.voter.Api.Api;
import com.pmap.voter.Api.WebServices;
import com.pmap.voter.CandidateAdapterList;
import com.pmap.voter.R;
import com.pmap.voter.VoterCandidate;
import com.pmap.voter.logic.P;
import com.pmap.voter.models.CandidateDetails;
import com.pmap.voter.models.CandidateListOutput;
import com.pmap.voter.models.CasteVoteInput;
import com.pmap.voter.models.CasteVoteOutput;
import com.pmap.voter.models.ElectionNameInput;
import com.pmap.voter.models.ElectionNameOutput;
import com.pmap.voter.models.VoteResultOutput;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CandidateListActivity extends AppCompatActivity {
    private CandidateAdapterList candidateAdapterList;
    private Context context;
    private RecyclerView rv_candidate_list;
    private SweetAlertDialog dialog;
    private List<CandidateDetails> candidateDetailsList = new ArrayList<>();
    private String elID = "";
    public static int FOR_FINGERPRINT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview_candidate_list);

        context = CandidateListActivity.this;

        rv_candidate_list = (RecyclerView) findViewById(R.id.rv_candidate_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_candidate_list.setLayoutManager(layoutManager);

        getCandidatesList();
    }

    private void getCandidatesList() {
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);


        dialog = P.showBufferDialog(context, "Processing...");
        //btn_login.setProgress(1);
        //btn_add_candidates.setEnabled(false);
        //P.hideSoftKeyboard(CandidateListActivity.this);*/


        //CALL NOW
        webServices.getCandidatesList()
                .enqueue(new Callback<CandidateListOutput>() {
                    @Override
                    public void onResponse(Call<CandidateListOutput> call, Response<CandidateListOutput> response) {
                        if (dialog.isShowing()) dialog.dismiss();
                        if (!P.analyseResponse(response)) {
                            //btn_login.setProgress(0);
                            // btn_add_candidates.setEnabled(true);
                            //Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                            P.ShowErrorDialogAndBeHere(context, "Error", "Server Error");
                            return;
                        }
                        CandidateListOutput result = response.body();
                        if (result.is_success) {
                            candidateDetailsList = result.candidate_list;
                            elID = result.el_id;
                            P.LogD("Tittle: " + elID);
                            candidateAdapterList = new CandidateAdapterList(context, candidateDetailsList, elID);
                            rv_candidate_list.setAdapter(candidateAdapterList);


                        } else {
                            // btn_login.setProgress(0);
                            //  btn_add_candidates.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, "Error", result.msg);
                            //Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<CandidateListOutput> call, Throwable t) {
                        if (dialog.isShowing()) dialog.dismiss();
                        P.displayNetworkErrorMessage(context, null, t);
                        t.printStackTrace();
                        P.ShowErrorDialogAndBeHere(context, getString(R.string.error), getString(R.string.check_internet_connection));
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (data == null) return;
        if (requestCode == CandidateListActivity.FOR_FINGERPRINT) {
            castVote(data.getStringExtra("el_id"),
                    data.getStringExtra("voter_id"),
                    data.getStringExtra("candidate_id"));
        }
    }

    private void castVote(String el_id, String voter_id, String candidate_id) {
        Retrofit retrofit = Api.getRetrofitBuilder(context);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
       CasteVoteInput casteVoteInput = new CasteVoteInput(
             el_id,
               voter_id,
               candidate_id
        );
        dialog = P.showBufferDialog(context, "Processing...");
        //btn_login.setProgress(1);
        //btn_submit.setEnabled(false);
        P.hideSoftKeyboard(CandidateListActivity.this);


        //CALL NOW
        webServices.castVote(casteVoteInput)
                .enqueue(new Callback<CasteVoteOutput>() {
                    @Override
                    public void onResponse(Call<CasteVoteOutput> call, Response<CasteVoteOutput> response) {
                        if (dialog.isShowing()) dialog.dismiss();
                        if (!P.analyseResponse(response)) {
                            //btn_login.setProgress(0);
                          //  btn_submit.setEnabled(true);
                            //Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                            P.ShowErrorDialogAndBeHere(context, "Error", "Server Error");

                            return;
                        }
                        CasteVoteOutput result = response.body();
                        if (result.is_success) {
                            P.ShowSuccessDialogAndBeHere(context, "Success", result.msg);


                        } else {
                            // btn_login.setProgress(0);
                          //  btn_submit.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, "Error", result.msg);
                            //Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CasteVoteOutput> call, Throwable t) {
                        P.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();
                        if (dialog.isShowing()) dialog.dismiss();
                        // btn_login.setProgress(0);

                     //   btn_submit.setEnabled(true);
                        P.ShowErrorDialogAndBeHere(context, "Error", "Check Internet Connection");
                    }
                });
    }
    }





