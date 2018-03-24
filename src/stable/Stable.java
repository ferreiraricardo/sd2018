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
    private boolean endDay = false;
    private int countCavalos = 0;
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
        while(countCavalos != 0){
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
        countCavalos++;
        if(countCavalos==4)
        {
            horsesReady=true;
            notifyAll();
        }      
    }
    
    public synchronized void proceedToStable(int id){
        this.callRace = false;
        horses.add(id);
        if(countCavalos>0)
        {
            countCavalos--;
            notifyAll();
        }
        while(!callRace && !endDay)
        {
            try{
                wait();
            }catch(InterruptedException ex){
                Logger.getLogger(Stable.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
        horses.pop();
        if(horses.size()==4)
        {
            callRace=false;
            notifyAll();
        }
        notifyAll();
    }
    
    
}
    
