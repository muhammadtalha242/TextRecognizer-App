package com.example.talha.textrecognizerapp.classes;

import android.content.Context;
import android.widget.TextView;

import java.io.File;

public class readFile extends Template {
    fileHandler filehandler =new fileHandler();
    Context context;
    TextView textView;
    String displayMessage;
    File file;

    public boolean isFileExits(File file){
        return file.exists();
    }
    public boolean isReadAble(File file){
        return file.canRead();
    }


    @Override
    public String read() {
        if(isFileExits(file) && isReadAble(file))
        displayMessage= fileHandler.ReadFile(context);
        return displayMessage;
    }

    @Override
    public void display() {
        textView.setText(read());
    }
}
