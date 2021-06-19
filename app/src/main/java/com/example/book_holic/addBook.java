package com.example.book_holic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class addBook extends AppCompatActivity {
    AutoCompleteTextView autoCompleteTextView1,autoCompleteTextView2;
    private Button selectImage,publish;
    private ImageView imageView;
    String phone;
    Uri resultUri;
    ImagePicker imagePicker;
    DatabaseReference root;
    TextInputLayout title,desc,amt;
    private final int PERMISSION_ALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        //
        title=findViewById(R.id.bookTitle);
        desc=findViewById(R.id.bookDesc);
        amt=findViewById(R.id.amount);
        autoCompleteTextView1 = findViewById(R.id.forSellExchange);
        autoCompleteTextView2 = findViewById(R.id.category1);
        //
        selectImage = findViewById(R.id.select_image);
        publish = findViewById(R.id.publish);
        imageView = findViewById(R.id.book_img);
       final String[] PERMISSIONS = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        };

        phone = getIntent().getStringExtra("phone");
        //System.out.println(phone);
        root= FirebaseDatabase.getInstance().getReference("book").child(phone);

        String []option = {"Exchange","Sell"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.sellexchange,option);
       // autoCompleteTextView1.setText(arrayAdapter.getItem(0).toString(),false);
        autoCompleteTextView1.setAdapter(arrayAdapter);


        String []op = {"Action & Adventure","Children's","Crime","Drama","Fantasy","Horror","Mystery","Sci-Fi","Thriller","Autobiography",
        "Business/Economics","Cookbook","Dictionary","Health/Fitness","Philosophy","Travel"};
        ArrayAdapter arrAdapter = new ArrayAdapter(this,R.layout.forcategory,op);
       // autoCompleteTextView2.setText(arrAdapter.getItem(0).toString(),false);
        autoCompleteTextView2.setAdapter(arrAdapter);

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload(resultUri);
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermissions(addBook.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(addBook.this, PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    imagePicker.choosePicture(true /*show camera intents*/);
                }
            }
        });
        imagePicker = new ImagePicker(this, /* activity non null*/
                null, /* fragment nullable*/
                new OnImagePickedListener() {
                    @Override
                    public void onImagePicked(Uri imageUri) {
                        UCrop.of(imageUri, getTempUri())
                                .withAspectRatio(1, 1)
                                .start(addBook.this);
                    }
                });

    }
    private Uri getTempUri()
    {
        String dir = Environment.getExternalStorageDirectory()+File.separator+"Temp";
        File dirFile = new File(dir);
        dirFile.mkdir();
        String file = dir+File.separator+"temp.jpg";
        File tempFile = new File(file);
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(tempFile);
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }

            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL:{
                if (grantResults.length > 0) {
                  boolean flag=true;
                    for (int i=0;i<permissionsList.length;i++) {
                        if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                            flag = false;
                        }
                        if(flag)
                        {
                            imagePicker.choosePicture(true /*show camera intents*/);//   imagePicker.choosePicture(true);
                        }

                    }
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode,requestCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            resultUri = UCrop.getOutput(data);
            imageView.setImageURI(null);
            imageView.setImageURI(resultUri);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    void upload(Uri uri) {
        if(!validateTitle())
        { return; }
        else if( !validateDesc()){ return;}
        else if(!validateAmt()) {return;}
        else
        {
        //loader.setVisibility(View.VISIBLE);
        String strTitle, strDesc, strCategory, strType, strAmt;
        strTitle = title.getEditText().getText().toString();
        strDesc = desc.getEditText().getText().toString();
        strAmt = amt.getEditText().getText().toString();
        strCategory = autoCompleteTextView1.getText().toString();
        strType = autoCompleteTextView2.getText().toString();
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("Temp/" + System.currentTimeMillis() + ".png");

        riversRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("error", "onfailure" + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        final Uri downloadUrl = uri;
                        String url = downloadUrl.toString();
                        //String modelId = root.push().getKey();
                        HashMap<String, String> userMap = new HashMap<>();
                        userMap.put("Title", strTitle);
                        userMap.put("Description", strDesc);
                        userMap.put("Category", strCategory);
                        userMap.put("Type", strType);
                        userMap.put("Amount", strAmt);
                        userMap.put("phone", phone);
                        userMap.put("ImageUrl", url);

                        root.push().setValue(userMap);
                        Picasso.get().load(uri).into(imageView);
                    }
                });
                Toast.makeText(getApplicationContext(), "Successfully added", Toast.LENGTH_LONG).show();
                title.getEditText().setText("");
                desc.getEditText().setText("");
                amt.getEditText().setText("");
                autoCompleteTextView1.setText("");
                autoCompleteTextView2.setText("");
                imageView.setImageResource(0);
                //imageView.setImageResource(android.R.color.transparent);
            }
        });
    }
    }

    private Boolean validateTitle()
    {
        String val = title.getEditText().getText().toString();
        if (val.isEmpty())
        {
            title.setError("Field can not be empty");
            return false;
        }
        else
        {
            title.setError(null);
            title.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateDesc()
    {
        String val = title.getEditText().getText().toString();
        if (val.isEmpty())
        {
            desc.setError("Field can not be empty");
            return false;
        }
        else
        {
            desc.setError(null);
            desc.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateAmt()
    {
        String val = title.getEditText().getText().toString();
        if (val.isEmpty())
        {
            amt.setError("Field can not be empty");
            return false;
        }
        else
        {
            amt.setError(null);
            amt.setErrorEnabled(false);
            return true;
        }
    }

}