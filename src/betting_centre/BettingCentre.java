/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package betting_centre;

import general_info_repo.Log;
import general_info_repo.RaceDay;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricar
 */
public class BettingCentre implements ISpectator, IBroker {
    private final Log log;
    private int bCount, cCount, lCount;
    private boolean sBets=false;
    private boolean sPay, ePay =false;
    
    public BettingCentre(){
        log = Log.getInstance();
        this.cCount=0;
        this.bCount=0;
        this.lCount=0;
    }
    
    @Override
    public synchronized void placeABet(int id)
    {   
        while(!sBets)
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
            if(quantity>wallet){
                quantity=(int)wallet;
            }
            int horseC = rand.nextInt(RaceDay.N_TRACKS)+1;
            log.updateSpecBetHorse(id,horseC);
            log.updateSpecBetMoney(id,quantity);
            wallet -= quantity;
            log.updateSpectatorWallet(id, wallet);
            
        }
        else{
           Random rand = new Random();
           int horseC = rand.nextInt(RaceDay.N_TRACKS)+1;
           log.updateSpecBetHorse(id, -1);
           log.updateSpecBetMoney(id, 0);
        }
        bCount++;
        notifyAll();
    }
    @Override
    public synchronized int goCollectTheGains(int id, boolean win){
      /*  while(!ePay)
        {
            try
            {
                wait();
            }catch(InterruptedException ex10){
                Logger.getLogger(BettingCentre.class.getName()).log(Level.SEVERE, null, ex10);

            }
        }*/
       
      if(win){
        int betHorse= log.getBetHorse(id);
        double quantity= log.getBetMoney(betHorse);
        double odd = log.getHorseOdds(betHorse);
        double wallet = log.getSpecatorWallet(id);
        wallet += odd*quantity;
        log.updateSpectatorWallet(id, wallet);
        cCount++;        
        if(cCount==log.getNwinners() && cCount+lCount==bCount){
           
            lCount=0;
            cCount=0;
            sPay=true;
        }
       
      }else{
          lCount++;
          if(lCount+cCount==bCount){
            sPay=true;
            lCount=0;
            cCount=0;
          
          }
      }
      notifyAll();
        if(log.getSpecatorWallet(id)<RaceDay.SPEC_MIN_BET){
            return 0;
        }
        else
        {
            return 1;
        }
    }
    @Override
    public synchronized void acceptTheBets()
    {   ePay=false;
        sPay=false;
        cCount=0;
        sBets=true;
        bCount=0;
        lCount=0;
        while(bCount<4)
        {
            try{
                wait();
            }catch(InterruptedException ex16){
                Logger.getLogger(BettingCentre.class.getName()).log(Level.SEVERE, null, ex16);
            }
        }
        sBets=false;
        log.updateRaceState(2);
        notifyAll();
       
    }
    @Override
    public synchronized boolean honnourTheBets(int id){
        ePay=true;
        sPay=false;
   
        while(!sPay)
        {
           try{
               wait();
           }catch(InterruptedException ex19)
           {
              Logger.getLogger(BettingCentre.class.getName()).log(Level.SEVERE, null, ex19); 
           }
        }
        notifyAll();
        return true;
    }
}
