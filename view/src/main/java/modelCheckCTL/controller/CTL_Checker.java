package modelCheckCTL.controller;

import modelCheckCTL.model.*;

import java.util.HashMap;
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

    public boolean SAT(ExpressionNode node, String sn) {
        String name = node.getNodeName();

        //atomic
        if (name.equals("p") || name.equals("q") || name.equals("r")||name.equals("t")
                || name.equals("n1") || name.equals("n2") || name.equals("c1") || name.equals("c2")
                || name.equals("t1") || name.equals("t2") || name.equals("1") || name.equals("2")) {
            Set<String> atomSet = ks.getState(sn).getAtomSet();
            if (atomSet.contains(name)) {
                return true;
            }
            return false;
        } else if (name.equals("^")) {
            return !SAT(node.getRightExpression(),sn);
        } else if (name.equals("&")){
            return SAT(node.getLeftExpression(),sn) && SAT(node.getRightExpression(),sn) ;
        } else if (name.equals("|")){
            return SAT(node.getLeftExpression(),sn) || SAT(node.getRightExpression(),sn);
        } else if (name.equals(">")){
            boolean left= !SAT(node.getLeftExpression(),sn);
            boolean right = SAT(node.getRightExpression(),sn);
            return !SAT(node.getLeftExpression(),sn) || SAT(node.getRightExpression(),sn);
        } else if(name.equals("A") && node.getRightExpression().getNodeName().equals("X")){  //AX
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
        } else if(name.equals("E") && node.getRightExpression().getNodeName().equals("X")) {  //AX
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
        }

        return false;
    }

    public HashSet<State> getNextStates(String stateName){
        HashSet<State> nextStates = new HashSet<>();
        for(Transition transition: ks.getTransitions()){
            if(transition.getFromState().equals(stateName)){
                nextStates.add(transition.getToState());
            }
        }
        return nextStates;
    }


}