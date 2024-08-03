package com.haridroid.saralcalculator;

import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.ClipData;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnAdd, btnSub, btnMul, btnDiv, btnLeft, btnRight, btnAC, btnDel, btnRoot, btnSquare;
    TextView inputField, outputField, copyEq;
    boolean validOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btnAdd = findViewById(R.id.btnPlus);
        btnSub = findViewById(R.id.btnMinus);
        btnMul = findViewById(R.id.btnMul);
        btnDiv = findViewById(R.id.btnDivide);
        btnAC = findViewById(R.id.btnAC);
        btnLeft = findViewById(R.id.btnOpenBracket);
        btnRight = findViewById(R.id.btnCloseBracket);
        btnDel = findViewById(R.id.btnDelete);
        btnRoot = findViewById(R.id.btnRoot);
        btnSquare = findViewById(R.id.btnSquare);
        inputField = findViewById(R.id.inputSection);
        outputField = findViewById(R.id.resultSection);
        copyEq = findViewById(R.id.copyEqBtn);

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInputField("0");
            }
        });
        btn1.setOnClickListener(v -> updateInputField("1"));
        btn2.setOnClickListener(v -> updateInputField("2"));
        btn3.setOnClickListener(v -> updateInputField("3"));

        btn4.setOnClickListener(v -> updateInputField("4"));
        btn5.setOnClickListener(v -> updateInputField("5"));
        btn6.setOnClickListener(v -> updateInputField("6"));
        btn7.setOnClickListener(v -> updateInputField("7"));
        btn8.setOnClickListener(v -> updateInputField("8"));
        btn9.setOnClickListener(v -> updateInputField("9"));
        btnLeft.setOnClickListener(v -> updateInputField("("));
        btnRight.setOnClickListener(v -> updateInputField(")"));
        btnAdd.setOnClickListener(v -> updateInputField("+"));
        btnMul.setOnClickListener(v -> updateInputField("x"));
        btnSub.setOnClickListener(v -> updateInputField("-"));
        btnDiv.setOnClickListener(v -> updateInputField("/"));
        btnSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInputField("²");
            }
        });
        btnRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInputField("√");
            }
        });


        btnAC.setOnClickListener(v -> {
            inputField.setText("");
            updateOutputField();
        });
        btnDel.setOnClickListener(v -> {
            String r = inputField.getText().toString();
            int len = r.length();
            if (len != 0) {
                r = r.substring(0, len - 1);
                inputField.setText(r);
                updateOutputField();
            }

        });


        //paste exp from clipboard to inputField
        inputField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData allClipItems = clipboard.getPrimaryClip();
                if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    ClipData.Item recent = allClipItems.getItemAt(0);
                    String inputExp = recent.getText().toString();
                    inputField.setText(inputExp);
                    updateOutputField();
                } else {
                    Toast.makeText(MainActivity.this, "Clipboard is either empty or item is not valid arithmetic expression", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //copy only output to clipboard
        outputField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validOutput) {
                    String outputValue = outputField.getText().toString();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData newClip = ClipData.newPlainText("output", outputValue);
                    clipboard.setPrimaryClip(newClip);
                    Toast.makeText(MainActivity.this, "Output copied to clipboard!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //copy equation to clipboard if exp is valid
        copyEq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputValue = inputField.getText().toString();
                String outputValue = outputField.getText().toString();
                String finall = inputValue + " = " + outputValue;
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData newClip = ClipData.newPlainText("eqn", finall);
                clipboard.setPrimaryClip(newClip);
                Toast.makeText(MainActivity.this, "Equation copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateInputField(String s) {
        String r = inputField.getText().toString();
        r = r + s;
        inputField.setText(r);
        updateOutputField();
    }

    private void updateOutputField() {
        String exp = inputField.getText().toString();

        if (!validArithmeticExpCheck(exp)) {
            validOutput = false;
            return;
        } else {
            validOutput = true;
        }
        outputField.setText(Evaluate(infixToPostfix(exp)));
    }

    private String Evaluate(String s) {
        Stack<Integer> operandStack = new Stack<Integer>();

        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) <= '9' && s.charAt(i) >= '0') {
                StringBuilder number = new StringBuilder();
                while (i < s.length() && s.charAt(i) <= '9' && s.charAt(i) >= '0') {
                    number.append(s.charAt(i));
                    i++;
                }
                i--;
                Integer num = Integer.parseInt(number.toString());
                operandStack.push(num);
            }

            if (isBinaryOP(s.charAt(i))) {
                if (s.charAt(i) == '+') {
//                    old weak logic:
//                    int spaceCount=0;
//                    ArrayList<String> Operands= new ArrayList<>();
//                    StringBuilder kk= new StringBuilder();
//
//                    int j=i-1;
//                    int out=0;
//                    while(out==0 && spaceCount<=2){
//
//                        if(s.charAt(j)==' '){
//                            spaceCount++;
//                            j--;
//                        }
//                        else if(isDigit(s.charAt(j))) {
//
//                            while (isDigit(s.charAt(j))) {
//                                kk.append(s.charAt(j));
//                                if(j==0){
//                                    out=1;
//                                    break;
//                                }
//                                j--;
//                            }
//                            kk.reverse();
//                            Operands.add(kk.toString());
//                            kk.setLength(0);
//                        }
//                    }
//                    //yha tak shi hai
//                    j+=2;
//                    int operand1= Integer.parseInt(Operands.get(1));
//                    int operand2 = Integer.parseInt(Operands.get(0));
//                    Toast.makeText(this,Operands.get(1)+ " "+ Operands.get(0),Toast.LENGTH_SHORT).show();
//
//                    int ans= operand1 + operand2;
//                    String replacement= String.valueOf(ans);
//                    int addedLength= replacement.length();
//
//                    if(out!=1){
//                        s= s.substring(0,j-1)+ replacement + s.substring(i+1,s.length()-1);
//                    }
//                    else{
//                        s= replacement + s.substring(i+1,s.length()-1);
//                    }
//
//                    i= j+addedLength-1;

                    //new logic:
                    Integer a = operandStack.peek();
                    operandStack.pop();
                    Integer b = operandStack.peek();
                    operandStack.pop();
                    operandStack.push(a + b);


                } else if (s.charAt(i) == '-') {
                    Integer a = operandStack.peek();
                    operandStack.pop();
                    Integer b = operandStack.peek();
                    operandStack.pop();
                    operandStack.push(b - a);
                } else if (s.charAt(i) == 'x') {
                    //old one
//                    int spaceCount=0;
//                    ArrayList<String> Operands= new ArrayList<>();
//                    StringBuilder kk= new StringBuilder();
//
//                    int j=i-1;
//                    int out=0;
//                    while(out==0 && spaceCount<=2){
//
//                        if(s.charAt(j)==' '){
//                            spaceCount++;
//                            j--;
//                        }
//                        else if(isDigit(s.charAt(j))) {
//
//                            while (isDigit(s.charAt(j))) {
//                                kk.append(s.charAt(j));
//                                if(j==0){
//                                    out=1;
//                                    break;
//                                }
//                                j--;
//                            }
//                            kk.reverse();
//                            Operands.add(kk.toString());
//                            kk.setLength(0);
//                        }
//                    }

//                    //yha tak shi hai
//                    j+=2;
//                    int operand1= Integer.parseInt(Operands.get(1));
//                    int operand2 = Integer.parseInt(Operands.get(0));
//                    Toast.makeText(this,Operands.get(1)+ " "+ Operands.get(0),Toast.LENGTH_SHORT).show();
//
//                    int ans= operand1 * operand2;
//                    String replacement= String.valueOf(ans);
//                    int addedLength= replacement.length();
//
//                    if(out!=1){
//                        s= s.substring(0,j-1)+ replacement + s.substring(i+1,s.length()-1);
//                    }
//                    else{
//                        s= replacement + s.substring(i+1,s.length()-1);
//                    }
//
//                    i= j+addedLength-1;

                    // new
                    Integer a = operandStack.peek();
                    operandStack.pop();
                    Integer b = operandStack.peek();
                    operandStack.pop();
                    operandStack.push(a * b);
                } else {
                    //divide
                    Integer a = operandStack.peek();
                    operandStack.pop();
                    Integer b = operandStack.peek();
                    operandStack.pop();
                    operandStack.push(b / a);
                }
            }
            if (isUnaryOP(s.charAt(i))) {
                if (s.charAt(i) == '²') {
                    Integer a = operandStack.peek();
                    operandStack.pop();
                    operandStack.push(a * a);
                } else {
                    // root
                    Integer a = operandStack.peek();
                    operandStack.pop();
                    Integer b = 1;
                    while (b * b < a) {
                        b++;
                    }
                    if (b * b > a) {
                        // nonSquare under root warning
                        return "Non-Square under root";
                    }
                    operandStack.push(b);
                }
            }

        }
        if (!s.isEmpty()) {
            Integer finalAnswer = operandStack.peek();
            return String.valueOf(finalAnswer);
        } else {
            return "Empty expression";
        }

    }

    private String infixToPostfix(String s) {
        StringBuilder postfixExp = new StringBuilder();
        Stack<Character> opStack = new Stack<>();
        Map<Character, Integer> precedence = new HashMap<>();

        // Adding entries to the map
        precedence.put('-', 1);
        precedence.put('+', 1);
        precedence.put('x', 2);
        precedence.put('/', 2);
        precedence.put('²', 3);
        precedence.put('√', 3);


        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '(') {
                opStack.push(s.charAt(i));
            } else if (s.charAt(i) == ')') {
                while (opStack.peek() != '(') {
                    postfixExp.append(opStack.peek());
                    postfixExp.append(' ');
                    opStack.pop();
                }
                opStack.pop();
            } else if (s.charAt(i) <= '9' && s.charAt(i) >= '0') {
                while (i < s.length() && s.charAt(i) <= '9' && s.charAt(i) >= '0') {
                    postfixExp.append(s.charAt(i));
                    i++;
                }
                postfixExp.append(' ');
                i--;

            } else {
                while (!opStack.empty() && (isBinaryOP(s.charAt(i)) || isUnaryOP(s.charAt(i))) && (isBinaryOP(opStack.peek()) || isUnaryOP(opStack.peek())) && precedence.get(opStack.peek()) >= precedence.get(s.charAt(i))) {
                    postfixExp.append(opStack.peek());
                    postfixExp.append(' ');
                    opStack.pop();
                }
                opStack.push(s.charAt(i));
            }
        }

        while (!opStack.empty()) {
            postfixExp.append(opStack.peek());
            postfixExp.append(' ');
            opStack.pop();
        }

        Toast.makeText(this, postfixExp, Toast.LENGTH_SHORT).show();
        return postfixExp.toString();
    }

    private boolean validArithmeticExpCheck(String s) {
        //string empty check
        if (s.isEmpty()) {
            outputField.setText("Enter something");
            return false;
        }

        //bracket checks
        Stack<Character> stack = new Stack<>();
        for (char it : s.toCharArray()) {
            if (it == '(') {
                stack.push(it);
            } else if (it == ')') {
                if (stack.empty()) {
                    outputField.setText("Complete Brackets correctly");
                    return false;
                }
                stack.pop();
            }

        }
        if (!stack.empty()) {
            outputField.setText("Open Brackets");
            return false;
        }
        //unary operator check
        if (s.length() == 1) {
            //just root
            if (s.charAt(0) == '√') {
                outputField.setText("Root contains nothing");
                return false;
            }
//            no number square
            if (s.charAt(0) == '²') {
                outputField.setText("Square of nothing");
                return false;
            }
        }
        //binary operator at start and end of string
        if (isBinaryOP(s.charAt(0))) {
            outputField.setText("binary operator missing operand");
            return false;
        }
        int len = s.length();
        if (isBinaryOP(s.charAt(len - 1))) {
            outputField.setText("binary operator missing operand");
            return false;
        }
        if (s.charAt(len - 1) == '√') {
            outputField.setText("Root contains nothing");
            return false;
        }

        //square op at begin
        if(s.charAt(0)=='²'){
            outputField.setText("invalid square");
            return false;
        }
        for (int i = 0; i < len - 1; i++) {

            if (isUnaryOP(s.charAt(i))) {
                // consecutive unary- unary operators, unary -binary allowed
                if (isUnaryOP(s.charAt(i + 1))) {
                    outputField.setText("consecutive operators");
                    return false;
                }
                if(s.charAt(i)=='²'){
                    if(!(s.charAt(i+1)=='²'|| isBinaryOP(s.charAt(i+1)))){
                        outputField.setText("not valid arithmetic exp");
                        return false;
                    }
                }


                //root negative......
//                if (s.charAt(i) == '/' && s.charAt(i + 1) == '0') {
//                    outputField.setText("Divide by zero");
//                    return false;
//                }
            }


            if (isBinaryOP(s.charAt(i))) {
                // consecutive binary- binary operators, binary-unary allowed
                if (isBinaryOP(s.charAt(i + 1)) ||(s.charAt(i+1)=='²')) {
                    outputField.setText("consecutive operators");
                    return false;
                }
                //divide by zero
                if (s.charAt(i) == '/' && s.charAt(i + 1) == '0') {
                    outputField.setText("Divide by zero");
                    return false;
                }
            }

            if(s.charAt(i+1)=='√'){
                if(!(isBinaryOP(s.charAt(i))|| s.charAt(i)=='√')){
                    outputField.setText("not valid arithmetic exp");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isBinaryOP(char c) {
        return c == '+' || c == '-' || c == '/' || c == 'x';
    }

    private boolean isUnaryOP(char c) {
        return c == '√' || c == '²';
    }

    private boolean isBracket(char c) {
        return c == ')' || c == '(';
    }

    private boolean isDigit(char c) {
        return c <= '9' && c >= '0';
    }
}