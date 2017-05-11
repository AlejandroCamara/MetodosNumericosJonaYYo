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


public class Gauss extends Fragment implements View.OnClickListener {

    private EditText entradaColumna;
    private TextView columnas;
    private Button agregarColumna;
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
        entradaColumna = (EditText) view.findViewById(R.id.txtEntradaColumnas);
        columnas = (TextView) view.findViewById(R.id.txtColumnasMatriz);
        agregarColumna = (Button) view.findViewById(R.id.btnAgregarColumnas);
        agregarColumna.setOnClickListener(this);
        matriz = new ArrayList<ArrayList<Double>>();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAgregarColumnas:
                try{
                    agregarColumna(entradaColumna.getText().toString());
                    entradaColumna.setText("");
                    imprimirColumnas();
                }
                catch (Exception e){
                    toast = Toast.makeText(view.getContext(),"Error en los datos",Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
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
            toast = Toast.makeText(getActivity(), "Número de coeficientes erróneo" , Toast.LENGTH_LONG);
            toast.show();
        } else{
            matriz.add(nuevaCol);
            numIncog--;
        }
        toast = Toast.makeText(getActivity(),"Ecuaciones faltantes: "+numIncog,Toast.LENGTH_LONG);

    }
        //faltantes.setText("Ecuaciones faltantes: "+numIncog);


    public void imprimirColumnas(){
        String res = "";
        for(ArrayList<Double> lista: matriz){
            for(Double val: lista){
                res += val + ",";
            }
            res += "\n";
        }
        columnas.setText(res);
    }


}
