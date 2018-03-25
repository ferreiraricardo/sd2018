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
public class Spectator extends Thread {
    private final betting_centre.ISpectator betting;
    private final control_centre.ISpectator control;
    private final paddock.ISpectator paddock;
    private final racing_track.ISpectator racing;
    
    private final int id;
    private final int mb;
    private SpectatorState state;
    
    public Spectator(betting_centre.ISpectator betting, control_centre.ISpectator control, paddock.ISpectator paddock, racing_track.ISpectator racing,int mb, int id){
        this.id = id;
        this.mb=mb;
        this.control = control;
        this.betting = betting;
        this.paddock = paddock;
        this.racing = racing;
        state = SpectatorState.WAITING_FOR_A_RACE_TO_START;
    }
    
    @Override
    public void run(){
        boolean raceOver = false;
        boolean won = false;
        
        while(!raceOver){
            switch(this.state){
                case WAITING_FOR_A_RACE_TO_START:
                    this.paddock.waitForNextRace(id);
                    //quando a corrida é anunciada
                    this.paddock.goCheckHorses(id);
                    this.state=SpectatorState.APPRAISING_THE_HORSES;
                    break;
                case APPRAISING_THE_HORSES:
                    this.betting.placeABet(id);
                    this.state=SpectatorState.PLACING_A_BET;
                    break;
                case PLACING_A_BET:
                    this.racing.goWatchTheRace(id);
                    this.state = SpectatorState.WATCHING_A_RACE;
                    break;
                case WATCHING_A_RACE:
                    won = this.racing.haveIWon(id);
                    if(!won){
                        //caso nao queira apostar mais
                        this.control.relaxABit();
                        this.state=SpectatorState.CELEBRATING;
                        //caso ainda queira apostar noutras corridas
                        this.paddock.waitForNextRace(id);
                        this.state=SpectatorState.WAITING_FOR_A_RACE_TO_START;
                    }
                    //caso queira recolher os ganhos
                    this.betting.goCollectTheGains(id);
                    this.state=SpectatorState.COLLECTING_THE_GAINS;
                    break;
                case COLLECTING_THE_GAINS:
                    this.control.relaxABit();
                    this.state = SpectatorState.CELEBRATING;
                    
                    this.paddock.waitForNextRace(id);
                    this.state = SpectatorState.WAITING_FOR_A_RACE_TO_START;
                    break;
                case CELEBRATING:
                    raceOver = true;
                    break;
                        
            }
        }
    }
    
    
    
}
