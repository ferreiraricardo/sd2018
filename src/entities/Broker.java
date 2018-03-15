/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author ricar
 */
public class Broker extends Thread {
    private final control_centre.IBroker control;
    private final betting_centre.IBroker betting;
    private final racing_track.IBroker racing;
    private final stable.IBroker stable;
    private BrokerState state;
    
    public Broker(control_centre.IBroker control,  betting_centre.IBroker betting, racing_track.IBroker racing, stable.IBroker stable ){
        this.control=control;
        this.betting=betting;
        this.racing=racing;
        this.stable=stable;
        
        state = BrokerState.OPENING_THE_EVENT;
    }
    
    @Override
    public void run(){
        boolean raceOver = false;
        
        while(!raceOver){
            switch(this.state){
                case OPENING_THE_EVENT:
                    this.stable.summonHorsesToPaddock();
                    this.state=BrokerState.ANNOUNCING_NEXT_RACE;
                    break;
                case ANNOUNCING_NEXT_RACE:
                    this.betting.acceptTheBets(1);
                    this.state=BrokerState.WAITING_FOR_BETS;
                    break;
                case WAITING_FOR_BETS:
                    this.racing.StartTheRace(1);
                    this.state=BrokerState.SUPERVISING_THE_RACE;
                    break;
                case SUPERVISING_THE_RACE:
                    //verificacao
                    this.racing.areThereAnyWinners(1);
                    this.racing.ReportResults(1);
                    
                    this.control.summonHorsesToPaddock();
                    this.state=BrokerState.ANNOUNCING_NEXT_RACE;
                    
                    //caso haja vencedores
                    this.betting.honnourTheBets(1);
                    this.state=BrokerState.SETTLING_ACCOUNTS;
                    //caso nao haja vencedores
                    this.control.entertainTheGuests();
                    this.state=BrokerState.PLAYING_HOST_AT_THE_BAR;
                    
                    break;
                case SETTLING_ACCOUNTS:
                    
                    this.control.summonHorsesToPaddock();
                    this.state=BrokerState.ANNOUNCING_NEXT_RACE;
                    
                    this.control.entertainTheGuests();
                    this.state=BrokerState.PLAYING_HOST_AT_THE_BAR;
                    break;
                case PLAYING_HOST_AT_THE_BAR:
                    raceOver = true;
                    break;
            }
        }
    }
    
    
    
    
}
