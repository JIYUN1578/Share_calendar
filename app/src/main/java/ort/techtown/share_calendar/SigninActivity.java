package ort.techtown.share_calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SigninActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private SignInButton btn_googlesign;
    Button btn_signout, btn_withdraw;
    private FirebaseAuth auth;
    GoogleSignInAccount gsa;
    private GoogleApiClient googleApiClient;
    private static final int REQ_GOOGLE_SIGN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();

        // 기존에 로그인 했던 계정을 확인한다.
        gsa = GoogleSignIn.getLastSignedInAccount(SigninActivity.this);

        // 로그인 되있는 경우
        if (gsa != null) {
            // 로그인 성공입니다.
            Toast.makeText(this, "이미 로그인 되어있음", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("name",gsa.getDisplayName());
            startActivity(intent);
        }

        auth = FirebaseAuth.getInstance();

        btn_googlesign = findViewById(R.id.btn_googlesign);
        btn_googlesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_GOOGLE_SIGN);
            }
        });

        btn_withdraw = (Button)findViewById(R.id.btn_withdraw);
        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeAccess();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_GOOGLE_SIGN){
            GoogleSignInResult result =  Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                gsa = result.getSignInAccount();
                resultLogin(gsa);
            }
        }
    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"호에엥 성공이야 ㅜ",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            String name = account.getDisplayName();
                            intent.putExtra("name",name);
                            Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"실패당ㅜ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void revokeAccess() {
        auth.getCurrentUser().delete();
        finishAffinity();
    }
}