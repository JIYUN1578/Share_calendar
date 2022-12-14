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
    public GrouplistAdapter() {
        datalist = null;
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
        if(datalist.get(position).isSeen()) {
            Log.d("exception for privacy12",datalist.get(position).getGrname()+" "
                    +String.valueOf(datalist.get(position).isSeen()));
            holder.ch_isSeen.setChecked(true);

        }
        else{
            Log.d("exception for privacy1",datalist.get(position).getGrname()+" "
                    +String.valueOf(datalist.get(position).isSeen()));
            holder.ch_isSeen.setChecked(false);
        }
        holder.tv_grname.setText(grn);
        holder.itemView.findViewById(R.id.ch_isSeen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datalist.get(position).setSeen(holder.ch_isSeen.isChecked());
            }
        });
    }

    public ArrayList<Grouplist> getDatalist(){
        if(datalist != null) return datalist;
        else {
            ArrayList<Grouplist> temp = new ArrayList<>();
            Grouplist grouplist = new Grouplist("??????", false)    ;
            temp.add(grouplist);
            return temp;
        }
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
            ch_isSeen.setButtonDrawable(R.drawable.check);
            tv_grname = itemView.findViewById(R.id.tv_grname);
            parentView = itemView.findViewById(R.id.parent_view);
        }
    }

}
