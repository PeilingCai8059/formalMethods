package modelCheckCTL.model;

import java.util.*;

public class State{
    //attributes
    private String stateName;
    private Set<String > atomSet;
    private Set<State> immediateStates;

    //methods
    public State() {
        stateName = "";
        atomSet = new HashSet<>();
        immediateStates = new HashSet<>();
    }

    public State(String stateName) {
        this();
        this.stateName = stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName.trim();
    }

    public Set<String> getAtomSet(){
        return atomSet;
    }

    public boolean equals(State state) {
        return getStateName().equals((state.getStateName()));
    }

    public void addImmediateStats(State child){
        immediateStates.add(child);
    }
    public Set<State> getImmediateStates(){
        return this.immediateStates;
    }

}