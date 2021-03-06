/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stable;
import general_info_repo.Log;
import general_info_repo.RaceDay;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ricar
 */
public class Stable implements IBroker, IHorses {
    private boolean horsesReady = false;
    private boolean callRace = false;
    private boolean endDay = false;
    private int countCavalos = 0;
    private final LinkedList<Integer> horses;
    private final Log log;
    private boolean flag=false;
    
    
    public Stable(){
        log = Log.getInstance();
        horses = new LinkedList<>();
    }
    
    
    @Override
    public synchronized void summonHorsesToPaddock(){
        while(!horsesReady){
            try{
                wait();
            }catch(InterruptedException ex) {
                Logger.getLogger(Stable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        flag=true;
        notifyAll();
    }
    
    
    @Override
    public synchronized void waitForProceedToPaddock(){
         while(!callRace){
                 try {
                     wait();
                 } catch (InterruptedException ex) {
                     Logger.getLogger(Stable.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
         horsesReady=true;
         notifyAll();
         }
    
    
    
    @Override 
    public synchronized void proceedToPaddock(){
        while(!this.flag){
            try{
                wait();
            }catch(InterruptedException ex) {
                Logger.getLogger(Stable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    horses.remove(0);
    notifyAll();
    }
    
    
    
    @Override
    public synchronized void proceedToStable(int id){
        
        if(countCavalos==RaceDay.N_TRACKS){
            this.horsesReady=false;
            this.callRace=false;
            this.countCavalos=0;
            this.flag=false;
        }
        horses.add(id);
        countCavalos++;
        while(countCavalos<=RaceDay.N_TRACKS-1)
        {
            try{
                wait();
            }catch(InterruptedException ex){
                Logger.getLogger(Stable.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        callRace=true;
        notifyAll();
       
    }
    
    
}
    
