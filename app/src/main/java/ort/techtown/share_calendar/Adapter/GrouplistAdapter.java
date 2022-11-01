package ort.techtown.share_calendar.Adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ort.techtown.share_calendar.Data.CalendarUtil;
import ort.techtown.share_calendar.Data.Grouplist;
import ort.techtown.share_calendar.R;

public class GrouplistAdapter extends RecyclerView.Adapter<ort.techtown.share_calendar.Adapter.GrouplistAdapter.GrouplistViewHolder>{
    ArrayList<Grouplist> datalist;


    public GrouplistAdapter(ArrayList<Grouplist> grouplist) {
        datalist = grouplist;
    }

    @NonNull
    @Override
    public GrouplistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.grouplist_cell, parent, false);
        return new GrouplistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrouplistAdapter.GrouplistViewHolder holder, int position) {
        String grn = datalist.get(position).getGrname();
        holder.tv_grname.setText(grn);
        Log.e("왜 안될까여?",grn);

        Log.e("호롤롤로12",String.valueOf(holder.ch_isSeen.isChecked())+grn);
        holder.itemView.findViewById(R.id.ch_isSeen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("지금이니?",String.valueOf(holder.ch_isSeen.isChecked()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class GrouplistViewHolder extends RecyclerView.ViewHolder{

        CheckBox ch_isSeen;
        TextView tv_grname;
        View parentView;
        public GrouplistViewHolder(@NonNull View itemView) {
            super(itemView);
            ch_isSeen = itemView.findViewById(R.id.ch_isSeen);
            tv_grname = itemView.findViewById(R.id.tv_grname);
            parentView = itemView.findViewById(R.id.parent_view);
        }
    }

}
