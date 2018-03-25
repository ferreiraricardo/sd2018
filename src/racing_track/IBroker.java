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
public interface IBroker {
    
    public void StartTheRace();
    
    public int[] ReportResults();
    
    public boolean areThereAnyWinners();
    
}
