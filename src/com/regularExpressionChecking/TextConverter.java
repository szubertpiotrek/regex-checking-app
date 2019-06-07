package com.regularExpressionChecking;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
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
    private String wordCaps;
    private int start;
    private int end;


    public TextConverter(){

    }

    public TextConverter(List<String> textLines,String word){
        this.textLines = textLines;
        this.word = word;
        this.wordCaps = createCaps(word);
        this.start = 0;
        this.end = textLines.size();
    }

    public TextConverter(List<String> textLines, String word, int start, int end){
        this.start = start;
        this.end = end;
        this.word = word;
        this.wordCaps = createCaps(word);
        this.textLines = textLines;
    }

    //metoda forkjoina wykonujaca zadanie
    @Override
    protected Integer compute() {

        int length =end-start;
        if(length<3){
            return getSeachedWords();
        }

        TextConverter textConverter1 = new TextConverter(textLines, word, start, start + Math.round(length/2));

        TextConverter textConverter2 = new TextConverter(textLines, word, start + Math.round(length/2), end);
        textConverter2.fork();

        int firstTaskResult = textConverter1.compute();
        int secondTaskResult = textConverter2.join();

        return firstTaskResult + secondTaskResult;
    }

    //statyczna metoda pozwalajaca zwrocic z pliku docx linie tesktu
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

    //statyczna metoda pozwalajaca zwrocic z pliku txt linie tesktu
    public static List<String> readTxtFile(String filePath) {
        List<String> textLines = new LinkedList<String>();

        try (FileReader reader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                textLines.add(line);
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        return textLines;
    }

    //metoda liczaca fraze w liniach tekstu
    public int getSeachedWords(){
        int result = 0;
        for (int i = start; i<end;i++) {
            result += searchWord(this.textLines.get(i), this.word, this.wordCaps);
        }
        return result;
    }

    //metoda szukajaca fraze w linii kodu
    public int searchWord(String textLine, String word, String wordCaps) {
        int countWord = 0;
        int j=0;
        boolean check= false;
        int first = 0;

        for(int i=0;i<textLine.length()-word.length()+1;i++){
            if((textLine.charAt(i)==word.charAt(0))||(textLine.charAt(i)==wordCaps.charAt(0))){
                j=0;
                while(j<word.length()){
                    if((textLine.charAt(i+j)==word.charAt(0))&&(!check)&&(j!=0)){
                        first = j;
                        check = true;
                    }
                    if((textLine.charAt(i+j)==word.charAt(j))||(textLine.charAt(i+j)==wordCaps.charAt(j))){
                        j++;
                        if(j==word.length()){
                            countWord++;
                            check = false;
                            i += first;
                            first = 0;
                        }
                    }
                    else{
                        check = false;
                        i += first;
                        first = 0;
                        j=word.length()+1;
                    }
                }
            }
        }
        return countWord;
    }

    //metoda tworząca liste linii i zadanie w ForkJoinPoolu
    public int startSeachingWord(String filePath, String word, String type){
        List<String> textLines = new LinkedList<String>();
        System.out.println(type);
        if(type == "docx"){
            textLines = TextConverter.readDocxFile(filePath);
        }else if(type == "txt"){
            textLines = TextConverter.readTxtFile(filePath);
        }
        task = new TextConverter(textLines,word);
        return new ForkJoinPool(Runtime.getRuntime().availableProcessors()).invoke(task);
    }

    //metoda do zwrócenia tasku do sprawdzenia stanu zadania
    public ForkJoinTask<Integer> getTask(){
        return task;
    }

    //metoda zwracajaca słowo z wielkimi literami
    private String createCaps(String word){
        String caps = "";
        int letter;
        for(int i=0; i<word.length();i++){
            letter = word.charAt(i);
            if((letter>64)&&(letter<91)){
                letter+=32;
                caps = caps + (char)letter;
            }
            else if((letter>96)&&(letter<123)){
                letter -= 32;
                caps = caps + (char)letter;
            }
        }
        return caps;
    }

}
