package com.example.shu.crocodile;

//структура сообщения
public class Message {
    int number;
    String sender;
    String text;
    Boolean mark;

    Message(int n, String name, String t, Boolean lm){
        this.number = n;
        this.sender = name;
        this.text = t;
        this.mark = lm;
    }
}
