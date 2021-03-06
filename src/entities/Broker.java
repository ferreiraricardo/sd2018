/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;
import general_info_repo.Log;
/**
 *
 * @author ricar
 */
public class Broker extends Thread {
    private final control_centre.IBroker control;
    private final betting_centre.IBroker betting;
    private final racing_track.IBroker racing;
    private final stable.IBroker stable;
    private final Log log;
    private BrokerState state;
    
    public Broker(control_centre.IBroker control,  betting_centre.IBroker betting, racing_track.IBroker racing, stable.IBroker stable ){
        this.control=control;
        this.betting=betting;
        this.racing=racing;
        this.stable=stable;
        this.log=Log.getInstance();
        this.setName("Broker");
        state = BrokerState.OPENING_THE_EVENT;
        
        this.log.initBrokerState(state);
    }
    
    @Override
    public void run(){
        boolean raceOver = false;
        boolean winners = false;
        int aux;
        while(!raceOver){
            switch(this.state){
                case OPENING_THE_EVENT:
                    this.log.updateRaceState(0);
                    this.log.newRaceDay();
                    this.state=BrokerState.ANNOUNCING_NEXT_RACE;
                    break;
                case ANNOUNCING_NEXT_RACE:
                    aux=this.log.getRaceNumber();
                    this.log.updateRaceNumber(aux+1);
                    this.log.updateRaceState(0);
                    this.log.updateNwinners(0);
                    this.stable.summonHorsesToPaddock();
                    this.state=BrokerState.WAITING_FOR_BETS;
                    break;
                case WAITING_FOR_BETS:
                     this.betting.acceptTheBets();
                    this.state=BrokerState.SUPERVISING_THE_RACE;
                    break;
                case SUPERVISING_THE_RACE:  
                    this.racing.StartTheRace();
                    winners=false;
                    while(!winners){
                       winners=this.racing.areThereAnyWinners();
                   }
                    this.control.ReportResults();
                    this.state=BrokerState.SETTLING_ACCOUNTS;
                    break;
                case SETTLING_ACCOUNTS:
                    this.betting.honnourTheBets(1);
                    this.state=BrokerState.ANNOUNCING_NEXT_RACE;
                    //this.control.entertainTheGuests();
                    break;
                case PLAYING_HOST_AT_THE_BAR:
                    raceOver = true;
                    break;
            }
            this.log.setBrokerState(state);
        }
    }
    
    
    
    
}
