/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paddock;

/**
 *
 * @author ricar
 */
public interface ISpectator {
    
    public void waitForNextRace(int id);
    
    public int goCheckHorses(int id);
}
