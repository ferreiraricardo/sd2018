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
public class Horses extends Thread {
    
    private final paddock.IHorses paddock;
    private final racing_track.IHorse racing;
    private final stable.IHorses stable;
    
    private final int id;
    private final int md;
    private HorsesState state;
    
    
    
    public Horses(paddock.IHorses paddock, racing_track.IHorse racing, stable.IHorses stable,int md, int id ){
        this.id=id;
        this.md=md;
        this.paddock=paddock;
        this.racing=racing;
        this.stable=stable;
        state = HorsesState.AT_THE_STABLE;
        
    }
    
    
    @Override
    public void run(){
        boolean raceOver = false;
        boolean hasCrossed = false;
        
        
        while(!raceOver){
            switch(this.state){
                case AT_THE_STABLE:
                    this.stable.proceedToPaddock();
                    this.state=HorsesState.AT_THE_PADDOCK;
                    break;
                case AT_THE_PADDOCK:
                    this.paddock.proceedToStartLine(id);
                    this.state=HorsesState.AT_THE_START_LINE;
                    break;
                case AT_THE_START_LINE:
                    this.racing.makeAMove(id);
                    this.state=HorsesState.RUNNING;
                    break;
                case RUNNING:
                    if(!hasCrossed){
                        this.racing.makeAMove(id);
                        hasCrossed=this.racing.hasFinishLineBeenCrossed(id);
                    }
                    this.state=HorsesState.AT_THE_FINISH_LINE;
                    break;
                case AT_THE_FINISH_LINE:
                    this.racing.proceedToStable(id);
                    break;
            }
        }
    }
       
    
}
