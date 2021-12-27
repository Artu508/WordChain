package kr.jung.chat;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button login, gotoSignup;
    EditText email, pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.emailAdd);
        pw = findViewById(R.id.password);
        login = findViewById(R.id.login);
        gotoSignup = findViewById(R.id.to_signup_page);

        login.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            Task<AuthResult> authResultTask = auth.signInWithEmailAndPassword(email.getText().toString(), pw.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isComplete()) {
                        Log.d(TAG, "Sign In Success");
                        Toast.makeText(LoginActivity.this, "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT);
                    } else {
                        Log.d(TAG, "Sign In Fail");
                        Toast.makeText(LoginActivity.this, "로그인에 실패하셨습니다. 이메일과 비밀번호를 확인하여주세요.", Toast.LENGTH_SHORT);
                    }
                }
            });

        });

    }
}