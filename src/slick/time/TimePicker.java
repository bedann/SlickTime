/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slick.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jfxtras.scene.layout.CircularPane;
import jfxtras.scene.layout.CircularPane.AnimationInterpolation;
import slick.Slick;

/**
 *
 * @author Monroe
 */
public class TimePicker extends Stage{
    
    private CircularPane hours = new CircularPane();
    private CircularPane minutes = new CircularPane();
    private ToggleGroup hourGp = new ToggleGroup();
    private ToggleGroup minGp = new ToggleGroup();
    private StackPane container = new StackPane();
    private Label picking = new Label("Hour");
    private Label timeLabel = new Label("12:00 AM");
    private VBox labels = new VBox(5,timeLabel);
    private Spinner<String> a = new Spinner<>(FXCollections.observableArrayList("AM","PM"));
    private Button refresh = new Button(null,icon("refresh"));
    private Button set = new Button("SET TIME");
    private String hr ="12",min = "00";
    private HBox top = new HBox(timeLabel);
    private Map<String,Integer> map = new HashMap();
    public static String LIGHT = "light",DARK = "dark",FLAT = "flat";
    private int h = 12,m = 0;
    private Stage stage;

    public TimePicker(Stage stage) {
        this.stage = stage;
        map.put("hour", h);
        map.put("min", m);
        labels.setAlignment(Pos.CENTER);
        timeLabel.getStyleClass().add("time-label");
        set.getStyleClass().add("set");
        refresh.getStyleClass().add("refresh");
        
        top.getStyleClass().add("top");
        top.setMinHeight(100);
        top.setPadding(new Insets(20));
        top.setAlignment(Pos.CENTER_LEFT);
        
        for(int i = 1;i<13;i++){
            ToggleButton tb = new ToggleButton(getDigit(i));
            hourGp.getToggles().add(tb);
            if(i == h)tb.setSelected(true);
            tb.setUserData(getDigit(i));
            tb.getStyleClass().add("hr");
            hours.getChildren().add(tb);
        }
        for(int i = 0;i<60;i+=5){
            ToggleButton tb = new ToggleButton(getDigit(i));
            tb.setToggleGroup(minGp);
            if(i == m)tb.setSelected(true);
            tb.setUserData(getDigit(i));
            tb.getStyleClass().add("min");
            minutes.getChildren().add(tb);
        }
   
        hours.setAnimationDuration(Duration.millis(500));
        hours.setChildrenAreCircular(Boolean.TRUE);
        hours.setStartAngle(15.0);
        hours.setGap(10.0);
        hourGp.selectedToggleProperty().addListener((obs,o,n)->{
            if(n == null)return;
            hr = n.getUserData().toString();
            picking.setText("Minutes");
            timeLabel.setText(String.format("%s:00 %s", hr,a.getValue()));
            hours.setAnimationInterpolation(CircularPane::animateFromTheOrigin);
            hours.animateOut();
            minutes.animateIn();
            minutes.setVisible(true);
            map.put("hour", time24(hr,a.getValue()));
        });
        hours.setOnAnimateOutFinished(e->{
            Platform.runLater(()->{
                container.getChildren().remove(hours);
            });
        });
        
        
        minutes.setVisible(false);
        minutes.setGap(10.0);
        minutes.setStartAngle(-15.0);
        minutes.setAnimationDuration(Duration.millis(500));
        minutes.setChildrenAreCircular(Boolean.TRUE);
        minutes.setAnimationInterpolation(CircularPane::animateFromTheOrigin);
        minGp.selectedToggleProperty().addListener((obs,o,n)->{
            if(n == null)return;
            min = n.getUserData().toString();
            picking.setText("Minutes");
            timeLabel.setText(String.format("%s:%s %s", hr,min,a.getValue()));
            map.put("min",Integer.parseInt(min));
        });
        minutes.setOnAnimateOutFinished(e->{minutes.setVisible(false);});
        
        a.valueProperty().addListener((obs,o,n)->{
            timeLabel.setText(String.format("%s:%s %s", hr,min,n));
            map.put("hour", time24(hr,a.getValue()));
        });
        
        a.setMaxWidth(60);
        labels.getChildren().add(a);
        labels.setMaxSize(70,70);
        container.setMaxSize(200, 200);
        container.getStyleClass().add("container");
        container.getChildren().addAll(minutes,hours,labels);
        
        set.setMaxWidth(Double.MAX_VALUE);
        set.setAlignment(Pos.CENTER_RIGHT);
        set.setOnAction(e->{
            close();
        });
        refresh.setOnAction(e->{
            if(!minutes.isVisible())return;
            container.getChildren().add(1, hours);
            hours.setAnimationInterpolation(CircularPane::animateFromTheOrigin);
            hours.animateIn();
            minutes.animateOut();
        });
        
        HBox hb = new HBox(refresh,set);
        hb.setPrefWidth(Double.MAX_VALUE);
        HBox.setHgrow(set, Priority.ALWAYS);
        
        VBox main = new VBox(10,top,container,hb);
        main.setMinWidth(250);
        main.setAlignment(Pos.CENTER);
        main.setPrefWidth(USE_COMPUTED_SIZE);
        
        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(stage);
        this.initStyle(StageStyle.UTILITY);
        Scene scne = new Scene(main);
        scne.getStylesheets().add(getClass().getResource(LIGHT+".css").toExternalForm());
        scne.getStylesheets().addAll(stage.getScene().getStylesheets());
        scne.setFill(Color.TEAL);
        this.setScene(scne);
        this.setTitle("Pick a Time");
        this.setMinWidth(230);
        this.setResizable(false);
    }
    
    public String getTime(){
        this.showAndWait();
        return timeLabel.getText();
    }
    
    public String getRawTime(){
        return timeLabel.getText();
    }
    
    public String getTime(String format) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        SimpleDateFormat f = new SimpleDateFormat("h:mm a");
        this.showAndWait();
        return sdf.format(f.parse(timeLabel.getText()));
    }
    
    public Map<String,Integer> getTimeMap24(){
        this.showAndWait();
        return map;
    }
    
    private int time24(String ...s){
        String t = "00";
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H");
            final SimpleDateFormat sdf2 = new SimpleDateFormat("h a");
            t = (sdf.format(sdf2.parse(String.format("%s %s", s))));
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(t);
    }
    
    private String getDigit(int i){
        return i<10?"0"+i:i+"";
    }
    
    private String twelve(String s){
        try {
            return new SimpleDateFormat("K").format(new SimpleDateFormat("H").parse(s));
        } catch (ParseException ex) {
            Logger.getLogger(TimePicker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }
    
    private ImageView icon(String s){
        ImageView image = new ImageView(new Image(getClass().getResourceAsStream(s+".png")));
        image.setFitHeight(15);
        image.setPreserveRatio(true);
        return image;
    }
    
}
