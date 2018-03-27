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
    private int bCount, cCount=0;
    private boolean sBets=false;
    private boolean sPay, ePay =false;
    
    public BettingCentre(){
        log = Log.getInstance();
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
            int horseC = rand.nextInt(RaceDay.N_TRACKS)+1;
            //System.out.println("id-"+horseC);
            log.updateSpecBetHorse(id,horseC);
            log.updateSpecBetMoney(id,quantity);
            wallet -= quantity;
            log.updateSpectatorWallet(id, wallet);
            
        }
        else{
            Random rand = new Random();
           int horseC = rand.nextInt(RaceDay.N_TRACKS)-1;
           log.updateSpecBetHorse(id, horseC);
           log.updateSpecBetMoney(id, 0);
        }
        bCount++;
        notifyAll();
    }
    @Override
    public synchronized int goCollectTheGains(int id){
        while(!ePay)
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
        System.out.print("ali");
        cCount++;
        if(cCount==log.getNwinners()-1){
            System.out.print("aqui");
            bCount=0;
            sPay=true;
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
        sBets=true;
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
