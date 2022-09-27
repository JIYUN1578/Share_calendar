package ort.techtown.share_calendar;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>{

    ArrayList<LocalDate>  datList;
    String TAG = "CalendarAdapter";

    public CalendarAdapter(ArrayList<LocalDate> datList) {
        this.datList = datList;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.calendar_cell, parent,false);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        LocalDate day = datList.get(position);
        if(day == null){
            holder.tv_day.setText("");
        }else{
            holder.tv_day.setText(String.valueOf(day.getDayOfMonth()));

            if(day.equals(CalendarUtil.today)){
                holder.parentView.setBackgroundColor(Color.LTGRAY);
            }

            ///텍스트 색상 지정 (토, 일)
            if((position+1)%7 == 0 ){//토
                holder.tv_day.setTextColor(Color.BLUE);
            }else if(position%7==0 || position  ==0){ //일요일
                holder.tv_day.setTextColor(Color.RED);

            }
        }

        //날짜 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int iYear = day.getYear();
                int iMonth = day.getMonthValue();
                int iDay = day.getDayOfMonth();

                String yearMonday = iYear + "년" + iMonth + "월" + iDay + "일";
                Toast.makeText(holder.itemView.getContext(),yearMonday,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datList.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{

        TextView tv_day;
        View parentView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_day = itemView.findViewById(R.id.tv_day);

            parentView = itemView.findViewById(R.id.parent_view);
        }
    }
}
