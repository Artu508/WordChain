package kr.jung.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword, editTextPassword2;
    private EditText editTextName;
    private Button buttonJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_passWord);
        editTextPassword2 = findViewById(R.id.editText_passWord2);
        editTextName = findViewById(R.id.editText_name);

        buttonJoin = findViewById(R.id.btn_join);
        buttonJoin.setOnClickListener(v -> {
            if(!editTextEmail.getText().toString().matches("[^@.]+@[^@]+")) {
                Toast.makeText(SignUpActivity.this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(editTextPassword.getText().toString().length() < 8) {
                Toast.makeText(SignUpActivity.this, "비밀번호가 너무 짧습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(editTextName.getText().toString().length() == 0) {
                Toast.makeText(SignUpActivity.this, "이름이 비어 있습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(!editTextPassword.getText().toString().equals(editTextPassword2.getText().toString())) {
                Toast.makeText(SignUpActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                createUser(editTextEmail.getText().toString(), editTextPassword.getText().toString(), editTextName.getText().toString());
            }
        });
    }

    private void createUser(String email, String password, String name) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // 회원가입 성공시
                        Toast.makeText(SignUpActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                        finish();
                    } else {
                        // 계정이 중복된 경우
                        Toast.makeText(SignUpActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}