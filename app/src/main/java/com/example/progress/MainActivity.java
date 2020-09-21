package com.example.progress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TimerTask timertk;
    private Timer timer;
    private Button start_btn, pause_btn;
    private ProgressBar pb ;
    int current =0;
    private TextView progress_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    init();
    //startTimerThread();
    }

    public void init() {
        //객체 초기화 하기
        pb = findViewById(R.id.progressbar);
        progress_tv = findViewById(R.id.progress_tv);
        start_btn = findViewById(R.id.startButton);
        pause_btn = findViewById(R.id.stopButton);
        //초기값 설정하기
        pb.setMax(100);     // pb바 최대치는 100
        pb.setProgress(0);  // pb바 초기값 0
    }

    public void startTimerThread() {
        timertk = new TimerTask() {
            @Override
            public void run() {
                increaseBar();
            }
        };
        timer = new Timer();
        timer.schedule(timertk, 0 , 100);       //timerTask 일정시간동안 실행
    }

    private void increaseBar() {
        runOnUiThread(          //UI 에 그리기
                new Runnable() {
                    @Override
                    public void run() {
                        current = pb.getProgress();
                        int maxcurrent = pb.getMax();
                        if(current >=0 && current < maxcurrent){
                            current += 1;
                            String str = Integer.toString(current);
                            progress_tv.setText(str + "%");
                        } else if (current == maxcurrent){
                            stopTimer();
                        }
                        pb.setProgress(current);        // pb바에 값 적용하기
                    }
                }
        );
    }

    //일시 정지와 다시 실행
    public void pause() {
        //일시정지- 실행중 정지
        if(timer != null){
            timer.cancel();
            timer.purge();
            timer = null;
        } else if (timer == null){  //다시 시작하면 다시 timer 생성하도록 startTimerThread
            startTimerThread();
        }
    }

    //초기화
    public void stop() {
        //실행중이면 취소하기
        if(timer != null){
            timer.cancel();
            timer.purge();
            timer =null;
            timertk.cancel();
            timertk = null;
        }   //지우고 아래 pb바와 문자열 변경
        if(timer == null){
            pb.setProgress(0);
            int zero = 0;
            String stop = Integer.toString(zero);
            progress_tv.setText(stop+"%");
        }
    }

    //최대치에 도달하면 멈추기
    private void stopTimer() {
        if(timertk != null){
            timertk.cancel();
            timertk = null;
        }
        if(timer != null){
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public void onStartButtonClicked(View view) {
        pause();
    }

    public void onStopButtonClicked(View view) {
        stop();
    }
}