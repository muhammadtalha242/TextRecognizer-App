package com.example.talha.textrecognizerapp.classes;

import com.example.talha.textrecognizerapp.Interface.Strategy;

public class FileContext {
    private Strategy strategy;

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
    public Strategy execute(){
        return strategy.fileHandler();
    }
}
