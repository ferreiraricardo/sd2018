/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddock;
import general_info_repo.RaceDay;
import general_info_repo.Log;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

/**
 *
 * @author ricar
 */
public class Paddock implements ISpectator , IHorses, IBroker{
    private final int[] horses;
    private final Log log;
    private boolean startRace = false;
    private boolean betsReady = false;
    private int countCavalos =0;
    private int countSpec =0;
    private int nHorsesWait=RaceDay.N_TRACKS;
    Random rand = new Random();
    private boolean horsesReady=false;
    
    public Paddock(){
        log = Log.getInstance();
        this.horses=new int[5];
    }
    @Override
    public synchronized int goCheckHorses(int eid)
    {
        countSpec++;
        while(!horsesReady)
        {
            try{
                wait();
            }catch(InterruptedException ex3){
                Logger.getLogger(Paddock.class.getName()).log(Level.SEVERE,null,ex3);       
            }
        }
        double odd = 0;
        int vHorses = 0;
        for(int i=1; i<=RaceDay.N_TRACKS;i++)
        {
            vHorses+=log.getHorsesMaxSpeed(horses[i]);
        }
        for(int j=1; j<=RaceDay.N_TRACKS;j++)
        {
            odd = log.getHorsesMaxSpeed(j)/vHorses;
            odd = (1-odd)*RaceDay.N_TRACKS;
            log.updateHorseOdds(horses[j],odd);
        }
        int choice = rand.nextInt(RaceDay.N_TRACKS);
        if(countSpec==RaceDay.N_SPECTATORS)
        {
            startRace=true;
        }
        notifyAll();
        return choice;
        
        
    }
    
    @Override
    public synchronized void proceedToStartLine()
    {
        while(!startRace)
        {
            try{
                wait();
            }catch(InterruptedException ex30){
                Logger.getLogger(Paddock.class.getName()).log(Level.SEVERE,null,ex30);
            }
        }
        notifyAll();
        
    }
    
    @Override
    public synchronized void proceedToPaddock(int id)
    {
        horses[id]=id;
        countCavalos++;
        while(countCavalos<RaceDay.N_TRACKS)
        {
            try{
                wait();
            }catch(InterruptedException ex1)
            {
                Logger.getLogger(Paddock.class.getName()).log(Level.SEVERE,null,ex1);
            }
        }
        log.updateRaceState(1);
        horsesReady=true;
        notifyAll();
        
    }
    
    
    @Override
    public synchronized void waitForNextRace(int id)
    {
        while(!horsesReady)
        {
            try{
                wait();
            }catch(InterruptedException ex4){
                Logger.getLogger(Paddock.class.getName()).log(Level.SEVERE, null, ex4);
            }
        }
        notifyAll();
        
    }
}
