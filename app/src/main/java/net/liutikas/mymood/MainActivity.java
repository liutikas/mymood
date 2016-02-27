package net.liutikas.mymood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SeekBar mSeekbar;
    private TextView mLabel;
    private FaceView mFaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFaceView = (FaceView) findViewById(R.id.faceView);

        mLabel = (TextView) findViewById(R.id.label);
        mLabel.setText("0");

        mSeekbar = (SeekBar) findViewById(R.id.seekBar);
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFaceView.setHappiness(progress);
                mLabel.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSeekbar.setProgress((mSeekbar.getProgress() + 4) / 10 * 10);
            }
        });
    }
}
