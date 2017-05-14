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


public class Gauss extends Fragment implements View.OnClickListener {

    private static StringBuilder procedimiento = new StringBuilder(10000);
    private EditText entradaColumna;
    private TextView columnas;
    private Button agregarColumna;
    private Button calcularGauss;
    private ArrayList<ArrayList<Double>> matriz;
    private Toast toast;
    private int numCoef = 0;
    private int numIncog = 0;

    public Gauss() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gauss, container, false);
        entradaColumna = (EditText) view.findViewById(R.id.txtEntradaFilas);
        columnas = (TextView) view.findViewById(R.id.txtColumnasMatriz);
        agregarColumna = (Button) view.findViewById(R.id.btnAgregarFilas);
        calcularGauss = (Button) view.findViewById(R.id.btnCalcularGauss);
        agregarColumna.setOnClickListener(this);
        calcularGauss.setOnClickListener(this);
        matriz = new ArrayList<ArrayList<Double>>();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAgregarFilas:
                try{
                    agregarColumna(entradaColumna.getText().toString());
                    entradaColumna.setText("");
                    toast = Toast.makeText(view.getContext(),"Fila Agregada",Toast.LENGTH_LONG);
                    toast.show();
                    imprimirColumnas();
                }
                catch (Exception e){
                    toast = Toast.makeText(view.getContext(),"Error en los datos",Toast.LENGTH_LONG);
                    toast.show();
                }
                break;

            case R.id.btnCalcularGauss:
                try{
                    calcularGauss();
                    procedimiento = new StringBuilder(10000);
                    matriz = new ArrayList<ArrayList<Double>>();
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
        if (matriz.size() == 0){
            numCoef = nuevaCol.size();
            matriz.add(nuevaCol);
            numIncog = matriz.get(0).size()-2;
        }
        else if(nuevaCol.size() != numCoef){
            toast = Toast.makeText(getActivity(), "Metiste mal los coeficientes" , Toast.LENGTH_LONG);
            toast.show();
        } else{
            matriz.add(nuevaCol);
            numIncog--;
        }
        toast = Toast.makeText(getActivity(),"Te faltan "+numIncog+" ecuaciones/columnas",Toast.LENGTH_LONG);

    }
        //faltantes.setText("Ecuaciones faltantes: "+numIncog);


    public void imprimirColumnas(){
        String res = "La matriz es:\n";
        /*for(ArrayList<Double> lista: matriz){
            for(Double val: lista){
                res += val + ",";
            }
            res += "\n";
        }*/
        for(ArrayList<Double> lista: matriz){
            res += lista.toString();
            res += "\n";
        }
        columnas.setText(res);
    }



    public void calcularGauss() {
        procedimiento.append("**** MATRIZ ORIGINAL ****\n");
        imprimirMatriz(matriz);
        matriz = gauss(matriz);
        procedimiento.append("**** MATRIZ FINAL ****\n");
        imprimirMatriz(matriz);
        columnas.setText(procedimiento.toString());

    }

    public static ArrayList<ArrayList<Double>> gauss(ArrayList<ArrayList<Double>> matriz){
        int m = 0;
        int n = 0;
        int c = 1;
        int numFilas = matriz.size();

        for (int fila = 0; fila<numFilas; fila++){
            // PIVOTE ES CERO
            if (matriz.get(fila).get(fila) == 0){
                // SI EL VALOR DEBAJO DEL PIVOTE NO ES CERO
                for (int k = fila+1; k<numFilas; k++){
                    if (matriz.get(k).get(fila) != 0){
                        m = fila;
                        n = k;
                        matriz = cambiaFilas(matriz,m,n);
                        matriz = gaussSimple(matriz,fila);
                    }
                }
            }
            else{
                matriz = gaussSimple(matriz,fila);
            }
        }

        return matriz;

    }


    public static ArrayList<ArrayList<Double>> gaussSimple(ArrayList<ArrayList<Double>> a, int fila){
        //System.out.println("***NUMERO DE FILA *** "+fila);
        ArrayList<ArrayList<Double>> mat = a;
        ArrayList<Double> filaPivote = mat.get(fila);
        ArrayList<Double> filaDebajoPivote;
        ArrayList<Double> nuevaFilaDebajoPivote;
        ArrayList<Double> filaArribaPivote;
        ArrayList<Double> nuevaFilaArribaPivote;
        Double pivote = mat.get(fila).get(fila);
        Double numDebajoPivote;
        Double numArribaPivote;

        // IGUALAR PIVOTE A 1
        filaPivote = multiplicarFila(filaPivote,1/pivote);
        mat.set(fila, filaPivote);
        procedimiento.append("\n**** DESPUÉS NORMALIZAR PIVOTE ****\n");
        imprimirMatriz(mat);
        //System.out.println(mat.size()-fila+" numfilas - filas");
        //SUSTITUCIÓN HACIA ABAJO
        procedimiento.append("**** SUSTITUCIÓN HACIA ABAJO ****\n\n");
        for(int col = 1; col< mat.size()-fila;col++){
            //System.out.println(fila+col+" fila+col");
            filaDebajoPivote = mat.get(fila+col);
            //System.out.println("filaDebajo"+filaDebajoPivote.toString());
            numDebajoPivote = filaDebajoPivote.get(fila);
            nuevaFilaDebajoPivote = multiplicarFila(filaPivote,-numDebajoPivote);
            nuevaFilaDebajoPivote = sumarFila(filaDebajoPivote, nuevaFilaDebajoPivote);
            //System.out.println("nuevaFila"+nuevaFilaDebajoPivote.toString());
            mat.set(fila+col, nuevaFilaDebajoPivote);
            procedimiento.append("****DESPUES ITERACION HACIA ABAJO****\n");
            imprimirMatriz(mat);

        }
        procedimiento.append("****DESPUES DE SUSTITUIR HACIA ABAJO*****\n");
        imprimirMatriz(mat);
        //SUSTITUCIÓN HACIA ARRIBA
        //System.out.println("FILA -1 "+(fila-1));
        procedimiento.append("**** SUSTITUCION HACIA ARRIBA ***\n\n");

        for(int col = 0; col<fila;col++){
            //System.out.println("COL "+col);
            filaArribaPivote = mat.get(fila-col-1);
            //System.out.println("filaArriba"+filaArribaPivote.toString());
            numArribaPivote = filaArribaPivote.get(fila);
            nuevaFilaArribaPivote = multiplicarFila(filaPivote,-numArribaPivote);
            nuevaFilaArribaPivote = sumarFila(filaArribaPivote, nuevaFilaArribaPivote);
            //System.out.println("nuevaFila"+nuevaFilaArribaPivote.toString());
            mat.set(fila-col-1, nuevaFilaArribaPivote);
            procedimiento.append("\n****DESPUES ITERACION HACIA ARRIBA****\n");
            imprimirMatriz(mat);

        }

        procedimiento.append("****DESPUES DE SUSTITUIR HACIA ARRIBA*****\n");
        imprimirMatriz(mat);
        return mat;
    }

    public static  ArrayList<ArrayList<Double>> cambiaFilas( ArrayList<ArrayList<Double>> a, int filaPivote, int siguiente){

        ArrayList<ArrayList<Double>> temp = new ArrayList<ArrayList<Double>>(a.size());
        for (ArrayList<Double> fila: a){
            temp.add(fila);
        }
        ArrayList<ArrayList<Double>> mat = a;
        temp.set(siguiente, mat.get(siguiente));
        mat.set(siguiente, mat.get(filaPivote));
        mat.set(filaPivote, temp.get(siguiente));
        return mat;
    }

    public static ArrayList<Double> multiplicarFila(ArrayList<Double> fila, Double multi){
        ArrayList<Double> nuevaFila = new ArrayList<Double>(fila.size());
        Double nuevoNum;
        for (Double num: fila){
            nuevoNum = num*multi;
            nuevaFila.add(nuevoNum);
        }
        return nuevaFila;

    }

    public static ArrayList<Double> sumarFila(ArrayList<Double> fila1, ArrayList<Double> fila2){
        ArrayList<Double> sumaFila = new ArrayList<Double>(fila1.size());
        for (int cont = 0; cont < fila1.size(); cont++){
            sumaFila.add(fila1.get(cont)+fila2.get(cont));
        }
        return sumaFila;

    }

    public static void imprimirMatriz(ArrayList<ArrayList<Double>> mat){
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

}
