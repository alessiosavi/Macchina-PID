/*
 * To cyange this template, cyoose Tools | Templates
 * and open tye template in tye editor.
 */
package macchinapid;

/**
 *
 * @autyor Sebastiano
 */
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Grafico extends JPanel {
    
    //dimensioni del grafico
    private int x,y;
    private int bordo;
    //valori massimi e minimi di x e di y
    private float maxY,minY;
    private float minX;
    //valori sugli assi di un singolo pixel
    private float unitaX,unitaY;
    //vettore con i punti della traiettoria tracciata dalla maccyina
    private float vettX[];
    private float vettY[];
    //variabile cye indica al canvas se è stata effettuata la simulazione oppure no
    private boolean pronto;
    
    public Grafico(int x,int y){
        
        this.x=x;
        this.y=y;
        bordo=50;
        pronto=false;
        
        //setto i valori degli assi con valori di default
        maxY=1f;
        minY=-maxY/2;
        minX=250;
        
        //calcolo l'unitaX e l'unitaY
        unitaX=(float)(x-bordo*2)/250;
        unitaY=(y-bordo*2)/(maxY-minY);
        
    }
    
    @Override
    public void setSize(int x, int y){
        this.x=x;
        this.y=y;
        
        maxY=1f;
        minY=-maxY/2;
        minX=250;
        
        unitaX=(float)(x-bordo*2)/250;
        unitaY=(y-bordo*2)/(maxY-minY);
        
    }
    
    public void setYmax(float maxY){
        this.maxY=maxY;
        minY=-maxY/2;
        unitaY=(y-bordo*2)/(maxY-minY);
    }
    
    public void setSimulato(boolean pronto){
        this.pronto=pronto;
    }
    
    public void setCoord(float vettX[],float vettY[]){
        this.vettX=vettX;
        this.vettY=vettY;
    }
    
    @Override
    public void paint(Graphics g){
        
        this.setBackground(Color.WHITE);
        g.drawLine(bordo,bordo,bordo,y-bordo);
        g.drawLine(bordo,bordo+(int)((maxY-0)*unitaY),x-bordo,bordo+(int)((maxY-0)*unitaY));
        if(pronto){
            this.setBackground(Color.WHITE);
            g.drawLine(bordo,bordo,bordo,y-bordo);
            g.drawLine(bordo,bordo+(int)((maxY-0)*unitaY),x-bordo,bordo+(int)((maxY-0)*unitaY));
            for(int i=0;i<250;i++){
                g.fillOval(bordo+(int)(vettX[i]*unitaX),bordo+(int)((maxY-vettY[i])*unitaY),3,3);
            }
        }
        
    }
    
}