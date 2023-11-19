package modelCheckCTL.model;

public class Transition {

    private String transitionName;
    private State fromState;
    private State toState;

    public Transition(){}

    public Transition(State fromState, State toState){
        this.transitionName = "";
        this.fromState = fromState;
        this.toState = toState;
    }
    public Transition(String transitionName, State fromState, State toState){
        this(fromState, toState);
        this.transitionName = transitionName;
    }

    public String getTransitionName(){
        return this.transitionName;
    }
    public State getFromState(){ return this.fromState;};
    public State getToState(){ return this.toState;}

    public boolean equals(Transition trans){
        boolean nameEqual = (this.transitionName.equals(trans.getTransitionName()));
        boolean fromStateEqual = this.fromState.equals(trans.getFromState());
        boolean toStateEqual = this.toState.equals(trans.getToState());
        return (nameEqual || (fromStateEqual && toStateEqual));
    }

}
