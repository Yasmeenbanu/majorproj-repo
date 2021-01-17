package com.pmap.voter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pmap.voter.Api.Api;
import com.pmap.voter.Api.WebServices;
import com.pmap.voter.activities.CandidateListActivity;
import com.pmap.voter.activities.FingerprintActivity;
import com.pmap.voter.activities.UserLoginActivity;
import com.pmap.voter.logic.P;
import com.pmap.voter.models.CandidateDetails;
import com.pmap.voter.models.CandidateRegisterInput;
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

public class CandidateAdapterList extends RecyclerView.Adapter<CandidateAdapterList.ViewHolder> {
    private List<CandidateDetails> candidateDetailsList = new ArrayList<>();
    Context context;
    private String el_id = "";
    private SweetAlertDialog dialog;
    private String elID;

    public CandidateAdapterList(Context context, List<CandidateDetails> candidateDetailsList, String elID) {
        this.context = context;
        this.candidateDetailsList = candidateDetailsList;
        this.elID = elID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidates_list, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        P.LogD("onBindViewHolder [" + holder.img_candidate.hashCode() + "] position=" + position);
        P.LogD("Tittle: " + candidateDetailsList.get(position).candidates_party);
        P.LogD("Image: " + P.Gallery_PATH + candidateDetailsList.get(position).candidates_photo);
        holder.txt_candidate.setText(candidateDetailsList.get(position).candidates_party);
        holder.btn_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, FingerprintActivity.class);
                intent.putExtra("el_id", elID);
                intent.putExtra("voter_id", UserLoginActivity.voterID);
                intent.putExtra("candidate_id", candidateDetailsList.get(position).getCandidates_id());
                ((CandidateListActivity) context).startActivityForResult(intent, CandidateListActivity.FOR_FINGERPRINT);
            }


        });
        Glide.with(holder.itemView.getContext()).load(P.Gallery_PATH + candidateDetailsList.get(position).candidates_photo)
                .into(holder.img_candidate);


    }


    @Override
    public int getItemCount() {
        return candidateDetailsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        ImageView img_candidate;
        TextView txt_candidate;
        Button btn_vote;

        public ViewHolder(View itemView) {
            super(itemView);
            img_candidate = (ImageView) itemView.findViewById(R.id.img_candidate);

            txt_candidate = (TextView) itemView.findViewById(R.id.txt_candidate);
            this.btn_vote = (Button) itemView.findViewById(R.id.btn_vote);


        }
    }
}

