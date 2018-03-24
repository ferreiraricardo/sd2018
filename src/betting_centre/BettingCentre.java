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

/**
 *
 * @author ricar
 */
public class BettingCentre {
    private final LinkedList<Object> horses;
    private final Log log;
    
    public BettingCentre(){
        log = Log.getInstance();
        horses = new LinkedList<>();
    }
    
    @Override
    public synchronized void placeABet(int id)
    {
        Random rand = new Random();
        int quantity = rand.nextInt(RaceDay.SPEC_MAX_BET-RaceDay.SPEC_MIN_BET)+RaceDay.SPEC_MIN_BET;
        int horseC = rand.nextInt(RaceDay.N_TRACKS)-1;
        RaceDay.setSpecBetHorse(id,horseC);
        RaceDay.setSpecBetMoney(id,quantity);
        wait();
    }
    public synchronized double goCollectTheGains(int id){
        int betHorse= log.getSpecBetHorse(id);
        int quantity= log.getSpecBetMoney(betHorse);
        double odd = log.getHorsesOdds(betHorse);
        return odd*quantity;
    } 
}
