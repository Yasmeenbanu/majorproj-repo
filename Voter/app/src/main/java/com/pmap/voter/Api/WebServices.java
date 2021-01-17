package com.pmap.voter.Api;


import com.pmap.voter.models.CandidateListOutput;
import com.pmap.voter.models.CandidateRegisterInput;
import com.pmap.voter.models.CandidateRegisterOutput;
import com.pmap.voter.models.CasteVoteInput;
import com.pmap.voter.models.CasteVoteOutput;
import com.pmap.voter.models.ElectionListOutput;
import com.pmap.voter.models.ElectionNameInput;
import com.pmap.voter.models.ElectionNameOutput;
import com.pmap.voter.models.ElectionResultInput;
import com.pmap.voter.models.ElectionResultOutput;
import com.pmap.voter.models.UserLoginInput;
import com.pmap.voter.models.UserLoginOutput;
import com.pmap.voter.models.ViewResultOutput;
import com.pmap.voter.models.VoteResultInput;
import com.pmap.voter.models.VoteResultOutput;
import com.pmap.voter.models.VoterRegisterInput;
import com.pmap.voter.models.VoterRegisterOutput;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebServices {
    @POST("VoterRegister_c/voterRegister")
    Call<VoterRegisterOutput> voterRegister(@Body VoterRegisterInput input);

    @POST("CandidateRegister_c/candidateRegister")
    Call<CandidateRegisterOutput> candidateRegister(@Body CandidateRegisterInput input);

    @POST("ElectionName_c/createelection")
    Call<ElectionNameOutput> createelection(@Body ElectionNameInput input);

    @POST("CandidateList_c/getCandidatesList")
    Call<CandidateListOutput>getCandidatesList();

    @POST("GetElections_c/getElectionList")
    Call<ElectionListOutput>getElectionList();

    @POST("VoterLogin_c/userLogin")
    Call<UserLoginOutput> userLogin(@Body UserLoginInput input);

    @POST("UserCastVote_c/castVote")
    Call<CasteVoteOutput> castVote(@Body CasteVoteInput input);


    @POST("GetElectionResult_c/getElectionResultList")
    Call<ElectionResultOutput>getElectionResultList(@Body ElectionResultInput input);
}



