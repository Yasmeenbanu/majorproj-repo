package com.pmap.voter.activities;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pmap.voter.Api.Api;
import com.pmap.voter.Api.WebServices;
import com.pmap.voter.ElectionListAdapter;
import com.pmap.voter.ElectionResultAdapter;
import com.pmap.voter.R;
import com.pmap.voter.logic.P;
import com.pmap.voter.models.ElectionListDetails;
import com.pmap.voter.models.ElectionListOutput;
import com.pmap.voter.models.ElectionNameInput;
import com.pmap.voter.models.ElectionResultDetails;
import com.pmap.voter.models.ElectionResultInput;
import com.pmap.voter.models.ElectionResultOutput;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ElectionResultActivity extends AppCompatActivity {

        private ElectionResultAdapter electionResultAdapter;
        private Context context;
        private RecyclerView rv_ele_res;
        private SweetAlertDialog dialog;
        private List<ElectionResultDetails> electionResultDetails = new ArrayList<>();
        private String elID = "";
        public static int FOR_FINGERPRINT = 1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.recycleview_election_result);

            context = ElectionResultActivity.this;

            rv_ele_res = (RecyclerView) findViewById(R.id.rv_ele_res);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            rv_ele_res.setLayoutManager(layoutManager);

            getElectionResultList();
        }

        private void getElectionResultList() {
            Retrofit retrofit = Api.getRetrofitBuilder(this);
            WebServices webServices = retrofit.create(WebServices.class);


            dialog = P.showBufferDialog(context, "Processing...");
            //btn_login.setProgress(1);
            //btn_add_candidates.setEnabled(false);
            //P.hideSoftKeyboard(CandidateListActivity.this);*/
            ElectionResultInput electionResultInput = new ElectionResultInput(
                    getIntent().getStringExtra("el_id")
            );

            //CALL NOW
            webServices.getElectionResultList(electionResultInput)
                    .enqueue(new Callback<ElectionResultOutput>() {
                        @Override
                        public void onResponse(Call<ElectionResultOutput> call, Response<ElectionResultOutput> response) {
                            if (dialog.isShowing()) dialog.dismiss();
                            if (!P.analyseResponse(response)) {
                                //btn_login.setProgress(0);
                                // btn_add_candidates.setEnabled(true);
                                //Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                                P.ShowErrorDialogAndBeHere(context, "Error", "Server Error");
                                return;
                            }
                            ElectionResultOutput result = response.body();
                            if (result.is_success) {
                                electionResultDetails = result.election_result_list;
                                electionResultAdapter = new ElectionResultAdapter(context,electionResultDetails);
                                rv_ele_res.setAdapter(electionResultAdapter);


                            } else {
                                // btn_login.setProgress(0);
                                //  btn_add_candidates.setEnabled(true);
                                P.ShowErrorDialogAndBeHere(context, "Error", result.msg);
                                //Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                            }
                        }


                        @Override
                        public void onFailure(Call<ElectionResultOutput> call, Throwable t) {
                            if (dialog.isShowing()) dialog.dismiss();
                            P.displayNetworkErrorMessage(context, null, t);
                            t.printStackTrace();
                            P.ShowErrorDialogAndBeHere(context, getString(R.string.error), getString(R.string.check_internet_connection));
                        }
                    });
        }



    }








