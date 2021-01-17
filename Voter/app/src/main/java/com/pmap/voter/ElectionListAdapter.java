package com.pmap.voter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pmap.voter.Api.Api;
import com.pmap.voter.Api.WebServices;
import com.pmap.voter.activities.CandidateListActivity;
import com.pmap.voter.activities.ElectionListActivity;
import com.pmap.voter.activities.ElectionResultActivity;
import com.pmap.voter.activities.FingerprintActivity;
import com.pmap.voter.activities.UserLoginActivity;
import com.pmap.voter.logic.P;
import com.pmap.voter.models.CandidateDetails;
import com.pmap.voter.models.CandidateRegisterInput;
import com.pmap.voter.models.ElectionListDetails;
import com.pmap.voter.models.ElectionNameInput;
import com.pmap.voter.models.ElectionNameOutput;
import com.pmap.voter.models.VoteResultInput;
import com.pmap.voter.models.VoteResultOutput;
import com.pmap.voter.models.VoterRegisterInput;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ElectionListAdapter extends RecyclerView.Adapter<ElectionListAdapter.ViewHolder> {
    private List<ElectionListDetails> electionListDetails = new ArrayList<>();
    Context context;
    private String el_id = "";
    private SweetAlertDialog dialog;


    public ElectionListAdapter(Context context, List<ElectionListDetails> electionListDetails) {
        this.context = context;
        this.electionListDetails = electionListDetails;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.electionlist, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        P.LogD("onBindViewHolder [" + holder.txt_ele_name.hashCode() + "] position=" + position);
        P.LogD("ID: " + electionListDetails.get(position).el_id);
        P.LogD("Name: " + electionListDetails.get(position).el_name);

        holder.txt_ele_id.setText(electionListDetails.get(position).el_id);
        holder.txt_ele_name.setText(electionListDetails.get(position).el_name);
        holder.lnr_ele_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ElectionResultActivity.class);
              intent.putExtra("el_id",electionListDetails.get(position).el_id);
                //getIntent().getStringExtra("el_id"),
                context.startActivity(intent);

            }
        });


    }


    @Override
    public int getItemCount() {
        return electionListDetails.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        public LinearLayout lnr_ele_list;
        TextView txt_ele_id,txt_ele_name;
        Button btn_vote;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_ele_id = (TextView) itemView.findViewById(R.id.txt_ele_id);

            txt_ele_name = (TextView) itemView.findViewById(R.id.txt_ele_name);
           // this.btn_vote = (Button) itemView.findViewById(R.id.btn_vote);
            lnr_ele_list=(LinearLayout) itemView.findViewById(R.id.lnr_ele_list);

        }
    }
}

