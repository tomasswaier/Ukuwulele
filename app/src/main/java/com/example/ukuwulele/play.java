package com.example.ukuwulele;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class play extends AppCompatActivity implements View.OnClickListener {
    private SeekBar seekBar;
    Button button;
    MediaPlayer player;
    boolean playing;
    String filename;
    TextView textView;
    TextView textViewCurrent;
    TextView textViewMax;
    String savedTimes = "times.txt";
    HashMap<String, String> timeStamps = new HashMap<>(); // Initialize here
    Handler handler = new Handler();
    Runnable updateSeekBar;
    SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");

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
        filename = intent.getStringExtra("SONG_NAME");
        textView = (TextView) findViewById(R.id.txtName);
        textViewCurrent = (TextView) findViewById(R.id.textViewCurrent);
        textViewMax = (TextView) findViewById(R.id.textViewMax);
        textView.setText(filename);
        playing = false;

        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        seekBar = findViewById(R.id.seekBar);

        timeStamps = new HashMap<>();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && player != null) {
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Add actions when touch starts
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Add actions when touch stops
            }
        });
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (player != null && playing) {
                    int currentPosition=player.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    textViewCurrent.setText(formatter.format(new Date(currentPosition)));
                    handler.postDelayed(this, 1000); // Update every 1000ms (1 second)
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (!playing) {
            button.setText("Pause");
            if (player == null) {
                loadAudioFile();
            }
            startPlayer();
        } else {
            button.setText("Start");
            stopPlayer();
        }
    }

    private void loadAudioFile() {
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File audiobooksFolder = new File(downloadsFolder, "audiobooks");
        player = MediaPlayer.create(play.this, Uri.fromFile(new File(audiobooksFolder, filename)));
    }

    boolean firstPlay = true;
    private void startPlayer() {
        if (firstPlay) {
            player.seekTo(readTime());
            firstPlay = false;
            int currentTime=player.getDuration();
            seekBar.setMax(currentTime);
            textViewMax.setText(formatter.format(new Date(currentTime)));
        }
        player.start();
        playing = true;
        handler.post(updateSeekBar);
    }

    private void stopPlayer() {
        playing = false;
        saveTime(player.getCurrentPosition());
        player.pause();
    }

    public void finishIntent(View view) {
        if (playing) {
            stopPlayer();
        }
        finish();
    }

    // filehandling
    // setStartTime
    public void saveTime(int time) {
        System.out.println(filename + "," + time + "\n");
        FileOutputStream fos = null;

        StringBuilder fileContents = new StringBuilder();
        for (HashMap.Entry<String, String> entry : timeStamps.entrySet()) {
            System.out.println(filename+" "+entry.getKey());
            if(Objects.equals(entry.getKey(), filename)) {
                fileContents.append(entry.getKey()).append(",").append(time).append("\n");
            }
            else {
                fileContents.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
            }
        }
        String fileContentsString=fileContents.toString();
        System.out.println(fileContentsString);
        try {
            fos = openFileOutput(savedTimes, Context.MODE_PRIVATE);
            fos.write(fileContentsString.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int readTime() {
        getTimeMap();
        int lastSavedTime = 0;
        boolean firstPlay = true;
        for (HashMap.Entry<String, String> entry : timeStamps.entrySet()) {
            String name = entry.getKey();
            if (Objects.equals(name, filename)) {
                lastSavedTime = Integer.parseInt(entry.getValue());
                firstPlay = false;
            }
        }
        if (firstPlay) {
            timeStamps.put(filename, "0");
        }

        return lastSavedTime;
    }

    public void getTimeMap() {
        FileInputStream file;
        try {
            file = openFileInput(savedTimes);
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    timeStamps.put(parts[0], parts[1]);
                }
            }
            file.close();
        } catch (Exception ignored) {

        }
    }

    public void playerBackwards(View view) {
        player.seekTo(player.getCurrentPosition()-10000);
    }

    public void playerForward(View view) {
        player.seekTo(player.getCurrentPosition()+10000);
    }
}
