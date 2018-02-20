/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slick.time;

import java.util.Calendar;
import java.util.Date;
import static javafx.animation.Animation.INDEFINITE;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import jfxtras.scene.layout.CircularPane;
import slick.MDate;

/**
 *
 * @author Monroe
 */
public class AnalogClock extends StackPane{

    private boolean light = true;
    private CircularPane hours = new CircularPane();
    private CircularPane minutes = new CircularPane();
    private Circle background = new Circle(75);
    private Line hr = new Line(0f,0f,0f,-90f),min = new Line(0f,0f,0f,-120f);
    private Timeline timer;
    private ImageView glass = icon("g1",150);
    private Label date = new Label();
    private HourListener hourListener;
    private int last_hr_of_day = -1;

    public AnalogClock() {
       init();
    }
    
    
    
    public AnalogClock(boolean light) {
        this.light = light;
        init();
    }

    public void setHourlistener(HourListener hourListener) {
        this.hourListener = hourListener;
    }
    
    
    
    private void init(){
        date.setText(new MDate().smartDate());
        date.setStyle("-fx-text-fill:"+(light?"#eeeeee":"#0b0b0b")+";-fx-font-size:20px;-fx-font-weight:bold;");
        date.setOpacity(0);
        StackPane.setMargin(date, new Insets(0,0,25,0));
        
        hr.setStroke(new LinearGradient(0d, -55d, 0d, 5d, false,
                CycleMethod.NO_CYCLE, new Stop(0,light?Color.WHITE:Color.BLACK), 
                                      new Stop(0.199,Color.TRANSPARENT)));
        min.setStroke(new LinearGradient(0d, -70d, 0d, 5d, false,
                CycleMethod.NO_CYCLE, new Stop(0,light?Color.WHITE:Color.BLACK), 
                                      new Stop(0.199,Color.TRANSPARENT)));
        
        for(int i = 1;i<13;i++){
            Label tb = new Label((i+""));
            tb.getStyleClass().add("hr");
            tb.setStyle("-fx-text-fill:"+(light?"#eeeeee":"#0b0b0b"));
            hours.getChildren().add(tb);
        }
        
        for(int i = 1;i<60;i++){
            if(i%5 != 0){
                Label l = new Label(".");
                l.getStyleClass().add("min");
                l.setStyle("-fx-text-fill:"+(light?"#eeeeee":"#0b0b0b"));
                minutes.add(l);
            }
        }
        
        hours.setStartAngle(15.0);
        hours.setGap(7.0);

        minutes.setDiameter(140.0);
        hours.setDiameter(150.0);
        
        this.moveHour();
        this.moveMinute();
        timer = new Timeline(new KeyFrame(Duration.millis(1000), e->{
            moveMinute();
            moveHour();
        }));
        timer.setCycleCount(INDEFINITE);
        timer.play();
        
        this.setOnMouseEntered(e->{
            FadeTransition fade = new FadeTransition(Duration.millis(1000),date);
            fade.setInterpolator(Interpolator.EASE_IN);
            fade.setFromValue(0f);
            fade.setToValue(0.3f);
            fade.play();
        });
        this.setOnMouseExited(e->{
            FadeTransition fade = new FadeTransition(Duration.millis(1000),date);
            fade.setFromValue(0.3f);
            fade.setInterpolator(Interpolator.EASE_IN);
            fade.setToValue(0f);
            fade.play();
        });
        
        background.setFill(Color.TRANSPARENT);
        this.getStylesheets().add(getClass().getResource("clock.css").toExternalForm());
        this.getChildren().addAll(background,minutes,hours,hr,min,
                new Circle(3,light?Color.LIGHTGRAY:Color.DARKGRAY),date,glass);
    }
    
    private void moveHour(){
        Calendar cal = Calendar.getInstance();
        int now = cal.get(Calendar.HOUR);
        int m = cal.get(Calendar.MINUTE);
        double angle = (30)*now;
        double rotate = 6*m;
        int units = (int)rotate/72;
        double offset = units*6;
        hr.setRotate(angle+offset);
        
        if(now != last_hr_of_day){
            if(hourListener != null){
                hourListener.onHourPassed(cal.get(Calendar.HOUR_OF_DAY),now);
            }
            last_hr_of_day = now;
        }
    }
    
    private void moveMinute(){
        int now;
        Calendar cal = Calendar.getInstance();
        now = cal.get(Calendar.MINUTE);
        RotateTransition r = new RotateTransition(Duration.millis(1000),min);
        r.setToAngle(6*now);
        r.play();
    }

    public void stopTime(){
        timer.stop();
    }
    
    public void startTime(){
        timer.play();
    }
    
    public void setBackground(Image background) {
        this.background.setFill(new ImagePattern(background));
    }
    
    
    private ImageView icon(String s,int a){
        ImageView image = new ImageView(new Image(getClass().getResourceAsStream(s+".png")));
        image.setFitHeight(a);
        image.setPreserveRatio(true);
        return image;
    }
    
    
    public void hideGlassCasing(){
        glass.setVisible(false);
    }
    
    public void switchTheme(boolean light){
        hr.setStroke(new LinearGradient(0d, -55d, 0d, 5d, false,
                CycleMethod.NO_CYCLE, new Stop(0,light?Color.WHITE:Color.BLACK), 
                                      new Stop(0.199,Color.TRANSPARENT)));
        min.setStroke(new LinearGradient(0d, -70d, 0d, 5d, false,
                CycleMethod.NO_CYCLE, new Stop(0,light?Color.WHITE:Color.BLACK), 
                                      new Stop(0.199,Color.TRANSPARENT)));
        
        for(Node n:hours.getChildren()){
            n.setStyle("-fx-text-fill:"+(light?"#eeeeee":"#0b0b0b"));
        }
        for(Node n:minutes.getChildren()){
                n.setStyle("-fx-text-fill:"+(light?"#eeeeee":"#0b0b0b"));
        }
    }
    
    public void setDarkTheme(){
        this.switchTheme(false);
    }
    
    public void setLightTheme(){
        this.switchTheme(true);
    }
    
    public interface HourListener{
        void onHourPassed(int hrOfday,int hr);
    }
    
    
}
