package com.example.ukuwulele;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.SparseArray;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;

/** @noinspection ALL*/
public class downloadScreen extends AppCompatActivity {
    TextView textView;
    EditText inputText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_download_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView= (TextView) findViewById(R.id.textView);
        inputText=(EditText) findViewById(R.id.editText);
    }

    public void download(View view) {
        StringBuilder videoUrlBuilder = new StringBuilder();
        videoUrlBuilder.append(inputText.getText().toString());
        videoUrlBuilder.append("&html5=1");
        String videoUrl=videoUrlBuilder.toString();



        // Ensure videoUrl is a valid YouTube URL
        if (videoUrl.isEmpty() || !videoUrl.startsWith("https://www.youtube.com/watch?v=")) {
            Toast.makeText(this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
            return;
        }

        YouTubeUriExtractor ytEx = new YouTubeUriExtractor(this) {
            @Override
            public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
                if (ytFiles != null) {
                    int itag = 140;
                    String downloadUrl = ytFiles.get(itag).getUrl();
                    System.out.println(downloadUrl);
                }
            }
        };

        System.out.println("before executing");
        System.out.println(videoUrl);
        ytEx.execute(videoUrl);

        System.out.println("after executing");
        return;
    }

}