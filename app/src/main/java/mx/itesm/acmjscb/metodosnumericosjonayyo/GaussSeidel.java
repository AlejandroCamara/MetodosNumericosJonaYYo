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
                    toast = Toast.makeText(view.getContext(),"wololo",Toast.LENGTH_LONG);
                    toast.show();
                    imprimirColumnas();
                }
                catch (Exception e){
                    toast = Toast.makeText(view.getContext(),"Error en los datos\n"+e.getMessage(),Toast.LENGTH_LONG);
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
                    toast = Toast.makeText(view.getContext(),"Error inesperado\n"+e.getMessage(),Toast.LENGTH_LONG);
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


//
//    private void imprimirValores(){
//        String res = "";
//        for(ArrayList<Float> lista: ecuaciones){
//            for(Float val: lista){
//                res += val + ",";
//            }
//            res += "\n";
//        }
//        coeficientes.setText(res);
//    }
//
//    private void eliminarValores(){
//        if(ecuaciones.size() > 0) {
//            numIncog++;
//            ecuaciones.remove(ecuaciones.size() - 1);
//            toast = Toast.makeText(getActivity(), "Valores eliminados", Toast.LENGTH_LONG);
//            toast.show();
//            faltantes.setText("Ecuaciones faltantes: "+numIncog);
//        }
//    }
//
//    private void leerEntrada(String valores) throws Exception{
//        ArrayList<Float> coef = new ArrayList<Float>();
//        String[] valoresSimples = valores.split(",");
//        for(int i = 0; i < valoresSimples.length; i++){
//            coef.add(Float.parseFloat(valoresSimples[i]));
//        }
//        toast = Toast.makeText(getActivity(), "Valores agregados" , Toast.LENGTH_LONG);
//        toast.show();
//        if (ecuaciones.size() == 0){
//            numCoef = coef.size();
//            ecuaciones.add(coef);
//            numIncog = ecuaciones.get(0).size()-2;
//        }
//        else if(coef.size() != numCoef){
//            toast = Toast.makeText(getActivity(), "Número de coeficientes erróneo" , Toast.LENGTH_LONG);
//            toast.show();
//        } else{
//            ecuaciones.add(coef);
//            numIncog--;
//        }
//        faltantes.setText("Ecuaciones faltantes: "+numIncog);
//    }
//
//    public boolean lecturaNumeros(String puntos){
//        //Tiene que ser uno menos a la longitud de uno de las líneas
//        String[] numerostext = puntos.split(",");
//        int nums = numCoef - 1;
//        if(numerostext.length == (nums)){
//            for(int i = 0; i < numerostext.length; i++) {
//                try {
//                    arregloNumeros.add(Float.parseFloat(numerostext[i]));
//                }catch (Exception e){
//                    return false;
//                }
//            }
//            return true;
//        } else{
//            return false;
//        }
//    }
//
//    public void matrizALista(){
//        for(ArrayList<Float> linea: ecuaciones){
//            for(Float num: linea){
//                arregloMatriz.add(num);
//            }
//        }
//    }
//
//    public void realizarGaussSeidel(float error){
//        matrizALista();
//        ArrayList<Float> arregloM = arregloMatriz;
//        int tamano = numCoef-1;
//        ArrayList<Float> arregloL = arregloNumeros;
//        Float[][] M = new Float[tamano][tamano+1];
//        float[] X = new float[tamano];
//        int y = 0;
//        int x = 0;
//
//        for (int i = 1; i <= arregloM.size(); i++) {
//            M[x][y] = arregloM.get(i-1);
//            if (i % (tamano+1) == 0) {
//                x++;
//                y = 0;
//            }else{
//                y++;
//            }
//        }
//
//        for (int i = 0; i < arregloL.size(); i++) {
//            X[i] =  arregloL.get(i);
//        }
//
//
//        if (!makeDominant(M)) {
//            toast = Toast.makeText(getActivity(), "Matriz sin diagonal dominante", Toast.LENGTH_LONG);
//            toast.show();
//            resultadosSeide = "Matriz sin diagonal dominante";
//        }else {
//            float[] res = solve(M, error, X);
//            //Toast.makeText(getBaseContext(), "El resultado de la operacion es " + res, Toast.LENGTH_LONG).show();
//
//            resultadosSeide = Arrays.toString(res);
//            // Log.d("********************** ", "RESULTADO  " + res);
//        }
//    }
//
//    public boolean transformToDominant(int r, boolean[] V, int[] R, Float[][] M) {
//        int n = M.length;
//        if (r == M.length) {
//            Float[][
//                    ] T = new Float[n][n + 1];
//            for (int i = 0; i < R.length; i++) {
//                for (int j = 0; j < n + 1; j++)
//                    T[i][j] = M[R[i]][j];
//            }
//
//            M = T;
//
//            return true;
//        }
//
//        for (int i = 0; i < n; i++) {
//            if (V[i]) continue;
//
//            double sum = 0;
//
//            for (int j = 0; j < n; j++)
//                sum += Math.abs(M[i][j]);
//
//            if (2 * Math.abs(M[i][r]) > sum) { // diagonally dominant?
//                V[i] = true;
//                R[r] = i;
//
//                if (transformToDominant(r + 1, V, R, M))
//                    return true;
//
//                V[i] = false;
//            }
//        }
//
//        return false;
//    }
//
//    public boolean makeDominant(Float[][] M)  {
//        boolean[] visited = new boolean[M.length];
//        int[] rows = new int[M.length];
//
//        Arrays.fill(visited, false);
//
//        return transformToDominant(0, visited, rows, M);
//    }
//
//    public float[] solve(Float[][] M, Float error, float[] X) {
//
//        float[] anterior =  X;//new double[M.length];// Prev
//        float[] actual = new float[M.length];
//        Float abs;
//
//        while (true) {
//            abs = 0.0f;
//            for (int i = 0; i < M.length; i++) {
//                Float sum = M[i][M.length]; // b_n
//                for (int j = 0; j < M.length; j++)
//                    if (j != i)
//                        sum -= M[i][j]*actual[j];
//                actual[i] = 1 / M[i][i] * sum;
//            }
//            for (int i = 0; i < actual.length; i++){
//                abs += Math.abs((actual[i] - anterior
//                        [i]) / anterior[i]);
//                //Log.d("**********************", "ABS " + (abs));
//                //Log.d("**********************", "actual[i] " + (actual[i]));
//                //Log.d("**********************", "anterior[i] " + (anterior[i]));
//            }
//
//            //Log.d("**********************", "ABS "+ (abs / (float)actual.length) * 100.0);
//            //Log.d("**********************", "ABS "+ error);
//            if ((abs / actual.length) * 100 < error)
//                break;
//
//            anterior = actual;
//        }
//
//        return actual;
//    }


    private String imprimirSeidel(String valores){
        valores = valores.replace("[", "");
        valores = valores.replace("]", "");
        String[] vals = valores.split(",");
        String res = "";
        for(int i = 0; i < vals.length; i++){
            res += "X"+i+"= "+vals[i] + "\n";
        }
        return res;
    }

    public static void calcularGaussSeidel(ArrayList<ArrayList<Double>> entrada) {
        ArrayList<ArrayList<Double>> matTemp = entrada;

        // PASAR DE ARRAYLIST A DOUBLE[][]
        matriz = new double[matTemp.size()][matTemp.get(0).size()];
        for (int fila = 0; fila < matTemp.size(); fila++) {
            for (int col = 0; col < matTemp.size(); col++) {
                matriz[fila][col] = matTemp.get(fila).get(col);
            }
        }
        GaussSeidelChido gaussSeidel = new GaussSeidelChido(matriz);
        if (hacerDominante()){
            procedimiento.append("\n*** EL SISTEMA NO ES DIAGONALMENTE DOMINANTE ***\n");
        }
        procedimiento.append("\n*** MATRIZ ORIGINAL ***\n");
        imprimirMatriz();
        resolver();


    }
    //NUEVO
    public static void imprimirMatriz(){
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
    public static void resolver(){
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


    //ESTE ES EL MAIN DEL CODIGAXO
//    public static void main(String[] args) throws IOException{
//        int tamanoFila;
//        double[][] matriz;
//
//        BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
//        PrintWriter escritor = new PrintWriter(System.out, true);
//
//        System.out.println("Número de variables:");
//        tamanoFila = Integer.parseInt(lector.readLine());
//        matriz = new double[tamanoFila][tamanoFila+1];
//        System.out.println("Introduce la matriz:");
//
//        for (int fila = 0; fila < tamanoFila; fila++)
//        {
//            StringTokenizer strtk = new StringTokenizer(lector.readLine());
//
//            while (strtk.hasMoreTokens())
//                for (int columna = 0; columna < tamanoFila + 1 && strtk.hasMoreTokens(); columna++)
//                    matriz[fila][columna] = Integer.parseInt(strtk.nextToken());
//        }
//
//
//        GaussSeidel gaussSeidel = new GaussSeidel(matriz);
//
//        if (!gaussSeidel.hacerDominante())
//        {
//            escritor.println("El sistema no es diagonalmente dominante: ");
//        }
//
//        escritor.println();
//        gaussSeidel.imprimirMatriz();
//        gaussSeidel.resolver();
//    }
}
