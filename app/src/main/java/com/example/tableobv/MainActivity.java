package com.example.tableobv;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

import java.nio.channels.AlreadyBoundException;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView nav_view;
    private DrawerLayout drawerLayout;
    private TextView useremail;
    private FirebaseAuth mAuth;
    private AlertDialog dialog;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onStart(){
        super.onStart();
        getUserData();
     }

    public void onClickedit(View view)
    {
        Intent i= new Intent(MainActivity.this, EditActivity.class);
        startActivity(i);
    }
    private void init()
    {

        nav_view = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.toggle_open, R.string.toggle_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);
        //drawerLayout = findViewById(R.id.);
        useremail = nav_view.getHeaderView(0).findViewById(R.id.tvEmail);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference("Table");
        myRef.setValue("Hello, World!");
        //test
    }
    private void getUserData()
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
         if (currentUser != null)
        {
            useremail.setText(currentUser.getEmail());
        } else
        {
            useremail.setText(R.string.sign_in_or_sign_up);
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.id_my_ads:
                Toast.makeText(this, "Pressed id my ads", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_cars_ads:
                Toast.makeText(this, "Pressed id cars", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_dm_ads:
                Toast.makeText(this, "Pressed id dm", Toast.LENGTH_SHORT).show();
                break;
            case R.id.smart:
                Toast.makeText(this, "Pressed id smart", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_pc_ads:
                Toast.makeText(this, "Pressed id pc", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_sign_in:
                signUpDialog(R.string.sig_in, R.string.sign_in_button, 1);
                Toast.makeText(this, "Pressed id sign in", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_sign_out:
                signOut();
                Toast.makeText(this, "Pressed id sign out", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_sing_up:
                signUpDialog(R.string.sign_up, R.string.sign_up_button, 0);
                Toast.makeText(this, "Pressed id sign up", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
    private void signUpDialog(int title, int btntitle, int index)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.alert_layout_sign_up, null);
        dialogBuilder.setView(dialogview);
        TextView titletextview = dialogview.findViewById(R.id.tvAlertTitle);
        titletextview.setText(title);
        EditText idEmail = dialogview.findViewById(R.id.idemail);
        EditText idPass = dialogview.findViewById(R.id.idpassword);
        Button b = dialogview.findViewById(R.id.buttonsign_up);
        b.setText(btntitle);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 0){
                    signUp(idEmail.getText().toString(), idPass.getText().toString());
                }
                else {
                    signIn(idEmail.getText().toString(), idPass.getText().toString());
                }
                dialog.dismiss();


            }
        });



        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void signUp(String email, String password)
    {
        if (!email.equals("") && !password.equals("")) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                getUserData();


                            } else {
                                Log.w("MyLogMainAct", "createwithEP fail", task.getException());
                                Toast.makeText(getApplicationContext(), "Auth failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Одно из полей пустое", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn(String email, String password)
    {
        if (!email.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                getUserData();
                            } else {
                                Log.w("MyLogAct", "signInfail", task.getException());
                                Toast.makeText(getApplicationContext(), "Авторизация провалена", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Одно из полей пустое", Toast.LENGTH_SHORT).show();

        }
    }

    private void signOut()
    {
        mAuth.signOut();
        getUserData();
    }
}