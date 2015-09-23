package net.tucompualdia.ajuste.fragments;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;

import net.tucompualdia.ajuste.MainActivity;
import net.tucompualdia.ajuste.R;
import net.tucompualdia.ajuste.modelos.Lamina;
import net.tucompualdia.ajuste.modelos.Operacion;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculatorFragment extends Fragment {

    List<Lamina> registros;
    Spinner spinner;
    TextView tvResultado;
    EditText etAncho;
    EditText etAlto;
    Button btnCalcular;
    Float total= Float.valueOf(0);
    Float valorCm = Float.valueOf(0);
    ArrayList<Operacion> miOperacion;
    ListView lvOperacion;
    ArrayAdapter<Operacion> adapterOperacion;

    public CalculatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       //

        return inflater.inflate(R.layout.fragment_calcualtor, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        spinner = (Spinner)getView().findViewById(R.id.spLamina);
        tvResultado = (TextView)getView().findViewById(R.id.tvTotal);
        etAncho = (EditText)getView().findViewById(R.id.etAncho);
        etAlto = (EditText)getView().findViewById(R.id.etAlto);
        btnCalcular = (Button)getView().findViewById(R.id.btnCalcular);
        miOperacion = new ArrayList<Operacion>();
        lvOperacion = (ListView)getView().findViewById(R.id.lvResultados);
        adapterOperacion = new ArrayAdapter<Operacion>(getContext(),android.R.layout.simple_list_item_1,miOperacion);
        lvOperacion.setAdapter(adapterOperacion);

        long numberRegistro;
        numberRegistro = Lamina.count(Lamina.class, null, null);
        Log.d("Cantidad", numberRegistro + "");
        if (numberRegistro == 0) {
            ObtDatos();
        }else{
            cargarSpiner();
        }

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcular();
            }
        });

        lvOperacion.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Operacion borrado = (Operacion) lvOperacion.getSelectedItem();
                Integer posicion = i;//lvOperacion.getSelectedItemPosition();
                Integer conteo = miOperacion.size();

                Operacion operacionCal = miOperacion.get(posicion);

                Float valorItem = Float.valueOf(0);
                Toast.makeText(getContext(), "Has borrado " + operacionCal.getDescripcion() + ".", Toast.LENGTH_LONG).show();
                valorItem = operacionCal.getValor();
                total = total - valorItem;
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                tvResultado.setText(df.format(total).toString());
                miOperacion.remove(posicion);
                adapterOperacion.remove(miOperacion.get(posicion));
                adapterOperacion.notifyDataSetChanged();
                return false;
            }
        });
        lvOperacion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }


    private void cargarSpiner(){
        registros = Lamina.listAll(Lamina.class);
        ArrayAdapter<Lamina> adapterF = new ArrayAdapter<Lamina>(getContext(),android.R.layout.simple_spinner_dropdown_item,registros);
        spinner.setAdapter(adapterF);
    }

    private void Borrar(){
        total = Float.valueOf(0);
        etAncho.setText("");
        etAlto.setText("");
        tvResultado.setText("0");
        Toast.makeText(getContext(),"Ha borrado las operaciones realizadas",Toast.LENGTH_LONG).show();
        notificacion("Borradas las operaciones.  Tucompualdia");
        //lvOperacion.setAdapter(null);
        adapterOperacion.clear();
        adapterOperacion.notifyDataSetChanged();
    }

    private void calcular(){
        Float ancho;
        Float alto;
        String anchoT;
        String altoT;
        Float totalParcial;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        Lamina miLamina = (Lamina) spinner.getSelectedItem();

        valorCm = miLamina.getValorCm();
        String descricpion = miLamina.toString();
        anchoT = etAncho.getText().toString();
        altoT = etAlto.getText().toString();

        if(anchoT.isEmpty() || altoT.isEmpty()){
            Toast.makeText(getContext(),"Debe llenar todos los campos para calcular",Toast.LENGTH_SHORT).show();
        }else {
            ancho = Float.parseFloat(anchoT);
            alto = Float.parseFloat(altoT);
            totalParcial = ancho * alto * valorCm;

            total = total + totalParcial;

            tvResultado.setText(df.format(total).toString());
            miOperacion.add(new Operacion(descricpion, totalParcial));

            lvOperacion.setAdapter(adapterOperacion);

        }

    }


    public void ObtDatos() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://multikloset.tucompualdia.net/kloset/catalogo";
        RequestParams parametros = new RequestParams();
        parametros.put("Android", "Android");
        client.post(url, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {

                    obtDatosJSON(new String(responseBody));
                    Toast.makeText(getContext(), "Actualizando base de datos", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                notificacion("Error, revisa tu conexión.  Tucompualdia");
                Toast.makeText(getContext(), "No se ha podido actualizar la base de datos", Toast.LENGTH_LONG).show();
            }
        });
    }


    public ArrayList<String> obtDatosJSON(String response) {
        ArrayList<String> listado = new ArrayList<String>();
        try {
            JSONArray resulArray = new JSONArray(response);
            String texto;
            String tipoLamina;
            Float ancho;
            Float alto;
            Float valor;
            Float valorCm;
            Lamina.deleteAll(Lamina.class);
            for (int i = 0; i < resulArray.length(); i++) {
                texto = resulArray.getJSONObject(i).getString("tipoLamina") + " (" +
                        resulArray.getJSONObject(i).getString("alto") + "X" +
                        resulArray.getJSONObject(i).getString("ancho") + ")";
                listado.add(texto);

                tipoLamina = resulArray.getJSONObject(i).getString("tipoLamina");
                ancho = Float.parseFloat(resulArray.getJSONObject(i).getString("ancho"));
                alto = Float.parseFloat(resulArray.getJSONObject(i).getString("alto"));
                valor = Float.parseFloat(resulArray.getJSONObject(i).getString("valor"));
                valorCm = Float.parseFloat(resulArray.getJSONObject(i).getString("valorCm"));

                Lamina lamina = new Lamina(tipoLamina, ancho, alto, valor, valorCm);
                lamina.save();
                Log.d("results", texto);
                notificacion("La calculadora ha actualizado su base de datos.  Tucompualdia");
            }

            cargarSpiner();

        } catch (Exception e) {

        }


        return listado;
    }


   // @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void notificacion(String mensaje) {
        Toast.makeText(getContext(),"Hola",Toast.LENGTH_LONG).show();
       // Intent intent = new Intent(getContext(), MainActivity.class);
     //   PendingIntent pIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
//        Notification n  = new Notification.Builder(getContext())
//                .setContentTitle("Multikloset")
//                .setContentText(mensaje)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pIntent)
//                .setAutoCancel(true)
//                .build();
//
//
//    NotificationManager notificationManager =
//           (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//      notificationManager.notify(0, n);
//    }

    }
}
