package com.regularExpressionChecking;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class Controller {

    final FileChooser fileChooser = new FileChooser();
    private Stage stage;
    private ExecutorService executorService;
    private Queue<Runnable> stringList;
    private Queue<ScheduledFuture<?>> stringListHandles;
    private TextConverter textConverter;
    private String filePath;
    private String fileExtension;
    private int result;

    @FXML
    ProgressIndicator progressIndicator;

    @FXML
    TextField inputWord;

    @FXML
    Label resultLabel;

    @FXML
    Label pathLabel;

    @FXML
    Button searchButton;

    @FXML
    final Button chooseFileButton = new Button();

    @FXML
    public void initialize() {
        progressIndicator.setVisible(false);
        stringList = new ArrayDeque<>();
    }


    @FXML
    public void chooseFile(){
        progressIndicator.setProgress(0);
        fileChooser.setTitle("Find File");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("docx","*.docx"),
                new FileChooser.ExtensionFilter("txt","*.txt")
        );
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            pathLabel.setText(file.getAbsolutePath());
            filePath = file.getAbsolutePath();
            fileExtension = fileChooser.getSelectedExtensionFilter().getDescription();
        }
    }

    @FXML
    public void searchWord(){
        progressIndicator.setProgress(0);
        progressIndicator.setVisible(true);

//        executorService =  Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//
//        TextConverter textConverter = new TextConverter();
//        List<String> textLineList = textConverter.readDocxFile(filePath);
//        textLineList.stream().forEach(element -> stringList.add(new TextConverter(inputWord.getText(),element)));
//
//        stringList.stream()
//                .map(element -> executorService.submit(element))
//                .collect(Collectors.toCollection(ArrayDeque::new));
//        executorService.shutdown();

        //stworzenie obiektu do wczytania pliku
        textConverter = new TextConverter();
        result = textConverter.startSeachingWord(filePath,inputWord.getText(), fileExtension);


        //watek javafx do sprawdzenia stanu forkjoina
        Platform.runLater(new Runnable() {
            boolean condition=true;
            @Override
            public void run() {
                while(condition){
                    if(textConverter.getTask().isDone()){
                        condition=false;
                        progressIndicator.setProgress(1);
                        resultLabel.setText(inputWord.getText() + ": " + result);
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getStage(Stage stage){
        this.stage = stage;
    }

}
