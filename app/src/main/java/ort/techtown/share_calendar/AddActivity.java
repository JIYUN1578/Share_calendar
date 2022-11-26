package ort.techtown.share_calendar;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ort.techtown.share_calendar.Adapter.GroupAdapter;
import ort.techtown.share_calendar.Adapter.GrouplistAdapter;
import ort.techtown.share_calendar.Adapter.TodoListAdapter;
import ort.techtown.share_calendar.Data.CalendarUtil;
import ort.techtown.share_calendar.Data.Grouplist;
import ort.techtown.share_calendar.Data.Info;

public class AddActivity extends AppCompatActivity {

    Button btn_addInfo;
    EditText edt_toDo;
    TextView tv_addStartDay, tv_addEndDay , tv_pageName;
    TextView tv_addStartTime, tv_addEndTime;
    ImageView color1, color2, color3, color4, color5;
    RecyclerView recyclerView;
    boolean isModify;
    int pStartHour, pStartMin, pEndHour, pEndMin;
    int pSYear, pSMonth, pSDay, pEYear, pEMonth, pEDay;
    GrouplistAdapter adapter;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    String pColor, oriPath1, oriPath2;

    //파이어베이스
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private String uid, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        isModify = false;
        pColor = "#FFAFB0";

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        if(intent.getStringExtra("from").toString().equals("modify")) {isModify = true;}
        else{
            name = intent.getStringExtra("name");}


        recyclerView = findViewById(R.id.group_list);
        btn_addInfo = (Button) findViewById(R.id.btn_addInfo);
        edt_toDo = (EditText) findViewById(R.id.edt_addTodo);
        tv_addStartDay = (TextView) findViewById(R.id.tv_addStartDay);
        tv_addEndDay = (TextView)findViewById(R.id.tv_addEndtDay);
        tv_addStartDay.setText(CalendarUtil.today.toString());
        tv_addStartTime = (TextView) findViewById(R.id.tv_addStartTime);
        tv_addEndTime = (TextView) findViewById(R.id.tv_addEndTime );
        tv_pageName = (TextView)findViewById(R.id.tv_pagename);
        if(isModify){
            tv_pageName.setText("일정 변경");
            oriPath1 = intent.getStringExtra("path1");
            oriPath2 = intent.getStringExtra("path2");
            Log.d("path2 받기",oriPath2);
        }
        tv_addStartTime.setText("08:00");
        tv_addEndTime.setText("09:00");
        tv_addStartDay.setText(CalendarUtil.selectedDate.toString());
        tv_addEndDay.setText(CalendarUtil.selectedDate.toString());

        pStartHour = 8;
        pStartMin = 0;
        pEndHour = 9;
        pEndMin = 0;

        color1 = (ImageView)findViewById(R.id.color1);
        color2 = (ImageView)findViewById(R.id.color2);
        color3 = (ImageView)findViewById(R.id.color3);
        color4 = (ImageView)findViewById(R.id.color4);
        color5 = (ImageView)findViewById(R.id.color5);

