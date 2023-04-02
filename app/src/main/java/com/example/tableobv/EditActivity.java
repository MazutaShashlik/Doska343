package com.example.tableobv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class EditActivity extends AppCompatActivity {

    private ImageView imItem;
    private StorageReference mStorageRef;
    private Uri uploadUri;
    private Spinner spinner;
    private EditText edTitle, edPrice, edTel, edDisc;

    private FirebaseAuth mAuth;

    private DatabaseReference dRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        init();
    }
    private void init()
    {
        edTitle = findViewById(R.id.idTitle);
        edDisc = findViewById(R.id.idDesc);
        edPrice = findViewById(R.id.idPrice);
        edTel = findViewById(R.id.idPhone);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this,R.array.category_spin, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.getSelectedItem();
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        imItem = findViewById(R.id.imageProduct);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null && data.getData() != null)
        {
            if(resultCode == RESULT_OK)
            {
                imItem.setImageURI(data.getData());
                uploadImage();
            }
        }
    }

    private void uploadImage()
    {
        Bitmap bitMap = ((BitmapDrawable)imItem.getDrawable()).getBitmap();
        ByteArrayOutputStream out  = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        byte[] byteArray= out.toByteArray();
        final StorageReference mRef = mStorageRef.child(System.currentTimeMillis() + "_image");
        UploadTask up =mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri = task.getResult();
                Toast.makeText(EditActivity.this, "upload done: " + uploadUri.toString(), Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void onClickSavePost(View view)
    {
        SavePost();

    }

    public void onClickImage(View view)
    {
        getImage();
    }

    private void  getImage()
    {
        Intent intent  = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 10);
    }

    private void SavePost()
        {
            dRef  = FirebaseDatabase.getInstance().getReference(spinner.getSelectedItem().toString());
            mAuth = FirebaseAuth.getInstance();


            if(mAuth.getUid() != null){

                String key = dRef.push().getKey();
                NewPost post = new NewPost();

                post.setImageID(uploadUri.toString());
                post.setTitle(edTitle.getText().toString());
                post.setTell(edTel.getText().toString());
                post.setPrice(edPrice.getText().toString());
                post.setDisc(edDisc.getText().toString());
                post.setKey(key);
                if(key!= null)dRef.child(mAuth.getUid()).child(key).setValue(post);
            }
        }
}
