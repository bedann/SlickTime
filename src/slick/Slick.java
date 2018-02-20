/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slick;

import java.io.FileNotFoundException;
import slick.time.TimePicker;
import java.text.ParseException;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import slick.time.AnalogClock;
import slick.time.AnalogClock.HourListener;

/**
 *
 * @author Monroe
 */
public class Slick extends Application {
    
    public static Stage stage;
    
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        primaryStage.setTitle("TimePicker Sample");
        Button btn = new Button();
        btn.setText("Button ");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                btn.setText(new TimePicker(Slick.stage).getTime());
            }
        });
        Button btn2 = new Button();
        btn2.setText("Button'");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    btn2.setText(new TimePicker(Slick.stage).getTime("H:mm a"));
                    
                } catch (ParseException ex) {
                    Logger.getLogger(Slick.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        Button btn3 = new Button();
        btn3.setText("Button");
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                    btn3.setText(new TimePicker(Slick.stage).getTimeMap24().toString());
            }
        });
        
        
        FlowPane textblocks = new FlowPane();
        textblocks.setHgap(10);
        textblocks.setVgap(10);
        
        AnalogClock analog = new AnalogClock(false);
        analog.setHourlistener(new HourListener() {
            @Override
            public void onHourPassed(int hrOfday, int hr) {
                System.out.println(hrOfday);
            }
        });
        
        
        
        try {
            Formatter formater = new Formatter("config");
            formater.format("%d", 0);
            formater.flush();
            formater.close();
        } catch (FileNotFoundException ex) {

        }
        
        
        
        
        
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setMinSize(400, 400);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(btn,btn2,btn3,analog,textblocks);
        
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
