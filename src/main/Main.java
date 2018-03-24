/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import entities.Broker;
import entities.Horses;
import entities.Spectator;
import betting_centre.BettingCentre;
import control_centre.ControlCentre;
import racing_track.RacingTrack;
import stable.Stable;
import paddock.Paddock;
import general_info_repo.Log;
import general_info_repo.RaceDay;
import java.util.*;
/**
 *
 * @author ricar
 */
public class Main {
    private static Broker broker;
    private static Horses [] horses;
    private static Spectator [] spectators;
    private static BettingCentre betting;
    private static ControlCentre control;
    private static Paddock paddock;
    private static RacingTrack racing;
    private static Stable stable;
    private static Log log;
    
    private static void main(String[] args){
        int nHorses = RaceDay.N_HORSES;
        int nSpectators = RaceDay.N_SPECTATORS;
        
        racing = new RacingTrack();
        betting = new BettingCentre();
        control = new ControlCentre();
        paddock = new Paddock();
        stable = new Stable();
        log = Log.getInstance();
        
        broker = new Broker((control_centre.IBroker) control, (betting_centre.IBroker) betting, (racing_track.IBroker) racing, (stable.IBroker) stable);
        
        horses = new Horses[nHorses];
        for(int i = 0; i<nHorses;i++){
            Random rand = new Random();
            int md = rand.nextInt(RaceDay.HORSE_MAX_MD+1-RaceDay.HORSE_MIN_MD)+RaceDay.HORSE_MIN_MD;
            horses[i]= new Horses((paddock.IHorses) paddock, (racing_track.IHorse) racing, (stable.IHorses) stable ,i+1);
        }
        
        spectators = new Spectator[nSpectators];
        for(int i=0; i<nSpectators;i++){
            Random rand1 = new Random();
            int mb = rand1.nextInt(RaceDay.SPEC_MAX_BET+1-RaceDay.SPEC_MIN_BET)+RaceDay.SPEC_MIN_BET;
            spectators[i]=new Spectator((betting_centre.ISpectator) betting, (control_centre.ISpectator) control, (paddock.ISpectator) paddock, (racing_track.ISpectator) racing, i+1);
        }
        
        broker.start();
        
        for(int i=0; i<nHorses; i++){
            horses[i].start();
        }
        
        for(int i=0; i<nSpectators; i++){
            spectators[i].start();
        }
        
        try{
            broker.join();
            System.err.println("Broker Quit");
        }catch(InterruptedException ex){
            
        }
        
        for(int i=0; i <nHorses; i++){
            try{
                horses[i].join();
                System.err.println("Horse "+i+" quit");
            }catch(InterruptedException ex){
                
            }
        }
        
        for(int i=0; i <nSpectators; i++){
            try{
                spectators[i].join();
                System.err.println("Spectator "+i+" quit");
            }catch(InterruptedException ex){
                
            }
        }
        
        log.writeEnd();
    }
    
}
