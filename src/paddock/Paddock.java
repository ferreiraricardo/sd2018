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
    private int[] horses;
    private final Log log;
    private boolean startRace = false;
    private boolean betsReady = false;
    private int countCavalos =0;
    private int countSpec =0;
    private int countF=0;
    private int nHorsesWait=RaceDay.N_TRACKS;
    Random rand = new Random();
    private boolean horsesReady=false;
    private final LinkedList<Integer> horses1;
    
    public Paddock(){
        log = Log.getInstance();
        horses1= new LinkedList<>();
        this.horses=new int[5];
       
    }
    @Override
    public synchronized int goCheckHorses(int eid)
    {
        countSpec++;

        double odd = 0;
        int vHorses = 0;
        for(int i=1; i<=RaceDay.N_TRACKS;i++)
        {
            vHorses+=log.getHorsesMaxSpeed(horses[i]);
        }
        for(int j=1; j<=RaceDay.N_TRACKS;j++)
        {
            odd = log.getHorsesMaxSpeed(j)/vHorses;
            odd = (1-odd);
            odd = odd+1;
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
    {   countF++;
        while(!this.startRace)
        {
            try{
                wait();
            }catch(InterruptedException ex30){
                Logger.getLogger(Paddock.class.getName()).log(Level.SEVERE,null,ex30);
            }
        }
        if(countF==3){
        horsesReady=false;
        countCavalos=0;
        countF=0;
        countSpec=0;
       startRace=false;
        }
        notifyAll();
    }
    
    @Override
    public synchronized void proceedToPaddock(int id)
    {   
        if(countCavalos==RaceDay.N_TRACKS){
           this.horsesReady=false;
           this.startRace=false;
           this.countCavalos=0;
           this.countSpec=0;
           this.horses = new int[5];
          }
        horses[id]=id;
        countCavalos++;
        while(countCavalos<=RaceDay.N_TRACKS-1)
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
        while(!horsesReady || log.getRaceState()!=1)
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
