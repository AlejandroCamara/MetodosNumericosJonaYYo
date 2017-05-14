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


public class GaussSeidel extends Fragment  implements  View.OnClickListener {

    //NUEVO
    public static final int ITERACIONES_MAXIMAS = 100;
    //NUEVO
    private  ArrayList<ArrayList<Double>> matrizEntrada;
    private static double[][] matriz;
    //NUEVO
    private static StringBuilder procedimiento = new StringBuilder(1000);

    private EditText entradaFila;
    private TextView filas;
    private Button agregarFila;
    private Button calcularSeidel;
    private Toast toast;


    public GaussSeidel() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gauss_seidel, container, false);
        entradaFila = (EditText) view.findViewById(R.id.txtEntradaFilas);
        filas = (TextView) view.findViewById(R.id.txtFilasMatriz);
        agregarFila = (Button) view.findViewById(R.id.btnAgregarFilas);
        calcularSeidel = (Button) view.findViewById(R.id.btnCalcularSeidel);
        agregarFila.setOnClickListener(this);
        calcularSeidel.setOnClickListener(this);

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

            case R.id.btnCalcularSeidel:
                try{
                    calcularGaussSeidel(matrizEntrada);
                    filas.setText(procedimiento.toString());
                    procedimiento = new StringBuilder(1000);
                    matrizEntrada = new ArrayList<ArrayList<Double>>();

                }
                catch (Exception e){
                    toast = Toast.makeText(view.getContext(),"Error inesperado",Toast.LENGTH_LONG);
                    toast.show();
                }
        }
    }

    private void agregarColumna(String valores){
        ArrayList<Double> nuevaCol = new ArrayList<Double>();
        String[] datos = valores.split(",");
        for (int i = 0; i<datos.length; i++){
            nuevaCol.add(Double.parseDouble(datos[i]));
        }
        matrizEntrada.add(nuevaCol);
    }

    private void imprimirColumnas(){
        String res = "La matriz es:\n";
        for(ArrayList<Double> lista: matrizEntrada){
            res += lista.toString();
            res += "\n";
        }
        filas.setText(res);
    }


    private static void calcularGaussSeidel( ArrayList<ArrayList<Double>> entrada) {
        int tamanoFila;

        ArrayList<ArrayList<Double>> mat = entrada;
        double[][] matriz = new double[mat.size()][mat.get(0).size()];
        for (int fila = 0; fila<mat.size(); fila++){
            for (int col = 0; col <mat.get(0).size(); col ++){
                matriz[fila][col] = mat.get(fila).get(col);
            }
        }

        if (!hacerDominante(matriz))
        {
            procedimiento.append("\n*** EL SISTEMA NO ES DIAGONALMENTE DOMINANTE ***\n");
        }
        procedimiento.append("\n*** MATRIZ ORIGINAL ***\n");
        imprimirMatriz(matriz);
        resolver(matriz);
        System.out.println(procedimiento.toString());
    }

    private static void imprimirMatriz(double[][] matriz){
        int tamanoFila = matriz.length;
        DecimalFormat df = new DecimalFormat("0.000");
        String filaBonita = "";


        for (int fila = 0; fila < tamanoFila; fila++)
        {
            filaBonita ="[";
            for (int columna = 0; columna < matriz[0].length; columna++) {
                filaBonita+=df.format(matriz[fila][columna])+"   ";
            }
            filaBonita = filaBonita.substring(0, filaBonita.length()-3);
            procedimiento.append(filaBonita+"]\n");

        }
        procedimiento.append("\n");
    }

    private static boolean transformarDominante(double[][] matriz,int tamanoDom, boolean[] Visitado, int[] Fila){
        int tamanoFila = matriz.length;
        if (tamanoDom == matriz.length)
        {
            double[][] temp = new double[tamanoFila][tamanoFila+1];
            for (int fila = 0; fila < Fila.length; fila++)
            {
                for (int columna = 0; columna < tamanoFila + 1; columna++)
                    temp[fila][columna] = matriz[Fila[fila]][columna];
            }

            matriz = temp;

            return true;
        }

        for (int fila = 0; fila < tamanoFila; fila++)
        {
            if (Visitado[fila]) continue;

            double suma = 0;

            for (int columna = 0; columna < tamanoFila; columna++)
                suma += Math.abs(matriz[fila][columna]);

            if (2 * Math.abs(matriz[fila][tamanoDom]) > suma)
            { // diagonally dominant?
                Visitado[fila] = true;
                Fila[tamanoDom] = fila;

                if (transformarDominante(matriz,tamanoDom + 1, Visitado, Fila))
                    return true;

                Visitado[fila] = false;
            }
        }

        return false;
    }

    private static boolean hacerDominante(double[][] matriz){
        boolean[] visitado = new boolean[matriz.length];
        int[] filas = new int[matriz.length];

        Arrays.fill(visitado, false);

        return transformarDominante(matriz,0, visitado, filas);
    }

    private static void resolver(double[][] matriz){
        int iteraciones = 0;
        int tamanoFila = matriz.length;
        double epsilon = 1e-15;
        double[] aproximacion = new double[tamanoFila]; // Aproximaciones
        double[] previo = new double[tamanoFila]; // Previo
        Arrays.fill(aproximacion, 0);
        procedimiento.append("\n*** ITERACIONES ***\n");
        while (true)
        {
            for (int fila = 0; fila < tamanoFila; fila++)
            {
                double suma = matriz[fila][tamanoFila]; // b_n

                for (int columna = 0; columna < tamanoFila; columna++)
                    if (columna != fila)
                        suma -= matriz[fila][columna] * aproximacion[columna];

                //Actualiza a la nueva fila
                aproximacion[fila] = 1/matriz[fila][fila] * suma;
            }

            imprimirIteracion(aproximacion, iteraciones);

            iteraciones++;
            if (iteraciones == 1)
                continue;

            boolean detener = true;
            for (int fila = 0; fila < tamanoFila && detener; fila++)
                if (Math.abs(aproximacion[fila] - previo[fila]) > epsilon)
                    detener = false;

            if (detener || iteraciones == ITERACIONES_MAXIMAS) break;
            previo = (double[])aproximacion.clone();
        }
    }
    //NUEVO
    private static void imprimirIteracion(double[] aproximacion, int iteraciones){
        int tamanoFila = aproximacion.length;
        DecimalFormat df = new DecimalFormat("0.00000000000000000000");
        String filaBonita = "";
        filaBonita+="Iteración ";
        filaBonita+=(iteraciones+1)+" = {";
        for (int fila = 0; fila < tamanoFila; fila++) {
            filaBonita += df.format(aproximacion[fila])+"\n";
        }
        filaBonita = filaBonita.substring(0, filaBonita.length()-1);
        filaBonita+="}";
        procedimiento.append(filaBonita+"\n\n");
    }

}