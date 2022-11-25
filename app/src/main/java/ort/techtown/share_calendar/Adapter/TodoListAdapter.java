package ort.techtown.share_calendar.Adapter;

import static android.widget.Toast.LENGTH_SHORT;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ort.techtown.share_calendar.AddActivity;
import ort.techtown.share_calendar.Data.CalendarUtil;
import ort.techtown.share_calendar.Data.Info;
import ort.techtown.share_calendar.MainActivity;
import ort.techtown.share_calendar.R;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {

    ArrayList<Info> datalist;
    boolean isMyCalendar;

    public TodoListAdapter(ArrayList<Info> datalist ) {
        this.datalist = datalist;
    }
    @NonNull
    @Override
    public TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        isMyCalendar = false;
        Log.d("datalist","*************************111 " +String.valueOf(datalist.size()));
        View view = inflater.inflate(R.layout.todolist_cell, parent,false);

        return new TodoListViewHolder(view);
    }

    public void setMyCalendar(boolean nnow){
        isMyCalendar = nnow;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListViewHolder holder, int position) {
        Log.d("datalist","222 "+String.valueOf(position));
        Info cur = datalist.get(position);
        holder.tv_title.setText(cur.getTitle().toString());
        holder.tv_startTime.setText(cur.getStart().toString().substring(11));
        holder.tv_startTime2.setText(cur.getStart().toString().substring(11) );
        holder.tv_endTime.setText(cur.getEnd().toString().substring(11));
        GradientDrawable background;
        //holder.tv_startTime.setVisibility(View.GONE);

        background = (GradientDrawable) holder.colorbar.getBackground();
        background.setColor(Color.parseColor(cur.getColor()));

        //롱클릭이벤트 처리하기
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final int position = holder.getAdapterPosition();
                final int orisize = datalist.size();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference();
                reference.child("User").child(CalendarUtil.UID).child("Calender").child(cur.getStart().substring(0,10))
                        .child(cur.getPath()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(view.getContext(),"삭제 성공", LENGTH_SHORT).show();
                            }
                        });

                notifyItemRemoved(position);
                for(int i = 0 ; i < orisize ; i ++)
                    datalist.remove(0);
                notifyDataSetChanged();
                Log.d("datalist","ori " + String.valueOf(orisize));
                Log.d("datalist",String.valueOf(datalist.size()));
//                for(int i = 0 ; i < orisize - datalist.size() && i < datalist.size() ; i++  ){
//                    datalist.remove(i);
//                    Log.d("datalist",String.valueOf(datalist.get(i).getPath()));
//                }

                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class TodoListViewHolder extends RecyclerView.ViewHolder {

        TextView tv_startTime, tv_title, tv_endTime, tv_startTime2;
        View colorbar;
        public TodoListViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("datalist","333 " +String.valueOf(datalist.size()));
            colorbar = itemView.findViewById(R.id.colorbar);
            tv_endTime = itemView.findViewById(R.id.tv_endTime);
            tv_startTime = itemView.findViewById(R.id.tv_startTime);
            tv_startTime2 = itemView.findViewById(R.id.tv_startTime2);
            tv_title = itemView.findViewById(R.id.tv_title);

        }
    }
}
