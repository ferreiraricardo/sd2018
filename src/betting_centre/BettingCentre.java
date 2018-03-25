/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package betting_centre;

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
public class BettingCentre {
    private final LinkedList<Object> horses;
    private final Log log;
    private boolean sBets=false;
    
    public BettingCentre(){
        log = Log.getInstance();
        horses = new LinkedList<>();
    }
    
    @Override
    public synchronized void placeABet(int id) throws InterruptedException
    {
        while(log.getRaceState!=1 || !sBets)
        {
            try{
                wait();
            }catch(InterruptedException ex11){
                Logger.getLogger(BettingCentre.class.getName()).log(Level.SEVERE, null, ex11);
            }
        }
        double wallet = log.getSpecatorWallet(id);
        if(wallet>RaceDay.SPEC_MIN_BET)
        {
            Random rand = new Random();
            int quantity = rand.nextInt(RaceDay.SPEC_MAX_BET-RaceDay.SPEC_MIN_BET)+RaceDay.SPEC_MIN_BET;
            int horseC = rand.nextInt(RaceDay.N_TRACKS)-1;
            log.updateSpecBetHorse(id,horseC);
            log.updateSpecBetMoney(id,quantity);
            wallet -= quantity;
            log.updateSpectatorWallet(id, wallet);
        }
        else{
            
            wait();
        }
    }
    public synchronized int goCollectTheGains(int id){
        while(log.getRaceState!=3)
        {
            try
            {
                wait();
            }catch(InterruptedException ex10){
                Logger.getLogger(BettingCentre.class.getName()).log(Level.SEVERE, null, ex10);

            }
        }
        int betHorse= log.getBetHorse(id);
        double quantity= log.getBetMoney(betHorse);
        double odd = log.getHorsesMaxSpeed(betHorse);
        double wallet = log.getSpecatorWallet(id);
        wallet += odd*quantity;
        log.updateSpectatorWallet(id, wallet);
        if(log.getSpecatorWallet(id)<RaceDay.SPEC_MIN_BET){
            return 0;
        }
        else
        {
            return 1;
        }
    }
    
    public void acceptTheBets(int id)
    {
        sBets=true;
        while(!betsDone)
        {
            try{
                wait();
            }catch(InterruptedException ex16){
                Logger.getLogger(BettingCentre.class.getName()).log(Level.SEVERE, null, ex16);
            }
        }
        notifyAll();
    }
    
    public boolean honnourTheBets(int id){
        while(log.getRaceState!=3)
        {
           try{
               wait();
           }catch(InterruptedException ex19)
           {
              Logger.getLogger(BettingCentre.class.getName()).log(Level.SEVERE, null, ex19); 
           }
        }
        notifyAll();
    }
}
