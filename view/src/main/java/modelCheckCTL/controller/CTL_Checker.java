package modelCheckCTL.controller;

import modelCheckCTL.model.*;

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


        return SAT(formula.trim());
    }

    //parse formula
    //public expressionNode formulaParser()

    public boolean SAT(String formula){

        //atomic
        if(formula.equals("p")||formula.equals("q")||formula.equals("r")){
            Set<String> atomSet = ks.getState(stateName).getAtomSet();
            if(atomSet.contains(formula)){
                return true;
            }
            return false;
        }

        //not
        if(formula.startsWith("not")){
            return !SAT(formula.substring(4));
        }

        // and

        // or

        // ->

        // EX
        // AX
        return true;
    }






}
