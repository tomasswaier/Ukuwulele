package com.example.ukuwulele;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.content.Context;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import java.io.File;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 123; // You can use any integer value here
    private static final int PERMISSION_REQUEST_CODE =321;
    private RadioGroup rgGroup;
    String[] filenames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        rgGroup = findViewById(R.id.rgGroup);
        String[] downloadsContents;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dirList();
        Button btnPlay = findViewById(R.id.BtnPlay);
        btnPlay.setOnClickListener(this);


    }
    public void dirList(){
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                startActivity(new Intent(this, MainActivity.class));
            } else { //request for the permission
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        } else {
            //below android 11=======
            startActivity(new Intent(this, MainActivity.class));
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, so you can access the Downloads folder
            File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (downloadsFolder.exists() && downloadsFolder.isDirectory()) {
                // Downloads folder exists and is a directory
                File audiobooksFolder = new File(downloadsFolder, "audiobooks");
                if (audiobooksFolder.exists() && audiobooksFolder.isDirectory()) {
                    // audiobooks folder exists and is a directory
                    // Now you can access the files inside the audiobooks folder
                    File[] audiobookFiles = audiobooksFolder.listFiles();
                    if (audiobookFiles != null) {
                        filenames=new String[audiobookFiles.length];
                        for(int i=0;i<audiobookFiles.length;i++){
                            filenames[i]=audiobookFiles[i].getName();
                            System.out.println(filenames[i]);
                        }

                    }
                    // Loop through audiobookFiles array to access individual files
                } else {
                    // audiobooks folder does not exist or is not a directory
                    // Handle this case
                }
            } else {
                // Downloads folder does not exist or is not a directory
                // Handle this case
            }
        } else {
            // Permission is not granted, so request it
            System.out.println("PROBLEM");
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);        }


    }
    @Override
    public void onClick(View v) {
        int checkedId=rgGroup.getCheckedRadioButtonId();
        System.out.println(checkedId);
        Intent intent=new Intent(this,play.class);
        startActivity(intent);

    }
}