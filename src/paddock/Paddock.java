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
    private final LinkedList<Integer> horses;
    private final Log log;
    private boolean betsReady = false;
    private int countCavalos =0;
    private int nHorsesWait=4;
    Random rand = new Random();
    
    public Paddock(){
        log = Log.getInstance();
        horses = new LinkedList<>();
    }
    @Override
    public synchronized int goCheckHorses(int eid)
    {
        notifyAll();
        while(countCavalos<RaceDay.N_TRACKS)
        {
            try{
                wait();
            }catch(InterruptedException ex3){
                Logger.getLogger(Paddock.class.getName()).log(Level.SEVERE,null,ex3);       
            }
        }
        double odd = 0;
        int vHorses = 0;
        for(int i=0; i<RaceDay.N_TRACKS;i++)
        {
            vHorses+=RaceDay.getVelocity(horses.get(i));
        }
        for(int j=0; j<RaceDay.N_TRACKS;j++)
        {
            odd = RaceDay.getVelocity(j)/vHorses;
            RaceDay.setHorsesOdds(horses.get(j),odd);
        }
        int choice = rand.nextInt(RaceDay.N_TRACKS+1);
        return choice;
        
        
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
}
