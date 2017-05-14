package mx.itesm.acmjscb.metodosnumericosjonayyo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;


public class GraficaInterpolacion extends Fragment implements View.OnClickListener {

    private XYPlot mySimpleXYPlot;

    public GraficaInterpolacion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grafica_interpolacion, container, false);
        mySimpleXYPlot = (XYPlot) view.findViewById(R.id.mySimpleXYPlot);
        crearGrafica();
        return view;
    }

    public void crearGrafica(){
        // Creamos dos arrays de prueba. En el caso real debemos reemplazar


        // Añadimos Línea Número UNO:
        XYSeries series1 = new SimpleXYSeries(
                MenuPrincipal.valoresX,  // ValoresX
                MenuPrincipal.valoresY, // ValoresY
                "Función Evaluada"); // Nombre de la primera serie

        // Modificamos los colores de la primera serie
        LineAndPointFormatter series1Format = new LineAndPointFormatter(
                Color.rgb(0, 200, 0),                   // Color de la línea
                Color.rgb(0, 100, 0),                   // Color del punto
                Color.rgb(150, 190, 150), null);              // Relleno

        // Una vez definida la serie (datos y estilo), la añadimos al panel
        mySimpleXYPlot.addSeries(series1, series1Format);

    }

    @Override
    public void onClick(View view) {

    }

}
