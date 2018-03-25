/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package racing_track;

import general_info_repo.Log;
import general_info_repo.RaceDay;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author ricar
 */
public class RacingTrack implements IBroker,IHorse,ISpectator{
    
    private final LinkedList<Integer> horses;
    private int hPosition=0;
    private int cPosition=0;
    private boolean sRace=false;
    private boolean eRace= false;
    private int countCavalos=0;
    private int cHorsesMoves=0;
    private final Log log;
    
    public RacingTrack(){
        log = Log.getInstance();
        horses = new LinkedList<>();
    }
    
    @Override
    public synchronized void makeAMove(int id)
    {
        Random rand = new Random();
        while(!sRace)
        {
            try{
                wait();
            }catch(InterruptedException ex6){
                Logger.getLogger(RacingTrack.class.getName()).log(Level.SEVERE, null, ex6);
            }
        }
        cHorsesMoves++;
        cPosition=log.getHorseMoves(id);
        hPosition=log.getHorseDistance(id);
        cPosition++;
        hPosition+=rand.nextInt(log.getVelocity(id)-RaceDay.HORSE_MIN_MD)+RaceDay.HORSE_MIN_MD;
        log.updateHorseDistance(id,hPosition);
        log.updateHorseMoves(id,cPosition);
        if(cHorsesMoves%horses.size()==1)
        {
            notifyAll();
        }
        else
        {
            wait();
        }
        
        ///////////////////////////////////////
    }
        
   @Override
   public synchronized boolean hasFinishLineBeenCrossed(int id)
   {
       int hDistance = log.getHorseDistance(id);
       if(hDistance>=RaceDay.RACE_DISTANCE)
       {
           eRace=true;
           return true;
       }
       return false;
   }
   
   @Override
   public synchronized void proceedToStable(int id)
   {
      
       if(eRace)
       {
           eRace=false;
           notifyAll();
       }
   }
   
   @Override
   public synchronized void StartTheRace(int id)
   {
       countCavalos=horses.size();
       if(countCavalos==RaceDay.N_TRACKS )/////|| RaceDay.bDone()==N_Spectators)
       {
           sRace=true;
           notifyAll();
       }
   }
   
   @Override
   public synchronized int ReportResults(int id)
   {
       int rCavalo = log.getHorseMoves(id);
       boolean win = false;
       for(int i =0;i<horses.size();i++)
       {
           if(rCavalo<log.getHorseMoves(horses.get(i)))
           {
               win=true;
           }
           else
           {
               win=false;
               break;
           }
       }
   }
   @Override
   public boolean areThereAnyWinners(int id)
   {
       if(eRace=true)
       {
           return true;
       }
       else
       {
           return false;
       }
   }
   
   @Override
   public synchronized void goWatchTheRace(int id)
   {
       while(!eRace)
       {
           try{
               wait();
           }catch(InterruptedException ex7)
           {
                Logger.getLogger(RacingTrack.class.getName()).log(Level.SEVERE, null, ex2);

           }
       }
       
   }
   
   @Override
   public synchronized boolean haveIWon(int id)
   {
       int sbHorse=log.getBetHorse(id);
       boolean win=false;
            for(int i =0;i<horses.size();i++)
            {
                if(log.getHorseMoves(sbHorse)<log.getHorseMoves(horses.get(i)))
                {
                    win=true;
                }
                else
                {
                    win=false;
                    break;
                }
            }
       return win;
   }
    
}
