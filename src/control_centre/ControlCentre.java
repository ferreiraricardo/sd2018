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
    private int[] list;
    
    
    public ControlCentre(){
        log=Log.getInstance();
        list=new int[5];
     
    }
    
    @Override
   public synchronized boolean haveIWon(int id){//SPECTATORS
    /*   while(log.getRaceState()!=3){
           try{
               wait();
           }catch(InterruptedException ex20){
               Logger.getLogger(ControlCentre.class.getName()).log(Level.SEVERE, null, ex20);
           }
       }*/
       int sbHorse=log.getBetHorse(id);
       boolean win=false;
       
     for(int i=0; i<list.length; i++){
         System.out.println("list-"+i+" "+list[i]);
     }
           
                if(log.getHorseMoves(sbHorse)==list[1])
                {
                   int i=0;
                   win = true;
                   i=log.getNwinners();
                   i++;
                   log.updateNwinners(i);
                }
                else
                {
                   win = false;
                  
                }
           
            return win;
   }
   
   
    @Override
   public synchronized int[] ReportResults()
   {
       int []hMoves=new int[5];
       for(int i =1;i<=4;i++)
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
       log.updateRaceState(3); 
       list=hMoves;
       return hMoves;
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
               System.out.print("aqui");
               wait();
               
           }catch(InterruptedException ex7)
           {
                Logger.getLogger(ControlCentre.class.getName()).log(Level.SEVERE, null, ex7);

           }
       }
       System.out.print("ali");
       notifyAll();
    
   }
   
}
