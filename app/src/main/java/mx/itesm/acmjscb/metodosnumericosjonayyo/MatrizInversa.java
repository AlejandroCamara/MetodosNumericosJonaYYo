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

public class MatrizInversa extends Fragment implements View.OnClickListener{

    private static StringBuilder procedimiento = new StringBuilder(1000);

    private EditText entradaFila;
    private TextView filas;
    private Button agregarFila;
    private Button calcularInversa;
    private ArrayList<ArrayList<Double>> matriz;
    private Toast toast;

    public MatrizInversa() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matriz_inversa, container, false);
        entradaFila = (EditText) view.findViewById(R.id.txtEntradaFilas);
        filas = (TextView) view.findViewById(R.id.txtFilasMatriz);
        agregarFila = (Button) view.findViewById(R.id.btnAgregarFilas);
        calcularInversa = (Button) view.findViewById(R.id.btnCalcularInversa);
        agregarFila.setOnClickListener(this);
        calcularInversa.setOnClickListener(this);
        matriz = new ArrayList<ArrayList<Double>>();

        // Inflate the layout for this fragment
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

            case R.id.btnCalcularInversa:
                try{
                    if (matriz.size() == matriz.get(0).size()){
                        calcularMatrizInversa(matriz);
                        filas.setText(procedimiento.toString());
                        procedimiento = new StringBuilder(1000);
                        matriz = new ArrayList<ArrayList<Double>>();
                    }
                    else{
                        matriz = new ArrayList<ArrayList<Double>>();
                        filas.setText("");
                        toast = Toast.makeText(view.getContext(),"La matriz debe ser cuadrada",Toast.LENGTH_LONG);
                        toast.show();
                    }
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
        matriz.add(nuevaCol);
    }

    private void imprimirColumnas(){
        String res = "La matriz es:\n";
        for(ArrayList<Double> lista: matriz){
            res += lista.toString();
            res += "\n";
        }
        filas.setText(res);
    }


    private static void calcularMatrizInversa(ArrayList<ArrayList<Double>> entrada)
    {
        ArrayList<ArrayList<Double>> matTemp = entrada;

        // PASAR DE ARRAYLIST A DOUBLE[][]
        Double[][] matriz= new Double [matTemp.size()][matTemp.size()];
        for (int fila = 0; fila<matTemp.size(); fila++){
            for (int col = 0; col <matTemp.size(); col ++){
                matriz[fila][col] = matTemp.get(fila).get(col);
            }
        }
        MatrizInversaChida mat=new MatrizInversaChida();
        mat.transpuestaMatriz(matriz);
        try
        {

            Double determinante=mat.determinante(0, matriz); //calculando la determinate
            Double[][]MatAdjunta=mat.adjuntaMatriz(matriz);
            Double[][]MatTrans=mat.transpuestaMatriz(MatAdjunta);
            Double[][]MatInv;
            Double[][]MatId;
            procedimiento.append("\n*** LA MATRIZ ORIGINAL ***\n");
            imprimirMatriz(matriz);
            procedimiento.append("\n*** EL DETERMINANTE ES ***\n");
            procedimiento.append(determinante.toString()+"\n");
            procedimiento.append("\n**** MATRIZ ADJUNTA ****\n");
            imprimirMatriz(MatAdjunta);
            procedimiento.append("\n**** MATRIZ TRANSPUESTA ****\n");
            imprimirMatriz(MatTrans);

            if (determinante==0) {
                procedimiento.append("\n*** NO EXISTE INVERSA DE LA MATRIZ ***\n");
            }
            else {
                procedimiento.append("\n*** LA INVERSA DE LA MATRIZ ES ***\n");
                MatInv = inversa(MatTrans,determinante);
                imprimirMatriz(MatInv);
                procedimiento.append("\n*** COMPROBACIÓN: MATRIZ IDENTIDAD OBTENIDA ***\n");
                MatId = identidad(matriz,MatTrans,determinante);
                imprimirMatriz(MatId);
            }
        }
        catch (Exception e) {
            System.out.println("error->"+e.getMessage());
        }
    }

    private static Double[][] identidad(Double[][] matriz, Double[][] MatTrans, Double determinante){
        Double[][] matID = new Double[MatTrans.length][MatTrans.length];
        for (int fila = 0; fila < MatTrans.length; fila++)
        {
            for (int columna = 0; columna < MatTrans.length; columna++)
            {
                Double elemento=0.0;
                for (int k = 0; k < MatTrans.length; k++) {

                    elemento=elemento+ Math.round(matriz[fila][k]*(MatTrans[k][columna]/determinante));
                }
                matID[fila][columna] = elemento;
            }
        }
        return matID;
    }

    private static Double[][] inversa(Double[][] MatTrans, Double determinante){
        Double[][] matInv = new Double[MatTrans.length][MatTrans.length];
        for (int fila = 0; fila < MatTrans.length; fila++) {
            for (int columna = 0; columna < MatTrans.length; columna++) {
                matInv[fila][columna] = MatTrans[fila][columna]/determinante;
            }
        }
        return matInv;
    }

    private Double determinante(int fila, Double [][]matriz)
    {
        if (matriz.length==2)
        {
            Double deter=matriz[0][0]*matriz[1][1]-matriz[0][1]*matriz[1][0];
            return  deter;
        }
        else
        {
            Double deter=0.0;
            for (int columna = 0; columna < matriz.length; columna++)
            {
                Double[][]temp = this.subMatriz(fila,columna,matriz);
                deter=deter+Math.pow(-1, fila+columna)*matriz[fila][columna]*this.determinante(0, temp);
            }
            return deter;
        }
    }

    //claculo de submatriz eliminado fila, columna
    private Double[][] subMatriz(int fila,int columna,Double [][]matriz)
    {
        Double[][]temp=new Double[matriz.length-1][matriz.length-1];
        int contador1=0;
        int contador2=0;
        for (int k = 0; k < matriz.length; k++)
        {
            if (k!=fila)
            {
                contador2=0;
                for (int l = 0; l < matriz.length; l++)
                {
                    if (l!=columna)
                    {
                        temp[contador1][contador2]=matriz[k][l];
                        contador2++;
                    }
                }
                contador1++;
            }
        }
        return temp;
    }

    //metodo para calcular la adjunta de una matrzi
    private Double [][] adjuntaMatriz(Double [][]matriz)
    {
        Double[][]tempAdjunta=new Double[matriz.length][matriz.length];
        for (int fila = 0; fila < tempAdjunta.length; fila++)
        {
            for (int columna = 0; columna < tempAdjunta.length; columna++)
            {
                Double[][]temp  = this.subMatriz(fila, columna, matriz) ;
                Double elementoAdjunto=Math.pow(-1, fila+columna)*this.determinante(0, temp);
                tempAdjunta[fila][columna]=elementoAdjunto;
            }
        }
        return tempAdjunta;
    }

    //metodo para obtener la transpuesta de la matriz
    private Double [][] transpuestaMatriz(Double [][]matriz) {

        Double[][]tempTranspuesta=new Double[matriz.length][matriz.length];
        for (int fila = 0; fila < tempTranspuesta.length; fila++)
        {
            for (int columna = 0; columna < tempTranspuesta.length; columna++) {
                tempTranspuesta[fila][columna]=matriz[columna][fila];
            }
        }
        return tempTranspuesta;
    }

    private static void imprimirMatriz(Double[][] mat){
        DecimalFormat df = new DecimalFormat("0.000");
        String filaBonita = "";
        for (Double[] fila: mat){
            filaBonita = "[";
            for (Double num : fila){
                filaBonita += df.format(num)+"  ";
            }
            filaBonita = filaBonita.substring(0, filaBonita.length()-3);
            filaBonita +="]";
            procedimiento.append(filaBonita+"\n");
            filaBonita = "";
        }

        procedimiento.append("\n");
    }
}
