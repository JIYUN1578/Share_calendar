package ort.techtown.share_calendar.Adapter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

import ort.techtown.share_calendar.Data.CalendarColor;
import ort.techtown.share_calendar.Data.CalendarUtil;
import ort.techtown.share_calendar.Data.Info;
import ort.techtown.share_calendar.R;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>{

    ArrayList<LocalDate> localDates;
    ArrayList<CalendarColor> cc;

    ArrayList<String> color = new ArrayList<>();

    String TAG = "CalendarAdapter";

    public CalendarAdapter(ArrayList<LocalDate> datList) {
        this.localDates = datList;
        cc = new ArrayList<>();
        for(int i = 0 ; i < 42 ; i ++){
            this.cc.add(new CalendarColor(new ArrayList<>(), false));
        }
    }
    public CalendarAdapter(ArrayList<LocalDate> datList, ArrayList<CalendarColor> cc) {
        this.localDates = datList;
        this.cc = cc;
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

        LocalDate localDate = localDates.get(position);
        //Info info = infos.get(position);
        if(localDate == null){
            holder.tv_day.setText("!");
        }else{
            holder.tv_day.setText(String.valueOf(localDate.getDayOfMonth()));

            if(localDate.equals(CalendarUtil.selectedDate)){
                holder.parentView.setBackgroundResource(R.drawable.selectedday);
            }
            if(localDate.equals(CalendarUtil.today)){
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

                int iYear = localDate.getYear();
                int iMonth = localDate.getMonthValue();
                int iDay = localDate.getDayOfMonth();

                CalendarUtil.selectedDate = LocalDate.of(iYear,iMonth,iDay);
                if (holder.getAdapterPosition()!=RecyclerView.NO_POSITION){
                    if (mListener!=null){
                        mListener.onItemClick (view,holder.getAdapterPosition());
                    }
                }
            }
        });
            if(position < cc.size()){
                switch (cc.get(position).getColors().size()){
                    case 0:
                        holder.v1.setVisibility(View.GONE);
                    case 1:
                        holder.v2.setVisibility(View.GONE);
                    case 2:
                        holder.v3.setVisibility(View.GONE);
                    default:
                        break;}
                        GradientDrawable background;

                    for(int i = 0 ; i <cc.get(position).getColors().size() ; i++ ){
                        switch (i){
                            case 0:
                                background = (GradientDrawable) holder.v1.getBackground();
                                background.setColor(Color.parseColor(cc.get(position).getColors().get(0)));
                                break;
                            case 1:
                                background = (GradientDrawable) holder.v2.getBackground();
                                background.setColor(Color.parseColor(cc.get(position).getColors().get(1)));
                                break;
                            case 2:
                                background = (GradientDrawable) holder.v3.getBackground();
                                background.setColor(Color.parseColor(cc.get(position).getColors().get(2)));
                                break;
                            default:
                                break;
                        }

            }



        }

    }



    @Override
    public int getItemCount() {
        return localDates.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{

        TextView tv_day;
        View parentView;

        View v1, v2, v3;
        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);


            tv_day = itemView.findViewById(R.id.tv_day);

            v1 = itemView.findViewById(R.id.v1);
            v2 = itemView.findViewById(R.id.v2);
            v3 = itemView.findViewById(R.id.v3);
            parentView = itemView.findViewById(R.id.parent_view);
        }
    }

}
