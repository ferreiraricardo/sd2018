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
public class Spectator extends Thread {
    private final betting_centre.ISpectator betting;
    private final control_centre.ISpectator control;
    private final paddock.ISpectator paddock;
    private final racing_track.ISpectator racing;
    private final Log log;
    private final int id;
    private final double mb;
    private SpectatorState state;
    
    public Spectator(betting_centre.ISpectator betting, control_centre.ISpectator control, paddock.ISpectator paddock, racing_track.ISpectator racing,double mb, int id){
        this.id = id;
        this.mb=mb;
        this.control = control;
        this.betting = betting;
        this.paddock = paddock;
        this.racing = racing;
        this.log=Log.getInstance();
        this.setName("Spectator"+this.id);
        state = SpectatorState.WAITING_FOR_A_RACE_TO_START;
        this.log.initSpectators(this.state, this.id, this.mb);
    }
    
    @Override
    public void run(){
        boolean raceOver = false;
        boolean won ;
        
        while(!raceOver){
            switch(this.state){
                case WAITING_FOR_A_RACE_TO_START:
                    this.paddock.waitForNextRace(id);
                    this.state=SpectatorState.APPRAISING_THE_HORSES;
                    break;
                case APPRAISING_THE_HORSES:
                    this.paddock.goCheckHorses(id);
                    this.state=SpectatorState.PLACING_A_BET;
                    break;
                case PLACING_A_BET:
                  
                    this.betting.placeABet(id);
                    this.state = SpectatorState.WATCHING_A_RACE;
                   
                    break;
                case WATCHING_A_RACE:
                    System.out.print("1");
                    this.racing.goWatchTheRace();
                    System.out.print("2");
                    won = this.control.haveIWon(id);
                    if(!won){
                        System.out.print("3");
                        this.paddock.waitForNextRace(id);
                        this.state=SpectatorState.WAITING_FOR_A_RACE_TO_START;
                    } 
                    this.state=SpectatorState.COLLECTING_THE_GAINS;
                    break;
                case COLLECTING_THE_GAINS:
                    this.betting.goCollectTheGains(id);
                    this.control.relaxABit();
                    this.state = SpectatorState.CELEBRATING;
                    
                    this.paddock.waitForNextRace(id);
                    this.state = SpectatorState.WAITING_FOR_A_RACE_TO_START;
                    break;
                case CELEBRATING:
                    raceOver = true;
                    break;
                        
            }
            this.log.setSpectatorState(this.state, this.id);
        }
    }
    
    
    
}
