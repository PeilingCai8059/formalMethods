package modelCheckCTL.controller;

import modelCheckCTL.model.ExpressionNode;

import java.util.HashMap;
import java.util.Stack;

public class FormulaParser {

    private static final HashMap<String,Integer> priorityTable = new HashMap<>();
    static {
        priorityTable.put("^", 1);
        priorityTable.put("A", 1);
        priorityTable.put("E", 1);
        priorityTable.put("X", 1);
        priorityTable.put("F", 1);
        priorityTable.put("G", 1);
        priorityTable.put("&", 2);
        priorityTable.put("|", 2);
        priorityTable.put(">", 3);
        priorityTable.put("U", 3);
    }

    public static ExpressionNode formulaParser(String formula){
        String convertedFormula = convertFormula(formula);
        System.out.println(convertedFormula);
        ExpressionNode root = buildFormulaTree(convertedFormula);
        return new ExpressionNode();
    }

    private static String convertFormula(String formula){
        StringBuilder convertedFormula = new StringBuilder(formula);

        //replace not with ^
        convertedFormula = replaceSymbol(convertedFormula,"not","^");

        //replace and with &
        convertedFormula = replaceSymbol(convertedFormula,"and","&");

        //replace or with |
        convertedFormula = replaceSymbol(convertedFormula,"or","|");

        //replace -> with >
        convertedFormula = replaceSymbol(convertedFormula,"->",">");

        return convertedFormula.toString();
    }

    private static  StringBuilder replaceSymbol(StringBuilder str, String symbol, String replacedSymbol){
        int idx = str.indexOf(symbol);
        while(idx != -1){
            str.replace(idx, idx + symbol.length(),replacedSymbol);
            idx = str.indexOf(symbol);
        }
        return str;
    }

    private static ExpressionNode buildFormulaTree(String str) {
        Stack<ExpressionNode> atomic = new Stack<>();
        Stack<ExpressionNode> symbol = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);
            String c = String.valueOf(currentChar);

            if (c.equals("p") || c.equals("q") || c.equals("r") || c.equals("0") || c.equals("1") || c.equals("2")) {
                atomic.push(new ExpressionNode(c));
            } else if (c.equals("n") || c.equals("t") || c.equals("c")) {
                if( i + 1 < str.length() && Character.isDigit(str.charAt(i+1))){
                    atomic.push(new ExpressionNode(c + String.valueOf(str.charAt(i + 1))));
                    i++;
                }else{
                    atomic.push(new ExpressionNode(c));
                }
            } else if (c.equals("A") || c.equals("E") || c.equals("X") || c.equals("F") || c.equals("G")) {
                symbol.push(new ExpressionNode(c));
            } else if (c.equals("&") || c.equals("|")) {
                while(!symbol.isEmpty()
                        && priorityTable.get(symbol.peek().getNodeName()) < priorityTable.get(c)){ //top symbol has higher priority
                    ExpressionNode right = atomic.pop();
                    ExpressionNode op = symbol.pop();
                    op.setRightExpression(right);
                    atomic.push(op);
                }
                symbol.push(new ExpressionNode(c));
            } else if (c.equals("U") || c.equals(">")) {
                while(!symbol.isEmpty()
                        && priorityTable.get(symbol.peek().getNodeName()) < priorityTable.get(c)){ //top symbol has higher priority
                    ExpressionNode right = atomic.pop();
                    ExpressionNode op = symbol.pop();
                    op.setRightExpression(right);
                    if(op.getNodeName().equals("&") ||op.getNodeName().equals("|")){ //binary operator
                        ExpressionNode left = atomic.pop();
                        op.setLeftExpression(left);
                    }
                    atomic.push(op);
                }
                symbol.push(new ExpressionNode(c));
            } else if (c.equals("(")) {
                Stack<String> parenthese = new Stack<>();
                int j = i;
                for(; j< str.length(); j++){
                    String ch = String.valueOf(str.charAt(j));
                    if(ch.equals("(")){
                        parenthese.push("(");
                    } else if (ch.equals(")")) {
                        parenthese.pop();
                    }
                    if(parenthese.isEmpty()) {
                        break;
                    }
                }
                String innerFormula = str.substring(i + 1, j );
                atomic.push(buildFormulaTree(innerFormula));
                i = j;
            } else if (c.equals("^")) {
                int j = i+1;
                boolean reached = false;
                boolean hasParentheses = false;
                Stack<String> parentheses = new Stack<>();
                while(j < str.length() && !reached){
                    String ch = String.valueOf(str.charAt(j));
                    if( ch.equals("(")){
                        parentheses.push(ch);
                        hasParentheses = false;
                    } else if (ch.equals(")")) {
                        parentheses.pop();
                    }
                    if( hasParentheses && parentheses.isEmpty()){
                        reached = true;
                    }
                    if (!hasParentheses && ch.equals(" ") && j > i +1){
                        reached = true;
                    }
                    j++;
                }
                ExpressionNode right = buildFormulaTree(str.substring(i + 1, j));
                ExpressionNode op = new ExpressionNode(c);
                op.setRightExpression(right);
                atomic.push(op);
                i += j;
                if(reached && !hasParentheses){
                    i--;
                }
            }
        }
        while(!symbol.isEmpty() && !atomic.isEmpty()){
            ExpressionNode right = atomic.pop();
            ExpressionNode op = symbol.pop();
            op.setRightExpression(right);
            if(op.getNodeName().equals("&") || op.getNodeName().equals("|") ||op.getNodeName().equals("U") || op.getNodeName().equals(">")){
                ExpressionNode left = atomic.pop();
                op.setLeftExpression(left);
            }
            atomic.push(op);
        }
        return atomic.pop();
    }
}
