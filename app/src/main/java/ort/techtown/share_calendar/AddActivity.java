package ort.techtown.share_calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ort.techtown.share_calendar.Data.CalendarUtil;
import ort.techtown.share_calendar.Data.Info;

public class AddActivity extends AppCompatActivity {

    ImageButton btn_addInfo;
    EditText edt_toDo;
    TextView tv_addStartDay, tv_addEndDay;
    TextView tv_addStartTime;
    TextView tv_addEndTime;
    CheckBox cb_isSecret;

    int pStartHour ;
    int pStartMin ;
    int pEndHour ;
    int pEndMin ;

    int pSYear ;
    int pSMonth ;
    int pSDay ;
    int pEYear;
    int pEMonth;
    int pEDay ;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    //파이어베이스
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        btn_addInfo = (ImageButton) findViewById(R.id.btn_addInfo);
        edt_toDo = (EditText) findViewById(R.id.edt_addTodo);
        tv_addStartDay = (TextView) findViewById(R.id.tv_addStartDay);
        tv_addEndDay = (TextView)findViewById(R.id.tv_addEndtDay);
        cb_isSecret = (CheckBox) findViewById(R.id.cb_isSecret);
        tv_addStartDay.setText(CalendarUtil.today.toString());
        tv_addStartTime = (TextView) findViewById(R.id.tv_addStartTime);
        tv_addEndTime = (TextView) findViewById(R.id.tv_addEndTime );

        tv_addStartTime.setText("08:00");
        tv_addEndTime.setText("09:00");
        tv_addStartDay.setText(CalendarUtil.selectedDate.toString());
        tv_addEndDay.setText(CalendarUtil.selectedDate.toString());


        pStartHour = 8;
        pStartMin = 0;
        pEndHour = 9;
        pEndMin = 0;

        pSYear = CalendarUtil.selectedDate.getYear();
        pSMonth = CalendarUtil.selectedDate.getMonthValue();
        pSDay = CalendarUtil.selectedDate.getDayOfMonth();
        pEYear = CalendarUtil.selectedDate.getYear();
        pEMonth = CalendarUtil.selectedDate.getMonthValue();
        pEDay = CalendarUtil.selectedDate.getDayOfMonth();

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
                                pStartTime = String.format("%02d",hour)+":"+String.format("%02d",min);
                                tv_addStartTime.setText(pStartTime);
                                updatecal(true);
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
                            pEndHour = hour;
                            pEMonth = min;
                            tv_addEndTime.setText(String.format("%02d",hour)+":"+String.format("%02d",min));
                            updatecal(false);
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

                                tv_addStartDay.setText(String.valueOf(pSYear)+"-"+
                                        String.format("%02d",pSMonth)+"-"+String.format("%02d",pSDay));
                                updatecal(true);
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

                                tv_addEndDay.setText(String.valueOf(pEYear)+"-"+
                                        String.format("%02d",pEMonth)+"-"+String.format("%02d",pEDay));
                                updatecal(false);
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
                Info newInfo = new Info(false,
                        cb_isSecret.isChecked(),
                        tv_addStartDay.getText()+" "+tv_addStartTime.getText(),
                        tv_addEndDay.getText()+" "+tv_addEndTime.getText().toString(),
                        edt_toDo.getText().toString(),
                        edt_toDo.getText().toString());

                //upload 하면 되지롱

                String nnow =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
                databaseReference.child("User").child(uid).child("Calender").child(tv_addStartDay.getText().toString())
                                .child(nnow).setValue(newInfo);

            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        intent.putExtra("uid",uid);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_left_in,R.anim.anim_left_out);
    }

    public void updatecal(boolean iscurrent){
        LocalDateTime current, future;
        current  = LocalDateTime.parse(tv_addStartDay.getText()+" "+tv_addStartTime.getText(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        future  = LocalDateTime.parse(tv_addEndDay.getText()+" "+tv_addEndTime.getText(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        if(current.isAfter(future)){
            if(iscurrent == true){ // 현재를 바꾼거면
                future =current.plusHours(1);
                tv_addEndDay.setText(future.toString().substring(0,10));
                tv_addEndTime.setText(future.toString().substring(11,future.toString().length()));
            }
            else{ //과거를 바꿨으면
                current =future.minusHours(1);
                tv_addStartDay.setText(current.toString().substring(0,10));
                tv_addStartTime.setText(current.toString().substring(11,current.toString().length()));
            }
        }
    }
}