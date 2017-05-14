package mx.itesm.acmjscb.metodosnumericosjonayyo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Alejandro on 13/05/2017.
 */

public class GaussSeidelChido {


    //NUEVO
    public static final int ITERACIONES_MAXIMAS = 100;
    private static StringBuilder procedimiento = new StringBuilder(1000);
    private static double[][] matriz;


    public static void main(String[] args){
        ArrayList<ArrayList<Double>> matTemp = ejemplo1();

        // PASAR DE ARRAYLIST A DOUBLE[][]
        Double[][] matriz = new Double[matTemp.size()][matTemp.size()];
        for (int fila = 0; fila < matTemp.size(); fila++) {
            for (int col = 0; col < matTemp.size(); col++) {
                matriz[fila][col] = matTemp.get(fila).get(col);
            }
        }
    }
    //NUEVO
    public void imprimirMatriz(){
        int tamanoFila = matriz.length;
        DecimalFormat df = new DecimalFormat("0.000");
        String filaBonita = "";


        for (int fila = 0; fila < tamanoFila; fila++)
        {
            for (int columna = 0; columna < tamanoFila + 1; columna++) {
                filaBonita+=df.format(matriz[fila][columna])+" ";
            }
            filaBonita+="\n";
        }
        filaBonita = filaBonita.substring(0, filaBonita.length()-1);
        procedimiento.append(filaBonita+"\n");
    }

    //NUEVO
    public  static boolean transformarDominante(int tamanoDom, boolean[] Visitado, int[] Fila){
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
        for (int fila = 0; fila < tamanoFila; fila++) {
            if (Visitado[fila]){
                continue;
            }
            double suma = 0;
            for (int columna = 0; columna < tamanoFila; columna++) {
                suma += Math.abs(matriz[fila][columna]);
            }
            if (2 * Math.abs(matriz[fila][tamanoDom]) > suma) { // diagonally dominant?
                Visitado[fila] = true;
                Fila[tamanoDom] = fila;
                if (transformarDominante(tamanoDom + 1, Visitado, Fila)) {
                    return true;
                }
                Visitado[fila] = false;
            }
        }
        return false;
    }

    //NUEVO
    public static boolean hacerDominante(){
        boolean[] visitado = new boolean[matriz.length];
        int[] filas = new int[matriz.length];

        Arrays.fill(visitado, false);

        return transformarDominante(0, visitado, filas);
    }

    //NUEVO
    public void resolver(){
        int iteraciones = 0;
        int tamanoFila = matriz.length;
        double epsilon = 1e-15;
        double[] aproximacion = new double[tamanoFila]; // Aproximaciones
        double[] previo = new double[tamanoFila]; // Previo
        Arrays.fill(aproximacion, 0);

        while (true) {
            for (int fila = 0; fila < tamanoFila; fila++) {
                double suma = matriz[fila][tamanoFila]; // b_n

                for (int columna = 0; columna < tamanoFila; columna++){
                    if (columna != fila){
                        suma -= matriz[fila][columna] * aproximacion[columna];
                    }
                }
                //Actualiza a la nueva fila
                aproximacion[fila] = 1/matriz[fila][fila] * suma;
            }
            imprimirIteracion(aproximacion, iteraciones);
            iteraciones++;
            if (iteraciones == 1) {
                continue;
            }
            boolean detener = true;
            for (int fila = 0; fila < tamanoFila && detener; fila++){
                if (Math.abs(aproximacion[fila] - previo[fila]) > epsilon) {
                    detener = false;
                }
            }
            if (detener || iteraciones == ITERACIONES_MAXIMAS){
                break;
            }
            previo = (double[])aproximacion.clone();
        }
    }

    //NUEVO
    public static void imprimirIteracion(double[] aproximacion, int iteraciones){
        int tamanoFila = aproximacion.length;
        DecimalFormat df = new DecimalFormat("0.00000");
        String filaBonita = "";
        filaBonita+="Iteracion ";
        filaBonita+=iteraciones+" = {";
        for (int fila = 0; fila < tamanoFila; fila++) {
            filaBonita += df.format(aproximacion[fila])+" ";
        }
        filaBonita+="}";
        filaBonita = filaBonita.substring(0, filaBonita.length()-1);
        procedimiento.append(filaBonita+"\n");
    }

    private static ArrayList<ArrayList<Double>> ejemplo1(){
        ArrayList<ArrayList<Double>> matriz = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> fila = new ArrayList<Double>();
        fila.add(10.0);
        fila.add(0.0);
        fila.add(-1.0);
        fila.add(-1.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(4.0);
        fila.add(12.0);
        fila.add(-4.0);
        fila.add(8.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(4.0);
        fila.add(4.0);
        fila.add(10.0);
        fila.add(4.0);
        matriz.add(fila);
        return matriz;
    }
}
