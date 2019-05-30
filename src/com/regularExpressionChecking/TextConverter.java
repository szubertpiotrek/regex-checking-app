package com.regularExpressionChecking;

public class TextConverter implements Runnable{
    private int i;
    private int flag;

    public TextConverter(int i){
        this.i = i;
        this.flag=0;
    }

    @Override
    public void run() {
        print();
        flag=1;
    }

    public void print(){
        System.out.println(this.i);
    }

    public int getFlag(){
        return flag;
    }
}
