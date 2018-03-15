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
public enum SpectatorState {
    WAITING_FOR_A_RACE_TO_START{
        @Override
        public String toString(){
            return "WFARTS";
        }
    },
    APPRAISING_THE_HORSES{
        @Override
        public String toString(){
            return "ATH";
        }
    },
    PLACING_A_BET{
        @Override
        public String toString(){
            return "PAB";
        }
    },
    WATCHING_A_RACE{
        @Override
        public String toString(){
            return "WAR";
        }
    },
    COLLECTING_THE_GAINS{
        @Override
        public String toString(){
            return "CTG";
        }
    },
    CELEBRATING{
        @Override
        public String toString(){
            return "C";
        }
    }
    
}
