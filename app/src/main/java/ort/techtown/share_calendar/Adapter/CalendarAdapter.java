package ort.techtown.share_calendar.Adapter;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ort.techtown.share_calendar.Data.CalendarUtil;
import ort.techtown.share_calendar.R;

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

    public interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값
    }
    //리스너 객체 참조 변수
    private OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        LocalDate day = datList.get(position);
        if(day == null){
            holder.tv_day.setText("!");
        }else{
            holder.tv_day.setText(String.valueOf(day.getDayOfMonth()));

            if(day.equals(CalendarUtil.selectedDate)){
                holder.parentView.setBackgroundResource(R.drawable.selectedday);
            }
            if(day.equals(CalendarUtil.today)){
                holder.tv_day.setBackgroundResource(R.drawable.gb_circle);
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

                CalendarUtil.selectedDate = LocalDate.of(iYear,iMonth,iDay);
                if (holder.getAdapterPosition()!=RecyclerView.NO_POSITION){
                    if (mListener!=null){
                        mListener.onItemClick (view,holder.getAdapterPosition());
                    }
                }
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
