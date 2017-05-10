package mx.itesm.acmjscb.metodosnumericosjonayyo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Biseccion extends Fragment implements View.OnClickListener{
    //TODO Problemas con los exponentes en la evaluación

    private Button upButton;
    private EditText funcion;
    private EditText puntoInicial;
    private EditText puntoFinal;
    private EditText error;
    private TextView resultados;
    private Toast toast;

    public Biseccion() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_biseccion, container, false);
        upButton = (Button) view.findViewById(R.id.Aceptar);
        funcion = (EditText) view.findViewById(R.id.Funcion);
        puntoInicial = (EditText) view.findViewById(R.id.PuntoInicial);
        puntoFinal = (EditText) view.findViewById(R.id.PuntoFinal);
        error = (EditText) view.findViewById(R.id.Error);
        resultados = (TextView) view.findViewById(R.id.Resultados);
        upButton.setOnClickListener(this);
        return view;
    }

    private double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    private double biseccion(String funcion, double puntoInicial, double puntoFinal, double error){
        String sustitucionMitad, sustitucionInicial;
        double mitad = (puntoInicial + puntoFinal)/2;
        while((Math.abs((mitad-puntoInicial)/mitad)*100)>error){
            try {
                sustitucionMitad = funcion.replaceAll("x", "(" + Double.toString(mitad) + ")");

                if (eval(sustitucionMitad) == 0.0)
                    break;

                sustitucionInicial = funcion.replaceAll("x", "(" + Double.toString(puntoInicial) + ")");
                if ((eval(sustitucionMitad) * eval(sustitucionInicial)) < 0) {
                    puntoFinal = mitad;
                } else {
                    puntoInicial = mitad;
                }

                mitad = (puntoInicial + puntoFinal) / 2;
            }catch (Exception e){
                return mitad;
            }
        }
        return mitad;
    }

    private ArrayList<Double> intervalos(String funcion, double puntoInicial, double puntoFinal, double paso){
        ArrayList<Double> intervalos = new ArrayList<Double>();
        String sustitucionInicial, sustitucionFinal;
        for(double i = puntoInicial; i < puntoFinal; i+=paso){
            sustitucionInicial = funcion.replaceAll("x", "("+Double.toString(i)+")");
            sustitucionFinal = funcion.replaceAll("x", "("+Double.toString(i+paso)+")");
            if((eval(sustitucionInicial)*eval(sustitucionFinal)) < 0 || eval(sustitucionInicial) == 0.0){
                intervalos.add(i);
                intervalos.add(i+paso);
            }
        }
        return intervalos;
    }

    private String biseccionCompleta(String funcion, double puntoInicial, double puntoFinal, double error){
        funcion = funcion.replace("pi", "(3.1416)");
        funcion = funcion.replace("e", "(2.71828)");
        String raices = "";
        ArrayList<Double> intervalos = intervalos(funcion, puntoInicial, puntoFinal, 0.3);
        for(int i = 0, count = 1; i < intervalos.size(); i+=2, count++){
            raices += "Raíz "+count+": "+Double.toString(biseccion(funcion, intervalos.get(i), intervalos.get(i+1), error))
                    +"\n";
        }
        return raices;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Aceptar:
                if(validar(funcion.getText().toString())) {
                    String res = biseccionCompleta(funcion.getText().toString(), Double.parseDouble(puntoInicial.getText().toString()),
                            Double.parseDouble(puntoFinal.getText().toString()), Double.parseDouble(error.getText().toString()));
                    toast = Toast.makeText(getActivity(), "Raíces obtenidas", Toast.LENGTH_LONG);
                    toast.show();
                    resultados.setText(res);
                }
                break;
        }
    }

    private boolean validar(String funcion){
        try{
            funcion = funcion.replace("x", "(-2)");
            funcion = funcion.replace("pi", "(3.1416)");
            funcion = funcion.replace("e", "(2.71828)");
            eval(funcion);
            return true;
        } catch(Exception e){
            toast = Toast.makeText(getActivity(), "Verificar escritura de la función", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
    }
}
