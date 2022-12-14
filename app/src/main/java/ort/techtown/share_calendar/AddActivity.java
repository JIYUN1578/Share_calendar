package ort.techtown.share_calendar;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
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
    TextView tv_addStartTime;
    TextView tv_addEndTime;
    ImageView color1, color2, color3, color4, color5;
    RecyclerView recyclerView;
    boolean isModify, isEmpty;
    int pStartHour, pStartMin, pEndHour, pEndMin;
    int pSYear, pSMonth, pSDay, pEYear, pEMonth, pEDay;
    GrouplistAdapter adapter;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    String pColor, oriPath1, oriPath2;

    //??????????????????
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private String uid, name , curColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        isModify = false;
        pColor = "#FFAFB0";
        isEmpty = true;
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        if(intent.getStringExtra("from").toString().equals("modify")) {isModify = true;}
        name = intent.getStringExtra("name");


        recyclerView = findViewById(R.id.group_list);
        btn_addInfo = (Button) findViewById(R.id.btn_addInfo);
        edt_toDo = (EditText) findViewById(R.id.edt_addTodo);
        tv_addStartDay = (TextView) findViewById(R.id.tv_addStartDay);
        tv_addEndDay = (TextView)findViewById(R.id.tv_addEndtDay);
        tv_addStartDay.setText(CalendarUtil.today.toString());
        tv_addStartTime = (TextView) findViewById(R.id.tv_addStartTime);
        tv_addEndTime = (TextView) findViewById(R.id.tv_addEndTime );
        tv_pageName = (TextView)findViewById(R.id.tv_pagename);

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
        if(isModify){
            tv_pageName.setText("?????? ??????");
            oriPath1 = intent.getStringExtra("path1");
            oriPath2 = intent.getStringExtra("path2");
            tv_addStartTime.setText(intent.getStringExtra("starttime"));
            tv_addEndTime.setText(intent.getStringExtra("endtime"));
            edt_toDo.setText(intent.getStringExtra("title"));
            curColor = intent.getStringExtra("color");
            setcolor(-1);
        }
        else setcolor(0);
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
        if(isModify) setGroupForModify();
        else setGroup();
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
                //???????????? 0??? 1???????????? ?????? ??? ????????? -1 ?????? ?????? ?????? ?????????
                datePickerDialog.show(); // ?????? ??????
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
                //???????????? 0??? 1???????????? ?????? ??? ????????? -1 ?????? ?????? ?????? ?????????
                datePickerDialog.show(); // ?????? ??????
            }
        });
        btn_addInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //?????? ?????? ???????????????
                //????????? ?????? ??????
                String nnow =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
                Info newInfo = new Info(false,
                        tv_addStartDay.getText()+" "+tv_addStartTime.getText(),
                        tv_addEndDay.getText()+" "+tv_addEndTime.getText().toString(),
                        edt_toDo.getText().toString(),edt_toDo.getText().toString()
                         ,pColor , nnow, name);
                //upload
                if(isModify){
                    //?????? ?????? ??????
                    FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
                    DatabaseReference mreference = mdatabase.getReference();
                    mreference.child("User").child(CalendarUtil.UID).child("Calender").child(oriPath1.substring(0,10))
                            .child(oriPath2).removeValue();
                }
                databaseReference.child("User").child(uid).child("Calender").child(tv_addStartDay.getText().toString())
                                .child(nnow).setValue(newInfo);
                if(!isEmpty) {
                    makeprivacy(nnow);
                }
                CalendarUtil.selectedDate = LocalDate.parse(tv_addStartDay.getText(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                onBackPressed();
            }
        });
    }

    private void setcolor(int num){
        //?????? ?????? ???????????? ???????????? ?????????
        color1.setBackgroundResource(R.drawable.bg_ab_circle);
        color2.setBackgroundResource(R.drawable.bg_ab_circle2);
        color3.setBackgroundResource(R.drawable.bg_ab_circle3);
        color4.setBackgroundResource(R.drawable.bg_ab_circle4);
        color5.setBackgroundResource(R.drawable.bg_ab_circle5);
        //?????? num??? ??????????????? ?????????
        switch (num){
            case -1:
                if (curColor.equals("#FFAFB0")) {setcolor(1);
                pColor = "#FFAFB0";}
                else if(curColor.equals("#FFE4AF")) {setcolor(2);
                    pColor = "#FFE4AF";}
                else if(curColor.equals("#8EB695")) {setcolor(3);
                    pColor = "#8EB695";}
                else if(curColor.equals("#C6E1FF")) {setcolor(4);
                    pColor = "#C6E1FF";}
                else {setcolor(5);
                    pColor = "#E0E0E0";}
                break;
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
        // ?????? ?????? ????????????
        ArrayList<Grouplist> grouplist = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference;
        try{
            reference = database.getReference("User").child(uid).child("Group");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Grouplist temp = new Grouplist("temp",false);
                        temp.setGrname((String)snapshot.getValue());
                        temp.setSeen(false);
                        grouplist.add(temp);
                        adapter = new GrouplistAdapter(grouplist);
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
                        // ????????? ??????
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(adapter);
                        isEmpty =false;
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ????????? ????????? ??????
    }

    private void setGroupForModify() {
        // ?????? ?????? ????????????
        ArrayList<Grouplist> grouplist;
        grouplist = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference;
        try{
            reference = database.getReference("User").child(uid).child("Group");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Grouplist temp = new Grouplist("temp",false);
                        String gName = (String)snapshot.getValue();
                        temp.setGrname(gName);
                        isEmpty = false;
                        if(isModify){
                            try {
                                databaseReference.child("User").child(uid).child("Calender").child(oriPath1.substring(0,10))
                                    .child(oriPath2).child("isSeen").child(gName).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.getValue() == null) {
                                                temp.setSeen(false);
                                                grouplist.add(temp);
                                                adapter = new GrouplistAdapter(grouplist);
                                                RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
                                                // ????????? ??????
                                                recyclerView.setLayoutManager(manager);
                                                recyclerView.setAdapter(adapter);
                                                return;
                                            }
                                            if(snapshot.getValue(boolean.class)) {
                                                Log.d("exception for privacy",gName+" "+"true");
                                                temp.setSeen(true);

                                                grouplist.add(temp);
                                                adapter = new GrouplistAdapter(grouplist);
                                                RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
                                                // ????????? ??????
                                                recyclerView.setLayoutManager(manager);
                                                recyclerView.setAdapter(adapter);
                                            }
                                            else{
                                                temp.setSeen(false);

                                                grouplist.add(temp);
                                                adapter = new GrouplistAdapter(grouplist);
                                                RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
                                                // ????????? ??????
                                                recyclerView.setLayoutManager(manager);
                                                recyclerView.setAdapter(adapter);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                           }catch (Exception e){
                             Log.d("exception for privacy","??????");
                          }
                        }
                        Log.d("exception for privacy3",gName);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ????????? ????????? ??????
    }
    public void makeprivacy(String nnow){ //???????????? ??????
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
        intent.putExtra("name",name);
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
            if(iscurrent == true){ // ????????? ????????????
                future =current.plusHours(1);
                tv_addEndDay.setText(future.toString().substring(0,10));
                tv_addEndTime.setText(future.toString().substring(11,future.toString().length()));
            }
            else{ //????????? ????????????
                current =future.minusHours(1);
                tv_addStartDay.setText(current.toString().substring(0,10));
                tv_addStartTime.setText(current.toString().substring(11,current.toString().length()));
            }
        }
    }
}