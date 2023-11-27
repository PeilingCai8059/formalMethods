package modelCheckCTL.controller;

import modelCheckCTL.model.*;

import java.util.HashSet;
import java.util.Set;


public class CTL_Checker {

    private KripkeStructure ks;
    private String formula;
    private String stateName;

    public CTL_Checker(KripkeStructure ks, String formula, String stateName){
        this.ks = ks;
        this.formula = formula;
        this.stateName = stateName;
    }

    public String getFormula(){return this.formula;}

    //start point to check if the given CTL formula holds in the given kripke structure
    public boolean ifHold(){
        String str = getFormula();
        ExpressionNode formulaRoot = FormulaParser.formulaParser(str);
        return SAT(formulaRoot, this.stateName);
    }

    public boolean SAT(ExpressionNode node, String state_name) {
        String symbol = node.getNodeName();

        //atomic
        if (symbol.equals("p") || symbol.equals("q") || symbol.equals("r")||symbol.equals("t")
                || symbol.equals("n1") || symbol.equals("n2") || symbol.equals("c1") || symbol.equals("c2")
                || symbol.equals("t1") || symbol.equals("t2") || symbol.equals("1") || symbol.equals("2")) {
            Set<String> atomSet = ks.getState(state_name).getAtomSet();
            return atomSet.contains(symbol);
        } else if (symbol.equals("^")) {
            return !SAT(node.getRightExpression(),state_name);
        } else if (symbol.equals("&")){
            return SAT(node.getLeftExpression(),state_name) && SAT(node.getRightExpression(),state_name) ;
        } else if (symbol.equals("|")){
            return SAT(node.getLeftExpression(),state_name) || SAT(node.getRightExpression(),state_name);
        } else if (symbol.equals(">")){
            return !SAT(node.getLeftExpression(),state_name) || SAT(node.getRightExpression(),state_name);
        } else if(symbol.equals("A") && node.getRightExpression().getNodeName().equals("X")){  //AX
            boolean truthValueForAX = true;
            ExpressionNode subFormula = node.getRightExpression().getRightExpression();
            for(Transition transition : ks.getTransitions()){
                if(transition.getFromState().getStateName().equals(stateName)){
                    truthValueForAX = (truthValueForAX && SAT(subFormula, transition.getToState().getStateName()));
                }
                if(!truthValueForAX){
                    break;
                }
            }
            return truthValueForAX;
        } else if(symbol.equals("E") && node.getRightExpression().getNodeName().equals("X")) {  //AX
            boolean truthValueForEX = false;
            ExpressionNode subFormula = node.getRightExpression().getRightExpression();
            for (Transition transition : ks.getTransitions()) {
                if (transition.getFromState().getStateName().equals(stateName)) {
                    truthValueForEX = (truthValueForEX || SAT(subFormula, transition.getToState().getStateName()));
                }
                if (truthValueForEX) {
                    break;
                }
            }
            return truthValueForEX;
        } else if(symbol.equals("A") && node.getRightExpression().getNodeName().equals("F")) {
            ExpressionNode subFormula = node.getRightExpression().getRightExpression();

            // Check if there exists a state that satisfies the subformula
            boolean truthValueForAF = false;

            for (State state : ks.getStates()) {
                boolean satisfiesSubFormula = SAT(subFormula, state.getStateName());

                if (satisfiesSubFormula) {
                    truthValueForAF = true;
                    break;
                }
            }

            return truthValueForAF;
        } else if(symbol.equals("E") && node.getRightExpression().getNodeName().equals("F")) {
            boolean truthValueForEF = false;
            ExpressionNode subFormula = node.getRightExpression().getRightExpression();

            for (State state : ks.getStates()) {
                boolean satisfiesSubFormula = SAT(subFormula, state.getStateName());

                if (satisfiesSubFormula) {
                    truthValueForEF = true;
                    break;
                }
            }

            return truthValueForEF;
        } else if(symbol.equals("A") && node.getRightExpression().getNodeName().equals("G")) {
            boolean truthValueForAG = true;
            ExpressionNode subFormula = node.getRightExpression().getRightExpression();
            for (Transition transition : ks.getTransitions()) {
                if (!SAT(subFormula, transition.getToState().getStateName())) {
                    truthValueForAG = false;
                    break;
                }
            }
            return truthValueForAG;
        } else if(symbol.equals("E") && node.getRightExpression().getNodeName().equals("G")) {
            boolean truthValueForEG = false;
            ExpressionNode subFormula = node.getRightExpression().getRightExpression();

            for (State state : ks.getStates()) {
                boolean satisfiesSubFormula = SAT(subFormula, state.getStateName());

                if (!satisfiesSubFormula) {
                    truthValueForEG = false;
                    break;
                }

                truthValueForEG = true;
            }

            return truthValueForEG;
        } else if(symbol.equals("A") && node.getRightExpression().getNodeName().equals("U")) {
            boolean truthValueForAU = true;
            ExpressionNode subLeftFormula = node.getRightExpression().getLeftExpression();
            ExpressionNode subRightFormula = node.getRightExpression().getRightExpression();

            for (Transition transition : ks.getTransitions()) {
                if (transition.getFromState().getStateName().equals(stateName)) {
                    boolean leftSatisfies = SAT(subLeftFormula, transition.getToState().getStateName());
                    boolean rightSatisfies = SAT(subRightFormula, transition.getToState().getStateName());

                    if (!rightSatisfies) {
                        truthValueForAU = false;
                        break;
                    }
                    if (leftSatisfies) {
                        truthValueForAU = true;
                        break;
                    }
                }
            }

            return truthValueForAU;
        } else if(symbol.equals("E") && node.getRightExpression().getNodeName().equals("U")) {
            boolean truthValueForEU = false;
            ExpressionNode subLeftFormula = node.getRightExpression().getLeftExpression();
            ExpressionNode subRightFormula = node.getRightExpression().getRightExpression();

            // Check if there exists a state that satisfies the subRightFormula
            for (State state : ks.getStates()) {
                boolean rightSatisfies = SAT(subRightFormula, state.getStateName());

                if (rightSatisfies) {
                    truthValueForEU = true;
                    break;
                }
            }

            // Check if there exists a path from the current state satisfying the subLeftFormula until subRightFormula is satisfied
            boolean leftSatisfiesForSomeTransition = false;

            for (Transition transition : ks.getTransitions()) {
                if (transition.getFromState().getStateName().equals(stateName)) {
                    boolean leftSatisfies = SAT(subLeftFormula, transition.getToState().getStateName());
                    boolean rightSatisfies = SAT(subRightFormula, transition.getToState().getStateName());

                    leftSatisfiesForSomeTransition = leftSatisfiesForSomeTransition || (leftSatisfies && rightSatisfies);

                    if (rightSatisfies) {
                        truthValueForEU = true;
                        break;
                    }
                }
            }

            return truthValueForEU || leftSatisfiesForSomeTransition;
        }
        return false;
    }
}