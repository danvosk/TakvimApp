package com.example.takvim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takvim.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ActivityMainBinding binding;

    private static final String TAG = "EmailPassword";
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null){
            reload();
        }
    }

    SignUpActivity signUpActivity = new SignUpActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        auth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = binding.editEmail.getText().toString();
                String Pass = binding.editPassword.getText().toString();
                signIn(Email,Pass);
            }
        });


        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void signIn(String email,String password){
        email = binding.editEmail.getText().toString();
        password = binding.editPassword.getText().toString();

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            signUpActivity.updateUI(user);

                            Intent toTakvim = new Intent(MainActivity.this,TakvimActivity.class);
                            startActivity(toTakvim);
                        }else {
                            Log.w(TAG, "signInWithEmail:failure",task.getException() );
                            Toast.makeText(MainActivity.this,"Doğrulama başarısız",
                                    Toast.LENGTH_SHORT).show();
                            signUpActivity.updateUI(null);
                        }
                    }
                });
    }

    public void reload(){}
}