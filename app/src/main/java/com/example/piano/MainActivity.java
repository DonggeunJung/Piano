package com.example.piano;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements JGameLib.GameEvent {
    Button btnOctave;
    int screenW = 42, screenH = 14;
    JGameLib gameLib;
    JGameLib.Card cardDo0, cardDo1;
    int[] audioRes = {R.raw.piano_3_0, R.raw.piano_3_1, R.raw.piano_3_2, R.raw.piano_3_3,
            R.raw.piano_3_4, R.raw.piano_3_5, R.raw.piano_3_6, R.raw.piano_3_7,
            R.raw.piano_3_8, R.raw.piano_3_9, R.raw.piano_3_10, R.raw.piano_3_11,
            R.raw.piano_4_0, R.raw.piano_4_1, R.raw.piano_4_2, R.raw.piano_4_3,
            R.raw.piano_4_4, R.raw.piano_4_5, R.raw.piano_4_6, R.raw.piano_4_7,
            R.raw.piano_4_8, R.raw.piano_4_9, R.raw.piano_4_10, R.raw.piano_4_11,
            R.raw.piano_5_0, R.raw.piano_5_1, R.raw.piano_5_2, R.raw.piano_5_3,
            R.raw.piano_5_4, R.raw.piano_5_5, R.raw.piano_5_6, R.raw.piano_5_7,
            R.raw.piano_5_8, R.raw.piano_5_9, R.raw.piano_5_10, R.raw.piano_5_11};
    final int minOctave = 3;
    int octaveStart = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        btnOctave = findViewById(R.id.btnOctave);
        gameLib = findViewById(R.id.gameLib);
        initGame();
    }

    void initGame() {
        gameLib.listener(this);
        gameLib.setScreenGrid(screenW, screenH);
        gameLib.addCard(R.drawable.piano_keyboard);
        cardDo0 = gameLib.addCardColor(Color.TRANSPARENT, 0,11,3,3);
        cardDo0.text("", Color.BLACK, 0.8);
        cardDo1 = gameLib.addCardColor(Color.TRANSPARENT, 21,11,3,3);
        cardDo1.text("", Color.BLACK, 0.8);
        setOctave();
    }

    void setOctave() {
        int lOctave = minOctave + octaveStart;
        cardDo0.text("C"+(lOctave));
        cardDo1.text("C"+(lOctave+1));
        lOctave = minOctave + (octaveStart+1)%2;
        String text = "C" + (lOctave) + " - B" + (lOctave+1);
        btnOctave.setText(text);
    }

    int keyIndex(float axisX, float axisY) {
        boolean isMain = screenH/2 < axisY;
        int whiteIdx = (int)(axisX / 3);
        int divIdx = (whiteIdx % 7) * 2;
        if(divIdx >= 6)
            divIdx --;
        if(whiteIdx >= 7)
            divIdx += 12;
        if(isMain)
            return divIdx;
        if((int)axisX % 3 == 0 && divIdx > 0)
            divIdx --;
        else if((int)axisX % 3 == 2 && divIdx < 23)
            divIdx ++;
        return divIdx;
    }

    public void onBtnOctave(View v) {
        octaveStart = (octaveStart+1) % 2;
        setOctave();
    }

    @Override
    public void onGameWorkEnded(JGameLib.Card card, JGameLib.WorkType workType) {}

    @Override
    public void onGameTouchEvent(JGameLib.Card card, int action, float x, float y) {
        if(action == MotionEvent.ACTION_DOWN) {
            int keyIdx = keyIndex(x, y);
            keyIdx += (octaveStart * 12);
            gameLib.playAudioBeep(audioRes[keyIdx]);
        }
    }

    @Override
    public void onGameSensor(int sensorType, float x, float y, float z) {}

    @Override
    public void onGameCollision(JGameLib.Card card1, JGameLib.Card card2) {}

    @Override
    public void onGameTimer() {}
}