package ort.techtown.share_calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ort.techtown.share_calendar.Adapter.CalendarAdapter;
import ort.techtown.share_calendar.Adapter.TodoListAdapter;
import ort.techtown.share_calendar.Data.BackKeyHandler;
import ort.techtown.share_calendar.Data.CalendarColor;
import ort.techtown.share_calendar.Data.CalendarUtil;
import ort.techtown.share_calendar.Data.Info;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);
    // drawerLayout
    private DrawerLayout drawerLayout;
    private Button btn_logout, btn_calendar, btn_search, btn_make, btn_group, btn_close, btn_profile;
    private View drawerView;
    private ImageView menu_open;
    private TextView tv_title;
    private String uid, name;
    boolean isModify;
    // ????????? ?????? ??????
    private static final int REQUEST_CODE = 0;
    private Uri uri;
    // ??????
    TextView tv_monthyear;
    RecyclerView recyclerView, todoListRecyclerView;
    // ??????????????????
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference tmpReference = database.getReference();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    // ?????? ?????? ??????
    ImageButton btn_goAddActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ??????
        tv_monthyear = findViewById(R.id.tv_month_year);
        ImageButton btn_pre = findViewById(R.id.btn_frontmonth);
        ImageButton btn_next = findViewById(R.id.btn_nextmonth);
        recyclerView = findViewById(R.id.recyclerview);
        todoListRecyclerView = findViewById(R.id.todoListRecyclerView);
        CalendarUtil.today = LocalDate.now();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        CalendarUtil.setUserName(name);
        uid = intent.getStringExtra("uid");
        CalendarUtil.UID = uid;
        // ?????? ??????
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("User").child(uid).child("Name").getValue() == null) {
                    databaseReference.child("User").child(uid).child("Name").setValue(name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // ?????? ?????? ??????
        boolean fromOther = false;
        fromOther = intent.getBooleanExtra("fromOther", false);
        if(!fromOther) CalendarUtil.selectedDate = LocalDate.now();
        setTodoList(uid);

        // ??????
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("???????????????");

        // drawerLayout
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);

        // drawerLayout ?????? ??????
        menu_open = (ImageView)findViewById(R.id.menu_open);
        menu_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        // drawLayout ?????? ??????
        btn_close = (Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

        // ?????? ?????? ??????
        btn_make = (Button)findViewById(R.id.btn_make);
        btn_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MakeActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);;
                startActivity(intent);
                finish();
            }
        });

        // ?????? ?????? ??????
        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);;
                startActivity(intent);
                finish();
            }
        });

        // ???????????? ??????
        btn_group = (Button)findViewById(R.id.btn_group);
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);;
                startActivity(intent);
                finish();
            }
        });

        // ??????????????? ??????
        btn_profile = (Button)findViewById(R.id.btn_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // ???????????? ??????
        btn_logout = (Button)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        // ?????? ??????
        setMonthview();
        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ?????? ??????
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusMonths(1);
                setMonthview();
                setTodoList(uid);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ?????? ???
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusMonths(1);
                setMonthview();
                setTodoList(uid);
            }
        });

        btn_goAddActivity = (ImageButton) findViewById(R.id.btn_gotoAddActivity);
        btn_goAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,AddActivity.class);
                // Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                intent.putExtra("from","main");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                try{
                    uri = data.getData();
                    String filename;
                    if(uri!=null) {
                        filename = uri.toString() + ".jpg";
                        StorageReference riverRef = storageReference.child("post_img/"+filename);
                        UploadTask uploadTask = riverRef.putFile(uri);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        });
                        tmpReference.child("User").child(uid).child("Image_url").setValue(filename);
                        Toast.makeText(getApplicationContext(),"????????? ????????? ?????????????????????.",Toast.LENGTH_SHORT).show();
                    }
                } catch(Exception e){
                }
            }
            else if(resultCode == RESULT_CANCELED){
            }
        }
    }

    // ??????
    private void setMonthview() {
        // ?????? ???????????? ??????
        if(CalendarUtil.selectedDate == null){
            CalendarUtil.selectedDate = LocalDate.now();
            Log.e("selected", CalendarUtil.selectedDate.toString());
        }
        tv_monthyear.setText(monthYearFromDate(CalendarUtil.selectedDate));
        // ???????????? ????????????, localdates == ?????? ??????,
        ArrayList<LocalDate> localDates = daysInMonthArray(CalendarUtil.selectedDate);
        setCalendarColor(CalendarUtil.selectedDate, localDates);
    }

    private void setTodoList(String uid) {
        // ?????? ?????? ????????????
        ArrayList<Info> infolist = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference;

        TodoListAdapter adapter = new TodoListAdapter(infolist , true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        // ????????? ??????
        todoListRecyclerView.setLayoutManager(manager);
        todoListRecyclerView.setAdapter(adapter);
        try{
            reference = database.getReference("User").child(uid).child("Calender").child(CalendarUtil.selectedDate.toString());
            Query myTopPostsQuery = reference.orderByChild("start");
            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        infolist.add(snapshot.getValue(Info.class));
                        TodoListAdapter adapter = new TodoListAdapter(infolist, true);
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
                        // ????????? ??????
                        todoListRecyclerView.setLayoutManager(manager);
                        todoListRecyclerView.setAdapter(adapter);

                        adapter.setOnItemClickListener(new TodoListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {

                                setTodoList(uid);
                                setMonthview();
                            }
                        });
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
    // ?????? ?????? ??????
    private String monthYearFromDate(LocalDate localDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy??? MM???");
        return localDate.format(formatter);
    }

    private ArrayList<LocalDate> daysInMonthArray(LocalDate localDate){
        ArrayList<LocalDate> localDates = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(localDate);
        // ?????? ??? ????????? ?????? ????????????
        int lastday = yearMonth.lengthOfMonth();
        // ???????????? ????????? ?????? ????????????
        LocalDate firstday = CalendarUtil.selectedDate.withDayOfMonth(1);
        // ????????? ??? ?????? ????????????
        int dayOfweek = firstday.getDayOfWeek().getValue();
        // ?????? ??????
        for(int i = 1 ; i <= 42 ; i++){
            if(i <=dayOfweek){
                LocalDate premonthDate = CalendarUtil.selectedDate.minusMonths(1);
                int lastdayOfpremonth = premonthDate.lengthOfMonth();
                localDates.add(LocalDate.of(premonthDate.getYear(), premonthDate.getMonth(),
                       lastdayOfpremonth - dayOfweek + i ));
            }
            else if(i> lastday + dayOfweek){
                LocalDate nextmonthDate = CalendarUtil.selectedDate.plusMonths(1);
                localDates.add(LocalDate.of(nextmonthDate.getYear(),
                        nextmonthDate.getMonth(),
                        i - lastday-dayOfweek));
            }else{
                localDates.add(LocalDate.of(CalendarUtil.selectedDate.getYear(),
                        CalendarUtil.selectedDate.getMonth(),
                        i - dayOfweek));
            }
        }
        return localDates;
    }

    private void setCalendarColor(LocalDate localDate, ArrayList<LocalDate> localDates){
        ArrayList<CalendarColor> calendarColors = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(localDate);
        boolean iscolor;
        // ?????? ??? ????????? ?????? ????????????
        int lastday = yearMonth.lengthOfMonth();
        // ???????????? ????????? ?????? ????????????
        LocalDate firstday = CalendarUtil.selectedDate.withDayOfMonth(1);
        // ????????? ??? ?????? ????????????
        int dayOfweek = firstday.getDayOfWeek().getValue();
        // ?????? ??????
        for(int i = 1 ; i <= 42 ; i++){
            ArrayList<String> colors = new ArrayList<>();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference;
            iscolor = false;
            LocalDate localDate1;
            if(i <=dayOfweek){
                LocalDate premonthDate = CalendarUtil.selectedDate.minusMonths(1);
                int lastdayOfpremonth = premonthDate.lengthOfMonth();
                localDate1 = LocalDate.of(premonthDate.getYear(), premonthDate.getMonth(),
                        lastdayOfpremonth - dayOfweek + i );
            }
            else if(i> lastday + dayOfweek){
                LocalDate nextmonthDate = CalendarUtil.selectedDate.plusMonths(1);

                localDate1 = LocalDate.of(nextmonthDate.getYear(), nextmonthDate.getMonth(),
                        i - lastday-dayOfweek );
            }else{
                localDate1 = LocalDate.of(CalendarUtil.selectedDate.getYear(),
                        CalendarUtil.selectedDate.getMonth(),
                        i - dayOfweek);
            }
            final int position = i-1;
            reference = database.getReference("User").child(uid).child("Calender").child(localDate1.toString());
            Query myTopPostsQuery = reference.orderByChild("start");

            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        colors.add(snapshot.getValue(Info.class).getColor());
                    }
                    CalendarColor calendarColor = new CalendarColor(colors, true);
                    calendarColors.add(calendarColor);
                    // ????????? ????????? ??????
                    CalendarAdapter adapter = new CalendarAdapter(localDates, calendarColors);
                    // ???????????? ?????? ??? 7???
                    RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(),7);
                    // ???????????? ??????
                    recyclerView.setLayoutManager(manager);
                    // ????????? ??????
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            setMonthview();
                            setTodoList(uid);
                        }
                    });
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    // drawerLayout ?????????
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }
        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }
        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }
        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onBackPressed() {
        backKeyHandler.onBackPressed();
    }
}