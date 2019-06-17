package com.example.talha.textrecognizerapp.classes;

import android.util.SparseArray;
import android.widget.TextView;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

public class readImage extends Template {
    TextView textView;
    String displayMessage;
    Detector.Detections detections;
    @Override
    public String read() {
        final SparseArray<TextBlock> items = detections.getDetectedItems();
        if (items.size() != 0) {

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < items.size(); i++) {
                TextBlock item = items.valueAt(i);
                stringBuilder.append(item.getValue());
                stringBuilder.append("\n");
                displayMessage= stringBuilder.toString();
            }
        }


        return displayMessage;
    }

    @Override
    public void display() {
        textView.setText(read());
    }
}
