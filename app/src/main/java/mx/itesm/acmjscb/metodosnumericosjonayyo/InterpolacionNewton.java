package mx.itesm.acmjscb.metodosnumericosjonayyo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class InterpolacionNewton extends Fragment implements View.OnClickListener {
    private Button agregarPunto;
    private Button calcularPolinomio;
    private Button graficar;
    private EditText puntoXY;
    private TextView salida;
    private ArrayList<ArrayList<Double>> coordenadas;
    private Toast toast;
    private String polinomio;

    public InterpolacionNewton(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interpolacion_newton, container, false);
        agregarPunto = (Button) view.findViewById(R.id.btnAgregarFilas);
        calcularPolinomio = (Button) view.findViewById(R.id.btnCalcularNewton);
        puntoXY = (EditText) view.findViewById(R.id.txtEntradaFilas);
        salida = (TextView) view.findViewById(R.id.txtPuntosNewton);
        graficar = (Button) view.findViewById(R.id.btnGraficar);
        graficar.setOnClickListener(this);
        agregarPunto.setOnClickListener(this);
        calcularPolinomio.setOnClickListener(this);
        coordenadas = new ArrayList<ArrayList<Double>>();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAgregarFilas:
                try{
                    String[] puntos = puntoXY.getText().toString().split(",");
                    agregarValores(puntos);
                    imprimirValores();
                }
                catch (Exception e){
                    toast = Toast.makeText(getActivity(), "Error en los puntos" , Toast.LENGTH_LONG);
                    toast.show();
                    coordenadas.clear();
                }
                puntoXY.setText("");
                break;
            case R.id.btnCalcularNewton:
                try{
                    polinomio = interpolacionNewton(coordenadas);
                    toast = Toast.makeText(getActivity(), "Polinomio Generado" , Toast.LENGTH_LONG);
                    toast.show();
                    imprimirValores();
                    String res = salida.getText().toString();
                    salida.setText(res+"\n"+"*** EL POLINOMIO ES ***\n"+polinomio);
                }
                catch (Exception e){
                    toast = Toast.makeText(getActivity(), "Error inesperado al generar polinomio" , Toast.LENGTH_LONG);
                    toast.show();
                    coordenadas.clear();
                    puntoXY.setText("");
                }

                break;
            case R.id.btnGraficar:
                try{
                    if(polinomio != null)
                        cambiarPantalla();
                    else{
                        toast = Toast.makeText(getActivity(), "No hay función a graficar" , Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                catch (Exception e){
                    toast = Toast.makeText(getActivity(), "Error inesperado al graficar" , Toast.LENGTH_LONG);
                    toast.show();
                    coordenadas.clear();
                    puntoXY.setText("");
                    MenuPrincipal.valoresX.clear();
                    MenuPrincipal.valoresY.clear();
                }

                break;
        }
    }


    private void agregarValores(String[] puntos){
        ArrayList<Double> puntosXY = new ArrayList<Double>(2);
        puntosXY.add(Double.parseDouble(puntos[0]));
        puntosXY.add(Double.parseDouble(puntos[1]));
        coordenadas.add(puntosXY);
        toast = Toast.makeText(getActivity(), "Punto agregado" , Toast.LENGTH_LONG);
        toast.show();
    }

    private void imprimirValores(){
        String res = "Los puntos son:\n";
        int iter = 0;
        for(ArrayList<Double> lista: coordenadas){
            res +="P_"+iter+" {";
            res += lista.get(0) + ", ";
            res += lista.get(1) + "}\n";
            iter++;
        }
        salida.setText(res);
    }

    private ArrayList<ArrayList<Double>> matrizConPuntos(ArrayList<ArrayList<Double>> puntos){
        ArrayList<ArrayList<Double>> matriz = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> linea;
        for(int i = 0; i < puntos.size(); i++){
            linea = new ArrayList<Double>(puntos.size()+1);
            for(int j = 0; j < puntos.size()+1; j++){
                if(j==0 || j == 1)
                    linea.add(puntos.get(i).get(j));
                else
                    linea.add(0.0);
            }
            matriz.add(linea);
        }
        return matriz;
    }

    private ArrayList<Double> multiplos(ArrayList<ArrayList<Double>> matriz){

        double[][] arr = new double[matriz.size()][matriz.size()+1];

        for(int i = 0; i < matriz.size(); i++){
            for(int j = 0; j < matriz.size(); j++){
                arr[i][j] = matriz.get(i).get(j);
            }
        }

        for(int j = 2; j < matriz.size() + 1; j++){
            for(int i = j-1; i < matriz.size(); i++){
                arr[i][j] = (arr[i][j-1]-arr[i-1][j-1])/(arr[i][0]-arr[i-(j-1)][0]);
            }
        }

        ArrayList<Double> multiplos = new ArrayList<Double>();

        for(int i = 0; i < matriz.size(); i++){
            multiplos.add(arr[i][i+1]);
        }

        return multiplos;
    }

    private String polinomio(ArrayList<Double> multiplos, ArrayList<ArrayList<Double>> puntos){
        String polinomio = Double.toString(multiplos.get(0));
        for(int i = 1; i < multiplos.size(); i++){
            polinomio += "+(" + Double.toString(multiplos.get(i))+"*";
            for(int j = 0; j < i; j++){
                polinomio += "(x-("+puntos.get(j).get(0)+"))";
                if(j+1 < i)
                    polinomio += "*";
            }
            polinomio += ")";
        }
        return polinomio;
    }

    private String interpolacionNewton(ArrayList<ArrayList<Double>> puntos){
        ArrayList<ArrayList<Double>> matrizCeros = matrizConPuntos(puntos);
        ArrayList<Double> multiplos = multiplos(matrizCeros);
        return polinomio(multiplos, puntos);
    }

    private void cambiarPantalla(){
        generarPuntos();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.Contenedor, new GraficaInterpolacion(), "Grafica");
        ft.commit();
    }

    private void generarPuntos() {
        ArrayList<Double> nums = new ArrayList<Double>();
        for(ArrayList<Double> parDatos: coordenadas){
            for(Double numero: parDatos){
                nums.add(numero);
            }
        }
        Collections.sort(nums);
        Double inferior = nums.get(0)-1;
        Double superior = nums.get(nums.size()-1)+1;
        MenuPrincipal.valoresX.clear();
        MenuPrincipal.valoresY.clear();
        String operaciones;
        Double resultado;
        for(Double i = inferior; i <= superior; i+=0.1){
            operaciones = polinomio.replaceAll("x", "(" + Double.toString(i) + ")");
            try{
                resultado = eval(operaciones);
                MenuPrincipal.valoresX.add(i);
                MenuPrincipal.valoresY.add(resultado);
                Log.v(Double.toString(i), Double.toString(resultado));
            } catch (Exception e){
                Log.v("Error", "Error");
            }
        }
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

}
