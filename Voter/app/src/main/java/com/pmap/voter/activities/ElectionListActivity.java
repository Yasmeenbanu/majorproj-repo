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
import com.pmap.voter.ElectionListAdapter;
import com.pmap.voter.R;
import com.pmap.voter.VoterCandidate;
import com.pmap.voter.logic.P;
import com.pmap.voter.models.CandidateDetails;
import com.pmap.voter.models.CandidateListOutput;
import com.pmap.voter.models.CasteVoteInput;
import com.pmap.voter.models.CasteVoteOutput;
import com.pmap.voter.models.ElectionListDetails;
import com.pmap.voter.models.ElectionListOutput;
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

public class ElectionListActivity extends AppCompatActivity {
    private ElectionListAdapter electionListAdapter;
    private Context context;
    private RecyclerView rv_ele_id_list;
    private SweetAlertDialog dialog;
    private List<ElectionListDetails> electionListDetails = new ArrayList<>();
    private String elID = "";
    public static int FOR_FINGERPRINT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview_electionlist);

        context = ElectionListActivity.this;

        rv_ele_id_list = (RecyclerView) findViewById(R.id.rv_ele_id_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_ele_id_list.setLayoutManager(layoutManager);

        getElectionList();
    }

    private void getElectionList() {
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);


        dialog = P.showBufferDialog(context, "Processing...");
        //btn_login.setProgress(1);
        //btn_add_candidates.setEnabled(false);
        //P.hideSoftKeyboard(CandidateListActivity.this);*/


        //CALL NOW
        webServices.getElectionList()
                .enqueue(new Callback<ElectionListOutput>() {
                    @Override
                    public void onResponse(Call<ElectionListOutput> call, Response<ElectionListOutput> response) {
                        if (dialog.isShowing()) dialog.dismiss();
                        if (!P.analyseResponse(response)) {
                            //btn_login.setProgress(0);
                            // btn_add_candidates.setEnabled(true);
                            //Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                            P.ShowErrorDialogAndBeHere(context, "Error", "Server Error");
                            return;
                        }
                        ElectionListOutput result = response.body();
                        if (result.is_success) {
                            electionListDetails = result.election_list;
                            electionListAdapter = new ElectionListAdapter(context, electionListDetails);
                            rv_ele_id_list.setAdapter(electionListAdapter);


                        } else {
                            // btn_login.setProgress(0);
                            //  btn_add_candidates.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, "Error", result.msg);
                            //Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<ElectionListOutput> call, Throwable t) {
                        if (dialog.isShowing()) dialog.dismiss();
                        P.displayNetworkErrorMessage(context, null, t);
                        t.printStackTrace();
                        P.ShowErrorDialogAndBeHere(context, getString(R.string.error), getString(R.string.check_internet_connection));
                    }
                });
    }



    }






