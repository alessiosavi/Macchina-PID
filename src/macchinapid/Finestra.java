/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package macchinapid;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Sebastiano
 */
public class Finestra extends JFrame implements WindowListener,ActionListener {

    private JPanel pannelloDati= new JPanel();
    private Grafico grafico;
    private JButton parti= new JButton("Parti");
    private JButton preset= new JButton ("Valori Predefiniti");
    
    // Parametri PID
    private JLabel l_pid=new JLabel("----Parametri PID");
    private JLabel l_kp= new JLabel("Kp");
    private JTextField kp= new JTextField();
    private JLabel l_kd = new JLabel("Kd");
    private JTextField kd= new JTextField();
    private JLabel l_ki = new JLabel("Ki");
    private JTextField ki= new JTextField();
    // parametri macchina
    private JLabel l_macchina=new JLabel("----Parametri Macchina");
    private JLabel l_len = new JLabel("Lunghezza");
    private JTextField len = new JTextField();
    private JLabel l_steeringNoise = new JLabel("Steering Noise");
    private JTextField steeringNoise = new JTextField();
    private JLabel l_steeringDrift = new JLabel("Steering Drift");
    private JTextField steeringDrift = new JTextField();
    private JLabel l_distNoise = new JLabel("Distance Noise");
    private JTextField distNoise = new JTextField();
    private JCheckBox tuning= new JCheckBox("Tuning(coeff.automatici)");
    float ErroreTotale;
    
    
    
    
    public Finestra(){
        
        this.setSize(800,450);
        this.setLayout(null);
        this.add(pannelloDati);
        this.add(parti);
        //this.add(grafico);
        this.addWindowListener(this);
        
        this.setVisible(true);
        
        parti.addActionListener(this);
        parti.setBounds(0,350,200,61);
        
        preset.addActionListener(this);
        
        //grafico.setBounds(200,0,800,411);
        //grafico.setSize(800,411);
        
        pannelloDati.setBounds(0,0,200,350);
        pannelloDati.setBackground(Color.red);
        pannelloDati.setLayout(new BoxLayout(pannelloDati,BoxLayout.Y_AXIS));
        
        pannelloDati.add(l_pid);
        pannelloDati.add(l_kp);
        pannelloDati.add(kp);
        pannelloDati.add(l_kd);
        pannelloDati.add(kd);
        pannelloDati.add(l_ki);
        pannelloDati.add(ki);
        
        pannelloDati.add(l_macchina);
        pannelloDati.add(l_len);
        pannelloDati.add(len);
        pannelloDati.add(l_steeringNoise);
        pannelloDati.add(steeringNoise);
        pannelloDati.add(l_steeringDrift);
        pannelloDati.add(steeringDrift);
        pannelloDati.add(l_distNoise);
        pannelloDati.add(distNoise);
        pannelloDati.add(tuning);
        pannelloDati.add(preset);
        
        
        grafico=new Grafico(800,411);
        grafico.setBounds(200,0,800,411);
        this.add(grafico);
        this.repaint();
        
        
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==preset){
            kp.setText("0.2");
            kd.setText("3");
            ki.setText("0.004");
            
            len.setText("5");
            steeringNoise.setText("0");
            steeringDrift.setText(Double.toString(Math.toRadians(2)));
            distNoise.setText("0");
        }
        if(e.getSource()==parti){
            
            float lenght, steering_noise, distance_noise;
            double steering_drift;
            
            float Kp, Kd, Ki;
            
            try{
            lenght=Float.parseFloat(len.getText());
            steering_noise = Float.parseFloat(steeringNoise.getText());
            steering_drift = Double.parseDouble(steeringDrift.getText());
            distance_noise = Float.parseFloat(distNoise.getText());
            
            Kp = Float.parseFloat(kp.getText());
            Kd = Float.parseFloat(kd.getText());
            Ki = Float.parseFloat(ki.getText());
            
            /*lenght=5;
            steering_noise=steering_noise=0f;
            steering_drift=0.03490658503988659;
            Kp=0.2f;
            Kd=3;
            Ki=0.004f;*/
            
            
            }catch(Exception exc){
            return;
            }            
            
            Macchina macchina=new Macchina(lenght,0,1f,0f,distance_noise,steering_noise,steering_drift);
            PID pid=new PID(Kp,Kd,Ki);
            if(tuning.isSelected()==false){Run(macchina,pid);}
            else{
                float p[]=new float[3];
                p[0]=Kp;
                p[1]=Kd;
                p[2]=Ki;
                try{
                    Tuning( p, macchina, pid);
                }catch(Exception exc){return;}
                
            }
            
            
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        this.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

public float Run(Macchina macchina, PID pid){
        int n=250;
        //distanza della macchina dalla traiettoria (asse y)
        float errore;
        //angolo di sterzata restituito dal pid
        float sterzata;
        //velocità della macchina
        float velocita=1f;
        float erroreTot=0;
        //vettore delle coordinate della macchina nel tempo
        float coordX[]=new float[n];
        float coordY[]=new float[n];
        
        for(int i=0;i<n;i++){
            coordX[i]=i;
            coordY[i]=macchina.getY();
            errore=macchina.getY();
            sterzata=pid.Calcola(errore);
            macchina=macchina.move(sterzata,velocita);
            erroreTot+=errore*errore;    
        }
        grafico.setSimulato(true);
        grafico.setCoord(coordX,coordY);
        grafico.repaint();
        this.repaint();
        return erroreTot;
    }

 public void Tuning(float p[],Macchina macchina, PID pid)throws IOException{
        int nPar=3;
        float tolleranza=0.001f;
        float dp[]=new float[nPar];
        //errore migliore reso dalla run
        float bestError;
        //errore reso dalla run
        float errore;
        for(int i=0;i<3;i++){
            dp[i]=1f;
        }
        bestError=Run( macchina, pid);
        while(dp[0]+dp[1]+dp[2]>tolleranza){
            for(int i=0;i<nPar;i++){
                p[i]+=dp[i];
                errore=Run( macchina, pid);
                if(errore<bestError){
                    bestError=errore;
                    dp[i]*=1.1;
                }
                else{
                    p[i]-=2*dp[i];
                    errore=Run( macchina, pid);
                    if(errore<bestError){
                        bestError=errore;
                        dp[i]*=1.1;
                    }
                    else{
                        p[i]+=dp[i];
                        dp[i]*=0.9;
                    }
                }
            }
        }
    }

}

