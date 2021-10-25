package com.medicare.prohealthymedicare.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.medicare.prohealthymedicare.R;
import com.medicare.prohealthymedicare.database.AppDatabase;
import com.medicare.prohealthymedicare.database.entity.UserEntity;
import com.medicare.prohealthymedicare.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.getLifecycleOwner();
        database = AppDatabase.getInstance(this);


        binding.btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = binding.edtfirstname.getText().toString().trim();
                String lastname = binding.edtlastname.getText().toString().trim();
                String username = binding.edtusername.getText().toString().trim();
                String password = binding.edtpassword.getText().toString().trim();

                if (!firstname.isEmpty() && !lastname.isEmpty() && !username.isEmpty()) {
                    UserEntity userEntity = database.userDao().cekuser(username);
                    if (userEntity==null){
                        database.userDao().insertAuth(firstname,lastname,username,password);
                        finish();
                    }else {
                        Snackbar.make(v, "email sudah terdaftar", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(v, "Isi semua kolom ", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }
}