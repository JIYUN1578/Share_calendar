package ort.techtown.share_calendar.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ort.techtown.share_calendar.Data.Grouplist;
import ort.techtown.share_calendar.Data.Vote;
import ort.techtown.share_calendar.NoticeActivity;
import ort.techtown.share_calendar.R;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.VoteViewHolder> {

    private ArrayList<Vote> arrayList;
    private ArrayList<Boolean> voteList;
    private Boolean voteResult;
    private Context context;

    public VoteAdapter(ArrayList<Vote> arrayList, ArrayList<Boolean> voteList, Boolean voteResult, Context context) {
        this.arrayList = arrayList;
        this.voteList = voteList;
        this.voteResult = voteResult;
        this.context = context;
    }

    @NonNull
    @Override
    public VoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_recycler_item, parent, false);
        VoteViewHolder holder = new VoteViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VoteViewHolder holder, int position) {
        holder.tv_votesummary.setText(arrayList.get(position).getVoteSummary());
        holder.tv_num.setText(arrayList.get(position).getVoteNum().toString() + '명');
        holder.checkbox_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(voteList.get(position) == false) {
                   voteList.set(position, true);
               }
               else{
                   voteList.set(position, false);
               }
            }
        });
    }

    public ArrayList<Boolean> getCheckList() {
        return voteList;
    }

    @Override
    public int getItemCount() {return (arrayList!=null?arrayList.size():0);}

    public class VoteViewHolder extends RecyclerView.ViewHolder {
        TextView tv_votesummary, tv_num;
        CheckBox checkbox_vote;

        public VoteViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_votesummary = itemView.findViewById(R.id.tv_votesummary);
            this.tv_num = itemView.findViewById(R.id.tv_num);
            this.checkbox_vote = itemView.findViewById(R.id.checkbox_vote);
            if(voteResult == true) {
                checkbox_vote.setVisibility(View.GONE);
                tv_num.setVisibility(View.VISIBLE);
            }
            if(voteResult == false) {
                checkbox_vote.setVisibility(View.VISIBLE);
                tv_num.setVisibility(View.GONE);
            }
            checkbox_vote.setButtonDrawable(R.drawable.check);
        }
    }
}
