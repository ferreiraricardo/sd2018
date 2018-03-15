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
    
    public void StartTheRace(int id);
    
    public int ReportResults(int id);
    
    public boolean areThereAnyWinners(int id);
    
}
