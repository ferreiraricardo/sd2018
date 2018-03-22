/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stable;
import general_info_repo.Log;
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
    private int count = 0;
    private int orders = -1;
    private final LinkedList<Integer> horses;
    private final Log log;
    
    
    public Stable(){
        log = Log.getInstance();
        horses = new LinkedList<>();
    }
    
    
    @Override
    public synchronized void summonHorsesToPaddock(){
        horsesReady = false;
        notifyAll();
        while(count != 0){
            try{
                wait();
            }catch(InterruptedException ex) {
                Logger.getLogger(Stable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        callRace=true;
        orders=0;
        notifyAll();
    }
    
    
    @Override
    public synchronized void waitForProceedToPaddock(){
        while(!this.horsesReady){
            try{
                wait();
            }catch(InterruptedException ex) {
                Logger.getLogger(Stable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    @Override 
    public synchronized void proceedToPaddock(){
        horsesReady=false;
    }

   
}
