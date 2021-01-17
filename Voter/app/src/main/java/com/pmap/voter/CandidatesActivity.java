package com.pmap.voter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.pmap.voter.Api.Api;
import com.pmap.voter.Api.WebServices;
import com.pmap.voter.logic.P;
import com.pmap.voter.models.CandidateRegisterInput;
import com.pmap.voter.models.CandidateRegisterOutput;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CandidatesActivity extends AppCompatActivity {
private String TAG = CandidatesActivity.class.getSimpleName();
    @BindView(R.id.edt_candidatename)
    public
    EditText edt_candidatename;
    @BindView(R.id.edt_mobile)
    EditText edt_mobile;
    @BindView(R.id.edt_adhaar)
    EditText edt_adhaar;
    @BindView(R.id.candidate_state_spinner)
    public Spinner spn_state;
    @BindView(R.id.candidate_district_spinner)
    public Spinner spn_district;
    @BindView(R.id.edt_taluk)
    public
    EditText edt_taluk;
    @BindView(R.id.edt_party)
    EditText edt_party;
    @BindView(R.id.img_candidates)
    ImageView img_candiadtes;


    /*@BindView(R.id.edt_password)
    EditText edt_password;*/
    @BindView(R.id.btn_add_candidates)
    Button btn_add_candidates;
    private Spinner district_Spinner;
    private Spinner state_Spinner;

    private SweetAlertDialog dialog;
    private ArrayAdapter<State> stateArrayAdapter;
    private ArrayAdapter<District> districtArrayAdapter;


    private ArrayList<State> states;
    private ArrayList<District> districts;

    private Context context;
    private Bitmap bpCandidate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidates);
        ButterKnife.bind(this);
        context = CandidatesActivity.this;
        btn_add_candidates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!P.isValidEditText(edt_candidatename, "CandidateName")) return;
                // if (!P.isValidEditText(edt_password, "Password")) return;
                if (!P.isValidEditText(edt_mobile, "candidate_mobile_number")) return;
                if (!P.isValidMobileNumber(edt_mobile, "candidate_mobile_number")) return;
                if (!P.isValidEditText(edt_adhaar, "Adhaar")) return;

                if (!P.isValidEditText(edt_taluk, "Taluk")) return;
                if (!P.isValidEditText(edt_party, "Party")) return;


                // if (!P.isValidEmail(edt_email.getText().toString().trim())) return;


                    /*if (!edt_password.getText().toString().trim().equals(edt_confirm_password.getText().toString().trim())) {
                        edt_confirm_password.setError("Password does not matched");
                        edt_confirm_password.requestFocus();
                        return;
                    }*/

                candidateRegister();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeUI();

        img_candiadtes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CandidatesActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bpCandidate = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    img_candiadtes.setImageBitmap(bpCandidate);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bpCandidate.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                bpCandidate = (BitmapFactory.decodeFile(picturePath));
                Log.d(TAG, picturePath + "");
                img_candiadtes.setImageBitmap(bpCandidate);
            }
        }
    }

    private void candidateRegister() {
        Retrofit retrofit = Api.getRetrofitBuilder(this);
        WebServices webServices = retrofit.create(WebServices.class);

        //PREPARE INPUT/REQUEST PARAMETERS
        CandidateRegisterInput candidateRegisterInput = new CandidateRegisterInput(
                edt_candidatename.getText().toString().trim(),
                edt_mobile.getText().toString().trim(),
                edt_adhaar.getText().toString().trim(),
                spn_state.getSelectedItem().toString(),
                spn_district.getSelectedItem().toString(),
                edt_taluk.getText().toString().trim(),
                edt_party.getText().toString().trim(),
                getIntent().getStringExtra("el_id"),
                P.BitmapToString(bpCandidate)

        );
        dialog = P.showBufferDialog(context, "Processing...");
        //btn_login.setProgress(1);
        btn_add_candidates.setEnabled(false);
        P.hideSoftKeyboard(CandidatesActivity.this);


        //CALL NOW
        webServices.candidateRegister(candidateRegisterInput)
                .enqueue(new Callback<CandidateRegisterOutput>() {
                    @Override
                    public void onResponse(Call<CandidateRegisterOutput> call, Response<CandidateRegisterOutput> response) {
                        if (dialog.isShowing()) dialog.dismiss();
                        if (!P.analyseResponse(response)) {
                            //btn_login.setProgress(0);
                            btn_add_candidates.setEnabled(true);
                            //Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                            P.ShowErrorDialogAndBeHere(context, "Error", "Server Error");
                            return;
                        }
                        CandidateRegisterOutput result = response.body();
                        if (result.is_success) {
                            P.ShowSuccessDialog(context, "Success", result.msg);
                            //btn_login.setProgress(100);
                            /*U.userDetails = result.user.get(0);
                            Intent intent = new Intent(context, MainpageActivity.class);
                            startActivity(intent);*/
                            //finish();
                           /* Intent intent = new Intent(context, SlidesActivity.class);
                            startActivity(intent);*/
                        } else {
                            // btn_login.setProgress(0);
                            btn_add_candidates.setEnabled(true);
                            P.ShowErrorDialogAndBeHere(context, "Error", result.msg);
                            //Toast.makeText(context, result.msg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CandidateRegisterOutput> call, Throwable t) {
                        P.displayNetworkErrorMessage(getApplicationContext(), null, t);
                        t.printStackTrace();
                        if (dialog.isShowing()) dialog.dismiss();
                        // btn_login.setProgress(0);

                        btn_add_candidates.setEnabled(true);
                        P.ShowErrorDialogAndBeHere(context, "Error", "Check Internet Connection");
                    }
                });
    }


    private void initializeUI() {
        state_Spinner = (Spinner) findViewById(R.id.candidate_state_spinner);
        district_Spinner = (Spinner) findViewById(R.id.candidate_district_spinner);


        states = new ArrayList<>();
        districts = new ArrayList<>();


        createLists();

        stateArrayAdapter = new ArrayAdapter<State>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, states);
        stateArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        state_Spinner.setAdapter(stateArrayAdapter);

        districtArrayAdapter = new ArrayAdapter<District>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, districts);
        districtArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        district_Spinner.setAdapter(districtArrayAdapter);


        state_Spinner.setOnItemSelectedListener(state_listener);
        district_Spinner.setOnItemSelectedListener(district_listener);


    }

    private AdapterView.OnItemSelectedListener state_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                final State state = (State) state_Spinner.getItemAtPosition(position);
                Log.d("SpinnerState", "onItemSelected: state: " + state.getStateID());
                ArrayList<District> tempDistricts = new ArrayList<>();

                tempDistricts.add(new District(0, new State(0, "Choose a State"), "Choose a District"));

                for (District singleDistrict : districts) {
                    if (singleDistrict.getState().getStateID() == state.getStateID()) {
                        tempDistricts.add(singleDistrict);
                    }
                }

                districtArrayAdapter = new ArrayAdapter<District>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, tempDistricts);
                districtArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                district_Spinner.setAdapter(districtArrayAdapter);
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener district_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                final District district = (District) district_Spinner.getItemAtPosition(position);
                Log.d("SpinnerState", "onItemSelected: district: " + district.getDistrictID());

                State state = new State(0, "Choose a State");
                District district1 = new District(0, state, "Choose a District");


            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private void createLists() {
        State state0 = new State(0, "Choose a State");
        State state1 = new State(1, "Andaman & Nicobar");
        State state2 = new State(2, "Andhra Pradesh");
        State state3 = new State(3, "Arunachal Pradesh");
        State state4 = new State(4, "Assam");
        State state5 = new State(5, "Bihar");
        State state6 = new State(6, "Chandigarh");
        State state7 = new State(7, "Chhattisghar");
        State state8 = new State(8, "Dadra & Nagar Haveli");
        State state9 = new State(9, "Daman & Diu");
        State state10 = new State(10, "Delhi / NCR");
        State state11 = new State(11, "Goa");
        State state12 = new State(12, "Gujarat");
        State state13 = new State(13, "Haryana");
        State state14 = new State(14, "Himachal Pradesh");
        State state15 = new State(15, "Jammu & Kashmir");
        State state16 = new State(16, "Jharkhand");
        State state17 = new State(17, "Karnataka");
        State state18 = new State(18, "Kerala");
        State state19 = new State(19, "Lakshadweep");
        State state20 = new State(20, "Madhya Pradesh");
        State state21 = new State(21, "Maharashtra");
        State state22 = new State(22, "Manipur");
        State state23 = new State(23, "Meghalaya");
        State state24 = new State(24, "Mizoram");
        State state25 = new State(25, "Nagaland");
        State state26 = new State(26, "Orissa");
        State state27 = new State(27, "Pondicherry");
        State state28 = new State(28, "Punjab");
        State state29 = new State(29, "Rajasthan");
        State state30 = new State(30, "Sikkim");
        State state31 = new State(31, "Tamil Nadu");
        State state32 = new State(32, "Telangana");
        State state33 = new State(33, "Tripura");
        State state34 = new State(34, "Uttar Pradesh");
        State state35 = new State(35, "Uttarakhand");
        State state36 = new State(36, "West Bengal");

        states.add(new State(0, "Choose a State"));
        states.add(new State(1, "Andaman & Nicobar"));
        states.add(new State(2, "Andhra Pradesh"));
        states.add(new State(3, "Arunachal Pradesh"));
        states.add(new State(4, "Assam"));
        states.add(new State(5, "Bihar"));
        states.add(new State(6, "Chandigarh"));
        states.add(new State(7, "Chhattisghar"));
        states.add(new State(8, "Dadra & Nagar Haveli"));
        states.add(new State(9, "Daman & Diu"));
        states.add(new State(10, "Delhi / NCR"));
        states.add(new State(11, "Goa"));
        states.add(new State(12, "Gujarat"));
        states.add(new State(13, "Haryana"));
        states.add(new State(14, "Himachal Pradesh"));
        states.add(new State(15, "Jammu & Kashmir"));
        states.add(new State(16, "Jharkhand"));
        states.add(new State(17, "Karnataka"));
        states.add(new State(18, "Kerala"));
        states.add(new State(19, "Lakshadweep"));
        states.add(new State(20, "Madhya Pradesh"));
        states.add(new State(21, "Maharashtra"));
        states.add(new State(22, "Manipur"));
        states.add(new State(23, "Meghalaya"));
        states.add(new State(24, "Mizoram"));
        states.add(new State(25, "Nagaland"));
        states.add(new State(26, "Orissa"));
        states.add(new State(27, "Pondicherry"));
        states.add(new State(28, "Punjab"));
        states.add(new State(29, "Rajasthan"));
        states.add(new State(30, "Sikkim"));
        states.add(new State(31, "Tamil Nadu"));
        states.add(new State(32, "Telangana"));
        states.add(new State(33, "Tripura"));
        states.add(new State(34, "Uttar Pradesh"));
        states.add(new State(35, "Uttarakhand"));
        states.add(new State(36, "West Bengal"));


        District district0 = new District(0, state0, "Choose a District");
        District district1 = new District(1, state1, "Nicobar");
        District district2 = new District(2, state1, "North and Middle Andaman");
        District district3 = new District(3, state1, "South Andaman");
        District district4 = new District(4, state2, "Anantapur");
        District district5 = new District(5, state2, "Chittoor");
        District district6 = new District(6, state2, "East Godavari");
        District district7 = new District(7, state2, "Guntur");
        District district8 = new District(8, state2, "YSR Kadapa district");
        District district9 = new District(9, state2, "Krishna");
        District district10 = new District(10, state2, "Kurnool");
        District district11 = new District(11, state2, "Nellore");
        District district12 = new District(12, state2, "Prakasam");
        District district13 = new District(13, state2, "Srikakulam");
        District district14 = new District(14, state2, "Visakhapatnam");
        District district15 = new District(15, state2, "Vizianagaram");
        District district16 = new District(16, state2, "West Godavari");
        District district17 = new District(17, state3, "Anjaw");
        District district18 = new District(18, state3, "Changlang");
        District district19 = new District(19, state3, "Dibang Valley");
        District district20 = new District(20, state3, "East Kameng");
        District district21 = new District(21, state3, "East Siang");
        District district22 = new District(22, state3, "Kamle");
        District district23 = new District(23, state3, "Kra Daadi");
        District district24 = new District(24, state3, "Kurung Kumey");
        District district25 = new District(25, state3, "Lepa-Rada");
        District district26 = new District(26, state3, "Lohit");
        District district27 = new District(27, state3, "Longding");
        District district28 = new District(28, state3, "Lower Dibang Valley");
        District district29 = new District(29, state3, "Lower Siang");
        District district30 = new District(30, state3, "Lower Subansiri");
        District district31 = new District(31, state3, "Namsai");
        District district32 = new District(32, state3, "Pakke-Kessang");
        District district33 = new District(33, state3, "Papum Pare");
        District district34 = new District(34, state3, "Shi-Yomi");
        District district35 = new District(35, state3, "Siang");
        District district36 = new District(36, state3, "Tawang");
        District district37 = new District(37, state3, "Tirap");
        District district38 = new District(38, state3, "Upper Siang");
        District district39 = new District(39, state3, "Upper Subansiri");
        District district40 = new District(40, state3, "West Kameng");
        District district41 = new District(41, state3, "West Siang");
        District district42 = new District(42, state4, "Baksa");
        District district43 = new District(43, state4, "Barpeta");
        District district44 = new District(44, state4, "Biswanath");
        District district45 = new District(45, state4, "Bongaigaon");
        District district46 = new District(46, state4, "Cachar");
        District district47 = new District(47, state4, "Charaideo");
        District district48 = new District(48, state4, "Chirang");
        District district49 = new District(49, state4, "Darrang");
        District district50 = new District(50, state4, "Dhemaji");
        District district51 = new District(51, state4, "Dhubri");
        District district52 = new District(52, state4, "Dibrugarh");
        District district53 = new District(53, state4, "Dima Hasao");
        District district54 = new District(54, state4, "Goalpara");
        District district55 = new District(55, state4, "Golaghat");
        District district56 = new District(56, state4, "Hailakandi");
        District district57 = new District(57, state4, "Hojai");
        District district58 = new District(58, state4, "Jorhat");
        District district59 = new District(59, state4, "Kamrup Metropolitan");
        District district60 = new District(60, state4, "Kamrup");
        District district61 = new District(61, state4, "Karbi Anglong");
        District district62 = new District(62, state4, "Karimganj");
        District district63 = new District(63, state4, "Kokrajhar");
        District district64 = new District(64, state4, "Lakhimpur");
        District district65 = new District(65, state4, "Majuli");
        District district66 = new District(66, state4, "Morigaon");
        District district67 = new District(67, state4, "Nagaon");
        District district68 = new District(68, state4, "Nalbari");
        District district69 = new District(69, state4, "Sivasagar");
        District district70 = new District(70, state4, "Sonitpur");
        District district71 = new District(71, state4, "South Salmara-Mankachar");
        District district72 = new District(72, state4, "Tinsukia");
        District district73 = new District(73, state4, "Udalguri");
        District district74 = new District(74, state4, "West Karbi Anglong");
        District district75 = new District(75, state5, "Araria");
        District district76 = new District(76, state5, "Arwal");
        District district77 = new District(77, state5, "Aurangabad");
        District district78 = new District(78, state5, "Banka");
        District district79 = new District(79, state5, "Begusarai");
        District district80 = new District(80, state5, "Bhagalpur");
        District district81 = new District(81, state5, "Bhojpur");
        District district82 = new District(82, state5, "Buxar");
        District district83 = new District(83, state5, "Darbhanga");
        District district84 = new District(84, state5, "East Champaran");
        District district85 = new District(85, state5, "Gaya");
        District district86 = new District(86, state5, "Gopalganj");
        District district87 = new District(87, state5, "Jamui");
        District district88 = new District(88, state5, "Jehanabad");
        District district89 = new District(89, state5, "Khagaria");
        District district90 = new District(90, state5, "Kishanganj");
        District district91 = new District(91, state5, "Kaimur");
        District district92 = new District(92, state5, "Katihar");
        District district93 = new District(93, state5, "Lakhisarai");
        District district94 = new District(94, state5, "Madhubani");
        District district95 = new District(95, state5, "Munger");
        District district96 = new District(96, state5, "Madhepura");
        District district97 = new District(97, state5, "Muzaffarpur");
        District district98 = new District(98, state5, "Nalanda");
        District district99 = new District(99, state5, "Nalanda");
        District district100 = new District(100, state5, "Patna");
        District district101 = new District(101, state5, "Purnia");
        District district102 = new District(102, state5, "Rohtas");
        District district103 = new District(103, state5, "Saharsa");
        District district104 = new District(104, state5, "Samastipur");
        District district105 = new District(105, state5, "Sheohar");
        District district106 = new District(106, state5, "Sheikhpura");
        District district107 = new District(107, state5, "Saran");
        District district108 = new District(108, state5, "Sitamarhi");
        District district109 = new District(109, state5, "Supaul");
        District district110 = new District(110, state5, "Siwan");
        District district111 = new District(111, state5, "Vaishali");
        District district112 = new District(112, state5, "West Champaran");
        District district113 = new District(113, state6, "Chandigarh");
        District district114 = new District(114, state7, "Balod");
        District district115 = new District(115, state7, "Baloda Bazar");
        District district116 = new District(116, state7, "Balrampur");
        District district117 = new District(117, state7, "Bastar");
        District district118 = new District(118, state7, "Bemetara");
        District district119 = new District(119, state7, "Bilaspur");
        District district120 = new District(120, state7, "Dantewada");
        District district121 = new District(121, state7, "Dhamtari");
        District district122 = new District(122, state7, "Durg");
        District district123 = new District(123, state7, "Gariaband");
        District district124 = new District(124, state7, "Gaurela-Pendra-Marwahi");
        District district125 = new District(125, state7, "Janjgir-Champa");
        District district126 = new District(126, state7, "Jashpur");
        District district127 = new District(127, state7, "Kabirdham");
        District district128 = new District(128, state7, "Kanker");
        District district129 = new District(129, state7, "Kondagaon");
        District district130 = new District(130, state7, "Korba");
        District district131 = new District(131, state7, "Koriya");
        District district132 = new District(132, state7, "Mahasamund");
        District district133 = new District(133, state7, "Mungeli");
        District district134 = new District(134, state7, "Narayanpur");
        District district135 = new District(135, state7, "Raigarh");
        District district136 = new District(136, state7, "Raipur");
        District district137 = new District(137, state7, "Rajnandgaon");
        District district138 = new District(138, state7, "Sukma");
        District district139 = new District(139, state7, "Surajpur");
        District district140 = new District(140, state7, "Surguja");
        District district141 = new District(141, state8, "Dadra And Nagar Haveli");
        District district142 = new District(142, state9, "Daman");
        District district143 = new District(143, state9, "Diu");
        District district144 = new District(144, state10, "Central Delhi");
        District district145 = new District(145, state10, "East Delhi");
        District district146 = new District(146, state10, "New Delhi");
        District district147 = new District(147, state10, "North Delhi");
        District district148 = new District(148, state10, "North East Delhi");
        District district149 = new District(149, state10, "North West Delhi");
        District district150 = new District(150, state10, "Shahdara");
        District district151 = new District(151, state10, "South Delhi");
        District district152 = new District(152, state10, "South East Delhi");
        District district153 = new District(153, state10, "South West Delhi");
        District district154 = new District(154, state10, "West Delhi");
        District district155 = new District(155, state11, "North Goa");
        District district156 = new District(156, state11, "South Goa");
        District district157 = new District(157, state12, "Ahmedabad");
        District district158 = new District(158, state12, "Amreli");
        District district159 = new District(159, state12, "Anand");
        District district160 = new District(160, state12, "Aravalli");
        District district161 = new District(161, state12, "Banaskantha (Palanpur)");
        District district162 = new District(162, state12, "Bharuch");
        District district163 = new District(163, state12, "Bhavnagar");
        District district164 = new District(164, state12, "Botad");
        District district165 = new District(165, state12, "Chhota Udepur");
        District district166 = new District(166, state12, "Dahod");
        District district167 = new District(167, state12, "Dangs (Ahwa)");
        District district168 = new District(168, state12, "Devbhoomi Dwarka");
        District district169 = new District(169, state12, "Gandhinagar");
        District district170 = new District(170, state12, "Gir Somnath");
        District district171 = new District(171, state12, "Jamnagar");
        District district172 = new District(172, state12, "Junagadh");
        District district173 = new District(173, state12, "Kachchh");
        District district174 = new District(174, state12, "Kheda (Nadiad)");
        District district175 = new District(175, state12, "Mahisagar");
        District district176 = new District(176, state12, "Mehsana");
        District district177 = new District(177, state12, "Morbi");
        District district178 = new District(178, state12, "Narmada (Rajpipla)");
        District district179 = new District(179, state12, "Navsari");
        District district180 = new District(180, state12, "Panchmahal (Godhra)");
        District district181 = new District(181, state12, "Patan");
        District district182 = new District(182, state12, "Porbandar");
        District district183 = new District(183, state12, "Rajkot");
        District district184 = new District(184, state12, "Sabarkantha (Himmatnagar)");
        District district185 = new District(185, state12, "Surat");
        District district186 = new District(186, state12, "Surendranagar");
        District district187 = new District(187, state12, "Tapi (Vyara)");
        District district188 = new District(188, state12, "Vadodara");
        District district189 = new District(189, state12, "Valsad");
        District district190 = new District(190, state13, "Ambala");
        District district191 = new District(191, state13, "Bhiwani");
        District district192 = new District(192, state13, "Charkhi Dadri");
        District district193 = new District(193, state13, "Faridabad");
        District district194 = new District(194, state13, "Gurgaon");
        District district195 = new District(195, state13, "Hisar");
        District district196 = new District(196, state13, "Jhajjar");
        District district197 = new District(197, state13, "Jind");
        District district198 = new District(198, state13, "Kaithal");
        District district199 = new District(199, state13, "Karnal");
        District district200 = new District(200, state13, "Kurukshetra");
        District district201 = new District(201, state13, "Mahendragarh");
        District district202 = new District(202, state13, "Mewat");
        District district203 = new District(203, state13, "Palwal");
        District district204 = new District(204, state13, "Panchkula");
        District district205 = new District(205, state13, "Panjpat");
        District district206 = new District(206, state13, "Rohtak");
        District district207 = new District(207, state13, "Sirsa");
        District district208 = new District(208, state13, "Sonipat");
        District district209 = new District(209, state13, "Yamunanagar");
        District district210 = new District(210, state13, "Fatehabad");
        District district211 = new District(211, state13, "Rewari");
        District district212 = new District(212, state14, "Bilaspur");
        District district213 = new District(213, state14, "Chamba");
        District district214 = new District(214, state14, "Hamirpur");
        District district215 = new District(215, state14, "Kangra");
        District district216 = new District(216, state14, "Kinnaur");
        District district217 = new District(217, state14, "Kullu");
        District district218 = new District(218, state14, "Lahaul & Spiti");
        District district219 = new District(219, state14, "Mandi");
        District district220 = new District(220, state14, "Shimla");
        District district221 = new District(221, state14, "Sirmaur");
        District district222 = new District(222, state14, "Solan");
        District district223 = new District(223, state14, "Una");
        District district224 = new District(224, state15, "Anantnag");
        District district225 = new District(225, state15, "Bandipore");
        District district226 = new District(226, state15, "Baramulla");
        District district227 = new District(227, state15, "Budgam");
        District district228 = new District(228, state15, "Doda");
        District district229 = new District(229, state15, "Ganderbal");
        District district230 = new District(230, state15, "Jammu");
        District district231 = new District(231, state15, "Kargil");
        District district232 = new District(232, state15, "Kathua");
        District district233 = new District(233, state15, "Kishtwar");
        District district234 = new District(234, state15, "Kulgam");
        District district235 = new District(235, state15, "Kupwara");
        District district236 = new District(236, state15, "Leh");
        District district237 = new District(237, state15, "Poonch");
        District district238 = new District(238, state15, "Pulwama");
        District district239 = new District(239, state15, "Rajouri");
        District district240 = new District(240, state15, "Ramban");
        District district241 = new District(241, state15, "Reasi");
        District district242 = new District(242, state15, "Samba");
        District district243 = new District(243, state15, "Shopian");
        District district244 = new District(244, state15, "Srinagar");
        District district245 = new District(245, state15, "Udhampur");
        District district246 = new District(246, state16, "Bokaro");
        District district247 = new District(247, state16, "Chatra");
        District district248 = new District(248, state16, "Deoghar");
        District district249 = new District(249, state16, "Dhanbad");
        District district250 = new District(250, state16, "Dumka");
        District district251 = new District(251, state16, "East Singhbhum");
        District district252 = new District(252, state16, "Garhwa");
        District district253 = new District(253, state16, "Giridih");
        District district254 = new District(254, state16, "Godda");
        District district255 = new District(255, state16, "Gumla");
        District district256 = new District(256, state16, "Hazaribag");
        District district257 = new District(257, state16, "Jamtara");
        District district258 = new District(258, state16, "Khunti");
        District district259 = new District(259, state16, "Koderma");
        District district260 = new District(260, state16, "Latehar");
        District district261 = new District(261, state16, "Lohardaga");
        District district262 = new District(262, state16, "Pakur");
        District district263 = new District(263, state16, "Palamu");
        District district264 = new District(264, state16, "Ramgarh");
        District district265 = new District(265, state16, "Ranchi");
        District district266 = new District(266, state16, "Sahibganj");
        District district267 = new District(267, state16, "Seraikela-Kharsawan");
        District district268 = new District(268, state16, "Simdega");
        District district269 = new District(269, state16, "West Singhbhum");
        District district270 = new District(270, state17, "Bagalkot");
        District district271 = new District(271, state17, "Ballari (Bellary)");
        District district272 = new District(272, state17, "Belagavi (Begaum)");
        District district273 = new District(273, state17, "Bengaluru (Bangalore) Rural");
        District district274 = new District(274, state17, "Bengaluru (Bangalore) Urban");
        District district275 = new District(275, state17, "Bidar");
        District district276 = new District(276, state17, "Chamarajanagar");
        District district277 = new District(277, state17, "Chikballapur");
        District district278 = new District(278, state17, "Chikkamagaluru (Chikmagalur)");
        District district279 = new District(279, state17, "Dakshina Kannada");
        District district280 = new District(280, state17, "Davangere");
        District district281 = new District(281, state17, "Dharwad");
        District district282 = new District(282, state17, "Gadag");
        District district283 = new District(283, state17, "Hassan");
        District district284 = new District(284, state17, "Haveri");
        District district285 = new District(285, state17, "Kalaburagi (Gulbaraga)");
        District district286 = new District(286, state17, "Kodagu");
        District district287 = new District(287, state17, "Kolar");
        District district288 = new District(288, state17, "Koppal");
        District district289 = new District(289, state17, "Mandya");
        District district290 = new District(290, state17, "Mysuru (Mysore)");
        District district291 = new District(291, state17, "Raichur");
        District district292 = new District(292, state17, "Ramanagara");
        District district293 = new District(293, state17, "Shivamogga (Shimoga)");
        District district294 = new District(294, state17, "Tumakuru (Tumkur)");
        District district295 = new District(295, state17, "Udupi");
        District district296 = new District(296, state17, "Uttara Kannada (Karwar)");
        District district297 = new District(297, state17, "Vijayapura (Bijapur)");
        District district298 = new District(298, state17, "Yadgir");
        District district299 = new District(299, state17, "Chitradurga");
        District district300 = new District(300, state18, "Alappuzha");
        District district301 = new District(301, state18, "Ernakulam");
        District district302 = new District(302, state18, "Idukki");
        District district303 = new District(303, state18, "Kannur");
        District district304 = new District(304, state18, "Kasaragod");
        District district305 = new District(305, state18, "Kollam");
        District district306 = new District(306, state18, "Kottayam");
        District district307 = new District(307, state18, "Kozhikode");
        District district308 = new District(308, state18, "Malappuram");
        District district309 = new District(309, state18, "Palakkad");
        District district310 = new District(310, state18, "Pathanamthitta");
        District district311 = new District(311, state18, "Thiruvananthapuram");
        District district312 = new District(312, state18, "Thrissur");
        District district313 = new District(313, state18, "Wayanad");
        District district314 = new District(314, state19, "Lakshadweep");
        District district315 = new District(315, state20, "Agar Malwa");
        District district316 = new District(316, state20, "Alirajpur");
        District district317 = new District(317, state20, "Anuppur");
        District district318 = new District(318, state20, "Ashoknagar");
        District district319 = new District(319, state20, "Balaghat");
        District district320 = new District(320, state20, "Barwani");
        District district321 = new District(321, state20, "Betul");
        District district322 = new District(322, state20, "Bhind");
        District district323 = new District(323, state20, "Bhopal");
        District district324 = new District(324, state20, "Burhanpur");
        District district325 = new District(325, state20, "Chhatarpur");
        District district326 = new District(326, state20, "Chhindwara");
        District district327 = new District(327, state20, "Damoh");
        District district328 = new District(328, state20, "Datia");
        District district329 = new District(329, state20, "Dewas");
        District district330 = new District(330, state20, "Dhar");
        District district331 = new District(331, state20, "Dindori");
        District district332 = new District(332, state20, "Guna");
        District district333 = new District(333, state20, "Gwalior");
        District district334 = new District(334, state20, "Harda");
        District district335 = new District(335, state20, "Hoshangabad");
        District district336 = new District(336, state20, "Indore");
        District district337 = new District(337, state20, "Jabalpur");
        District district338 = new District(338, state20, "Jhabua");
        District district339 = new District(339, state20, "Katni");
        District district340 = new District(340, state20, "Khandwa");
        District district341 = new District(341, state20, "Khargone");
        District district342 = new District(342, state20, "Mandla");
        District district343 = new District(343, state20, "Mandsaur");
        District district344 = new District(344, state20, "Morena");
        District district345 = new District(345, state20, "Narsinghpur");
        District district346 = new District(346, state20, "Neemuch");
        District district347 = new District(347, state20, "Panna");
        District district348 = new District(348, state20, "Raisen");
        District district349 = new District(349, state20, "Rajgarh");
        District district350 = new District(350, state20, "Ratlam");
        District district351 = new District(351, state20, "Rewa");
        District district352 = new District(352, state20, "Sagar");
        District district353 = new District(353, state20, "Satna");
        District district354 = new District(354, state20, "Sehore");
        District district355 = new District(355, state20, "Seoni");
        District district356 = new District(356, state20, "Shahdol");
        District district357 = new District(357, state20, "Shajapur");
        District district358 = new District(358, state20, "Sheopur");
        District district359 = new District(359, state20, "Shivpuri");
        District district360 = new District(360, state20, "Sidhi");
        District district361 = new District(361, state20, "Singrauli");
        District district362 = new District(362, state20, "Tikamgarh");
        District district363 = new District(363, state20, "Ujjain");
        District district364 = new District(364, state20, "Umaria");
        District district365 = new District(365, state20, "Vidisha");
        District district366 = new District(366, state21, "Ahmednagar");
        District district367 = new District(367, state21, "Akola");
        District district368 = new District(364, state21, "Amravati");
        District district369 = new District(369, state21, "Beed");
        District district370 = new District(370, state21, "Bhandara");
        District district371 = new District(371, state21, "Buldhana");
        District district372 = new District(372, state21, "Chandrapur");
        District district373 = new District(373, state21, "Dhule");
        District district374 = new District(374, state21, "Gadchiroli");
        District district375 = new District(375, state21, "Gondia");
        District district376 = new District(376, state21, "Hingoli");
        District district377 = new District(377, state21, "Jalgaon");
        District district378 = new District(378, state21, "Jalna");
        District district379 = new District(379, state21, "Kolhapur");
        District district380 = new District(380, state21, "Latur");
        District district381 = new District(381, state21, "Mumbai City");
        District district382 = new District(382, state21, "Mumbai Suburban");
        District district383 = new District(383, state21, "Nagpur");
        District district384 = new District(384, state21, "Nanded");
        District district385 = new District(385, state21, "Nandurbar");
        District district386 = new District(386, state21, "Nashik");
        District district387 = new District(387, state21, "Osmanabad");
        District district388 = new District(388, state21, "Palghar");
        District district389 = new District(389, state21, "Parbhani");
        District district390 = new District(390, state21, "Pune");
        District district391 = new District(391, state21, "Raigad");
        District district392 = new District(392, state21, "Ratnagiri");
        District district393 = new District(393, state21, "Sangli");
        District district394 = new District(394, state21, "Satara");
        District district395 = new District(395, state21, "Sindhudurg");
        District district396 = new District(396, state21, "Solapur");
        District district397 = new District(397, state21, "Thane");
        District district398 = new District(398, state21, "Wardha");
        District district399 = new District(399, state21, "Washim");
        District district400 = new District(400, state21, "Yavatmal");
        District district401 = new District(401, state21, "Aurangabad");
        District district402 = new District(402, state22, "Bishnupur");
        District district403 = new District(403, state22, "Chandel");
        District district404 = new District(404, state22, "Churachandpur");
        District district405 = new District(405, state22, "Imphal East");
        District district406 = new District(406, state22, "Imphal West");
        District district407 = new District(407, state22, "Jiribam");
        District district408 = new District(408, state22, "Kakching");
        District district409 = new District(409, state22, "Kamjong");
        District district410 = new District(410, state22, "Kangpokpi");
        District district411 = new District(411, state22, "Noney");
        District district412 = new District(412, state22, "Pherzawl");
        District district413 = new District(413, state22, "Senapati");
        District district414 = new District(414, state22, "Tamenglong");
        District district415 = new District(415, state22, "Tengnoupal");
        District district416 = new District(416, state22, "Aurangabad");
        District district417 = new District(417, state22, "Ukhrul");
        District district418 = new District(418, state23, "East Garo Hills");
        District district419 = new District(419, state23, "East Jaintia Hills");
        District district420 = new District(420, state23, "East Khasi Hills");
        District district421 = new District(421, state23, "North Garo Hills");
        District district422 = new District(422, state23, "Ri Bhoi");
        District district423 = new District(423, state23, "South Garo Hills");
        District district424 = new District(424, state23, "South West Garo Hills");
        District district425 = new District(425, state23, "South West Khasi Hills");
        District district426 = new District(426, state23, "West Garo Hills");
        District district427 = new District(427, state23, "West Jaintia Hills");
        District district428 = new District(428, state23, "West Khasi Hills");
        District district429 = new District(429, state24, "Aizawl");
        District district430 = new District(430, state24, "Champhai");
        District district431 = new District(431, state24, "Kolasib");
        District district432 = new District(432, state24, "Lawngtlai");
        District district433 = new District(433, state24, "Lunglei");
        District district434 = new District(434, state24, "Mamit");
        District district435 = new District(435, state24, "Saiha");
        District district436 = new District(436, state24, "Serchhip");
        District district437 = new District(437, state25, "Dimapur");
        District district438 = new District(438, state25, "Kiphire");
        District district439 = new District(439, state25, "Kohima");
        District district440 = new District(440, state25, "Longleng");
        District district441 = new District(441, state25, "Mokokchung");
        District district442 = new District(442, state25, "Mon");
        District district443 = new District(443, state25, "Peren");
        District district444 = new District(444, state25, "Phek");
        District district445 = new District(445, state25, "Tuensang");
        District district446 = new District(446, state25, "Wokha");
        District district447 = new District(447, state25, "Zunheboto");
        District district448 = new District(448, state26, "Angul");
        District district449 = new District(449, state26, "Balangir");
        District district450 = new District(450, state26, "Balasore");
        District district451 = new District(451, state26, "Bargarh");
        District district452 = new District(452, state26, "Bhadrak");
        District district453 = new District(453, state26, "Boudh");
        District district454 = new District(454, state26, "Cuttack");
        District district455 = new District(455, state26, "Deogarh");
        District district456 = new District(456, state26, "Dhenkanal");
        District district457 = new District(457, state26, "Gajapati");
        District district458 = new District(458, state26, "Ganjam");
        District district459 = new District(459, state26, "Jagatsinghapur");
        District district460 = new District(460, state26, "Jajpur");
        District district461 = new District(461, state26, "Jharsuguda");
        District district462 = new District(462, state26, "Kalahandi");
        District district463 = new District(463, state26, "Kandhamal");
        District district464 = new District(464, state26, "Kendrapara");
        District district465 = new District(465, state26, "Kendujhar (Keonjhar)");
        District district466 = new District(466, state26, "Khordha");
        District district467 = new District(467, state26, "Koraput");
        District district468 = new District(468, state26, "Malkangiri");
        District district469 = new District(469, state26, "Mayurbhanj");
        District district470 = new District(470, state26, "Nabarangpur");
        District district471 = new District(471, state26, "Nayagarh");
        District district472 = new District(472, state26, "Nuapada");
        District district473 = new District(473, state26, "Puri");
        District district474 = new District(474, state26, "Rayagada");
        District district475 = new District(475, state26, "Sambalpur");
        District district476 = new District(476, state26, "Sonepur");
        District district477 = new District(477, state26, "Sundargarh");
        District district478 = new District(478, state27, "Karaikal");
        District district479 = new District(479, state27, "Yanam");
        District district480 = new District(480, state27, "Mahe");
        District district481 = new District(481, state27, "Pondicherry");
        District district482 = new District(482, state28, "Amritsar");
        District district483 = new District(483, state28, "Barnala");
        District district484 = new District(484, state28, "Bathinda");
        District district485 = new District(485, state28, "Faridkot");
        District district486 = new District(486, state28, "Fatehgarh Sahib");
        District district487 = new District(487, state28, "Fazilka");
        District district488 = new District(488, state28, "Ferozepur");
        District district489 = new District(489, state28, "Gurdaspur");
        District district490 = new District(490, state28, "Hoshiarpur");
        District district491 = new District(491, state28, "Jalandhar");
        District district492 = new District(492, state28, "Kapurthala");
        District district493 = new District(493, state28, "Ludhiana");
        District district494 = new District(494, state28, "Mansa");
        District district495 = new District(495, state28, "Moga");
        District district496 = new District(496, state28, "Muktsar");
        District district497 = new District(497, state28, "Nawanshahr (Shahid Bhagat Singh Nagar)");
        District district498 = new District(498, state28, "Parhankot");
        District district499 = new District(499, state28, "Patiala");
        District district500 = new District(500, state28, "Rupnagar");
        District district501 = new District(501, state28, "Sahibzada Ajit Singh Nagar (Mohali)");
        District district502 = new District(502, state28, "Sangrur");
        District district503 = new District(503, state28, "Tarn Taran");
        District district504 = new District(504, state29, "Ajmer");
        District district505 = new District(505, state29, "Alwar");
        District district506 = new District(506, state29, "Banswara");
        District district507 = new District(507, state29, "Baran");
        District district508 = new District(508, state29, "Barmer");
        District district509 = new District(509, state29, "Bharatpur");
        District district510 = new District(510, state29, "Bhilwara");
        District district511 = new District(511, state29, "Bikaner");
        District district512 = new District(512, state29, "Bundi");
        District district513 = new District(513, state29, "Chittorgarh");
        District district514 = new District(514, state29, "Churu");
        District district515 = new District(515, state29, "Dausa");
        District district516 = new District(516, state29, "Dholpur");
        District district517 = new District(517, state29, "Dungarpur");
        District district518 = new District(518, state29, "Hanumangarh");
        District district519 = new District(519, state29, "Jaipur");
        District district520 = new District(520, state29, "Jaisalmer");
        District district521 = new District(521, state29, "Jalore");
        District district522 = new District(522, state29, "Jhalawar");
        District district523 = new District(523, state29, "Jhunjhunu");
        District district524 = new District(524, state29, "Jodhpur");
        District district525 = new District(525, state29, "Karauli");
        District district526 = new District(526, state29, "Kota");
        District district527 = new District(527, state29, "Nagaur");
        District district528 = new District(528, state29, "Pali");
        District district529 = new District(529, state29, "Pratapgarh");
        District district530 = new District(530, state29, "Rajsamand");
        District district531 = new District(531, state29, "Sawai Madhopur");
        District district532 = new District(532, state29, "Sikar");
        District district533 = new District(533, state29, "Sirohi");
        District district534 = new District(534, state29, "Sri Ganganagar");
        District district535 = new District(535, state29, "Tonk");
        District district536 = new District(536, state29, "Udaipur");
        District district537 = new District(537, state30, "East Sikkim");
        District district538 = new District(538, state30, "North Sikkim");
        District district539 = new District(539, state30, "South Sikkim");
        District district540 = new District(540, state30, "West Sikkim");
        District district541 = new District(541, state31, "Ariyalur");
        District district542 = new District(542, state31, "Chennai");
        District district543 = new District(543, state31, "Coimbatore");
        District district544 = new District(544, state31, "Cuddalore");
        District district545 = new District(545, state31, "Dharmapuri");
        District district546 = new District(546, state31, "Dindigul");
        District district547 = new District(547, state31, "Erode");
        District district548 = new District(548, state31, "Kanchipuram");
        District district549 = new District(549, state31, "Kanyakumari");
        District district550 = new District(550, state31, "Karur");
        District district551 = new District(551, state31, "Krishnagiri");
        District district552 = new District(552, state31, "Madurai");
        District district553 = new District(553, state31, "Nagapattinam");
        District district554 = new District(554, state31, "Namakkal");
        District district555 = new District(555, state31, "Nilgiris");
        District district556 = new District(556, state31, "Perambalur");
        District district557 = new District(557, state31, "Pudkkottai");
        District district558 = new District(558, state31, "Ramanathapuram");
        District district559 = new District(559, state31, "Salem");
        District district560 = new District(560, state31, "Sivanganga");
        District district561 = new District(561, state31, "Thanjavur");
        District district562 = new District(562, state31, "Theni");
        District district563 = new District(563, state31, "Thoothukundi (Tuticorin)");
        District district564 = new District(564, state31, "Tiruchirappalli");
        District district565 = new District(565, state31, "Tirunelveli");
        District district566 = new District(566, state31, "Tiruppur");
        District district567 = new District(567, state31, "Tiruvallur");
        District district568 = new District(568, state31, "Tiruvannamalai");
        District district569 = new District(569, state31, "Tiruvarur");
        District district570 = new District(570, state31, "Vellore");
        District district571 = new District(571, state31, "Viluppuram");
        District district572 = new District(572, state31, "Virdhunagar");
        District district573 = new District(573, state32, "Adilabad");
        District district574 = new District(574, state32, "Bhadradri Kothagudem");
        District district575 = new District(575, state32, "Hyderabad");
        District district576 = new District(576, state32, "Jagital");
        District district577 = new District(577, state32, "Jangaon");
        District district578 = new District(578, state32, "Jayashankar Bhoopalpally");
        District district579 = new District(579, state32, "Jogulamba Gadwal");
        District district580 = new District(580, state32, "Kamareddy");
        District district581 = new District(581, state32, "Karimnagar");
        District district582 = new District(582, state32, "Khammam");
        District district583 = new District(583, state32, "Komaram Bheem Asifabad");
        District district584 = new District(584, state32, "Mahabubabad");
        District district585 = new District(585, state32, "Mahabubnagar");
        District district586 = new District(586, state32, "Mancherial");
        District district587 = new District(587, state32, "Medak");
        District district588 = new District(588, state32, "Medchal");
        District district589 = new District(589, state32, "Nagarkurnool");
        District district590 = new District(590, state32, "Nalgonda");
        District district591 = new District(591, state32, "Nirmal");
        District district592 = new District(592, state32, "Nizamabad");
        District district593 = new District(593, state32, "Peddapalli");
        District district594 = new District(594, state32, "Rajanna Sircilla");
        District district595 = new District(595, state32, "Rangareddy");
        District district596 = new District(596, state32, "Sangareddy");
        District district597 = new District(597, state32, "Siddipet");
        District district598 = new District(598, state32, "Suryapet");
        District district599 = new District(599, state32, "Vikarabad");
        District district600 = new District(600, state32, "Wanaparthy");
        District district601 = new District(601, state32, "Warangal (Rural)");
        District district602 = new District(602, state32, "Warangal (Urban)");
        District district603 = new District(603, state32, "Yadadri Bhuvanagiri");
        District district604 = new District(604, state33, "Dhalai");
        District district605 = new District(605, state33, "Gomati");
        District district606 = new District(606, state33, "Khowai");
        District district607 = new District(607, state33, "North Tripura");
        District district608 = new District(608, state33, "Sepahijala");
        District district609 = new District(609, state33, "South Tripura");
        District district610 = new District(610, state33, "Unakoti");
        District district611 = new District(611, state33, "West Tripura");
        District district612 = new District(612, state34, "Agra");
        District district613 = new District(613, state34, "Aligarh");
        District district614 = new District(614, state34, "Allahabad");
        District district615 = new District(615, state34, "Ambedkar Nagar");
        District district616 = new District(616, state34, "Amethi (Chatrapati sahuji Mahraj Nagar)");
        District district617 = new District(617, state34, "Amroha (J.P.Nagar)");
        District district618 = new District(618, state34, "Auraiya");
        District district619 = new District(619, state34, "Azangarh");
        District district620 = new District(620, state34, "Baghpat");
        District district621 = new District(621, state34, "Bahraich");
        District district622 = new District(622, state34, "Ballia");
        District district623 = new District(623, state34, "Balrampur");
        District district624 = new District(624, state34, "Banda");
        District district625 = new District(625, state34, "Barabanki");
        District district626 = new District(626, state34, "Bareilly");
        District district627 = new District(627, state34, "Basti");
        District district628 = new District(628, state34, "Bhadohi");
        District district629 = new District(629, state34, "Bijnor");
        District district630 = new District(630, state34, "Budaun");
        District district631 = new District(631, state34, "Buldandshahr");
        District district632 = new District(632, state34, "Chandauli");
        District district633 = new District(633, state34, "Chitrakoot");
        District district634 = new District(634, state34, "Deoria");
        District district635 = new District(635, state34, "Etah");
        District district636 = new District(636, state34, "Etawah");
        District district637 = new District(637, state34, "Faizabad");
        District district638 = new District(638, state34, "Farrukhabad");
        District district639 = new District(639, state34, "Fatehpur");
        District district640 = new District(640, state34, "Firozabad");
        District district641 = new District(641, state34, "Gautam Buddha Nagar");
        District district642 = new District(642, state34, "Ghaziabad");
        District district643 = new District(643, state34, "Ghazipur");
        District district644 = new District(644, state34, "Gonda");
        District district645 = new District(645, state34, "Gorakhpur");
        District district646 = new District(646, state34, "Hamirpur");
        District district647 = new District(647, state34, "Hapur (Panchsheel Nagar)");
        District district648 = new District(648, state34, "Hardoi");
        District district649 = new District(649, state34, "Hathras");
        District district650 = new District(650, state34, "Jalaun");
        District district651 = new District(651, state34, "Jaunpur");
        District district652 = new District(652, state34, "Jhansi");
        District district653 = new District(653, state34, "Kannauj");
        District district654 = new District(654, state34, "Kanpur Dehat");
        District district655 = new District(655, state34, "Kanpur Nagar");
        District district656 = new District(656, state34, "Kanshiram Nagar (Kasganj)");
        District district657 = new District(657, state34, "Kaushambi");
        District district658 = new District(658, state34, "Kushinagar (Padrauna)");
        District district659 = new District(659, state34, "Lakhimpur - Kheri");
        District district660 = new District(660, state34, "Lalitpur");
        District district661 = new District(661, state34, "Lucknow");
        District district662 = new District(662, state34, "Maharajganj");
        District district663 = new District(663, state34, "Mahoba");
        District district664 = new District(664, state34, "Mainpuri");
        District district665 = new District(665, state34, "Mathura");
        District district666 = new District(666, state34, "Mau");
        District district667 = new District(667, state34, "Meerut");
        District district668 = new District(668, state34, "Mizapur");
        District district669 = new District(669, state34, "Moradabad");
        District district670 = new District(670, state34, "Muzaffarnagar");
        District district671 = new District(671, state34, "Pilibhit");
        District district672 = new District(672, state34, "Pratapgarh");
        District district673 = new District(673, state34, "RaeBareli");
        District district674 = new District(674, state34, "Rampur");
        District district675 = new District(675, state34, "Saharanpur");
        District district676 = new District(676, state34, "Sambhal (Bhim Nagar)");
        District district677 = new District(677, state34, "Sant Kabir Nagar");
        District district678 = new District(678, state34, "Shahjahanpur");
        District district679 = new District(679, state34, "Shamali (Prabuddh Nagar)");
        District district680 = new District(680, state34, "Shravasti");
        District district681 = new District(681, state34, "Siddharth Nagar");
        District district682 = new District(682, state34, "Sitapur");
        District district683 = new District(683, state34, "Sonbhandra");
        District district684 = new District(684, state34, "Sultanpur");
        District district685 = new District(685, state34, "Unnao");
        District district686 = new District(686, state34, "Varanasi");
        District district687 = new District(687, state35, "Almora");
        District district688 = new District(688, state35, "Bageshwar");
        District district689 = new District(689, state35, "Chamoli");
        District district690 = new District(690, state35, "Champawat");
        District district691 = new District(691, state35, "Dehradun");
        District district692 = new District(692, state35, "Haridwar");
        District district693 = new District(693, state35, "Nainital");
        District district694 = new District(694, state35, "Pauri Garhwal");
        District district695 = new District(695, state35, "Pithoragarh");
        District district696 = new District(696, state35, "Rudraprayag");
        District district697 = new District(697, state35, "Tehri Garhwal");
        District district698 = new District(698, state35, "Udham Singh Nagar");
        District district699 = new District(699, state35, "Uttarkashi");
        District district700 = new District(700, state36, "Alipurduar");
        District district701 = new District(701, state36, "Bankura");
        District district702 = new District(702, state36, "Birbhum");
        District district703 = new District(703, state36, "Cooch Behar");
        District district704 = new District(704, state36, "Dakshin Dinajpur (South Dinajpur)");
        District district705 = new District(705, state36, "Darjeeling");
        District district706 = new District(706, state36, "Hooghly");
        District district707 = new District(707, state36, "Howrah");
        District district708 = new District(708, state36, "Jalpaiguri");
        District district709 = new District(709, state36, "Jhargram");
        District district710 = new District(710, state36, "Kalimpong");
        District district711 = new District(711, state36, "Kolkata");
        District district712 = new District(712, state36, "Malda");
        District district713 = new District(713, state36, "Murshidabad");
        District district714 = new District(714, state36, "Nadia");
        District district715 = new District(715, state36, "North 24 Parganas");
        District district716 = new District(716, state36, "Paschim Medinipur (West Medinipur)");
        District district717 = new District(717, state36, "Paschim (West) Burdwan (Bardhaman)");
        District district718 = new District(718, state36, "Purba Burdwan (Bardhaman)");
        District district719 = new District(719, state36, "Purba Medinipur (East Medinipur)");
        District district720 = new District(720, state36, "Purulia");
        District district721 = new District(721, state36, "South 24 Parganas");
        District district722 = new District(722, state36, "Uttar Dinajpur (North Dinajpur)");


        districts.add(district0);
        districts.add(district1);
        districts.add(district2);
        districts.add(district3);
        districts.add(district4);
        districts.add(district5);
        districts.add(district6);
        districts.add(district7);
        districts.add(district8);
        districts.add(district9);
        districts.add(district10);
        districts.add(district11);
        districts.add(district12);
        districts.add(district13);
        districts.add(district14);
        districts.add(district15);
        districts.add(district16);
        districts.add(district17);
        districts.add(district18);
        districts.add(district19);
        districts.add(district20);
        districts.add(district21);
        districts.add(district22);
        districts.add(district23);
        districts.add(district24);
        districts.add(district25);
        districts.add(district26);
        districts.add(district27);
        districts.add(district28);
        districts.add(district29);
        districts.add(district30);
        districts.add(district31);
        districts.add(district32);
        districts.add(district33);
        districts.add(district34);
        districts.add(district35);
        districts.add(district36);
        districts.add(district37);
        districts.add(district38);
        districts.add(district39);
        districts.add(district40);
        districts.add(district41);
        districts.add(district42);
        districts.add(district43);
        districts.add(district44);
        districts.add(district45);
        districts.add(district46);
        districts.add(district47);
        districts.add(district48);
        districts.add(district49);
        districts.add(district50);
        districts.add(district51);
        districts.add(district52);
        districts.add(district53);
        districts.add(district54);
        districts.add(district55);
        districts.add(district56);
        districts.add(district57);
        districts.add(district58);
        districts.add(district59);
        districts.add(district60);
        districts.add(district61);
        districts.add(district62);
        districts.add(district63);
        districts.add(district64);
        districts.add(district65);
        districts.add(district66);
        districts.add(district67);
        districts.add(district68);
        districts.add(district69);
        districts.add(district70);
        districts.add(district71);
        districts.add(district72);
        districts.add(district73);
        districts.add(district74);
        districts.add(district75);
        districts.add(district76);
        districts.add(district77);
        districts.add(district78);
        districts.add(district79);
        districts.add(district80);
        districts.add(district81);
        districts.add(district82);
        districts.add(district83);
        districts.add(district84);
        districts.add(district85);
        districts.add(district86);
        districts.add(district87);
        districts.add(district88);
        districts.add(district89);
        districts.add(district90);
        districts.add(district91);
        districts.add(district92);
        districts.add(district93);
        districts.add(district94);
        districts.add(district95);
        districts.add(district96);
        districts.add(district97);
        districts.add(district98);
        districts.add(district99);
        districts.add(district100);
        districts.add(district101);
        districts.add(district102);
        districts.add(district103);
        districts.add(district104);
        districts.add(district105);
        districts.add(district106);
        districts.add(district107);
        districts.add(district108);
        districts.add(district109);
        districts.add(district110);
        districts.add(district111);
        districts.add(district112);
        districts.add(district113);
        districts.add(district114);
        districts.add(district115);
        districts.add(district116);
        districts.add(district117);
        districts.add(district118);
        districts.add(district119);
        districts.add(district120);
        districts.add(district121);
        districts.add(district122);
        districts.add(district123);
        districts.add(district124);
        districts.add(district125);
        districts.add(district126);
        districts.add(district127);
        districts.add(district128);
        districts.add(district129);
        districts.add(district130);
        districts.add(district131);
        districts.add(district132);
        districts.add(district133);
        districts.add(district134);
        districts.add(district135);
        districts.add(district136);
        districts.add(district137);
        districts.add(district138);
        districts.add(district139);
        districts.add(district140);
        districts.add(district141);
        districts.add(district142);
        districts.add(district143);
        districts.add(district144);
        districts.add(district145);
        districts.add(district146);
        districts.add(district147);
        districts.add(district148);
        districts.add(district149);
        districts.add(district150);
        districts.add(district151);
        districts.add(district152);
        districts.add(district153);
        districts.add(district154);
        districts.add(district155);
        districts.add(district156);
        districts.add(district157);
        districts.add(district158);
        districts.add(district159);
        districts.add(district160);
        districts.add(district161);
        districts.add(district162);
        districts.add(district163);
        districts.add(district164);
        districts.add(district165);
        districts.add(district166);
        districts.add(district167);
        districts.add(district168);
        districts.add(district169);
        districts.add(district170);
        districts.add(district171);
        districts.add(district172);
        districts.add(district173);
        districts.add(district174);
        districts.add(district175);
        districts.add(district176);
        districts.add(district177);
        districts.add(district178);
        districts.add(district179);
        districts.add(district180);
        districts.add(district181);
        districts.add(district182);
        districts.add(district183);
        districts.add(district184);
        districts.add(district185);
        districts.add(district186);
        districts.add(district187);
        districts.add(district188);
        districts.add(district189);
        districts.add(district190);
        districts.add(district191);
        districts.add(district192);
        districts.add(district193);
        districts.add(district194);
        districts.add(district195);
        districts.add(district196);
        districts.add(district197);
        districts.add(district198);
        districts.add(district199);
        districts.add(district200);
        districts.add(district201);
        districts.add(district202);
        districts.add(district203);
        districts.add(district204);
        districts.add(district205);
        districts.add(district206);
        districts.add(district207);
        districts.add(district208);
        districts.add(district209);
        districts.add(district210);
        districts.add(district211);
        districts.add(district212);
        districts.add(district213);
        districts.add(district214);
        districts.add(district215);
        districts.add(district216);
        districts.add(district217);
        districts.add(district218);
        districts.add(district219);
        districts.add(district220);
        districts.add(district221);
        districts.add(district222);
        districts.add(district223);
        districts.add(district224);
        districts.add(district225);
        districts.add(district226);
        districts.add(district227);
        districts.add(district228);
        districts.add(district229);
        districts.add(district230);
        districts.add(district231);
        districts.add(district232);
        districts.add(district233);
        districts.add(district234);
        districts.add(district235);
        districts.add(district236);
        districts.add(district237);
        districts.add(district238);
        districts.add(district239);
        districts.add(district240);
        districts.add(district241);
        districts.add(district242);
        districts.add(district243);
        districts.add(district244);
        districts.add(district245);
        districts.add(district246);
        districts.add(district247);
        districts.add(district248);
        districts.add(district249);
        districts.add(district250);
        districts.add(district251);
        districts.add(district252);
        districts.add(district253);
        districts.add(district254);
        districts.add(district255);
        districts.add(district256);
        districts.add(district257);
        districts.add(district258);
        districts.add(district259);
        districts.add(district260);
        districts.add(district261);
        districts.add(district262);
        districts.add(district263);
        districts.add(district264);
        districts.add(district265);
        districts.add(district266);
        districts.add(district267);
        districts.add(district268);
        districts.add(district269);
        districts.add(district270);
        districts.add(district271);
        districts.add(district272);
        districts.add(district273);
        districts.add(district274);
        districts.add(district275);
        districts.add(district276);
        districts.add(district277);
        districts.add(district278);
        districts.add(district279);
        districts.add(district280);
        districts.add(district281);
        districts.add(district282);
        districts.add(district283);
        districts.add(district284);
        districts.add(district285);
        districts.add(district286);
        districts.add(district287);
        districts.add(district288);
        districts.add(district289);
        districts.add(district290);
        districts.add(district291);
        districts.add(district292);
        districts.add(district293);
        districts.add(district294);
        districts.add(district295);
        districts.add(district296);
        districts.add(district297);
        districts.add(district298);
        districts.add(district299);
        districts.add(district300);
        districts.add(district301);
        districts.add(district302);
        districts.add(district303);
        districts.add(district304);
        districts.add(district305);
        districts.add(district306);
        districts.add(district307);
        districts.add(district308);
        districts.add(district309);
        districts.add(district310);
        districts.add(district311);
        districts.add(district312);
        districts.add(district313);
        districts.add(district314);
        districts.add(district315);
        districts.add(district316);
        districts.add(district317);
        districts.add(district318);
        districts.add(district319);
        districts.add(district320);
        districts.add(district321);
        districts.add(district322);
        districts.add(district323);
        districts.add(district324);
        districts.add(district325);
        districts.add(district326);
        districts.add(district327);
        districts.add(district328);
        districts.add(district329);
        districts.add(district330);
        districts.add(district331);
        districts.add(district332);
        districts.add(district333);
        districts.add(district334);
        districts.add(district335);
        districts.add(district336);
        districts.add(district337);
        districts.add(district338);
        districts.add(district339);
        districts.add(district340);
        districts.add(district341);
        districts.add(district342);
        districts.add(district343);
        districts.add(district344);
        districts.add(district345);
        districts.add(district346);
        districts.add(district347);
        districts.add(district348);
        districts.add(district349);
        districts.add(district350);
        districts.add(district351);
        districts.add(district352);
        districts.add(district353);
        districts.add(district354);
        districts.add(district355);
        districts.add(district356);
        districts.add(district357);
        districts.add(district358);
        districts.add(district359);
        districts.add(district360);
        districts.add(district361);
        districts.add(district362);
        districts.add(district363);
        districts.add(district364);
        districts.add(district365);
        districts.add(district366);
        districts.add(district367);
        districts.add(district368);
        districts.add(district369);
        districts.add(district370);
        districts.add(district371);
        districts.add(district372);
        districts.add(district373);
        districts.add(district374);
        districts.add(district375);
        districts.add(district376);
        districts.add(district377);
        districts.add(district378);
        districts.add(district379);
        districts.add(district380);
        districts.add(district381);
        districts.add(district382);
        districts.add(district383);
        districts.add(district384);
        districts.add(district385);
        districts.add(district386);
        districts.add(district387);
        districts.add(district388);
        districts.add(district389);
        districts.add(district390);
        districts.add(district391);
        districts.add(district392);
        districts.add(district393);
        districts.add(district394);
        districts.add(district395);
        districts.add(district396);
        districts.add(district397);
        districts.add(district398);
        districts.add(district399);
        districts.add(district400);
        districts.add(district401);
        districts.add(district402);
        districts.add(district403);
        districts.add(district404);
        districts.add(district405);
        districts.add(district406);
        districts.add(district407);
        districts.add(district408);
        districts.add(district409);
        districts.add(district410);
        districts.add(district411);
        districts.add(district412);
        districts.add(district413);
        districts.add(district414);
        districts.add(district415);
        districts.add(district416);
        districts.add(district417);
        districts.add(district418);
        districts.add(district419);
        districts.add(district420);
        districts.add(district421);
        districts.add(district422);
        districts.add(district423);
        districts.add(district424);
        districts.add(district425);
        districts.add(district426);
        districts.add(district427);
        districts.add(district428);
        districts.add(district429);
        districts.add(district430);
        districts.add(district431);
        districts.add(district432);
        districts.add(district433);
        districts.add(district434);
        districts.add(district435);
        districts.add(district436);
        districts.add(district437);
        districts.add(district438);
        districts.add(district439);
        districts.add(district440);
        districts.add(district441);
        districts.add(district442);
        districts.add(district443);
        districts.add(district444);
        districts.add(district445);
        districts.add(district446);
        districts.add(district447);
        districts.add(district448);
        districts.add(district449);
        districts.add(district450);
        districts.add(district451);
        districts.add(district452);
        districts.add(district453);
        districts.add(district454);
        districts.add(district455);
        districts.add(district456);
        districts.add(district457);
        districts.add(district458);
        districts.add(district459);
        districts.add(district460);
        districts.add(district461);
        districts.add(district462);
        districts.add(district463);
        districts.add(district464);
        districts.add(district465);
        districts.add(district466);
        districts.add(district467);
        districts.add(district468);
        districts.add(district469);
        districts.add(district470);
        districts.add(district471);
        districts.add(district472);
        districts.add(district473);
        districts.add(district474);
        districts.add(district475);
        districts.add(district476);
        districts.add(district477);
        districts.add(district478);
        districts.add(district479);
        districts.add(district480);
        districts.add(district481);
        districts.add(district482);
        districts.add(district483);
        districts.add(district484);
        districts.add(district485);
        districts.add(district486);
        districts.add(district487);
        districts.add(district488);
        districts.add(district489);
        districts.add(district490);
        districts.add(district491);
        districts.add(district492);
        districts.add(district493);
        districts.add(district494);
        districts.add(district495);
        districts.add(district496);
        districts.add(district497);
        districts.add(district498);
        districts.add(district499);
        districts.add(district500);
        districts.add(district501);
        districts.add(district502);
        districts.add(district503);
        districts.add(district504);
        districts.add(district505);
        districts.add(district506);
        districts.add(district507);
        districts.add(district508);
        districts.add(district509);
        districts.add(district510);
        districts.add(district511);
        districts.add(district512);
        districts.add(district513);
        districts.add(district514);
        districts.add(district515);
        districts.add(district516);
        districts.add(district517);
        districts.add(district518);
        districts.add(district519);
        districts.add(district520);
        districts.add(district521);
        districts.add(district522);
        districts.add(district523);
        districts.add(district524);
        districts.add(district525);
        districts.add(district526);
        districts.add(district527);
        districts.add(district528);
        districts.add(district529);
        districts.add(district530);
        districts.add(district531);
        districts.add(district532);
        districts.add(district533);
        districts.add(district534);
        districts.add(district535);
        districts.add(district536);
        districts.add(district537);
        districts.add(district538);
        districts.add(district539);
        districts.add(district540);
        districts.add(district541);
        districts.add(district542);
        districts.add(district543);
        districts.add(district544);
        districts.add(district545);
        districts.add(district546);
        districts.add(district547);
        districts.add(district548);
        districts.add(district549);
        districts.add(district550);
        districts.add(district551);
        districts.add(district552);
        districts.add(district553);
        districts.add(district554);
        districts.add(district555);
        districts.add(district556);
        districts.add(district557);
        districts.add(district558);
        districts.add(district559);
        districts.add(district560);
        districts.add(district561);
        districts.add(district562);
        districts.add(district563);
        districts.add(district564);
        districts.add(district565);
        districts.add(district566);
        districts.add(district567);
        districts.add(district568);
        districts.add(district569);
        districts.add(district570);
        districts.add(district571);
        districts.add(district572);
        districts.add(district573);
        districts.add(district574);
        districts.add(district575);
        districts.add(district576);
        districts.add(district577);
        districts.add(district578);
        districts.add(district579);
        districts.add(district580);
        districts.add(district581);
        districts.add(district582);
        districts.add(district583);
        districts.add(district584);
        districts.add(district585);
        districts.add(district586);
        districts.add(district587);
        districts.add(district588);
        districts.add(district589);
        districts.add(district590);
        districts.add(district591);
        districts.add(district592);
        districts.add(district593);
        districts.add(district594);
        districts.add(district595);
        districts.add(district596);
        districts.add(district597);
        districts.add(district598);
        districts.add(district599);
        districts.add(district600);
        districts.add(district601);
        districts.add(district602);
        districts.add(district603);
        districts.add(district604);
        districts.add(district605);
        districts.add(district606);
        districts.add(district607);
        districts.add(district608);
        districts.add(district609);
        districts.add(district610);
        districts.add(district611);
        districts.add(district612);
        districts.add(district613);
        districts.add(district614);
        districts.add(district615);
        districts.add(district616);
        districts.add(district617);
        districts.add(district618);
        districts.add(district619);
        districts.add(district620);
        districts.add(district621);
        districts.add(district622);
        districts.add(district623);
        districts.add(district624);
        districts.add(district625);
        districts.add(district626);
        districts.add(district627);
        districts.add(district628);
        districts.add(district629);
        districts.add(district630);
        districts.add(district631);
        districts.add(district632);
        districts.add(district633);
        districts.add(district634);
        districts.add(district635);
        districts.add(district636);
        districts.add(district637);
        districts.add(district638);
        districts.add(district639);
        districts.add(district640);
        districts.add(district641);
        districts.add(district642);
        districts.add(district643);
        districts.add(district644);
        districts.add(district645);
        districts.add(district646);
        districts.add(district647);
        districts.add(district648);
        districts.add(district649);
        districts.add(district650);
        districts.add(district651);
        districts.add(district652);
        districts.add(district653);
        districts.add(district654);
        districts.add(district655);
        districts.add(district656);
        districts.add(district657);
        districts.add(district658);
        districts.add(district659);
        districts.add(district660);
        districts.add(district661);
        districts.add(district662);
        districts.add(district663);
        districts.add(district664);
        districts.add(district665);
        districts.add(district666);
        districts.add(district667);
        districts.add(district668);
        districts.add(district669);
        districts.add(district670);
        districts.add(district671);
        districts.add(district672);
        districts.add(district673);
        districts.add(district674);
        districts.add(district675);
        districts.add(district676);
        districts.add(district677);
        districts.add(district678);
        districts.add(district679);
        districts.add(district680);
        districts.add(district681);
        districts.add(district682);
        districts.add(district683);
        districts.add(district684);
        districts.add(district685);
        districts.add(district686);
        districts.add(district687);
        districts.add(district688);
        districts.add(district689);
        districts.add(district690);
        districts.add(district691);
        districts.add(district692);
        districts.add(district693);
        districts.add(district694);
        districts.add(district695);
        districts.add(district696);
        districts.add(district697);
        districts.add(district698);
        districts.add(district699);
        districts.add(district700);
        districts.add(district701);
        districts.add(district702);
        districts.add(district703);
        districts.add(district704);
        districts.add(district705);
        districts.add(district706);
        districts.add(district707);
        districts.add(district708);
        districts.add(district709);
        districts.add(district710);
        districts.add(district711);
        districts.add(district712);
        districts.add(district713);
        districts.add(district714);
        districts.add(district715);
        districts.add(district716);
        districts.add(district717);
        districts.add(district718);
        districts.add(district719);
        districts.add(district720);
        districts.add(district721);
        districts.add(district722);


    }

}





