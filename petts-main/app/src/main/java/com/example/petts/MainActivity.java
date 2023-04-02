package com.example.petts;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.RecyclerView;

    import android.os.Bundle;


    import android.util.Log;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.ProgressBar;
    import android.widget.TextView;
    import android.widget.Toast;


    import com.squareup.picasso.Picasso;

    import java.io.IOException;

    import okhttp3.ResponseBody;
    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class MainActivity extends AppCompatActivity {

        private ProgressBar mProgressBar;
        RecyclerView mRecyclerView;
        private ImageView photo;
        private TextView text;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

            mProgressBar = (ProgressBar) findViewById(R.id.eff);
            photo = (ImageView) findViewById(R.id.imageView2);
            text = (TextView) findViewById(R.id.textView);

            mProgressBar.setVisibility(View.INVISIBLE);


            PetInfo petInfo = PetInfo.retrofit.create(PetInfo.class);
            final Call<Pet> call = petInfo.getData();


            call.enqueue(new Callback<Pet>() {
                             @Override
                             public void onResponse(Call<Pet> call, Response<Pet> response) {
                                 if (response.isSuccessful()) {
                                     Pet pet = response.body();
                                     text.setText(pet.getName());

                                     Picasso.with(MainActivity.this)
                                             .load(pet.getPhotoUrls().get(0))
                                             .into(photo);

                                     mProgressBar.setVisibility(View.INVISIBLE);
                                 } else {
                                     // Обрабатываем ошибку
                                     ResponseBody errorBody = response.errorBody();
                                     try {
                                         Toast.makeText(MainActivity.this, errorBody.string(),
                                                 Toast.LENGTH_SHORT).show();
                                         mProgressBar.setVisibility(View.INVISIBLE);
                                     } catch (IOException e) {
                                         e.printStackTrace();
                                     }
                                 }
                             }

                             @Override
                             public void onFailure(Call<Pet> call, Throwable throwable)  {
                                 Toast.makeText(MainActivity.this, "Что-то пошло не так " + throwable.getMessage(),
                                         Toast.LENGTH_SHORT).show();
                                 Log.d("ERRORS_GIT", ""+throwable.getMessage());
                                 mProgressBar.setVisibility(View.INVISIBLE);
                             }
                         }
            );
        }
    }