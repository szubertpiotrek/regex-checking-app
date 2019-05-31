package com.regularExpressionChecking;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;


import java.io.FileInputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class TextConverter extends RecursiveTask<Integer> {

    private ForkJoinTask<Integer> task;
    private List<String> textLines;
    private String word;
    private int start;
    private int end;


    public TextConverter(){

    }

    public TextConverter(List<String> textLines,String word){
        this.textLines = textLines;
        this.word = word;
        this.start = 0;
        this.end = textLines.size();
    }

    public TextConverter(List<String> textLines, int start, int end){
        System.out.println(start + " " + end);
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {

        int length =end-start;
        if(length<10){
            return getSeachedWords();
        }

        TextConverter textConverter1 = new TextConverter(textLines,0, Math.round(length/2));

        TextConverter textConverter2 = new TextConverter(textLines, Math.round(length/2), end);
        textConverter2.fork();

        int firstTaskResult = textConverter1.compute();
        int secondTaskResult = textConverter2.join();

        return firstTaskResult + secondTaskResult;
    }

    public static List<String> readDocxFile(String filePath) {
        List<String> textLines = new LinkedList<String>();
        try {

            FileInputStream fis = new FileInputStream(filePath);

            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = Arrays.asList(document.getParagraphs());

            System.out.println("Total no of paragraph "+paragraphs.size());
            for (XWPFParagraph para : paragraphs) {
                textLines.add(para.getText());
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return textLines;
    }

    //metoda liczaca fraze w liniach tekstu
    private int getSeachedWords(){
        int result = 0;
        for (int i = start; i<end;i++) {
            result += searchWord();
        }
        return result;
    }

    //metoda szukajaca fraze w linii kodu
    private int searchWord(){
        int countWord = 0;
       return countWord;
    }

    public int startSeachingWord(String filePath, String word){
        List<String> textLines = TextConverter.readDocxFile(filePath);
        task = new TextConverter(textLines,word);
        return new ForkJoinPool(Runtime.getRuntime().availableProcessors()).invoke(task);
    }

    public ForkJoinTask<Integer> getTask(){
        return task;
    }

}
