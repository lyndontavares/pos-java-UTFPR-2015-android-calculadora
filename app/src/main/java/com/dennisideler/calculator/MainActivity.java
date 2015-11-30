package com.dennisideler.calculator;

import java.text.DecimalFormat;
import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public static final String SOM = "\u002B"; //soma
	public static final String SUB = "\u2212"; //subtracao
	public static final String DIV = "\u00F7"; //divisao
	public static final String MUL = "\u2715"; //multiplicacao
	public String value = "";
	public LinkedList<String> operadores = new LinkedList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    
    public void registerKey(View view)
    {
    	switch(view.getId())
    	{
    	case R.id.button0:   		adicionaOperando("0");    	break;
    	case R.id.button1:    		adicionaOperando("1");    	break;
    	case R.id.button2:    		adicionaOperando("2");    	break;
    	case R.id.button3:    		adicionaOperando("3");    	break;
    	case R.id.button4:    		adicionaOperando("4");    	break;
    	case R.id.button5:    		adicionaOperando("5");    	break;
    	case R.id.button6:    		adicionaOperando("6");    	break;
    	case R.id.button7:    		adicionaOperando("7");    	break;
    	case R.id.button8:    		adicionaOperando("8");    	break;
    	case R.id.button9:    		adicionaOperando("9");    	break;
    	case R.id.buttonAdd:   		adicionaOperador(SOM);    	break;
    	case R.id.buttonSub:  		adicionaOperador(SUB);    	break;
    	case R.id.buttonDiv:  		adicionaOperador(DIV);    	break;
    	case R.id.buttonMul:  		adicionaOperador(MUL);    	break;
    	case R.id.buttonDel:  		deleteFromLeft();    		break;
    	}
    	display();
    }
    
    private void display()
    {
    	TextView tvAns = (TextView) findViewById(R.id.textViewAns);
    	tvAns.setText(value);
    }
    
    private void display(String s)
    {
    	TextView tvAns = (TextView) findViewById(R.id.textViewAns);
    	tvAns.setText(s);
    }
    
    private void adicionaOperando(String op)
    {
    	int operator_idx = findLastOperator();
    	// resolve duplo zeros...
    	if (operator_idx != value.length()-1 && value.charAt(operator_idx+1) == '0')
    		deleteTrailingZero(); 
    	value += op;
    }
    
    private void adicionaOperador(String op)
    {
    	if (endsWithOperator())  // evita operador dobrado
		{
			deleteFromLeft();
			value += op;
			operadores.add(op);
		}
		else if (endsWithNumber())  // adiciona operador
		{
			value += op;
			operadores.add(op);
		}
    }
    
    private void deleteTrailingZero()
    {
    	if (value.endsWith("0")) deleteFromLeft();
    }
    
    private void deleteFromLeft()
    {
    	if (value.length() > 0)
    	{
    		if (endsWithOperator()) operadores.removeLast();
    		value = value.substring(0, value.length()-1);
    	}
    }
    
    private boolean endsWithNumber()
    {
    	return value.length() > 0 && Character.isDigit(value.charAt(value.length()-1));
    }
     
    private boolean endsWithOperator()
    {
    	if (value.endsWith(SOM) || value.endsWith(SUB) || value.endsWith(MUL) || value.endsWith(DIV)) return true;
    	else return false;
    }
    
    private int findLastOperator()
    {
    	int add_idx = value.lastIndexOf(SOM);
    	int sub_idx = value.lastIndexOf(SUB);
    	int mul_idx = value.lastIndexOf(MUL);
    	int div_idx = value.lastIndexOf(DIV);
    	return Math.max(add_idx, Math.max(sub_idx, Math.max(mul_idx, div_idx)));
    }
    
    public void calculate(View view)  // resultado
    {
    	if (operadores.isEmpty()) return;  // sem operador
    	if (endsWithOperator())
    	{
    		display("formato incorreto");
    		resetCalculator();
    		return;
    	}
    		
    	String[] operadores = value.split("\\u002B|\\u2212|\\u00F7|\\u2715");

    	int i = 0;
    	double ans = Double.parseDouble(operadores[i]);
    	for (String operator : this.operadores)
    		ans = applyOperation(operator, ans, Double.parseDouble(operadores[++i]));

    	DecimalFormat df = new DecimalFormat("0.###");
    	display(df.format(ans));
    	resetCalculator();
    }
    
    private double applyOperation(String operador, double operando1, double operando2)
    {
    	if (operador.equals(SOM)) return operando1 + operando2;
    	if (operador.equals(SUB)) return operando1 - operando2;
    	if (operador.equals(MUL)) return operando1 * operando2;
    	if (operador.equals(DIV)) return operando1 / operando2;
    	return 0.0;
    }
    
    private void resetCalculator()
    {
    	value = "";
    	operadores.clear();
    }
}
