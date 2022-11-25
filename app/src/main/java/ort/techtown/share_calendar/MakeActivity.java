package ort.techtown.share_calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ort.techtown.share_calendar.Data.BackKeyHandler;
import ort.techtown.share_calendar.Data.Group;

public class MakeActivity extends AppCompatActivity {

    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);
    // drawerLayout
    private DrawerLayout drawerLayout;
    private Button btn_logout, btn_calendar, btn_search, btn_group, btn_close,btn_make;
    private ImageButton  btn_save;
    private View drawerView;
    private ImageView menu_open;
    private TextView tv_title;
    // 그룹 생성
    private EditText et_groupname, et_introduce;
    private ImageView iv_image;
    private FloatingActionButton btn_camera;
    private static final int REQUEST_CODE = 0;
    private Uri uri;
    // 파이어베이스
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    private String uid, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        name = intent.getStringExtra("name");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("그룹생성");

        // drawerLayout
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);

        // drawerLayout 열기 버튼
        menu_open = (ImageView)findViewById(R.id.menu_open);
        menu_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        // drawLayout 닫기 버튼
        btn_close = (Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

        // 마이캘린더 버튼
        btn_calendar = (Button)findViewById(R.id.btn_calendar);
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeActivity.this, MainActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });

        // 그룹 검색 버튼
        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeActivity.this, SearchActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });

        // 마이그룹 버튼
        btn_group = (Button)findViewById(R.id.btn_group);
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeActivity.this, GroupActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });

        // 로그아웃 버튼
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

        // 그룹 이미지
        iv_image = (ImageView)findViewById(R.id.iv_image);
        btn_camera = (FloatingActionButton)findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_image.setImageResource(0);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // 그룹 생성
        et_groupname = (EditText)findViewById(R.id.et_groupname);
        et_introduce = (EditText)findViewById(R.id.et_introduce);
        btn_save = (ImageButton)findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                }
                else {
                    filename = " ";
                }
                addGroup(et_groupname.getText().toString(), et_introduce.getText().toString(), uid,1, filename);
                Toast.makeText(getApplicationContext(),"그룹이 생성되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MakeActivity.this, MainActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
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
                    Glide.with(getApplicationContext()).load(uri).into(iv_image);
                } catch(Exception e){
                }
            }
            else if(resultCode == RESULT_CANCELED){
            }
        }
    }

    // drawerLayout 리스너
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

    // 그룹 데이터 저장 함수
    public void addGroup(String groupname, String introduce, String hostuid, Integer groupnum, String image_url) {
        Group group = new Group(groupname, introduce, hostuid, groupnum, image_url);
        databaseReference.child("Group").child(groupname).setValue(group);
        databaseReference.child("Group").child(groupname).child("Uid").push().setValue(uid);
        databaseReference.child("User").child(uid).child("Group").push().setValue(groupname);
    }

    @Override
    public void onBackPressed() {
        backKeyHandler.onBackPressed();
    }
}