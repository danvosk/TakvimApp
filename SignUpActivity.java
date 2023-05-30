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

import com.example.takvim.databinding.ActivitySignUpBinding;
import com.example.takvim.databinding.ActivityTakvimBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth auth;

    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();


        binding.signUpBtnKayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = binding.signUpEmail.getText().toString();
                String Pass = binding.signUpPassword.getText().toString();
                createAccount(Email,Pass);
                Intent toSignin = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(toSignin);
            }
        });

        binding.btnCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignin = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(toSignin);
            }
        });

    }

    private void createAccount(String email,String password){
        email = binding.signUpEmail.getText().toString();
        password = binding.signUpPassword.getText().toString();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        }else {
                            Log.w(TAG, "createUserWithEmail:failure",task.getException());
                            Toast.makeText(SignUpActivity.this,"Doğrulama başarısız",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    public void reload(){}

    public void updateUI(FirebaseUser user) {

    }
}