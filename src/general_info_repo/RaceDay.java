/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general_info_repo;

import entities.HorsesState;
import entities.BrokerState;
import entities.SpectatorState;
import java.util.HashMap;
/**
 *
 * @author ricar
 */
public class RaceDay {
    public static final int N_HORSES=4;
    public static final int N_TRACKS=4;
    public static final int N_SPECTATORS=4;
    public static final int HORSE_MIN_MD = 5;
    public static final int HORSE_MAX_MD = 20;
    public static final int SPEC_WALLET = 1000;
    public static final int SPEC_MAX_BET = 100;
    public static final int SPEC_MIN_BET = 1;
    public static final int RACE_DISTANCE = 100;
    
    private BrokerState broker_state;
    private final HashMap<Integer, HorsesState> horses_state;
    private final HashMap<Integer, SpectatorState> spectator_state;
    private final HashMap<Integer, Integer> horses_position;
    private final HashMap<Integer, Integer> spec_bet_horse;
    private final HashMap<Integer, Double> spec_bet_money;
    private final HashMap<Integer, Integer> horses_speed;
    private final HashMap<Integer, Double> horses_odds;
    
    
    
    private static RaceDay instance = null;
    
    
    private RaceDay(){
        this.horses_state=new HashMap<>();
        this.spectator_state=new HashMap<>();
        this.horses_position=new HashMap<>();
        this.spec_bet_horse=new HashMap<>();
        this.spec_bet_money=new HashMap<>();
        this.horses_speed= new HashMap<>();
        this.horses_odds = new HashMap<>();
    }
    
    
    
    public static RaceDay getInstance(){
        if(instance==null){
            instance=new RaceDay();
        }
        return instance;
    }
    
    public synchronized void setBrokerState(BrokerState state){
        this.broker_state=state;
    }
    
    public synchronized void setHorsesState(int id, HorsesState state){
        if(this.horses_state.containsKey(id)){
            this.horses_state.replace(id, state);
        }else{
            this.horses_state.put(id, state);
        }
    }
    
    public synchronized void setSpectatorState(int id, SpectatorState state){
        if(this.spectator_state.containsKey(id)){
            this.spectator_state.replace(id, state);
        }else{
            this.spectator_state.put(id, state);
        }
        
    }
    
    public synchronized void setHorsesPosition(int id, int pos){
        if(this.horses_position.containsKey(id)){
            this.horses_position.replace(id, pos);
        }else{
            this.horses_position.put(id, pos);
        }
    }
    
    public synchronized void setSpecBetHorse(int id_spec, int id_horse){
        if(this.spec_bet_horse.containsKey(id_spec)){
            this.spec_bet_horse.replace(id_spec, id_horse);
        }else{
            this.spec_bet_horse.put(id_spec, id_horse);
        }
    }
    
    public synchronized void setSpecBetMoney(int id_spec, double money){
        if(this.spec_bet_money.containsKey(id_spec)){
            this.spec_bet_money.replace(id_spec, money);
        }else{
            this.spec_bet_money.put(id_spec, money);
        }
    }
    
    public synchronized void setHorsesSpeed(int id, int speed){
        if(this.horses_speed.containsKey(id)){
            this.horses_speed.replace(id, speed);
        }else{
            this.horses_speed.put(id, speed);
        }
    }
    
    public synchronized void setHorsesOdds(int id, double odd){
        if(this.horses_odds.containsKey(id)){
            this.horses_odds.replace(id, odd);
        }else{
            this.horses_odds.put(id, odd);
        }
        
    }
    
    public synchronized BrokerState getBrokerState(){
        return broker_state;
    }
    
    public synchronized HorsesState getHorsesState(int id){
        return horses_state.get(id);
    }
    
    public synchronized SpectatorState getSpectatorState(int id){
        return spectator_state.get(id);
    }
    
    public synchronized int getHorsesPosition(int id){
        return horses_position.get(id);
    }
    
    public synchronized int getSpectatorBetHorse(int id_spec){
        return spec_bet_horse.get(id_spec);
    }
    
    public synchronized double getSpectatorBetMoney(int id_spec){
        return spec_bet_money.get(id_spec);
    }
    
    public synchronized int getHorsesSpeed(int id){
        return horses_speed.get(id);
    }
    
    public synchronized Double getHorsesOdds(int id){
        return horses_odds.get(id);
    }
}
