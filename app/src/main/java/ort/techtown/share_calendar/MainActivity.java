package ort.techtown.share_calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.Objects;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    TextView temp;
    Button btn_logout;
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        temp = (TextView) findViewById(R.id.temptext);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if(name != null){
            Toast.makeText(getApplicationContext(),intent.getStringExtra("name").toString(),Toast.LENGTH_SHORT).show();
            temp.setText(name);
        }
        else{
            Toast.makeText(getApplicationContext(),"없다는데?",Toast.LENGTH_SHORT).show();
        }

        btn_logout = (Button)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,FirebaseAuth.getInstance().toString());
                FirebaseAuth.getInstance().signOut();
                Log.d(TAG,FirebaseAuth.getInstance().toString());
                startActivity(new Intent(MainActivity.this, SigninActivity.class));
                Log.d(TAG, "넘어갔어요~");
                finish();

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}