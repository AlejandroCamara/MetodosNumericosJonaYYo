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


public class GaussJordan extends Fragment implements View.OnClickListener{
    private static StringBuilder procedimiento = new StringBuilder(10000);
    private EditText entradaColumna;
    private TextView columnas;
    private Button agregarColumna;
    private Button calcularGaussJordan;
    private ArrayList<ArrayList<Double>> matriz;
    private Toast toast;
    private int numCoef = 0;
    private int numIncog = 0;
    private static boolean pivoteEsCero;

    public GaussJordan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gauss_jordan, container, false);
        entradaColumna = (EditText) view.findViewById(R.id.txtEntradaFilas);
        columnas = (TextView) view.findViewById(R.id.txtColumnasMatriz);
        agregarColumna = (Button) view.findViewById(R.id.btnAgregarFilas);
        calcularGaussJordan = (Button) view.findViewById(R.id.btnCalcularGaussJordan);
        agregarColumna.setOnClickListener(this);
        calcularGaussJordan.setOnClickListener(this);
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

            case R.id.btnCalcularGaussJordan:
                try{
                    calcularGaussJordan();
                    procedimiento = new StringBuilder(10000);
                    matriz = new ArrayList<ArrayList<Double>>();
                }
                catch (Exception e){
                    toast = Toast.makeText(view.getContext(),"Error inesperado",Toast.LENGTH_LONG);
                    toast.show();
                }
        }
    }

    private void validarEntrada(){
        boolean esMatrizValida = true;
        for (ArrayList<Double> fila : matriz){
            for (int i = 0; i<matriz.size();i++){
                if (fila.get(i) != 0){
                    esMatrizValida = true;
                }
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

    public void imprimirColumnas(){
        String res = "La matriz es:\n";
        for(ArrayList<Double> lista: matriz){
            res += lista.toString();
            res += "\n";
        }
        columnas.setText(res);
    }

    public  void calcularGaussJordan() {

            procedimiento.append("**** MATRIZ ORIGINAL ****\n");
            imprimirMatriz(matriz);
            matriz = gaussJordan(matriz);
        if (!pivoteEsCero){
            procedimiento.append("\n**** MATRIZ FINAL ****\n");
            imprimirMatriz(matriz);
            columnas.setText(procedimiento.toString());
        }
        else{
            columnas.setText("No se puede resolver por este método");
        }

    }

    public static ArrayList<ArrayList<Double>> gaussJordan(ArrayList<ArrayList<Double>> entrada) {
        int columna = 0;
        ArrayList<ArrayList<Double>> matriz = entrada;
        ArrayList<Double> temp;

        for (int fila = 0; fila < entrada.size(); fila++, columna++) {
            for (int filaPivote = 0; filaPivote < entrada.size(); filaPivote++) {
                int iter = 0;
                pivoteEsCero = false;
                while (matriz.get(fila).get(columna) == 0) {
                    if (iter> matriz.size()*2){
                        pivoteEsCero = true;
                        break;
                    }
                    procedimiento.append("\n*** EL PIVOTE ES IGUAL A CERO ***\n");
                    temp = matriz.get(fila);
                    matriz.set(fila, matriz.get(fila + 1));
                    matriz.set(fila + 1, temp);
                    procedimiento.append("\n*** CAMBIO DE FILAS ***\n");
                    imprimirMatriz(matriz);
                    iter++;
                }

                if (matriz.get(fila).get(columna) != 1) {
                    procedimiento.append("*** PIVOTE SIN NORMALIZAR ***");
                    ArrayList<Double> valores = matriz.get(fila);
                    Double valor;
                    for (int valorContador = valores.size() - 1; valorContador >= 0; valorContador--) {
                        valor = valores.get(valorContador) / valores.get(columna);
                        valores.set(valorContador, valor);
                    }
                    procedimiento.append("\n*** DESPUÉS DE NORMALIZAR EL PIVOTE: FILA "+(columna+1)+" ***\n");
                    imprimirMatriz(matriz);
                }
                /*else if (fila == 0){
                	procedimiento.append("\n*** PIVOTE YA NORMALIZADO: FILA "+(columna+1)+" ***\n");

                }
                imprimirMatriz(matriz);*/
                if (filaPivote != columna) {
                    Double valor;
                    ArrayList<Double> valores = matriz.get(fila);
                    Double pivote = matriz.get(filaPivote).get(columna);
                    procedimiento.append("\n*** SUSTITUCIÓN EN EL RESTO DE LA MATRIZ ***\n");
                    for (int valorPivote = valores.size() - 1; valorPivote >= 0; valorPivote--) {
                        valor = (valores.get(valorPivote) * (-pivote) + matriz.get(filaPivote).get(valorPivote));
                        matriz.get(filaPivote).set(valorPivote, valor);
                    }
                    imprimirMatriz(matriz);
                }
            }
        }
        return matriz;
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
