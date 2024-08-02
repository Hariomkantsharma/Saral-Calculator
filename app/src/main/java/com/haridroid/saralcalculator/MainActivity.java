package com.haridroid.saralcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnAdd, btnSub, btnMul,btnDiv, btnLeft, btnRight, btnAC, btnDel , btnRoot, btnSquare;
    TextView inputField, outputField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn0= findViewById(R.id.btn0);
        btn1= findViewById(R.id.btn1);
        btn2= findViewById(R.id.btn2);
        btn3= findViewById(R.id.btn3);
        btn4= findViewById(R.id.btn4);
        btn5= findViewById(R.id.btn5);
        btn6= findViewById(R.id.btn6);
        btn7= findViewById(R.id.btn7);
        btn8= findViewById(R.id.btn8);
        btn9= findViewById(R.id.btn9);
        btnAdd= findViewById(R.id.btnPlus);
        btnSub= findViewById(R.id.btnMinus);
        btnMul= findViewById(R.id.btnMul);
        btnDiv= findViewById(R.id.btnDivide);
        btnAC= findViewById(R.id.btnAC);
        btnLeft= findViewById(R.id.btnOpenBracket);
        btnRight= findViewById(R.id.btnCloseBracket);
        btnDel= findViewById(R.id.btnDelete);
        btnRoot= findViewById(R.id.btnRoot);
        btnSquare= findViewById(R.id.btnSquare);
        inputField= findViewById(R.id.inputSection  );
        outputField= findViewById(R.id.resultSection);

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFields("0");
            }
        });
        btn1.setOnClickListener(v -> updateFields("1"));
        btn2.setOnClickListener(v -> updateFields("2"));
        btn3.setOnClickListener(v -> updateFields("3"));

        btn4.setOnClickListener(v -> updateFields("4"));
        btn5.setOnClickListener(v -> updateFields("5"));
        btn6.setOnClickListener(v -> updateFields("6"));
        btn7.setOnClickListener(v -> updateFields("7"));
        btn8.setOnClickListener(v -> updateFields("8"));
        btn9.setOnClickListener(v -> updateFields("9"));
        btnLeft.setOnClickListener(v -> updateFields("("));
        btnRight.setOnClickListener(v -> updateFields(")"));
        btnAdd.setOnClickListener(v -> updateFields("+"));
        btnMul.setOnClickListener(v -> updateFields("x"));
        btnSub.setOnClickListener(v -> updateFields("-"));
        btnDiv.setOnClickListener(v -> updateFields("/"));
        btnSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFields("²");
            }
        });
        btnRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFields("√");
            }
        });

        btnAC.setOnClickListener(v -> {
            inputField.setText("");
            showFields();
        });
        btnDel.setOnClickListener(v -> {
            String r= inputField.getText().toString();
            int len= r.length();
            if(len !=0){
                r= r.substring(0,len-1);
                inputField.setText(r);
                showFields();
            }

        });

    }
    private void updateFields(String s){
        String r= inputField.getText().toString();
        r=r+s;
        inputField.setText(r);
        showFields();
    }
    private void showFields(){
        String exp= inputField.getText().toString();

        if(!validArithmeticExp(exp)){
            return;
        }
        outputField.setText(Evaluate(infixToPostfix(exp)));
    }
    private String Evaluate(String s){
        Stack<Integer> operandStack= new Stack<Integer>();

        for (int i=0; i<s.length(); i++){

            if (s.charAt(i)<='9'&& s.charAt(i)>='0') {
                StringBuilder number= new StringBuilder();
                while(i<s.length()&& s.charAt(i)<='9'&& s.charAt(i)>='0') {
                    number.append(s.charAt(i));
                    i++;
                }
                i--;
                Integer num= Integer.parseInt(number.toString());
                operandStack.push(num);
            }

            if(isOp(s.charAt(i))){
                if(s.charAt(i)=='+'){
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
                    Integer a= operandStack.peek();
                    operandStack.pop();
                    Integer b= operandStack.peek();
                    operandStack.pop();
                    operandStack.push(a+b);


                }
                else if(s.charAt(i)== '-'){
                    Integer a= operandStack.peek();
                    operandStack.pop();
                    Integer b= operandStack.peek();
                    operandStack.pop();
                    operandStack.push(b-a);
                }
                else if(s.charAt(i)== 'x'){
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
                    Integer a= operandStack.peek();
                    operandStack.pop();
                    Integer b= operandStack.peek();
                    operandStack.pop();
                    operandStack.push(a*b);
                }
                else if(s.charAt(i)== '/') {
                    Integer a= operandStack.peek();
                    operandStack.pop();
                    Integer b= operandStack.peek();
                    operandStack.pop();
                    operandStack.push(b/a);
                }
                else if(s.charAt(i)== '²'){
                    Integer a= operandStack.peek();
                    operandStack.pop();
                    operandStack.push(a*a);
                    }
                else{
                    // root
                    Integer a= operandStack.peek();
                    operandStack.pop();
                    Integer b= 1;
                    while (b*b<a) {
                        b++;
                    }
                    if(b*b>a) {
                        // nonSquare under root warning
                        return "Non-Square under root";
                    }
                    operandStack.push(b);
                    }
                }


            }

        Integer finalAnswer= operandStack.peek();
        return String.valueOf(finalAnswer);
    }
    private String infixToPostfix(String s){
        StringBuilder postfixExp = new StringBuilder();
        Stack<Character> opStack= new Stack<>();
        Map<Character, Integer> precedence = new HashMap<>();

        // Adding entries to the map
        precedence.put('-', 1);
        precedence.put('+', 1);
        precedence.put('x', 2);
        precedence.put('/', 2);
        precedence.put('²', 3);
        precedence.put('√', 3);


        for(int i=0;i<s.length(); i++){

            if(s.charAt(i)=='('){
                opStack.push(s.charAt(i));
            }
            else if(s.charAt(i)==')'){
                while(opStack.peek()!= '('){
                    postfixExp.append(opStack.peek());
                    postfixExp.append(' ');
                    opStack.pop();
                }
                opStack.pop();
            }
            else if (s.charAt(i)<='9'&& s.charAt(i)>='0') {
                while(i<s.length()&& s.charAt(i)<='9'&& s.charAt(i)>='0') {
                    postfixExp.append(s.charAt(i));
                    i++;
                }
                postfixExp.append(' ');
                i--;

            }
            else {
                if(s.charAt(i)== '²' || s.charAt(i)== '√'){
                    postfixExp.append(s.charAt(i));
                    postfixExp.append(' ');
                    continue;
                }

                    while (!opStack.empty() && isOp(s.charAt(i)) && precedence.get(opStack.peek()) > precedence.get(s.charAt(i)) ){
                        postfixExp.append(opStack.peek());
                        postfixExp.append(' ');
                        opStack.pop();
                }
                opStack.push(s.charAt(i));
            }
        }

        while(!opStack.empty()){
            postfixExp.append(opStack.peek());
            postfixExp.append(' ');
            opStack.pop();
        }

        return postfixExp.toString();
    }
    private boolean validArithmeticExp(String s){
        if(s.isEmpty()){
            outputField.setText("Enter something");
            return false;
        }

//        Stack<Character> stack= new Stack<>();
//        for (char it: s.toCharArray()){
//            if(it== '('){
//                stack.push(it);
//            }
//            else if(it== ')' ){
//                if(stack.empty()){
//                    outputField.setText("Complete Brackets correctly");
//                    return false;
//                }
//                stack.pop();
//            }
//
//        }
//        if(stack.empty()){
//            outputField.setText("Missing Brackets");
//            return false;
//        }

        if(isOp(s.charAt(0)) && s.charAt(0)!= '²' && s.charAt(0)!= '√'){
            outputField.setText("operator missing opearand");
            return false;
        }
        int len= s.length();
        if(isOp(s.charAt(len-1)) && s.charAt(len-1)!= '²' && s.charAt(len-1)!= '√'){
            outputField.setText("operator missing opearand");
            return false;
        }

        for (int i=1; i<len-1; i++){

            if(isOp(s.charAt(i)) && s.charAt(i)!= '²' && s.charAt(i)!= '√'){
                if(isOp(s.charAt(i - 1)) || isOp(s.charAt(i + 1))){
                    outputField.setText("Operator after operator");
                    return false;
                }
                if(s.charAt(i) == '/' && s.charAt(i + 1) == '0'){
                    outputField.setText("Divide by zero");
                    return false;
                }
            }
        }
        for (int i=1; i<len-1; i++){

            if(isBracket(s.charAt(i))){
                if(isBracket(s.charAt(i - 1)) || isBracket(s.charAt(i + 1))){

                    outputField.setText("Bracket after braket");
                    return false;
                }
            }
        }


return true;
    }
    private boolean isOp(char c){
        return c == '+' || c == '-' || c == '/' || c == 'x'||c =='√'|| c== '²';
    }
    private boolean isBracket(char c){
        return c == ')' || c == '(';
    }
    private boolean isDigit(char c){
        return c<='9'&& c>='0';
    }
}