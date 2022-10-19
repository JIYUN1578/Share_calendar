package ort.techtown.share_calendar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ort.techtown.share_calendar.Data.GroupRecyclerView;
import ort.techtown.share_calendar.R;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private ArrayList<GroupRecyclerView> arrayList;
    private Context context;

    public GroupAdapter(ArrayList<GroupRecyclerView> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_recycler_item,parent,false);
        GroupViewHolder holder = new GroupViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.group_name.setText(arrayList.get(position).getGroup_name());
        holder.group_introduce.setText(arrayList.get(position).getGroup_introduce());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView group_name, group_introduce;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            this.group_name = itemView.findViewById(R.id.group_name);
            this.group_introduce = itemView.findViewById(R.id.group_introduce);
        }
    }
}
