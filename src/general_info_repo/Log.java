/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general_info_repo;

import com.sun.javafx.binding.Logging;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import entities.BrokerState;
import entities.HorsesState;
import entities.SpectatorState;

/**
 *
 * @author ricar
 */
public class Log {
    
    private final RaceDay raceday = RaceDay.getInstance();
    private final File log;
    
    private static PrintWriter pw;
    
    private static Log instance = null;
    
    
    private Log(String filename){
        if(filename.length()==0){
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat date = new SimpleDateFormat("yyyMMddhhmmss");
            filename = "RaceDay_" + date.format(today) + ".log";
        }
        this.log = new File(filename);
    }
    
    static{
        instance = new Log("");
        instance.writeInit();
    }
    
    
    public synchronized static Log getInstance(){
        return instance;
    }
    
    
    private void writeInit(){
        try{
            pw = new PrintWriter(log);
            pw.println("                            AFTERNOON AT THE RACE TRACK - Description of the internal state of the problem");
            
            String head = "MAN/BRK              SPECTATOR/BETTER              HORSE/JOCKEY PAIR at RACE RN";
           
            pw.println(head);
            
            
            head += "  Stat  St0  Am0 St1  Am1 St2  Am2 St3  Am3 RN St0 Len0 St1 Len1 St2 Len2 Sr3 Len3 ";
            pw.println(head);
            
            head +="                                                           Race RN Status  ";
            
            pw.println(head);
            
            head += " RN Dist ";
            
            for(int i=0; i<=3; i++){
                head += "BS"+Integer.toString(i)+"  BA"+Integer.toString(i)+" ";
            }
            
            for(int i=0; i<=3;i++){
                head += " Od"+Integer.toString(i)+" N"+Integer.toString(i)+" Ps"+Integer.toString(i)+" SD"+Integer.toString(i);
            }
            
            pw.println(head);
            
            pw.flush();
            
            
            
 
    }catch(FileNotFoundException ex){
        Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
    }
    
   }
    
    public synchronized void writeEnd(){
        
        pw.println("\nLegend:");
        pw.println("Stat - manager/broker state");
        pw.println("St# - spectator/better state (# - 0 .. 3)");
        pw.println("Am# - spectator/better amount of money she has presently (# - 0 .. 3)");
        pw.println("RN - race number");
        pw.println("St# - horse/jockey pair state in present race (# - 0 .. 3)");
        pw.println("Len# - horse/jockey pair maximum moving length per iteration step in present race (# - 0 .. 3)");
        pw.println("RN - race number");
        pw.println("Dist - race track distance in present race");
        pw.println("BS# - spectator/better bet selection in present race (# - 0 .. 3)");
        pw.println("BA# - spectator/better bet amount in present race (# - 0 .. 3)");
        pw.println("Od# - horse/jockey pair winning probability in present race (# - 0 .. 3)");
        pw.println("N# - horse/jockey pair iteration step number in present race (# - 0 .. 3)");
        pw.println("Ps# - horse/jockey pair track position in present race (# - 0 .. 3)");
        pw.println("SD# - horse/jockey pair standing at the end of present race (# - 0 .. 3)");
                
    }
    
    public synchronized void newRaceDay(){
        this.printStatesLine();
    }
    
    public synchronized void initBrokerState(BrokerState state){
        this.raceday.setBrokerState(state);
    }
    
    public synchronized void setBrokerState(BrokerState state){
        BrokerState tmp = this.raceday.getBrokerState();
        this.raceday.setBrokerState(state);
        if(tmp!=state){
            this.printStatesLine();
        }
    }
    
    public synchronized void initHorses(HorsesState state, int id, int md){
        this.raceday.setHorsesState(id, state);
        this.raceday.setHorsesSpeed(id, md);
    }
    
    public synchronized int getHorsesMaxSpeed(int id){
        return this.raceday.getHorsesSpeed(id);
    }
    
    public synchronized void setHorsesState(HorsesState state, int id){
        HorsesState tmp = this.raceday.getHorsesState(id);
        this.raceday.setHorsesState(id, state);
        if(tmp!=state){
            this.printStatesLine();
        }
    }
    
    public synchronized void initSpectators(SpectatorState state, int id, Double mb){
        this.raceday.setSpectatorWallet(id, mb);
        this.raceday.setSpectatorState(id, state);        
    }
    
    public synchronized void setSpectatorState(SpectatorState state, int id){
        SpectatorState tmp= this.raceday.getSpectatorState(id);
        this.raceday.setSpectatorState(id, state);
        if(tmp!=state){
            this.printStatesLine();
        }    
    }
    
    public synchronized void updateSpectatorWallet(int id, double money){
        this.raceday.setSpectatorWallet(id, money);
    }
    
    public synchronized double getSpecatorWallet(int id){
       return this.raceday.getSpectatorWallet(id);
       
    }
    
    public synchronized void updateSpecBetHorse(int id_spec, int id_horse){
        this.raceday.setSpecBetHorse(id_spec, id_horse);
    }
    
    public synchronized void updateSpecBetMoney(int id, int money){
        this.raceday.setSpecBetMoney(id, money);
    }
    
    public synchronized void updateHorseOdds(int id, double odd){
        this.raceday.setHorsesOdds(id, odd);
    }
    
    public synchronized int getBetHorse(int id){
        return this.raceday.getSpectatorBetHorse(id);
     
    }
    
    public synchronized double getBetMoney(int id){
     return this.raceday.getSpectatorBetMoney(id);
    }
    
    public synchronized void updateHorseDistance(int id, int dist){
        this.raceday.setHorseDistance(id, dist);
    }
    
    public synchronized void updateHorseMoves(int id, int moves){
        this.raceday.setHorseMoves(id, moves);
    }
    
    
    public synchronized int getHorseDistance(int id){
        return this.raceday.getHorseDistance(id);
    }
    
    public synchronized int getHorseMoves(int id){
        return this.raceday.getHorseMoves(id);
    }
    
    public synchronized double getHorseOdds(int id){
        return this.raceday.getHorsesOdds(id);
    }
    
    public synchronized void updateRaceState(int s){
        this.raceday.setRaceState(s);
    }
    
    public synchronized int getRaceState(){
        return this.raceday.getRaceState();
    }
    
    private void printStatesLine(){
        pw.print(this.raceday.getBrokerState());
        pw.print("  ");
        
        for(int i=1; i<=4; i++){
            pw.print(this.raceday.getSpectatorState(i));
            pw.print(" ");
            pw.print(this.raceday.getSpectatorWallet(i));
            pw.print("  ");
        }
        
        pw.print(this.raceday.getRaceNumber());
        pw.print(" ");
        
        for(int i=1; i<=4; i++){
            pw.print(this.raceday.getHorsesState(i));
            pw.print(" ");
            pw.print(this.raceday.getHorsesSpeed(i));
            pw.print(" ");
        }
       
    }
}   
