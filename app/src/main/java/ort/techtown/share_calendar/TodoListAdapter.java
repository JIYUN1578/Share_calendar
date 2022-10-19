package ort.techtown.share_calendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

import ort.techtown.share_calendar.Data.Info;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {

    ArrayList<Info> datalist;

    public TodoListAdapter(ArrayList<Info> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.todolist_cell, parent,false);

        return new TodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListViewHolder holder, int position) {
        Info cur = datalist.get(position);

        holder.tv_title.setText(cur.getTitle().toString());
        holder.tv_startTime2.setText(cur.getStart().toString());
        holder.tv_startTime.setText(cur.getStart().toString());
        holder.tv_endTime.setText(cur.getEnd().toString());


    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class TodoListViewHolder extends RecyclerView.ViewHolder {

        TextView tv_startTime, tv_title, tv_endTime, tv_startTime2;
        public TodoListViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_endTime = itemView.findViewById(R.id.tv_endTime);
            tv_startTime = itemView.findViewById(R.id.tv_startTime);
            tv_startTime2 = itemView.findViewById(R.id.tv_startTime2);
            tv_title = itemView.findViewById(R.id.tv_title);

        }
    }
}
