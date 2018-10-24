package org.dtos.genfreq;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;

import org.dtos.genfreq.R;
import github.nisrulz.zentone.ToneStoppedListener;
import github.nisrulz.zentone.ZenTone;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

  private EditText editTextFreq;
  private EditText editTextDuration;
  private EditText editWidthFreq;
  private EditText editBaud;
  private int freq = 5000;
  private int width = 100;
  private int duration = 1;
  private boolean isPlaying = false;
  private FloatingActionButton myFab;
  private ArrayList<Integer> listChannel = new ArrayList<Integer>();
  private ArrayList<Integer> oddChannel = new ArrayList<Integer>();
  private ArrayList<Integer> baudAct = new ArrayList<Integer>();


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    editTextFreq = (EditText) findViewById(R.id.editTextFreq);
    editTextDuration = (EditText) findViewById(R.id.editTextDuration);
    editWidthFreq = (EditText) findViewById(R.id.editWidthFreq);
    editBaud = (EditText) findViewById(R.id.editBaud);
    SeekBar seekBarFreq = (SeekBar) findViewById(R.id.seekBarFreq);

    seekBarFreq.setMax(22000);

    SeekBar seekBarDuration = (SeekBar) findViewById(R.id.seekBarDuration);
    seekBarDuration.setMax(60);

    myFab = (FloatingActionButton) findViewById(R.id.myFAB);
    myFab.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        handleTonePlay();
      }
    });

    seekBarFreq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        editTextFreq.setText(String.valueOf(progress));
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
        // Stop Tone
        ZenTone.getInstance().stop();
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
        //Do nothing
      }
    });

    seekBarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        editTextDuration.setText(String.valueOf(progress));
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
        // Stop Tone
        ZenTone.getInstance().stop();
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
          // Do nothing
      }
    });
  }

  private ArrayList<Integer> generateChannel(){
    String baseFrequency = editTextFreq.getText().toString();
    String widthFrequency = editWidthFreq.getText().toString();

    freq = Integer.parseInt(baseFrequency);
    width = Integer.parseInt(widthFrequency);
    int sum = 0;
    for (int i=0; i<=16; i++){
        sum = (freq + width);
        listChannel.add(sum);
        sum = sum + width;
    }

    return listChannel;
  }

  private void handleTonePlay() {
    String freqString = editTextFreq.getText().toString();
    String durationString = editTextDuration.getText().toString();
    String baud = editBaud.getText().toString();



    if (!"".equals(freqString) && !"".equals(durationString) && !"".equals(baud)) {
      if (!isPlaying) {
        myFab.setImageResource(R.drawable.ic_stop_white_24dp);
        freq = Integer.parseInt(freqString);
        duration = Integer.parseInt(durationString);
        listChannel = generateChannel();
        for (int i=0; i<=7; i++){
          baudAct.add(Character.getNumericValue(baud.charAt(i)));
        }
        for (int j=0; j<=16; j++){
          if(j % 2 !=0){
            oddChannel.add(listChannel.get(j));
          }

        }
        // Play Tone
        for (int i=0; i<=7; i++){
            if (baudAct.get(i)==1) {
                ZenTone.getInstance().generate(oddChannel.get(i), duration, 1.0f, new ToneStoppedListener() {
                    @Override public void onToneStopped() {
                        isPlaying = false;
                        myFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                    }
                });
            }

        }

        isPlaying = true;
      } else {
        // Stop Tone
        ZenTone.getInstance().stop();
        isPlaying = false;
        myFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
      }
    } else if ("".equals(freqString)) {
      Toast.makeText(MainActivity.this, "Please enter a frequency!", Toast.LENGTH_SHORT).show();
    } else if ("".equals(durationString)) {
      Toast.makeText(MainActivity.this, "Please enter duration!", Toast.LENGTH_SHORT).show();
    }
  }
}
