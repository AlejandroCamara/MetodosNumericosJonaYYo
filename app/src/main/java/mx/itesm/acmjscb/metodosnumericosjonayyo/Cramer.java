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

import java.util.ArrayList;

public class Cramer extends Fragment implements View.OnClickListener{

    private Button agregar;
    private Button borrar;
    private Button calcular;
    private EditText valores;
    private TextView coeficientes;
    private TextView faltantes;
    private TextView resultados;
    private Toast toast;
    private ArrayList<ArrayList<Double>> ecuaciones;
    private Integer numIncog = 0;
    private Integer numCoef = 0;

    public Cramer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cramer, container, false);
        ecuaciones = new ArrayList<ArrayList<Double>>();
        agregar = (Button) view.findViewById(R.id.btnAgregarFila);
        borrar = (Button) view.findViewById(R.id.Borrar);
        calcular = (Button) view.findViewById(R.id.Calcular);
        valores = (EditText) view.findViewById(R.id.Valores);
        faltantes = (TextView) view.findViewById(R.id.Faltantes);
        coeficientes = (TextView) view.findViewById(R.id.Puntos);
        resultados = (TextView) view.findViewById(R.id.Resultados);
        agregar.setOnClickListener(this);
        borrar.setOnClickListener(this);
        calcular.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAgregarFila:
                try {
                    leerEntrada(valores.getText().toString());
                } catch(Exception e){
                    toast = Toast.makeText(getActivity(), "Error en los valores ingresados" , Toast.LENGTH_LONG);
                    toast.show();
                }
                imprimirValores();
                break;
            case R.id.Borrar:
                eliminarValores();
                imprimirValores();
                break;
            case R.id.Calcular:
                if(numIncog == 0) {
                    toast = Toast.makeText(getActivity(), "Resultados calculados", Toast.LENGTH_LONG);
                    toast.show();
                    resultados.setText(metodoDeCrammer(ecuaciones));
                }else if(numIncog < 0){
                    toast = Toast.makeText(getActivity(), "Sobran líneas de coeficientes" , Toast.LENGTH_LONG);
                    toast.show();
                } else{
                    toast = Toast.makeText(getActivity(), "Faltan líneas de coeficientes" , Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
        }
    }

    private void leerEntrada(String valores) throws Exception{
        ArrayList<Double> coef = new ArrayList<Double>();
        String[] valoresSimples = valores.split(",");
        for(int i = 0; i < valoresSimples.length; i++){
            coef.add(Double.parseDouble(valoresSimples[i]));
        }
        toast = Toast.makeText(getActivity(), "Valores agregados" , Toast.LENGTH_LONG);
        toast.show();
        if (ecuaciones.size() == 0){
            numCoef = coef.size();
            ecuaciones.add(coef);
            numIncog = ecuaciones.get(0).size()-2;
        }
        else if(coef.size() != numCoef){
            toast = Toast.makeText(getActivity(), "Número de coeficientes erróneo" , Toast.LENGTH_LONG);
            toast.show();
        } else{
            ecuaciones.add(coef);
            numIncog--;
        }
        faltantes.setText("Ecuaciones faltantes: "+numIncog);
    }

    private void imprimirValores(){
        String res = "";
        for(ArrayList<Double> lista: ecuaciones){
            for(Double val: lista){
                res += val + ",";
            }
            res += "\n";
        }
        coeficientes.setText(res);
    }

    private void eliminarValores(){
        if(ecuaciones.size() > 0) {
            numIncog++;
            ecuaciones.remove(ecuaciones.size() - 1);
            toast = Toast.makeText(getActivity(), "Valores eliminados", Toast.LENGTH_LONG);
            toast.show();
            faltantes.setText("Ecuaciones faltantes: "+numIncog);
        }
    }

    private ArrayList<ArrayList<Double>> matrizSencilla(ArrayList<ArrayList<Double>> matrizCompleta){
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

    private double determinante(ArrayList<ArrayList<Double>> matriz) {

        double[][] arr = new double[matriz.size()][matriz.size()];

        for(int i = 0; i < matriz.size(); i++){
            System.out.println("i= "+i);
            for(int j = 0; j < matriz.size(); j++){
                System.out.println("j= "+j);
                arr[i][j] = matriz.get(i).get(j);
            }
        }

        return determinanteUtil(arr);

    }

    private double determinanteUtil(double[][] arr) {
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

    private String metodoDeCrammer(ArrayList<ArrayList<Double>> matrizCompleta){
        ArrayList<Double> resultados = new ArrayList<Double>();
        ArrayList<ArrayList<Double>> matrizSencilla = matrizSencilla(matrizCompleta);
        System.out.println("Otro");
        Double determinante = determinante(matrizSencilla);
        if(determinante == 0.0) {
            toast = Toast.makeText(getActivity(), "No se puede resolver", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            for(int i = 0; i < matrizCompleta.size(); i++){
                matrizSencilla = new ArrayList<ArrayList<Double>>();
                for(int j = 0; j < matrizCompleta.size(); j++){
                    ArrayList<Double> temp = new ArrayList<Double>();
                    for(int k = 0; k < matrizCompleta.size(); k++){
                        if(k==i)
                            temp.add(matrizCompleta.get(j).get(matrizCompleta.size()));
                        else
                            temp.add(matrizCompleta.get(j).get(k));
                    }
                    matrizSencilla.add(temp);
                }
                System.out.println("Extra");
                resultados.add(determinante(matrizSencilla)/determinante);
            }
        }
        return imprimirCrammer(resultados);
    }

    private String imprimirCrammer(ArrayList<Double> valores){
        String res = "";
        for(Double val: valores){
            res += "X"+valores.indexOf(val)+"= "+val + "\n";
        }
        return res;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    //private OnFragmentInteractionListener mListener;

   /* public Cramer() {
        // Required empty public constructor
    }*/


    // TODO: Rename and change types and number of parameters
    /*public static Cramer newInstance(String param1, String param2) {
        Cramer fragment = new Cramer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cramer, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

}
