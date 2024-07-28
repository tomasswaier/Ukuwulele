package com.example.ukuwulele;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class play extends AppCompatActivity implements View.OnClickListener {
    Button button;
    MediaPlayer player;
    boolean playing;
    String filename;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        filename=intent.getStringExtra("SONG_NAME");
        textView = (TextView) findViewById(R.id.txtName);
        textView.setText(filename);
        playing = false;
        button = findViewById(R.id.button);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!playing) {
            if (player == null) {
                File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File audiobooksFolder = new File(downloadsFolder, "audiobooks");
                player = MediaPlayer.create(play.this, Uri.fromFile(new File(audiobooksFolder,filename)));
                System.out.println("test");
            }
            player.start();
            playing=true;
        }
        else{
            playing=false;
            player.pause();


        }


    }
}