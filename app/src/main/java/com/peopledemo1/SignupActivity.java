package com.peopledemo1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText email_edit;
    private EditText id_edit;
    private EditText password_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email_edit = findViewById(R.id.signup_email_edit);
        id_edit = findViewById(R.id.signup_id_edit);
        password_edit = findViewById(R.id.signup_password_edit);

        findViewById(R.id.signup_accept_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(id_edit.getText().toString(),password_edit.getText().toString(),email_edit.getText().toString());
                RetrofitHelper.getApiService().sendSign(user).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String res = response.body();
                        if(res == null)
                            Log.e("res","null");
                        if(res != null && res.equals("성공")) {
                            Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                            intent.putExtra("email",email_edit.getText().toString());
                            startActivity(intent);
                        }else
                            Log.e("Signup Error","회원가입 실패");
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("Signup Error",t.getMessage());
                    }
                });
            }
        });

        findViewById(R.id.signup_profile_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}
