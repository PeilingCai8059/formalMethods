package modelCheckCTL.model;

public class ExpressionNode {
    String nodeName;
    ExpressionNode leftExpression;
    ExpressionNode rightExpression;

    public ExpressionNode(){
        this.nodeName = "name";
        leftExpression = null;
        rightExpression = null;
    }
    public ExpressionNode(String str){
        this.nodeName = str;
        leftExpression = null;
        rightExpression = null;
    }

    public ExpressionNode(String name, ExpressionNode left, ExpressionNode right){
       this.nodeName = name;
       leftExpression = left;
       rightExpression = right;
    }

    public ExpressionNode getLeftExpression() { return this.leftExpression ;}

    public ExpressionNode getRightExpression() { return this.rightExpression ;}

    public void setLeftExpression(ExpressionNode leftExpression) {
        this.leftExpression = leftExpression;
    }

    public void setRightExpression(ExpressionNode rightExpression) {
        this.rightExpression = rightExpression;
    }

    public String getNodeName() {
        return this.nodeName;
    }
}
