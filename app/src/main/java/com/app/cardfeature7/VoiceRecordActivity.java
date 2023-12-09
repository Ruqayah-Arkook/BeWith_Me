

package com.app.cardfeature7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VoiceRecordActivity extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PERMISSION_CODE = 101;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    ImageView ibRecord, ibPlay, ivSimplebg;
    TextView tvTime;
    Button voice;
    boolean isRecording = false;
    boolean isPlaying = false;
    int seconds = 0;
    public static String path = "";
    LottieAnimationView lavPlaying;
    int dummySeconds = 0;
    int playableSeconds = 0;
    Handler handler;

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    int random = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_record);
        ibRecord = findViewById(R.id.ib_record);
        ibPlay = findViewById(R.id.ib_play);
        tvTime = findViewById(R.id.tv_time);
        ivSimplebg = findViewById(R.id.iv_simple_bg);
        lavPlaying = findViewById(R.id.lav_playing);
        voice = findViewById(R.id.voice_created);
        mediaPlayer = new MediaPlayer();
        random = new Random().nextInt(61) + 20;
        ibRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkRecordingPermission()) {
                    if (!isRecording) {
                        isRecording = true;
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    mediaRecorder = new MediaRecorder(VoiceRecordActivity.this);
                                }else {
                                    mediaRecorder =  new MediaRecorder();
                                }
                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                                mediaRecorder.setOutputFile(getRecordingFilePath());
                                path = getRecordingFilePath();
                                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                                try {
                                    mediaRecorder.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mediaRecorder.start();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivSimplebg.setVisibility(View.VISIBLE);
                                        lavPlaying.setVisibility(View.GONE);
                                        playableSeconds = 0;
                                        seconds = 0;
                                        dummySeconds = 0;
                                        ibRecord.setImageDrawable(ContextCompat.getDrawable(VoiceRecordActivity.this, R.drawable.recording_active));
                                        runTimer();
                                    }
                                });
                            }
                        });
                    } else {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                mediaRecorder.stop();
                                mediaRecorder.release();
                                mediaRecorder = null;
                                playableSeconds = seconds;
                                dummySeconds = seconds;
                                seconds = 0;
                                isRecording = false;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivSimplebg.setVisibility(View.VISIBLE);
                                        lavPlaying.setVisibility(View.GONE);
                                        handler.removeCallbacksAndMessages(null);
                                        ibRecord.setImageDrawable(ContextCompat.getDrawable(VoiceRecordActivity.this, R.drawable.recording_in_active));
                                    }
                                });
                            }
                        });
                    }
                } else {
                    requestRecordingPermission();
                }
            }
        });

        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    if (path != null) {
                        try {
                            mediaPlayer.setDataSource(getRecordingFilePath());
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            isPlaying = true;
                            ibPlay.setImageDrawable(ContextCompat.getDrawable(VoiceRecordActivity.this, R.drawable.pausebutton));
                            ivSimplebg.setVisibility(View.GONE);
                            lavPlaying.setVisibility(View.VISIBLE);
                            runTimer();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No Recording Present", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer = new MediaPlayer();
                    isPlaying = false;
                    seconds = 0;
                    handler.removeCallbacksAndMessages(null);
                    ivSimplebg.setVisibility(View.VISIBLE);
                    lavPlaying.setVisibility(View.GONE);
                    ibPlay.setImageDrawable(ContextCompat.getDrawable(VoiceRecordActivity.this, R.drawable.playbutton));
                }
            }
        });

        voice.setOnClickListener(v -> {
            // Return to ImageClass activity
            Toast.makeText(VoiceRecordActivity.this,"Voice Created Successfully",Toast.LENGTH_SHORT).show();
            finish();
        });
    }



    private void runTimer() {
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
                tvTime.setText(time);

                if (isRecording || (isPlaying && playableSeconds != -1)) {
                    seconds++;
                    playableSeconds--;

                    if (playableSeconds == -1 && isPlaying) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer = new MediaPlayer();
                        playableSeconds = dummySeconds;
                        seconds = 0;
                        handler.removeCallbacksAndMessages(null);
                        ivSimplebg.setVisibility(View.VISIBLE);
                        lavPlaying.setVisibility(View.GONE);
                        ibPlay.setImageDrawable(ContextCompat.getDrawable(VoiceRecordActivity.this, R.drawable.playbutton));
                        return;
                    }
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void requestRecordingPermission() {
        ActivityCompat.requestPermissions(VoiceRecordActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    public boolean checkRecordingPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            requestRecordingPermission();
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (permissionToRecord) {
                    Toast.makeText(getApplicationContext(), "Permission Given", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getRecordingFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(music,"testFile"+random+".mp3");
        return file.getPath();
    }
}
