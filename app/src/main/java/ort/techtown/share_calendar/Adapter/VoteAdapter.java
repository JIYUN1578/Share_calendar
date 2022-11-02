package ort.techtown.share_calendar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ort.techtown.share_calendar.Data.Vote;
import ort.techtown.share_calendar.NoticeActivity;
import ort.techtown.share_calendar.R;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.VoteViewHolder> {

    private ArrayList<Vote> arrayList;
    private Context context;

    public VoteAdapter(ArrayList<Vote> arrayList, Context context) {
        this.arrayList = arrayList;
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
        holder.tv_num.setText(arrayList.get(position).getVoteNum().toString());
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
        }
    }
}
