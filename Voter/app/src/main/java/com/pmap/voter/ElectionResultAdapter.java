package com.pmap.voter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pmap.voter.logic.P;
import com.pmap.voter.models.ElectionListDetails;
import com.pmap.voter.models.ElectionResultDetails;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

    public class ElectionResultAdapter extends RecyclerView.Adapter<ElectionResultAdapter.ViewHolder> {
        private List<ElectionResultDetails> electionResultDetails = new ArrayList<>();
        Context context;
        private String el_id = "";
        private SweetAlertDialog dialog;


        public ElectionResultAdapter(Context context, List<ElectionResultDetails> electionResultDetails) {
            this.context = context;
            this.electionResultDetails = electionResultDetails;

        }

        @Override
        public ElectionResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.election_result, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ElectionResultAdapter.ViewHolder vh = new ElectionResultAdapter.ViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ElectionResultAdapter.ViewHolder holder, int position) {
            P.LogD("onBindViewHolder [" + holder.img_candidate1.hashCode() + "] position=" + position);
            P.LogD("Image: " + P.Gallery_PATH + electionResultDetails.get(position).candidates_photo);
            P.LogD("ID: " + electionResultDetails.get(position).er_id);
            P.LogD("Count: " + electionResultDetails.get(position).er_count);
            P.LogD("Candidate_id: " + electionResultDetails.get(position).er_candidates_id);
            P.LogD("Candidate_name: " + electionResultDetails.get(position).candidates_name);

            holder.txt_er_id.setText(electionResultDetails.get(position).er_id);
            holder.txt_er_count.setText(electionResultDetails.get(position).er_count);
            holder.txt_candidate_id.setText(electionResultDetails.get(position).er_candidates_id);
            holder.txt_candidate_name.setText(electionResultDetails.get(position).candidates_name);


            Glide.with(holder.itemView.getContext()).load(P.Gallery_PATH + electionResultDetails.get(position).candidates_photo)
                    .into(holder.img_candidate1);


        }


        @Override
        public int getItemCount() {
            return electionResultDetails.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // init the item view's

ImageView img_candidate1;
            TextView txt_er_id,txt_er_count,txt_candidate_id,txt_candidate_name;
            Button btn_vote;

            public ViewHolder(View itemView) {
                super(itemView);
                txt_er_id = (TextView) itemView.findViewById(R.id.txt_er_id);
                txt_er_count = (TextView) itemView.findViewById(R.id.txt_er_count);
                txt_candidate_id = (TextView) itemView.findViewById(R.id.txt_candidate_id);
                txt_candidate_name = (TextView) itemView.findViewById(R.id.txt_candidate_name);
                img_candidate1 = (ImageView) itemView.findViewById(R.id.img_candidate1);
                // this.btn_vote = (Button) itemView.findViewById(R.id.btn_vote);



            }
        }
    }



