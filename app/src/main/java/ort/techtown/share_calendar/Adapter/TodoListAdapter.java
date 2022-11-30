package ort.techtown.share_calendar.Adapter;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ort.techtown.share_calendar.AddActivity;
import ort.techtown.share_calendar.Data.CalendarUtil;
import ort.techtown.share_calendar.Data.Grouplist;
import ort.techtown.share_calendar.Data.Info;
import ort.techtown.share_calendar.MainActivity;
import ort.techtown.share_calendar.R;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {

    ArrayList<Info> datalist;
    ArrayList<String> seenList;
    boolean isMyCalendar;
    String groupName;
    private TodoListAdapter.OnItemClickListener mListener = null;

    public TodoListAdapter(ArrayList<Info> datalist , boolean isMyCalendar) {
        this.datalist = datalist;
        this.isMyCalendar = isMyCalendar;
        this.groupName = "";
    }

    public TodoListAdapter(ArrayList<Info> datalist , boolean isMyCalendar, String groupName, ArrayList<String> seenList) {
        this.datalist = datalist;
        this.isMyCalendar = isMyCalendar;
        this.groupName = groupName;
        this.seenList = seenList;
    }

    @NonNull
    @Override
    public TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.todolist_cell, parent,false);

        return new TodoListViewHolder(view);
    }
    public interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }
    public void setOnItemClickListener(TodoListAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }
    @Override
    public void onBindViewHolder(@NonNull TodoListViewHolder holder, int position) {
        Info cur = datalist.get(position);
        holder.tv_title.setText(cur.getTitle().toString());
        holder.tv_startTime.setText(cur.getStart().toString().substring(11));
        holder.tv_startTime2.setText(cur.getStart().toString().substring(11) );
        holder.tv_endTime.setText(cur.getEnd().toString().substring(11));
        GradientDrawable background;

        //holder.tv_startTime.setVisibility(View.GONE);
        holder.modify.setVisibility(View.INVISIBLE);
        holder.delete.setVisibility(View.INVISIBLE);
        background = (GradientDrawable) holder.colorbar.getBackground();
        background.setColor(Color.parseColor(cur.getColor()));

        // 마이캘린더일 경우
        if(isMyCalendar){
            holder.iv_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = holder.getAbsoluteAdapterPosition();
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    popupMenu.inflate(R.menu.grouplist_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.btn_delete:
                                    final int orisize = datalist.size();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference();
                                    reference.child("User").child(CalendarUtil.UID).child("Calender").child(cur.getStart().substring(0,10))
                                            .child(cur.getPath()).removeValue();
                                    notifyItemRemoved(position);
                                    for(int i = 0 ; i < orisize ; i ++)
                                        datalist.remove(0);
                                    notifyDataSetChanged();
                                    if (position!=RecyclerView.NO_POSITION){
                                        if (mListener!=null){
                                            mListener.onItemClick (view,position);
                                        }
                                        else{
                                        }
                                    }
                                    else{
                                    }
                                    break;
                                case R.id.btn_modify:
                                    Intent gotoAddActivity = new Intent(view.getContext(), AddActivity.class);
                                    gotoAddActivity.putExtra("from","modify");
                                    gotoAddActivity.putExtra("uid",CalendarUtil.UID);
                                    gotoAddActivity.putExtra("path2",cur.getPath());
                                    gotoAddActivity.putExtra("path1",cur.getStart().substring(0,10));
                                    gotoAddActivity.putExtra("starttime",cur.getStart().substring(11));
                                    gotoAddActivity.putExtra("endtime",cur.getEnd().substring(11));
                                    gotoAddActivity.putExtra("endday",cur.getEnd().substring(0,10));
                                    gotoAddActivity.putExtra("title",cur.getTitle());
                                    gotoAddActivity.putExtra("color",cur.getColor());
                                    gotoAddActivity.putExtra("name",CalendarUtil.getUserName());
                                    //해당 일정 삭제는 addActivity에서 실행 예정
                                    ((MainActivity)view.getContext()).startActivity(gotoAddActivity);
                                    ((MainActivity)view.getContext()).overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                                    ((MainActivity)view.getContext()).finish();
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
        // 공유캘린더일 경우
        else{
            holder.iv_menu.setVisibility(View.GONE);
            holder.tv_startTime.setText(cur.getName());
            // 비공개 처리
            Log.e("position",Integer.toString(position));
            Log.e("seenList",seenList.get(position));
            if(seenList.get(position).toString().equals("false")) {
                holder.tv_title.setText("비공개");
            }
        }
    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class TodoListViewHolder extends RecyclerView.ViewHolder {

        TextView tv_startTime, tv_title, tv_endTime, tv_startTime2 , modify, delete;
        ImageView iv_menu;
        View colorbar;
        public TodoListViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("datalist","333 " +String.valueOf(datalist.size()));
            colorbar = itemView.findViewById(R.id.colorbar);
            tv_endTime = itemView.findViewById(R.id.tv_endTime);
            iv_menu = itemView.findViewById(R.id.iv_menu);
            tv_startTime = itemView.findViewById(R.id.tv_startTime);
            tv_startTime2 = itemView.findViewById(R.id.tv_startTime2);
            tv_title = itemView.findViewById(R.id.tv_title);
            modify = itemView.findViewById(R.id.btn_modify);
            delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
