package modelCheckCTL.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class KripkeStructure {

    private List<State> states ;
    private List<Transition> transitions;
    private List<String> atoms;

    public KripkeStructure()throws Exception{
        this.states = new ArrayList<>();
        this.transitions = new ArrayList<>();
        this.atoms = new ArrayList<>();
    }

    public List<State> getStates(){ return this.states;}
    public List<Transition> getTransitions(){ return this.transitions;}

    public KripkeStructure(String definition) throws Exception {
        this();
        String[] sections = definition.trim().split(";");
        if( sections == null ||sections.length != 3){
            throw new Exception("This model does not contain appropriate sections to build the kripke structure !");
        }
        String[] stateNames = sections[0].replace(" ", "").split(",");
        String[] transitionsInfo = sections[1].replace(" ", "").split(",");
        String[] stateAtomStructures = sections[2].split(",");

        //construct states
        for(String stateName : stateNames){
            String name = stateName.trim();
            State state = new State(name);
            if(containsState(state)){
                throw new Exception(state.getStateName() + " : duplicate state");
            }
            states.add(state);
        }

        //construct transition
        for(String transition: transitionsInfo){
            String[] transitionSegment = transition.split(":");
            if( transitionSegment == null ||transitionSegment.length != 2){
                throw new Exception("Transition is not in [transitionName] : [transition] format");
            }

            String transitionName = transitionSegment[0].trim();
            String[] fromAndToStates = transitionSegment[1].split("-");

            if( fromAndToStates == null ||fromAndToStates.length != 2){
                throw new Exception("Transition " + transitionName + ": not in [fromState] - [toState] format ");

            }
            State fromState = new State(fromAndToStates[0].trim());
            State toState = new State(fromAndToStates[1].trim());
            if(!containsState(fromState) || !containsState(toState)){
                throw new Exception("Transition " + transitionName + ": contains invalid state ");
            }
            Transition newTransition = new Transition(transitionName, fromState, toState);
            if(containsTransition(newTransition)){
                throw new Exception("Transition " + transitionName + ": duplicate transition ");
            }
            transitions.add(newTransition);
        }

        //construct atoms
        for (String stateAtomPair :stateAtomStructures ){
            String [] pair = stateAtomPair.trim().split(":");
            if( pair == null || (pair.length != 2 && pair.length != 1)){
                throw new Exception("Definition of atoms is not in [stateName] : [atoms] format");
            }
            String stateNameInPair = pair[0].trim();
            if(!containsState(new State(stateNameInPair))){
                throw new Exception("State " + stateNameInPair + " is invalid");
            }

            if(pair.length == 2){
                String[] atoms = pair[1].trim().split(" ");
                for(String atom: atoms){
                    if(!getState(stateNameInPair).getAtomSet().add(atom)){
                        throw new Exception("State " + stateNameInPair + ": atom " + atom +" is duplicate");
                    }
                }
            }




        }

    }


    private boolean containsState(State state){
        for (State exsitState: states){
            if(exsitState.equals(state)){
                return true;
            }
        }
        return false;
    }

    public boolean containsState(String stateName){
        for (State state: states){
            if(state.getStateName().equals(stateName)){
                return true;
            }
        }
        return false;
    }

    public boolean containsTransition(Transition trans){
        for (Transition existTrans: transitions){
            if(existTrans.equals(trans)){
                return true;
            }
        }
        return false;
    }

    public State getState(String stateName){
        for(State state: states){
            if(state.getStateName().equals(stateName)){
                return state;
            }
        }
        return new State();
    }




}
