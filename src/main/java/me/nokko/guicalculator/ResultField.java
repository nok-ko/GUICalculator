package me.nokko.guicalculator;

import javafx.scene.control.TextField;
import me.nokko.guicalculator.ResultField.Operation.Type;

//import java.util.ArrayList;
import java.util.Stack;

public class ResultField extends TextField {

    // Currently queued-up operation
    private Operation op;

    // Stack of numbers we will do operations on
    private Stack<Double> stack;
    private StringBuilder number;

    public ResultField() {
        super();
        // Cannot edit the field manually, must be done through our event handlers.
        this.setEditable(false);

        // Init the stack
        this.stack = new Stack<>();
        this.number = new StringBuilder();
        pushCharacter("0");
    }

    public void pushCharacter(String character) {
        System.out.println(number.toString());

        if (number.length() > 0 && number.substring(0,1).equals("0"))
            backspace();

        number.append(character);
        refreshDisplay();
    }

    // remove a character from the end of the current number
    public void backspace() {
        if (number.length() > 0) {
            number.setLength(number.length() - 1);
        }
        refreshDisplay();
    }

    // push the number to the stack
    // essentially, what happens when the user presses Enter
    public void push() {
        stack.push(Double.valueOf(number.toString()));

        number.setLength(0);
        pushCharacter("0");

        refreshDisplay();
    }

    public void pop() {

        if (stack.size() == 1) {
            stack.set(0, 0.0D);
            refreshDisplay();
            return;
        }

        stack.pop();
        refreshDisplay();
    }

    public void setOp(Type type) {
        this.op = new Operation(type);
    }

    public void applyOp() {
        if (op != null) {
            // get the number currently in the field on the stack
            push();
            op.apply(stack);
            op = null;

            // get the result in the field
            number = new StringBuilder(String.valueOf(stack.pop()));
            refreshDisplay();
        }
    }

    // look at the number being entered and update the display
    private void refreshDisplay() {
        System.out.println(stack.toString());
        this.setText(number.toString());
    }

    record Operation(Type type) {
        public void apply(Stack<Double> stack) {
            // TODO: check that the stack is big enough
            double a = stack.pop();
            double b = stack.pop();

            switch (type) {
                case ADD -> stack.push(b + a);
                case SUB -> stack.push(b - a);
                case MULT -> stack.push(b * a);
                case DIV -> stack.push(b / a);

                // unary ops: push the second operand back up
                case SQRT -> {
                    stack.push(b);
                    stack.push(Math.sqrt(a));
                }
                case NEGATE -> {
                    stack.push(b);
                    stack.push(-a);
                }
            }
        }

        enum Type {
            ADD,
            SUB,
            MULT,
            DIV,
            SQRT,
            NEGATE
        }

    }
}
