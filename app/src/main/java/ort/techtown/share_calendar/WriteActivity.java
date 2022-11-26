package ort.techtown.share_calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ort.techtown.share_calendar.Data.Comment;
import ort.techtown.share_calendar.Data.Post;

public class WriteActivity extends AppCompatActivity {

    // drawerLayout
    private DrawerLayout drawerLayout;
    private Button btn_logout, btn_calendar, btn_search, btn_make, btn_group, btn_close, btn_profile;
    private View drawerView;
    private ImageView menu_open;
    private TextView tv_title;
    // 파이어베이스
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference tmpReference = database.getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    // 그룹 정보
    private String groupname, uid, name;
    // 게시글 관련
    private EditText et_title, et_summary;
    private ImageView iv_image;
    private FloatingActionButton btn_camera;
    private static final int REQUEST_CODE = 0;
    private static final int TMP_REQUEST_CODE = 1;
    private TextView tv_register;
    private Uri uri, tmp_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        groupname = getIntent().getStringExtra("groupname");
        uid = getIntent().getStringExtra("uid");
        name = getIntent().getStringExtra("name");
        Log.e("name",name);

        // 툴바
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(groupname);

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
                Intent intent = new Intent(WriteActivity.this, MainActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });
        // 그룹 생성 버튼
        btn_make = (Button)findViewById(R.id.btn_make);
        btn_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WriteActivity.this, MakeActivity.class);
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
                Intent intent = new Intent(WriteActivity.this, SearchActivity.class);
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
                Intent intent = new Intent(WriteActivity.this, GroupActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                startActivity(intent);
                finish();
            }
        });
        // 마이프로필 버튼
        btn_profile = (Button)findViewById(R.id.btn_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, TMP_REQUEST_CODE);
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

        // 게시글 적기
        et_title = (EditText)findViewById(R.id.et_title);
        et_summary = (EditText)findViewById(R.id.et_summary);
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

        // 게시글 등록하기
        tv_register = (TextView) findViewById(R.id.tv_postmove);
        tv_register.setText("완료");
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String time = sdf.format(date);
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
                Post post = new Post(uid, name, time, filename, et_title.getText().toString(), et_summary.getText().toString(),false,null,null,null);
                databaseReference.child("Group").child(groupname).child("Post").child(time).setValue(post);
                Toast.makeText(getApplicationContext(),"작성이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(3, intent);
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
        else if(requestCode == TMP_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                try{
                    tmp_uri = data.getData();
                    String filename;
                    if(tmp_uri!=null) {
                        filename = tmp_uri.toString() + ".jpg";
                        StorageReference riverRef = storageReference.child("post_img/"+filename);
                        UploadTask uploadTask = riverRef.putFile(tmp_uri);
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
                        Toast.makeText(getApplicationContext(),"프로필 사진이 변경되었습니다.",Toast.LENGTH_SHORT).show();
                    }
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
}