        setcolor(0);
        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setcolor(1);
                pColor = "#FFAFB0";
            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setcolor(2);
                pColor = "#FFE4AF";
            }
        });
        color3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setcolor(3);pColor = "#8EB695";
            }
        });
        color4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setcolor(4);
                pColor = "#C6E1FF";
            }
        });
        color5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setcolor(5);
                pColor = "#E0E0E0";
            }
        });

        pSYear = CalendarUtil.selectedDate.getYear();
        pSMonth = CalendarUtil.selectedDate.getMonthValue();
        pSDay = CalendarUtil.selectedDate.getDayOfMonth();
        pEYear = CalendarUtil.selectedDate.getYear();
        pEMonth = CalendarUtil.selectedDate.getMonthValue();
        pEDay = CalendarUtil.selectedDate.getDayOfMonth();

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

        setGroup();
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
                                pEMonth = month + 1;

                                tv_addEndDay.setText(String.valueOf(pEYear) + "-" +
                                        String.format("%02d", pEMonth) + "-" + String.format("%02d", pEDay));
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
                //이게 일정 변경이라면
                if(isModify){
                    //원래 일정 삭제
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference();
                    Toast.makeText(getApplicationContext(),"dmddmd", LENGTH_SHORT).show();
                    Log.d("path 확인",oriPath1.substring(0,10)+" "+oriPath2);
                    reference.child("User").child(CalendarUtil.UID).child("Calender").child(oriPath1.substring(0,10))
                            .child(oriPath2).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(view.getContext(),"삭제 성공", LENGTH_SHORT).show();
                                }
                            });

                }
                String nnow =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
                Info newInfo = new Info(false,
                        tv_addStartDay.getText()+" "+tv_addStartTime.getText(),
                        tv_addEndDay.getText()+" "+tv_addEndTime.getText().toString(),
                        edt_toDo.getText().toString(),edt_toDo.getText().toString()
                         ,pColor , nnow);
                //upload
                databaseReference.child("User").child(uid).child("Calender").child(tv_addStartDay.getText().toString())
                                .child(nnow).setValue(newInfo);
                makeprivacy(nnow);
                CalendarUtil.selectedDate = LocalDate.parse(tv_addStartDay.getText(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                Log.d("selected date: ",CalendarUtil.selectedDate.toString());
                onBackPressed();
            }
        });
    }

    private void setcolor(int num){
        //모든 컬러 이미지들 선택표시 없애기
        color1.setBackgroundResource(R.drawable.bg_ab_circle);
        color2.setBackgroundResource(R.drawable.bg_ab_circle2);
        color3.setBackgroundResource(R.drawable.bg_ab_circle3);
        color4.setBackgroundResource(R.drawable.bg_ab_circle4);
        color5.setBackgroundResource(R.drawable.bg_ab_circle5);
        //해당 num의 컬러표시만 만들기
        switch (num){
            case 1:
                color1.setBackgroundResource(R.drawable.bg_ab_circle_picked);
                break;
            case 2:
                color2.setBackgroundResource(R.drawable.bg_ab_circle2_picked);
                break;
            case 3:
                color3.setBackgroundResource(R.drawable.bg_ab_circle3_picked);
                break;
            case 4:
                color4.setBackgroundResource(R.drawable.bg_ab_circle4_picked);
                break;
            case 5:
                color5.setBackgroundResource(R.drawable.bg_ab_circle5_picked);
                break;
            default:
                break;
        }
    }

    private void setGroup( ) {
        // 해당 일정 가져오기
        ArrayList<Grouplist> grouplist = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference;

        adapter = new GrouplistAdapter(grouplist);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        // 어뎁터 적용
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        try{
            reference = database.getReference("User").child(uid).child("Group");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Grouplist temp = new Grouplist("temp",false);
                        temp.setGrname((String)snapshot.getValue());
                        grouplist.add(temp);
                        Log.e("111",grouplist.toString());
                        GrouplistAdapter adapter = new GrouplistAdapter(grouplist);
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
                        // 어뎁터 적용
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(adapter);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "지금 안돼유",Toast.LENGTH_SHORT).show();
            Log.e("222",grouplist.toString());
        }
        // 어뎁터 데이터 적용
    }

    public void makeprivacy(String nnow){ //체크박스 넣자
        ArrayList<Grouplist> grouplist ;
        grouplist = adapter.getDatalist();
        for( Grouplist glist : grouplist){
            databaseReference.child("User").child(uid).child("Calender").child(tv_addStartDay.getText().toString())
                    .child(nnow).child("isSeen").child(glist.getGrname()).setValue(glist.isSeen());
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        intent.putExtra("uid",uid);
        if(!isModify) intent.putExtra("name",name);
        intent.putExtra("fromOther", true);
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