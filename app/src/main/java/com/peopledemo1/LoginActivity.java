package com.peopledemo1;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView emailView = (TextView) findViewById(R.id.loginemail);
        final TextView passwordView = (TextView) findViewById(R.id.loginpassword);

        Button loginButton = (Button) findViewById(R.id.loginbutton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitHelper.getApiService().sendLogin(emailView.getText().toString(),passwordView.getText().toString()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String res = response.body();
                        if (res != null && res.equals("성공")){
                            Intent intent2 = new Intent(context, MainActivity.class);
                            intent2.putExtra("email",emailView.getText().toString());
                            startActivity(intent2);
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"로그인에 실패했띠용 @.@",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,"다시 시도해주라잉",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.login_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignupActivity.class));
            }
        });
    }
}
