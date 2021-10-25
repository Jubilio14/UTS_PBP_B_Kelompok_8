package com.medicare.prohealthymedicare.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.medicare.prohealthymedicare.MainActivity;
import com.medicare.prohealthymedicare.R;
import com.medicare.prohealthymedicare.database.AppDatabase;
import com.medicare.prohealthymedicare.database.dao.UsersDao;
import com.medicare.prohealthymedicare.database.entity.UserEntity;
import com.medicare.prohealthymedicare.databinding.ActivityLoginBinding;
import com.medicare.prohealthymedicare.session.Session;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AppDatabase database;
    private List<UserEntity> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        binding.getLifecycleOwner();

        database = AppDatabase.getInstance(this);

        binding.btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.edtusername.getText().toString().trim();
                String password = binding.edtpassword.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()){
                    final UsersDao userDao = database.userDao();
                    UserEntity userEntity =  userDao.login(username,password);
                    if (userEntity==null){
                        Snackbar.make(v, "Login Gagal ", Snackbar.LENGTH_LONG).show();
                    }else {
                        Session.setIsLogin(getBaseContext(),true);
                        Session.setIsFirstname(getBaseContext(),userEntity.getFirstname());
                        Session.setIsLastName(getBaseContext(),userEntity.getLastname());
                        Session.setIsUsername(getBaseContext(),userEntity.getUsername());
                        Session.setIsPassword(getBaseContext(),userEntity.getPassword());
                        Session.setIsFoto(getBaseContext(),userEntity.getFoto());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }else {
                    Snackbar.make(v, "Isi semua kolom ", Snackbar.LENGTH_LONG).show();
                }

            }
        });

        binding.btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        binding.btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtusername.setText(null);
                binding.edtpassword.setText(null);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Session.getIsLogin(getBaseContext())){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}