package mx.itesm.acmjscb.metodosnumericosjonayyo;

import java.util.Arrays;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MatrizInversaChida
{
    private static StringBuilder procedimiento = new StringBuilder(1000);
    public static void main(String[] args)
    {
        ArrayList<ArrayList<Double>> matTemp = ejemplo4();

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
                System.out.println(procedimiento.toString());
            }
        }
        catch (Exception e) {
            System.out.println("error->"+e.getMessage());
        }
    }

    public static Double[][] identidad(Double[][] matriz, Double[][] MatTrans, Double determinante){
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

    public static Double[][] inversa(Double[][] MatTrans, Double determinante){
        Double[][] matInv = new Double[MatTrans.length][MatTrans.length];
        for (int fila = 0; fila < MatTrans.length; fila++) {
            for (int columna = 0; columna < MatTrans.length; columna++) {
                matInv[fila][columna] = MatTrans[fila][columna]/determinante;
            }
        }
        return matInv;
    }

    public Double determinante(int fila, Double [][]matriz)
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
    public Double [][] adjuntaMatriz(Double [][]matriz)
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
    public Double [][] transpuestaMatriz(Double [][]matriz) {

        Double[][]tempTranspuesta=new Double[matriz.length][matriz.length];
        for (int fila = 0; fila < tempTranspuesta.length; fila++)
        {
            for (int columna = 0; columna < tempTranspuesta.length; columna++) {
                tempTranspuesta[fila][columna]=matriz[columna][fila];
            }
        }
        return tempTranspuesta;
    }

    public static void imprimirMatriz(Double[][] mat){
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

    private static ArrayList<ArrayList<Double>> ejemplo1(){
        ArrayList<ArrayList<Double>> matriz = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> fila = new ArrayList<Double>();
        fila.add(1.0);
        fila.add(-3.0);
        fila.add(2.0);
        fila.add(4.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(2.0);
        fila.add(5.0);
        fila.add(4.0);
        fila.add(1.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(0.0);
        fila.add(-1.0);
        fila.add(8.0);
        fila.add(-2.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(3.0);
        fila.add(-1.0);
        fila.add(-4.0);
        fila.add(-2.0);
        matriz.add(fila);
        return matriz;
    }

    private static ArrayList<ArrayList<Double>> ejemplo2(){
        ArrayList<ArrayList<Double>> matriz = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> fila = new ArrayList<Double>();
        fila.add(1.0);
        fila.add(-3.0);
        fila.add(5.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(-3.0);
        fila.add(-2.0);
        fila.add(0.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(5.0);
        fila.add(0.0);
        fila.add(-1.0);
        matriz.add(fila);
        return matriz;
    }

    private static ArrayList<ArrayList<Double>> ejemplo3(){
        ArrayList<ArrayList<Double>> matriz = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> fila = new ArrayList<Double>();
        fila.add(1.0);
        fila.add(3.0);
        fila.add(3.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(1.0);
        fila.add(4.0);
        fila.add(5.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(1.0);
        fila.add(3.0);
        fila.add(4.0);
        matriz.add(fila);
        return matriz;
    }

    private static ArrayList<ArrayList<Double>> ejemplo4(){
        ArrayList<ArrayList<Double>> matriz = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> fila = new ArrayList<Double>();
        fila.add(0.0);
        fila.add(-4.0);
        fila.add(-3.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(1.0);
        fila.add(-3.0);
        fila.add(1.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(1.0);
        fila.add(2.0);
        fila.add(5.0);
        matriz.add(fila);
        return matriz;
    }

    private static ArrayList<ArrayList<Double>> ejemplo5(){
        ArrayList<ArrayList<Double>> matriz = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> fila = new ArrayList<Double>();
        fila.add(1.0);
        fila.add(-5.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(3.0);
        fila.add(-1.0);
        matriz.add(fila);
        return matriz;
    }

    private static ArrayList<ArrayList<Double>> ejemplo6(){
        ArrayList<ArrayList<Double>> matriz = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> fila = new ArrayList<Double>();
        fila.add(3.0);
        fila.add(0.0);
        fila.add(2.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(2.0);
        fila.add(0.0);
        fila.add(-2.0);
        matriz.add(fila);
        fila = new ArrayList<Double>();
        fila.add(0.0);
        fila.add(1.0);
        fila.add(1.0);
        matriz.add(fila);
        return matriz;
    }

}
