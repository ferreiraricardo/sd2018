/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddock;
import general_info_repo.Log;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricar
 */
public class Paddock implements ISpectator , IHorses, IBroker{
    private final LinkedList<Integer> horses;
    private final Log log;
    private boolean betsReady = false;
    private int countCavalos =0;
    private int nHorsesWait=4;
    
    public Paddock(){
        log = Log.getInstance();
        horses = new LinkedList<>();
    }
    @Override
    public synchronized void goCheckHorses(int id)
    {  
    }
    
    @Override
    public synchronized void waitForProceedToStartLine(){
        while(!this.betsReady){
            try{
                wait();
            }catch(InterruptedException ex2) {
                Logger.getLogger(Paddock.class.getName()).log(Level.SEVERE, null, ex2);
            }
        }
    }
    
    @Override
    public synchronized void proceedToStartLine(int id){
        betsReady=false;
        countCavalos++;
        if(countCavalos==4)
        {
            betsReady=true;
            notifyAll();
        }
    }
    
    @Override
    public synchronized void proceedToPaddock(int id)
    {
        
        this.betsReady=false;
        horses.add(id);
        countCavalos=horses.size();
        if(countCavalos>0)
        {
            countCavalos--;
            notifyAll();
        }
        while(!betsReady)
        {
            try{
                wait();
            }catch(InterruptedException ex1)
            {
                Logger.getLogger(Paddock.class.getName()).log(Level.SEVERE,null,ex1);
            }
              horses.pop();
        if(horses.size()==4)
        {
            betsReady=false;
            notifyAll();
        }
        notifyAll();
        }
    }
     @Override
    public synchronized void summonHorsesToStartLine(){
        betsReady = false;
        notifyAll();
        while(countCavalos != 0){
            try{
                wait();
            }catch(InterruptedException ex) {
                Logger.getLogger(Paddock.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        betsReady=true;
        notifyAll();
    }
    
    @Override
    public synchronized void waitForNextRace(int id){
        
    }
}
