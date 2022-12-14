package ort.techtown.share_calendar;

import static ort.techtown.share_calendar.R.drawable.post_con;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.bumptech.glide.Glide;
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
import java.util.List;

import ort.techtown.share_calendar.Adapter.CalendarAdapter;
import ort.techtown.share_calendar.Adapter.TodoListAdapter;
import ort.techtown.share_calendar.Data.BackKeyHandler;
import ort.techtown.share_calendar.Data.CalendarUtil;
import ort.techtown.share_calendar.Data.Info;

public class PostActivity extends AppCompatActivity {

    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);
    // drawerLayout
    private DrawerLayout drawerLayout;
    private Button btn_logout, btn_calendar, btn_search, btn_make, btn_group, btn_close, btn_profile;
    private View drawerView;
    private ImageView menu_open;
    private TextView tv_title;
    // ??????????????????
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference mReference = database.getReference();
    private DatabaseReference reference = database.getReference();
    private DatabaseReference seenReference = database.getReference();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    // ??????
    TextView tv_monthyear;
    RecyclerView recyclerView, todoListRecyclerView;
    // ?????? ??????
    private String groupname, uid, name, tmp_uid;
    private Integer cnt, tmp_cnt;
    private TextView group_name, group_introduce;
    private ImageView group_image, iv_postmove;
    // ????????? ?????? ??????
    private static final int REQUEST_CODE = 0;
    private Uri uri;
    // ????????? ??????
    private TextView tv_postmove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        groupname = getIntent().getStringExtra("groupname");
        uid = getIntent().getStringExtra("uid");
        name = getIntent().getStringExtra("name");

        iv_postmove = findViewById(R.id.iv_postmove);
        iv_postmove.setVisibility(View.VISIBLE);
        iv_postmove.setBackgroundResource(post_con);
        iv_postmove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this,NoticeBoardActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("groupname",groupname);
                intent.putExtra("uid",uid);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                finish();
            }
        });

        // ??????
        tv_monthyear = findViewById(R.id.tv_month_year);
        ImageButton btn_pre = findViewById(R.id.btn_frontmonth);
        ImageButton btn_next = findViewById(R.id.btn_nextmonth);
        recyclerView = findViewById(R.id.recyclerview);
        todoListRecyclerView = findViewById(R.id.todoListRecyclerView);
        CalendarUtil.today = LocalDate.now();
        CalendarUtil.selectedDate = LocalDate.now();
        setTodoList(uid);
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

        // ??????
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(groupname);

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
        // ??????????????? ??????
        btn_calendar = (Button)findViewById(R.id.btn_calendar);
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, MainActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });
        // ?????? ?????? ??????
        btn_make = (Button)findViewById(R.id.btn_make);
        btn_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, MakeActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });
        // ?????? ?????? ??????
        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, SearchActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });
        // ???????????? ??????
        btn_group = (Button)findViewById(R.id.btn_group);
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, GroupActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
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

        // ?????? ?????? ????????????
        group_name = (TextView)findViewById(R.id.group_name);
        group_introduce = (TextView)findViewById(R.id.group_introduce);
        group_image = (ImageView)findViewById(R.id.group_image);
        group_image.setClipToOutline(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("Group").child(groupname).child("groupname").getValue().toString();
                String introduce = snapshot.child("Group").child(groupname).child("introduce").getValue().toString();
                if(!snapshot.child("Group").child(groupname).child("image_url").getValue().toString().equals(" ")) {
                    String url = snapshot.child("Group").child(groupname).child("image_url").getValue().toString();
                    StorageReference pathReference = storageReference.child("post_img/"+url);
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(PostActivity.this).load(uri).centerCrop().into(group_image);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
                else {
                    group_image.setImageBitmap(null);
                }
                group_name.setText(name);
                group_introduce.setText(introduce);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
                        databaseReference.child("User").child(uid).child("Image_url").setValue(filename);
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
        tv_monthyear.setText(monthYearFromDate(CalendarUtil.selectedDate));
        // ?????? ??? ?????? ????????????
        ArrayList<LocalDate> dayList = daysInMonthArray(CalendarUtil.selectedDate);
        // ????????? ????????? ??????
        CalendarAdapter adapter = new CalendarAdapter(dayList);
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

    private void setTodoList(String uid) {
        // ?????? ?????? ????????????
        ArrayList<Info> infolist = new ArrayList<>();
        ArrayList<String> seenList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        cnt = 0;
        List<String> uidList = new ArrayList<>();

        TodoListAdapter adapter = new TodoListAdapter(infolist, false, groupname, seenList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        // ????????? ??????
        todoListRecyclerView.setLayoutManager(manager);
        todoListRecyclerView.setAdapter(adapter);
        // UidList ?????? ????????????
        mReference = database.getReference("/Group/"+groupname+"/Uid");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot uid_snapshot) {
                for(DataSnapshot Uid_Snapshot : uid_snapshot.getChildren()) {
                    tmp_uid = Uid_Snapshot.getValue().toString();
                    uidList.add(tmp_uid);
                    reference = database.getReference("User").child(tmp_uid).child("Calender").child(CalendarUtil.selectedDate.toString());
                    Query myTopPostsQuery = reference.orderByChild("start");
                    myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            cnt++;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                infolist.add(snapshot.getValue(Info.class));
                                seenReference = database.getReference("User").child(uidList.get(cnt - 1)).child("Calender").child(CalendarUtil.selectedDate.toString())
                                        .child(snapshot.getKey()).child("isSeen").child(groupname);
                                seenReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot seen_snapshot) {
                                        if(seen_snapshot.getValue() == null) {
                                            seenList.add("false");
                                        }
                                        else if(seen_snapshot.getValue().toString().equals("false")) {
                                            seenList.add("false");
                                        }
                                        else {
                                            seenList.add("true");
                                        }
                                        if(infolist.size() == seenList.size()){
                                            TodoListAdapter adapter = new TodoListAdapter(infolist, false, groupname, seenList);
                                            RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
                                            // ????????? ??????
                                            todoListRecyclerView.setLayoutManager(manager);
                                            todoListRecyclerView.setAdapter(adapter);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    // ?????? ?????? ??????
    private String monthYearFromDate(LocalDate localDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM??? yyyy");
        return localDate.format(formatter);
    }

    private ArrayList<LocalDate> daysInMonthArray(LocalDate localDate){
        ArrayList<LocalDate> daylist = new ArrayList<>();
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
                daylist.add(LocalDate.of(premonthDate.getYear(), premonthDate.getMonth(),
                        lastdayOfpremonth - dayOfweek + i ));
            }
            else if(i> lastday + dayOfweek){
                LocalDate nextmonthDate = CalendarUtil.selectedDate.plusMonths(1);
                daylist.add(LocalDate.of(nextmonthDate.getYear(),
                        nextmonthDate.getMonth(),
                        i - lastday-dayOfweek));
            }else{
                daylist.add(LocalDate.of(CalendarUtil.selectedDate.getYear(),
                        CalendarUtil.selectedDate.getMonth(),
                        i - dayOfweek));
            }
        }
        return daylist;
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
    public void onBackPressed() {

        backKeyHandler.onBackPressed();
    }
}