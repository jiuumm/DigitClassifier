package com.example.mine;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Pair;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.divyanshu.draw.widget.DrawView;
import com.example.mine.databinding.ActivityDrawBinding;

import java.io.IOException;
import java.util.Locale;

public class DrawActivity extends AppCompatActivity {

    private ActivityDrawBinding binding; // 1. binding 변수 선언
    Classifier cls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDrawBinding.inflate(getLayoutInflater()); // 2. binding 초기화
        setContentView(binding.getRoot());
        DrawView drawView = binding.drawView;
        drawView.setStrokeWidth(100.0f);
        drawView.setBackgroundColor(Color.BLACK);
        drawView.setColor(Color.WHITE);

        TextView resultView = binding.resultView;

        Button classifyBtn = binding.classifyBtn;

        classifyBtn.setOnClickListener(v -> {
            try {
                Bitmap image = drawView.getBitmap();
                Pair<Integer, Float> res = cls.classify(image);
                String outStr = String.format(Locale.ENGLISH, "%d, %.2f%%", res.first, res.second * 100.0f);
                resultView.setText(outStr);
            } catch (Exception e) {
                Log.e("DigitClassifier", "Error in classifyBtn click", e);
            }
        });
        Button clearBtn = binding.clearBtn;
        clearBtn.setOnClickListener(v->{
            drawView.clearCanvas();
        });

        cls = new Classifier(this);
        try{
            cls.init();
        }
        catch(IOException ioe){
            Log.d("DigitClassifier", "Failed to init Classifier", ioe);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        cls.finish();
    }
}
