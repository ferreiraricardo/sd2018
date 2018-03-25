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
        while(!sRace || log.getRaceState()!=2)
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
        hPosition+=rand.nextInt(log.getHorsesMaxSpeed(id) -RaceDay.HORSE_MIN_MD)+RaceDay.HORSE_MIN_MD;
        log.updateHorseDistance(id,hPosition);
        log.updateHorseMoves(id,cPosition);
        if(cHorsesMoves%horses.size()==1)
        {
            notifyAll();
        }
        else
        {
           try
           {
               wait();
           }catch(InterruptedException ex15){
                Logger.getLogger(RacingTrack.class.getName()).log(Level.SEVERE, null, ex15);
           }
        }
        
        ///////////////////////////////////////
    }
        
   @Override
   public synchronized boolean hasFinishLineBeenCrossed(int id)
   {
       int hDistance = log.getHorseDistance(id);
       if(hDistance>=RaceDay.RACE_DISTANCE)
       {
           log.updateRaceState(3);
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
       if(countCavalos==RaceDay.N_TRACKS || log.getRaceState()==2)
       {
           sRace=true;
           notifyAll();
       }
   }
   
   @Override
   public synchronized int[] ReportResults(int id)
   {
       int rCavalo = log.getHorseMoves(id);
       boolean win = false;
       int []hMoves={0};
       for(int i =0;i<horses.size();i++)
       {
          hMoves[i]=log.getHorseMoves(horses.get(i));
       }
       for (int i = 1; i < hMoves.length; i++){
			
	int aux = hMoves[i];
        int j = i;
	
	while ((j > 0) && (hMoves[j-1] > aux)){
            hMoves[j] = hMoves[j-1];
            j -= 1;
	}
	hMoves[j] = aux;
                
       }
       notifyAll();
       int[] winners={0};
       int j=0;
       for(int i=0;i<hMoves.length;i++)
       {
           if(hMoves[i]==hMoves[hMoves.length])
           {
               winners[j]=hMoves[i];
           }
       }
       return winners;
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
                Logger.getLogger(RacingTrack.class.getName()).log(Level.SEVERE, null, ex7);

           }
       }
       
   }
   
   @Override
   public synchronized boolean haveIWon(int id){//SPECTATORS
       while(!eRace){
           try{
               wait();
           }catch(InterruptedException ex20){
               Logger.getLogger(RacingTrack.class.getName()).log(Level.SEVERE, null, ex20);
           }
       }
       int sbHorse=log.getBetHorse(id);
       boolean win=false;
            for(int i =0;i<horses.size();i++)
            {
                if(log.getHorseMoves(sbHorse)<=log.getHorseMoves(horses.get(i)))
                {
                   win = true;
                }
                else
                {
                   win = false;
                   break;
                  
                }
            }
            notifyAll();
            return win;
   }
}
