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
        }

        return false;
    }


}