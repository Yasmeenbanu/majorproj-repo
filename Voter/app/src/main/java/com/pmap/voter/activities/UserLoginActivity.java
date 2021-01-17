package com.pmap.voter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pmap.voter.Api.Api;
import com.pmap.voter.Api.WebServices;
import com.pmap.voter.CandidatesActivity;
import com.pmap.voter.R;
import com.pmap.voter.VoterActivity;
import com.pmap.voter.logic.P;
import com.pmap.voter.models.CandidateDetails;
import com.pmap.voter.models.UserLoginInput;
import com.pmap.voter.models.UserLoginOutput;
import com.pmap.voter.models.VoterRegisterInput;
import com.pmap.voter.models.VoterRegisterOutput;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserLoginActivity extends AppCompatActivity {
    String who = "user";
    public Context context;
    Button btn_signin;
    private SweetAlertDialog dialog;
    EditText edt_username, edt_password;
    public static String voterID = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context = UserLoginActivity.this;
        btn_signin = (Button) findViewById(R.id.btn_signin);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        who = getIntent().getStringExtra("who");

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!P.isValidEditText(edt_username, "Username")) return;
                if (!P.isValidEditText(edt_password, "Password")) return;
                if (who.equalsIgnoreCase("admin")) {
                    adminLogin();
                } else {
                    userLogin();
                }

            }
        });


    }

    private void adminLogin() {
        if (edt_username.getText().toString().equals("admin") &&
                edt_password.getText().toString().equals("admin")) {

            Intent intent = new Intent(context, CreateElection.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();

        }
    }

    private void userLogin() {
            Retrofit retrofit = Api.getRetrofitBuilder(this);
            WebServices webServices = retrofit.create(WebServices.class);

            //PREPARE INPUT/REQUEST PARAMETERS
            UserLoginInput userRegisterInput = new UserLoginInput(
                    edt_username.getText().toString().trim(),
                    edt_password.getText().toString().trim()



            );
            dialog = P.showBufferDialog(context, "Processing...");
            //btn_login.setProgress(1);
           // btn_add_voter.setEnabled(false);
         //   P.hideSoftKeyboard(UserLoginActivity.this);


            //CALL NOW
            webServices.userLogin(userRegisterInput)
                    .enqueue(new Callback<UserLoginOutput>() {
                        @Override
                        public void onResponse(Call<UserLoginOutput> call, Response<UserLoginOutput> response) {
                            if (dialog.isShowing()) dialog.dismiss();
                            if (!P.analyseResponse(response)) {
                                //btn_login.setProgress(0);
                              //  btn_add_voter.setEnabled(true);
                                //Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                                P.ShowErrorDialogAndBeHere(context, "Error", "Server Error");
                                return;
                            }
                            UserLoginOutput result = response.body();
                            if (result.is_success) {
                                //P.ShowSuccessDialog(context, "Success", result.msg);
                                //sendSMSMessage();
                                //btn_login.setProgress(100);
                            /*U.userDetails = result.user.get(0);
                            Intent intent = new Intent(context, MainpageActivity.class);
                            startActivity(intent);*/
                                //finish();
                                Intent intent = new Intent(context, VoterDashboard.class);
                                startActivity(intent);
                            } else {
                                // btn_login.setProgress(0);
                                //btn_add_voter.setEnabled(true);
                                P.ShowErrorDialogAndBeHere(context, "Error", result.msg);
                                //Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserLoginOutput> call, Throwable t) {
                            P.displayNetworkErrorMessage(getApplicationContext(), null, t);
                            t.printStackTrace();
                            if (dialog.isShowing()) dialog.dismiss();
                            // btn_login.setProgress(0);

                           // btn_add_voter.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, "Error", "Check Internet Connection");
                        }
                    });
        }

    }




