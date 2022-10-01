package ort.techtown.share_calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddActivity extends AppCompatActivity {

    ImageButton btn_addInfo;
    EditText edt_toDo;
    TextView tv_addStartDay, tv_addEndDay;
    TextView tv_addStartTime;
    TextView tv_addEndTime;
    CheckBox cb_isSecret;

    int pStartHour = 8;
    int pStartMin = 0;
    int pEndHour = 9;
    int pEndMin = 0;

    int pSYear = CalendarUtil.today.getYear();
    int pSMonth = CalendarUtil.today.getMonthValue();
    int pSDay = CalendarUtil.today.getDayOfMonth();
    int pEYear = CalendarUtil.today.getYear();
    int pEMonth = CalendarUtil.today.getMonthValue();
    int pEDay = CalendarUtil.today.getDayOfMonth();

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btn_addInfo = (ImageButton) findViewById(R.id.btn_addInfo);
        edt_toDo = (EditText) findViewById(R.id.edt_addTodo);
        tv_addStartDay = (TextView) findViewById(R.id.tv_addStartDay);
        tv_addEndDay = (TextView)findViewById(R.id.tv_addEndtDay);
        cb_isSecret = (CheckBox) findViewById(R.id.cb_isSecret);
        tv_addStartDay.setText(CalendarUtil.today.toString());
        tv_addStartTime = (TextView) findViewById(R.id.tv_addStartTime);
        tv_addEndTime = (TextView) findViewById(R.id.tv_addEndTime );


       // Log.d("시간", String.valueOf());

        tv_addStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = new TimePickerDialog(AddActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                                String pStartTime;
                                pStartHour = hour;
                                pStartMin = min;
                                pStartTime = String.valueOf(hour)+":"+String.valueOf(min);
                                tv_addStartTime.setText(pStartTime);

                                if(pStartHour > pEndHour) {
                                    pEndHour = (pStartHour + 1) % 24;
                                    pEndMin = pStartMin;
                                    tv_addEndTime.setText(pEndHour+":"+pEndMin);
                                }
                                else if(pStartHour == pEndHour && pStartMin > pEndMin){
                                    pEndHour = (pStartHour + 1) % 24;
                                    pEndMin = pStartMin;
                                    tv_addEndTime.setText(pEndHour+":"+pEndMin);
                                }
                                else if(tv_addEndTime.getText().equals("종료시간")){
                                    pEndHour = (pStartHour + 1) % 24;
                                    pEndMin = pStartMin;
                                    tv_addEndTime.setText(pEndHour+":"+pEndMin);
                                }
                            }
                        }
                        ,pStartHour, pStartMin, false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                timePickerDialog.show();
            }
        });

        tv_addEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = new TimePickerDialog(AddActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int min) {
                            String pEndTime;
                            pEndTime = String.valueOf(hour)+":"+String.valueOf(min);

                            pEndHour = hour;
                            pEMonth = min;

                            tv_addEndTime.setText(pEndTime);
                            if(pEndHour == 0 ){
                                pStartHour = 0;
                                if(pEndMin < pStartMin)
                                    pStartMin = pEndMin;
                            }
                            if(pStartHour > pEndHour) {
                                pStartHour = (pEndHour -1) % 24 ;
                                pStartMin = pEndMin;
                            }
                            else if(pStartHour == pEndHour && pStartMin > pEndMin){
                                pStartHour = (pEndHour -1) % 24 ;
                                pStartMin = pEndMin;
                            }else if(tv_addStartTime.getText().equals("시작시간")){
                                pStartHour = (pEndHour -1) % 24 ;
                                Log.d("신나는 좆버그", String.valueOf(pStartHour));
                                pStartMin = pEndMin;
                            }
                            tv_addStartTime.setText(pStartHour+":"+pStartMin);
                        }
                    }
                    ,pEndHour, pEndMin, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
                }
            });


        tv_addStartDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                pSYear = year;
                                pSDay = day;
                                pSMonth = month +1 ;

                                String date;
                                if(pSMonth < 10){
                                    date = pSYear + "-0" + pSMonth;
                                }else{
                                    date = pSYear + "-" + pSMonth ;
                                }

                                if(pSDay < 10){
                                    date = date + "-0"+pSDay;
                                }else{
                                    date = date + "-" + pSDay;
                                }
                                tv_addStartDay.setText(date);

                                if(pSYear < pEYear){

                                }
                            }
                        },pSYear,pSMonth-1,pSDay );
                //달력에서 0이 1월이니까 지금 달 하려면 -1 해야 해당 달이 나온다
                datePickerDialog.show(); // 달력 호출
            }
        });

        tv_addEndDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {


                                pEYear = year;
                                pEDay = day;
                                pEMonth = month +1 ;

                                String date;
                                if(pEMonth < 10){
                                    date = pSYear + "-0" + pSMonth;
                                }else{
                                    date = pSYear + "-" + pSMonth ;
                                }

                                if(pEDay < 10){
                                    date = date + "-0"+pSDay;
                                }else{
                                    date = date + "-" + pSDay;
                                }
                                tv_addEndDay.setText(date);
                            }
                        },pEYear,pEMonth-1,pEDay );
                //달력에서 0이 1월이니까 지금 달 하려면 -1 해야 해당 달이 나온다
                datePickerDialog.show(); // 달력 호출
            }
        });
        btn_addInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toDo = edt_toDo.getText().toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate pDate = LocalDate.parse(tv_addStartDay.getText().toString(),formatter);
                boolean isSecret = cb_isSecret.isChecked(); //체크 되어있으면 비공개

                //upload 하면 되지롱

            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_left_in,R.anim.anim_left_out);
    }
}