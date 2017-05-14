package mx.itesm.acmjscb.metodosnumericosjonayyo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Cramer extends Fragment implements View.OnClickListener{

    //NUEVO
    public static final int ITERACIONES_MAXIMAS = 100;
    //NUEVO
    private  ArrayList<ArrayList<Double>> matrizEntrada;
    //NUEVO
    private static StringBuilder procedimiento = new StringBuilder(1000);

    private EditText entradaFila;
    private TextView filas;
    private Button agregarFila;
    private Button calcularCramer;
    private Toast toast;

    public Cramer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cramer, container, false);
        entradaFila = (EditText) view.findViewById(R.id.txtEntradaFilas);
        filas = (TextView) view.findViewById(R.id.txtFilasMatriz);
        agregarFila = (Button) view.findViewById(R.id.btnAgregarFilas);
        calcularCramer = (Button) view.findViewById(R.id.btnCalcularCramer);
        agregarFila.setOnClickListener(this);
        calcularCramer.setOnClickListener(this);

        matrizEntrada = new ArrayList<ArrayList<Double>>();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAgregarFilas:
                try{
                    agregarColumna(entradaFila.getText().toString());
                    entradaFila.setText("");
                    toast = Toast.makeText(view.getContext(),"Fila Agregada",Toast.LENGTH_LONG);
                    toast.show();
                    imprimirColumnas();
                }
                catch (Exception e){
                    toast = Toast.makeText(view.getContext(),"Error en los datos",Toast.LENGTH_LONG);
                    toast.show();
                }
                break;

            case R.id.btnCalcularCramer:
                try{
                    if (matrizEntrada.get(0).size()-1 == matrizEntrada.size()){
                        calcularCramer(matrizEntrada);
                        filas.setText(procedimiento.toString());
                    }
                    else{
                        toast = Toast.makeText(view.getContext(),"Cuida el número de ecuaciones e incógnitas",Toast.LENGTH_LONG);
                        toast.show();
                        filas.setText("No se puede resolver");
                    }
                    procedimiento = new StringBuilder(1000);
                    matrizEntrada = new ArrayList<ArrayList<Double>>();

                }
                catch (Exception e){
                    toast = Toast.makeText(view.getContext(),"Error inesperado",Toast.LENGTH_LONG);
                    toast.show();
                }
        }
    }

    public void agregarColumna(String valores){
        ArrayList<Double> nuevaCol = new ArrayList<Double>();
        String[] datos = valores.split(",");
        for (int i = 0; i<datos.length; i++){
            nuevaCol.add(Double.parseDouble(datos[i]));
        }
        matrizEntrada.add(nuevaCol);
    }

    public void imprimirColumnas(){
        String res = "La matriz es:\n";
        for(ArrayList<Double> lista: matrizEntrada){
            res += lista.toString();
            res += "\n";
        }
        filas.setText(res);
    }

    public static void calcularCramer( ArrayList<ArrayList<Double>> entrada) {
        ArrayList<ArrayList<Double>> matrizCompleta = entrada;
        procedimiento.append("*** MATRIZ ORIGINAL ***\n");
        imprimirMatriz(matrizCompleta);
        metodoDeCrammer(matrizCompleta);
    }

    private static void imprimirMatriz(ArrayList<ArrayList<Double>> mat) {
        DecimalFormat df = new DecimalFormat("0.000");
        String filaBonita = "";
        for (ArrayList<Double> fila : mat){
            filaBonita = "[";
            for (Double num: fila){
                filaBonita += df.format(num)+"   ";
            }
            filaBonita = filaBonita.substring(0, filaBonita.length()-3);
            filaBonita +="]";
            procedimiento.append(filaBonita+"\n");
            filaBonita = "";

        }
        procedimiento.append("\n");

    }

    private static ArrayList<ArrayList<Double>> matrizIncognitas(ArrayList<ArrayList<Double>> matrizCompleta){
        ArrayList<ArrayList<Double>> matriz = new ArrayList<ArrayList<Double>>();

        for(ArrayList<Double> arreglo: matrizCompleta){
            ArrayList<Double> res = new ArrayList<Double>();
            for(int i = 0; i < arreglo.size()-1; i++){
                res.add(arreglo.get(i));
            }
            matriz.add(res);
        }

        return matriz;
    }

    private static double determinante(ArrayList<ArrayList<Double>> matriz) {

        double[][] arr = new double[matriz.size()][matriz.size()];

        for(int i = 0; i < matriz.size(); i++){
            for(int j = 0; j < matriz.size(); j++){
                arr[i][j] = matriz.get(i).get(j);
            }
        }

        return determinanteUtil(arr);

    }

    private static double determinanteUtil(double[][] arr) {
        double result = 0.0;
        if (arr.length == 1) {
            result = arr[0][0];
            return result;
        }
        if (arr.length == 2) {
            result = arr[0][0] * arr[1][1] - arr[0][1] * arr[1][0];
            return result;
        }
        for (int i = 0; i < arr[0].length; i++) {
            double temp[][] = new double[arr.length - 1][arr[0].length - 1];

            for (int j = 1; j < arr.length; j++) {
                for (int k = 0; k < arr[0].length; k++) {

                    if (k < i) {
                        temp[j - 1][k] = arr[j][k];
                    } else if (k > i) {
                        temp[j - 1][k - 1] = arr[j][k];
                    }
                }
            }
            result += arr[0][i] * Math.pow(-1, (int) i) * determinanteUtil(temp);
        }
        return result;
    }

    private static void metodoDeCrammer(ArrayList<ArrayList<Double>> matrizCompleta){
        ArrayList<Double> resultados = new ArrayList<Double>();
        ArrayList<ArrayList<Double>> matrizIncognitas = matrizIncognitas(matrizCompleta);
        Double determinante = determinante(matrizIncognitas);
        if(determinante == 0.0) {
            procedimiento.append("\n*** EL DETERMINANTE ES IGUAL A CERO ***\n");
        }
        else{
            procedimiento.append("\n*** EL DETERMINANTE ES  ***\n"+determinante+"\n");
            procedimiento.append("\n*** LAS MATRICES RESULTANTES SON ***\n");
            for(int i = 0; i < matrizCompleta.size(); i++){
                matrizIncognitas = new ArrayList<ArrayList<Double>>();
                for(int j = 0; j < matrizCompleta.size(); j++){
                    ArrayList<Double> temp = new ArrayList<Double>();
                    for(int k = 0; k < matrizCompleta.size(); k++){
                        if(k==i)
                            temp.add(matrizCompleta.get(j).get(matrizCompleta.size()));
                        else
                            temp.add(matrizCompleta.get(j).get(k));
                    }
                    matrizIncognitas.add(temp);
                }
                imprimirMatriz(matrizIncognitas);
                resultados.add(determinante(matrizIncognitas)/determinante);
            }
        }

        imprimirIncognitas(resultados);
    }

    private static void imprimirIncognitas(ArrayList<Double> valores){
        String res = "";
        for(Double val: valores){
            res += "X_"+(valores.indexOf(val)+1)+" = "+val + "\n";
        }
        procedimiento.append("\n*** LOS VALORES DE LAS INCÓGNITAS SON:\n"+res+"\n");
    }


}
