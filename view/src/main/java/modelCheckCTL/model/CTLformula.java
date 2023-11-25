package modelCheckCTL.model;


import java.util.Set;

public abstract class CTLformula {

    public abstract boolean evaluate(State state, KripkeStructure kripkeStructure);

    public static CTLformula parse(String formula) {
        
        if (formula.matches("[a-zA-Z]+")) {
            return new Proposition(formula);
        }

        throw new IllegalArgumentException("Unsupported CTL formula: " + formula);
    }
}

class Proposition extends CTLformula {
    private final String proposition;

    public Proposition(String proposition) {
        this.proposition = proposition;
    }
    @Override
    public boolean evaluate(State state, KripkeStructure kripkeStructure) {
        Set<String> statePropositions = kripkeStructure.getPropositionsForState(state);
        return statePropositions.contains(proposition);
    }
}

class Not extends CTLformula {
    private final CTLformula operand;

    public Not(CTLformula operand) {
        this.operand = operand;
    }

    @Override
    public boolean evaluate(State state, KripkeStructure kripkeStructure) {
        return !operand.evaluate(state, kripkeStructure);
    }
}

class And extends CTLformula {
    private final CTLformula leftOperand;
    private final CTLformula rightOperand;

    public And(CTLformula leftOperand, CTLformula rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public boolean evaluate(State state, KripkeStructure kripkeStructure) {
        return leftOperand.evaluate(state, kripkeStructure) && rightOperand.evaluate(state, kripkeStructure);
    }
}
    


