package com.example.jutai;



public class FAQItem {
    public String question;
    public String answer;

    public FAQItem(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return question;
    }
}


