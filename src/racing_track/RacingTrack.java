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
    private int countWinners=0;
    private boolean hAtStartLine =false;
    private int countCavalos=0;
    private int cHorsesMoves=0;
    private int countFinish=0;
    private boolean finish=false;
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
        hPosition+=rand.nextInt(log.getHorsesMaxSpeed(id)+1 -RaceDay.HORSE_MIN_MD)+RaceDay.HORSE_MIN_MD;
        log.updateHorseDistance(id,hPosition);
        log.updateHorseMoves(id,cPosition);        
        notifyAll();
    }
        
   @Override
   public synchronized boolean hasFinishLineBeenCrossed(int id)
   {       
           if(log.getHorseDistance(id)>=RaceDay.RACE_DISTANCE){
            countWinners++;
            return eRace=true;
            
           }
           else
           {
               return eRace=false; 
           }
         
           
   }
   

   
   @Override
   public synchronized void proceedToStable(int id)
   {
      while(countWinners<4)
      {
          try {
              wait();
          } catch (InterruptedException ex34) {
              Logger.getLogger(RacingTrack.class.getName()).log(Level.SEVERE, null, ex34);
          }
      }
      
      notifyAll();
   }
   @Override
   public synchronized void proceedToStartLine(int id)
   {
       if(countCavalos==4){
           countCavalos=0;
           hAtStartLine=false;
           sRace=false;
           countWinners=0;
       }
       horses.add(id);
       countCavalos++;
       while(countCavalos<RaceDay.N_TRACKS){
           try{
               wait();
           }catch(InterruptedException ex32){
               Logger.getLogger(RacingTrack.class.getName()).log(Level.SEVERE,null,ex32);
           }
       }
       hAtStartLine=true;
       notifyAll();
   }
   
   @Override
   public synchronized void StartTheRace()
   {
       while(!hAtStartLine)
       {
           try {
               wait();
           } catch (InterruptedException ex33) {
               Logger.getLogger(RacingTrack.class.getName()).log(Level.SEVERE, null, ex33);
           }
       }
       log.updateRaceState(2);
       sRace=true;
       notifyAll();
   }
   
   @Override
   public synchronized int[] ReportResults()
   {
       int []hMoves=new int[5];
       for(int i =1;i<=horses.size();i++)
       {
          hMoves[i]=log.getHorseMoves(i);
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
       int[] winners=new int[5];
       int j=0;
       for(int i=1;i<hMoves.length;i++)
       {
           if(hMoves[i]==hMoves[hMoves.length-1])
           {
               winners[j]=hMoves[i];
           }
       }
       log.updateRaceState(3); 
       return winners;
   }
   @Override
   public synchronized boolean areThereAnyWinners()
   {
       while(countWinners<4)
       {
           try{
               wait();
           }catch(InterruptedException ex31){
               Logger.getLogger(RacingTrack.class.getName()).log(Level.SEVERE,null,ex31);
           }
           return false;
           
       }
       log.updateRaceState(3);
       notifyAll();
       return true;
      /* if(countWinners<4){
           return false;
           
       }else{
           
           return true;
       }*/
     
      
   }
   
      @Override
   public synchronized void goWatchTheRace(){
       while(log.getRaceState()!=3)
       {
           try{
               wait();
               
           }catch(InterruptedException ex7)
           {
                Logger.getLogger(RacingTrack.class.getName()).log(Level.SEVERE, null, ex7);

           }
       }
       notifyAll();
    
   }
}