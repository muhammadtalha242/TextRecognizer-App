package com.example.talha.textrecognizerapp.classes;

public abstract class Template {
    public final void templateMethod(){
        read();
        display();
    }
    public abstract  String read();
    public abstract void display();


}
