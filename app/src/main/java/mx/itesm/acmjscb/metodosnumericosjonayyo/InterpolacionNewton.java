package mx.itesm.acmjscb.metodosnumericosjonayyo;

import android.content.Context;
import android.net.Uri;
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


public class InterpolacionNewton extends Fragment implements View.OnClickListener {
    private Button agregarPunto;
    private Button eliminarPunto;
    private Button calcular;
    private EditText puntoX;
    private EditText puntoY;
    private TextView puntos;
    private TextView resultado;
    private ArrayList<ArrayList<Double>> coordenadas;
    private Toast toast;

    public InterpolacionNewton(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interpolacion_newton, container, false);
        agregarPunto = (Button) view.findViewById(R.id.Agregar);
        eliminarPunto = (Button) view.findViewById(R.id.Eliminar);
        calcular = (Button) view.findViewById(R.id.Calcular);
        puntoX = (EditText) view.findViewById(R.id.PuntoX);
        puntoY = (EditText) view.findViewById(R.id.PuntoY);
        puntos = (TextView) view.findViewById(R.id.Puntos);
        resultado = (TextView) view.findViewById(R.id.Resultados);
        agregarPunto.setOnClickListener(this);
        eliminarPunto.setOnClickListener(this);
        calcular.setOnClickListener(this);
        coordenadas = new ArrayList<ArrayList<Double>>();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Agregar:
                agregarValores(Double.parseDouble(puntoX.getText().toString()), Double.parseDouble(puntoY.getText().toString()));
                imprimirValores();
                break;
            case R.id.Eliminar:
                eliminarValores();
                imprimirValores();
                break;
            case R.id.Calcular:
                String res = interpolacionNewton(coordenadas);
                toast = Toast.makeText(getActivity(), "Polinomio Generado" , Toast.LENGTH_LONG);
                toast.show();
                resultado.setText(res);
                break;
        }
    }


    private void agregarValores(Double puntoX, Double puntoY){
        ArrayList<Double> puntosXY = new ArrayList<Double>(2);
        puntosXY.add(puntoX);
        puntosXY.add(puntoY);
        coordenadas.add(puntosXY);
        toast = Toast.makeText(getActivity(), "Valor agregado" , Toast.LENGTH_LONG);
        toast.show();
    }

    private void imprimirValores(){
        String res = "";
        for(ArrayList<Double> lista: coordenadas){
            res += lista.get(0) + ", ";
            res += lista.get(1) + "\n";
        }
        puntos.setText(res);
    }

    private void eliminarValores(){
        if(coordenadas.size() > 0) {
            coordenadas.remove(coordenadas.size() - 1);
            toast = Toast.makeText(getActivity(), "Valor eliminado", Toast.LENGTH_LONG);
            toast.show();
        }
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
		/*
		ArrayList<ArrayList<Double>> temp = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> lista;
		for(int i = 0; i < matriz.size(); i++){
			lista = new ArrayList<Double>(matriz.size()+1);
			for(int j = 0; j < matriz.size()+1; j++){
				lista.add(arr[i][j]);
			}
			temp.add(lista);
		}*/

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
}
