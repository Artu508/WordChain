package kr.jung.chat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogIn;
    private TextView buttonSignUp;

    private CheckBox cbAutoLogin;
    private SharedPreferences loginData;
    private boolean saveLoginData;
    private String id;
    private String pwd;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
 
        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPassword = (EditText) findViewById(R.id.editText_passWord);

        cbAutoLogin = (CheckBox) findViewById(R.id.cb_autologin);

        saveLoginData();

        buttonSignUp = (TextView) findViewById(R.id.btn_signup);
        buttonSignUp.setOnClickListener(v -> {
            // SignUpActivity 연결
            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
 
        buttonLogIn = (Button) findViewById(R.id.btn_login);
        buttonLogIn.setOnClickListener(v -> {
            if (!editTextEmail.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")) {
                loginUser(editTextEmail.getText().toString(), editTextPassword.getText().toString(), cbAutoLogin.isChecked());
            } else {
                Toast.makeText(LogInActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
            }
        });
 
        firebaseAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(LogInActivity.this, RoomListActivity.class);
                startActivity(intent);
                finish();
            }
        };

    }

    public void loginUser(String email, String password, boolean checked) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // 로그인 성공
                        Toast.makeText(LogInActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        firebaseAuth.addAuthStateListener(firebaseAuthListener);
                        if (checked){
                            setLoginData();
                        }
                    } else {
                        // 로그인 실패
                        Toast.makeText(LogInActivity.this, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveLoginData(){
        loginData = getSharedPreferences("loginData", MODE_PRIVATE);
        loadData();

        if (saveLoginData) {
            editTextEmail.setText(id);
            editTextPassword.setText(pwd);
            cbAutoLogin.setChecked(saveLoginData);
        }

    }

    private void setLoginData(){
        SharedPreferences.Editor editor = loginData.edit();

        editor.putBoolean("SAVE_LOGIN_DATA", cbAutoLogin.isChecked());
        editor.putString("ID", editTextEmail.getText().toString().trim());
        editor.putString("PWD", editTextPassword.getText().toString().trim());

        editor.apply();

    }

    private void loadData() {
        saveLoginData = loginData.getBoolean("SAVE_LOGIN_DATA", false);
        id = loginData.getString("ID", "");
        pwd = loginData.getString("PWD", "");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
 
    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
}