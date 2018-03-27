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
public enum BrokerState {
    
    OPENING_THE_EVENT{
        @Override
        public String toString(){
            return "OTE";
        }
    },
    ANNOUNCING_NEXT_RACE{
        @Override
        public String toString(){
            return "ANR";
        }
    },
    WAITING_FOR_BETS{
        @Override
        public String toString(){
            return "WFB";
        }
    },
    SUPERVISING_THE_RACE{
        @Override
        public String toString(){
            return "STR";
        }
    },
    SETTLING_ACCOUNTS{
        @Override
        public String toString(){
            return "SAC";
        }
    },
    PLAYING_HOST_AT_THE_BAR{
        @Override
        public String toString(){
            return "PHB";
        }
    }
    
}
