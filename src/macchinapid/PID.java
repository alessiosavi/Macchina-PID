/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package macchinapid;

/**
 *
 * @author Sebastiano
 */
public class PID {
    
    private float kp;
    private float kd;
    private float ki;
    private float lastEr;
    private float areaEr;
    
    public PID(float kp,float kd,float ki){
        this.kp=kp;
        this.kd=kd;
        this.ki=ki;        
        this.lastEr=0;
        this.areaEr=0;
    }

    public float getKp() {
        return kp;
    }

    public float getKd() {
        return kd;
    }

    public float getKi() {
        return ki;
    }

    public float getLastEr() {
        return lastEr;
    }
    
    public float getAreaEr(){
       return areaEr;
   }
    
    public float Calcola(float x){
        
        float ris;
        
        areaEr=areaEr+x;
        ris=-kp*x-kd*(x-lastEr)-ki*areaEr;
        lastEr=x;
        return ris;

   }
    
   
}
