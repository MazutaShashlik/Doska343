package com.example.petts;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private EditText nickname;
    private EditText fname;
    private EditText lname;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zagruser);



        fname = (EditText) findViewById(R.id.nameuser);
        lname = (EditText) findViewById(R.id.famuser);


    }

    public void Click(View view){
        EditText nickname = (EditText) findViewById(R.id.nickuser);
        String n = String.valueOf(nickname.getText());
        Toast.makeText(UserActivity.this, n,
                Toast.LENGTH_SHORT).show();

        UserInfo userInfo = UserInfo.retrofit.create(UserInfo.class);
        final Call<User> call = userInfo.getData(n);
        call.enqueue(new Callback<User>() {

                         @Override
                         public void onResponse(Call<User> call, Response<User> response) {
                             if (response.isSuccessful()) {
                                 User user = response.body();
                                 nickname.setText(user.getUsername());
                                 fname.setText(user.getFirstName());
                                 lname.setText(user.getLastName());

                             } else {
                                 // Обрабатываем ошибку
                                 ResponseBody errorBody = response.errorBody();
                                 try {
                                     Toast.makeText(UserActivity.this, errorBody.string(),
                                             Toast.LENGTH_SHORT).show();
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<User> call, Throwable throwable)  {
                             Toast.makeText(UserActivity.this, "Что-то пошло не так " + throwable.getMessage(),
                                     Toast.LENGTH_SHORT).show();
                             Log.d("ERRORS_GIT", ""+throwable.getMessage());
                         }
                     }
        );

    }

}