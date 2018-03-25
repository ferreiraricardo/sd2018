/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control_centre;
import general_info_repo.Log;
import general_info_repo.RaceDay;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;
/**
 *
 * @author ricar
 */

public class ControlCentre implements ISpectator, IBroker {
    private final Log log;
    
    
    public ControlCentre(){
        log=Log.getInstance();
     
    }
    
    @Override
   public synchronized boolean haveIWon(int id){//SPECTATORS
       while(log.getRaceState()!=3){
           try{
               wait();
           }catch(InterruptedException ex20){
               Logger.getLogger(ControlCentre.class.getName()).log(Level.SEVERE, null, ex20);
           }
       }
       int sbHorse=log.getBetHorse(id);
       boolean win=false;
       

            for(int i =0;i<RaceDay.N_HORSES;i++)
            {
                if(log.getHorseMoves(sbHorse)<=log.getHorseMoves(i))
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
   
   @Override
   public synchronized void summonHorsesToPaddock(){
        while(log.getRaceState()!=3){
            try{
                wait();
            }catch(InterruptedException ex) {
                Logger.getLogger(ControlCentre.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        notifyAll();
   }
   
   @Override
   public synchronized void entertainTheGuests(){
       System.out.println("Putas e Vinho Verde");
   }
   
   @Override
   public synchronized void relaxABit(){
       System.out.println("Relaxado pa Caralho");
   }
   
   @Override
   public synchronized void goWatchTheRace(){
       while(log.getRaceState()!=3)
       {
           try{
               wait();
           }catch(InterruptedException ex7)
           {
                Logger.getLogger(ControlCentre.class.getName()).log(Level.SEVERE, null, ex7);

           }
       }
   }
   
}
