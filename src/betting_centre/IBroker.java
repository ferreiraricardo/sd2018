/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package betting_centre;

/**
 *
 * @author ricar
 */
public interface IBroker {
    
    public void acceptTheBets();//waiting for everyone
    
    public boolean honnourTheBets(int id);//pay everyone
    
}
