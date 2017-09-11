/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package macchinapid;

import java.util.Random;

/**
 *
 * @author Sebastiano
 */
public class Macchina {
    public float x , y;
    public float orientation;
    public float length;
    public float steering_noise;
    public float distance_noise;
    public double steering_drift;
    public float stering_noise;
    public float tollerance;
    
    public Macchina(){}
    
    public Macchina(float lenght){
        this.length=lenght;
        x=0;
        y=1;
    }
    
    public Macchina(float lenght, float x, float y, float orientation, float distance_noise, float steering_noise, double steering_drift){
        this.length=lenght;
        this.x=x;
        this.y=y;
        if(orientation>(float)(2*Math.PI)){this.orientation=orientation-(float)(Math.PI);}
        else{ this.orientation=orientation;}
        this.distance_noise=distance_noise;
        this.steering_noise=steering_noise;
        this.steering_drift=steering_drift;
        
    }
    
    public void setPosition(float x, float y, float orientation){
        
        this.x=x;
        this.y=y;
        if(orientation>(float)(2*Math.PI)){this.orientation=orientation-(float)(Math.PI);}
        else{ this.orientation=orientation;}   
        
    }
    
    public void setNoise(float distance_noise,float steering_noise){
                 this.distance_noise=distance_noise;
                 this.steering_noise=steering_noise;
                 
    }
    
    public void setDrift(double steering_drift){
        this.steering_drift=steering_drift;
    }
    
    /*public Macchina move(float steer,float distance){
            
            float tollerence=(float)0.001;
            float maxsteringdrift=(float)Math.PI/4;
            float steering, radiant, cx, cy, drift3, distance3;

            if(steer>maxsteringdrift){
                        steer=maxsteringdrift;
            }
          
            if(steer<-maxsteringdrift){
                        steer=-maxsteringdrift;
            }
           
            if(distance<0.0f){
                    distance=0.0f;
            }
           
            Macchina res=new Macchina(this.length);
            res.x=this.x;
            res.y=this.y;
            res.orientation=this.orientation;
            res.stering_noise=this.stering_noise;
            res.steering_drift=this.steering_drift;
            res.distance_noise = this.distance_noise;
            
            Random steer2=new Random();
            drift3=(float)(steer+(this.stering_noise*steer2.nextGaussian()));
            distance3=(float)(distance+(this.stering_noise*steer2.nextGaussian()));
           
            drift3+=this.steering_drift;
            
            steering=(float)(Math.tan(drift3)*distance3/res.length);
           
            if(Math.abs(steering)<tollerence){
                res.x=this.x+(float)(distance3*Math.cos(this.orientation));
                res.y=this.y+(float)(distance3*Math.sin(this.orientation));
                orientation=orientation+steering;
                res.orientation=(orientation>(float)(2*Math.PI))?orientation-(float)(2*Math.PI):orientation;
            }
            else{
                radiant=distance3/steering;
                cx=this.x-(float)(Math.sin(this.orientation)*radiant);
                cy=this.y+(float)(Math.cos(this.orientation)*radiant);
                orientation=orientation+steering;
                res.orientation=(orientation>(float)(2*Math.PI))?orientation-(float)(2*Math.PI):orientation;
                res.x=cx+(float)(Math.sin(res.orientation)*radiant);
                res.y=cy-(float)(Math.cos(res.orientation)*radiant);
            }
            return res;
            
            
    }
    */
    
    public Macchina move(float sterzata,float distanza){
        //angolo di sterzata massimo delle ruote; equivale a 45 gradi
        float sterzataMax=(float)Math.PI/4;
        //costante per il controllo dell'angolo di sterzata effettivo
        float tolleranza=(float)0.001;
        //angolo di sterzata effettivo
        float sterzata2;
        //distanza effettivamente percorsa
        float distanza2;
        //angolo per il quale la macchina si Ã¨ spostata
        float angSterzata;
        //angolo di sterzata effettivo nel bicycle model
        float raggio;
        //coordinate del centro dellacirconferenza di sterzata
        float cx,cy;
        
        //controllo i parametri passati
        if(sterzata>sterzataMax)
            sterzata=sterzataMax;
        if(sterzata<-sterzataMax)
            sterzata=-sterzataMax;
        if(distanza<0.0f)
            distanza=0.0f;
        
        //creiamo una copia dell'oggetto corrente
        Macchina copia=new Macchina(this.length);
        copia.x=this.x;
        copia.y=this.y;
        copia.orientation=this.orientation;
        copia.steering_noise=this.steering_noise;
        copia.distance_noise=this.distance_noise;
        copia.steering_drift=this.steering_drift;
        
        //applichiamo i rumori ai parametri per calcolare
        //i valori effettivi di distanza percorsa e angolo di sterzata
        Random r=new Random();
        sterzata2=(float)(sterzata+(this.steering_noise*r.nextGaussian()));
        distanza2=(float)(distanza+(this.distance_noise*r.nextGaussian()));
        
        //appplichiamo il gioco dello sterzo all'angolo di sterzata
        sterzata2+=this.steering_drift;
        
        //eseguiamo il movimento
        angSterzata=(float)(Math.tan(sterzata2)*distanza2/copia.length);
        if(Math.abs(angSterzata)<tolleranza){
            //applichiamo il movimento in linea retta
            copia.x=this.x+(float)(distanza2*Math.cos(this.orientation));
            copia.y=this.y+(float)(distanza2*Math.sin(this.orientation));
            orientation = orientation + angSterzata;
            copia.orientation=(orientation > (float)(2*Math.PI))? orientation - (float)(2*Math.PI): orientation;
        }
        else{
            //applichiamo il bicycle model
            raggio=distanza2/angSterzata;
            cx=this.x-(float)(Math.sin(this.orientation)*raggio);
            cy=this.y+(float)(Math.cos(this.orientation)*raggio);
            orientation = orientation + angSterzata;
            copia.orientation=(orientation > (float)(2*Math.PI))? orientation - (float)(2*Math.PI): orientation;
            copia.x=cx+(float)(Math.sin(copia.orientation)*raggio);
            copia.y=cy-(float)(Math.cos(copia.orientation)*raggio);
        }
        return copia;
    }

    public float getDistance_noise() {
        return distance_noise;
    }

    public float getLength() {
        return length;
    }

    public float getOrientation() {
        return orientation;
    }

    public double getSteering_drift() {
        return steering_drift;
    }

    public float getSteering_noise() {
        return steering_noise;
    }

    public float getStering_noise() {
        return stering_noise;
    }

    public float getTollerance() {
        return tollerance;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
}
