package com.example.ukuwulele;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 123;
    private static final int PERMISSION_REQUEST_CODE = 321;
    String[] filenames;
    String choseFile;//imsorry
    Boolean loadFiles =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(!loadFiles){
            dirList();
            for(String filename: filenames) {
                System.out.println(filename);
                addButton(filename);

            }
            loadFiles=true;
        }
        requestNecessaryPermissions();
    }


    public void dirList() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                System.out.println("wat PROBLEM");
                requestNecessaryPermissions();
                return;
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("wat PROBLEM");
                requestNecessaryPermissions();
                return;
            }
        }

        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (downloadsFolder.exists() && downloadsFolder.isDirectory()) {
            File audiobooksFolder = new File(downloadsFolder, "audiobooks");
            if (audiobooksFolder.exists() && audiobooksFolder.isDirectory()) {
                File[] audiobookFiles = audiobooksFolder.listFiles();
                if (audiobookFiles != null) {
                    filenames = new String[audiobookFiles.length];
                    for (int i = 0; i < audiobookFiles.length; i++) {
                        filenames[i] = audiobookFiles[i].getName();
                    }
                }
            }
        }
    }
    public void switchToDownload(View view) {
        System.out.println("download");
        Intent intent= new Intent(MainActivity.this,downloadScreen.class);
        startActivity(intent);
    }

    public void addButton(String name){
        LinearLayout layout = (LinearLayout) findViewById(R.id.main);
        Button newbtn= new Button(this);
        newbtn.setText(name);
        newbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                choseFile=name;
                System.out.println(choseFile);
                Intent intent = new Intent(MainActivity.this, play.class);
                intent.putExtra("SONG_NAME",choseFile);
                startActivity(intent);
            }
        });
        layout.addView(newbtn);
    }

    private void requestNecessaryPermissions() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {

                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
            }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    dirList();
                } else {
                    System.out.println("Permission denied");
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                dirList();
            } else {
                // Permission is denied
                System.out.println("Permission denied");
            }
        }
    }

    @Override
    public void onClick(View view) {
        //meow meow meow meow meow
    }

}
