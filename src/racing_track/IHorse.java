/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package racing_track;

/**
 *
 * @author ricar
 */
public interface IHorse {
    
    public void makeAMove(int id);
    
    
    public boolean hasFinishLineBeenCrossed(int id);
    
    
    public void proceedToStable(int id);
    
    public void proceedToStartLine(int id);
    
}
