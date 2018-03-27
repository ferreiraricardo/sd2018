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
public enum HorsesState {
    AT_THE_STABLE{
        @Override
        public String toString(){
            return "ATS";
        }
    },
    AT_THE_PADDOCK{
        @Override
        public String toString(){
            return "ATP";
        }
    },
    AT_THE_START_LINE{
        @Override
        public String toString(){
            return "ASL";
        }
    },
    RUNNING{
        @Override
        public String toString(){
           return "RUN";
        }
    },
    AT_THE_FINISH_LINE{
        @Override
        public String toString(){
            return "AFL";
        }
    }
    
}
