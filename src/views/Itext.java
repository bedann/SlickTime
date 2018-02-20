/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

/**
 *
 * @author Monroe
 */
public class Itext extends TextField{
public BooleanProperty decimal = new SimpleBooleanProperty(true);
    public Itext() {
    }
     public boolean getDecimal() {
        return decimal.get();
    }

    public void setDecimal(boolean decimal) {
        this.decimal.set(decimal);
    }
    public boolean isDecimal() {
        return decimal.get();
    }
    
    
        public Itext(String prompt){
            this.setPromptText(prompt);
        }
        
        public Itext(String prompt, Pos pos){
            this.setPromptText(prompt);
            this.setAlignment(pos);
        }
    
        @Override
        public void replaceText(int start, int end, String text){
            if(validate(text)){
                super.replaceText(start, end, text);
            }
        }
        
        private boolean validate(String text){
            return decimal.get()?text.matches("[0-9.]*") || "".equals(text):text.matches("[0-9]*") || "".equals(text);
        }
        
        public void setValue(int val){
            this.setText(val+"");
        }
        
        public void setValue(double val){
            this.setText(val+"");
        }
        public int getValue(){
            try{
                return Integer.parseInt(getText());
            }catch(Exception e){
                return 0;
            }
        }
        public double getDoubleValue(){
            try{
                return Double.parseDouble(getText());
            }catch(Exception e){
                return 0;
            }
        }
}
