package com.jtristan.librosendpoint.ui;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.jtristan.librosendpoint.CloudEndpointUtils;
import com.jtristan.librosendpoint.R;
import com.jtristan.librosendpoint.entidades.libroendpoint.Libroendpoint;
import com.jtristan.librosendpoint.entidades.libroendpoint.model.CollectionResponseLibro;
import com.jtristan.librosendpoint.entidades.libroendpoint.model.Libro;
import com.jtristan.librosendpoint.preferencias.helper.PreferenciasHelper;

public class MainActivity extends Activity {
	
	private final static int REST_OK = 0;
	private final static int REST_FALLIDO = 1;
			
	
	private Button btSalvar;
	private Button btSolicitar;
	private GoogleAccountCredential credenciales;
	private static Context context;	
	private EditText etId;
	private EditText etTitulo;
	private EditText etAutor;
	private EditText etPuntuacion;
	private EditText etUsuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
					
		context = this;
		credenciales = PreferenciasHelper.getCredenciales(this);		
		
		etId = (EditText)findViewById(R.id.etId);
		etTitulo = (EditText)findViewById(R.id.etTitulo);
		etAutor = (EditText)findViewById(R.id.etAutor);
		etPuntuacion = (EditText)findViewById(R.id.etPuntuacion);
		etUsuario = (EditText)findViewById(R.id.etUsuario);
						
		btSalvar = (Button)findViewById(R.id.btSalvar);
		btSolicitar = (Button)findViewById(R.id.btSolicitar);
		
		btSalvar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new EndpointSalvarTask().execute(context);				
			}
		});
		
		btSolicitar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new EndpointLeerTask().execute(context);				
			}
		});


		
	}

		
	
	/**
	 * Clase para llamar a nuestra API Rest para insertar un libro.
	 * Requiere de autenticación.
	 * @author josemaria.tristan
	 *
	 */
	public class EndpointSalvarTask extends AsyncTask<Context, Integer, Integer> {
        protected Integer doInBackground(Context... contexts) {
        	
        	Libro result=null;
        	
            Libroendpoint.Builder endpointBuilder = new Libroendpoint.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    new JacksonFactory(),
                    credenciales);

            Libroendpoint endpoint = CloudEndpointUtils.updateBuilder(
                    endpointBuilder).build();
            try {
                Libro libro = new Libro();
                
                libro.setId(Long.valueOf(etId.getText().toString()));
                libro.setTitulo(etTitulo.getText().toString());
                libro.setAutor(etAutor.getText().toString());
                libro.setPuntuacion(Integer.valueOf(etPuntuacion.getText().toString()));
               
                result = endpoint.insertLibro(libro).execute();
                                
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (result!=null)
            	return REST_OK;
            else
            	return REST_FALLIDO;
        }

		@Override
		protected void onPostExecute(Integer result) {
			if (result==REST_OK){
				Toast.makeText(context, "Salvado", Toast.LENGTH_SHORT).show();
			}
			
		}
        
        
        
    }


	
	/**
	 * Clase para llamar a nuestra API Rest para leer un libro.
	 * No es necesario pasarla la autenticación.
	 * @author josemaria.tristan
	 *
	 */
	public class EndpointLeerTask extends AsyncTask<Context, Libro, Libro> {
        protected Libro doInBackground(Context... contexts) {
        	
        	Libro libro = null;
        	
            Libroendpoint.Builder endpointBuilder = new Libroendpoint.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    new JacksonFactory(),
                    new HttpRequestInitializer() {                       
						@Override
						public void initialize(
								com.google.api.client.http.HttpRequest arg0)
								throws IOException {						
							}
                    });

            Libroendpoint endpoint = CloudEndpointUtils.updateBuilder(
                    endpointBuilder).build();
            try {
                                
                libro = endpoint.getLibro(Long.valueOf(etId.getText().toString())).execute();
                                                
            } catch (IOException e) {
                e.printStackTrace();
            }
            return libro;
        }

		@Override
		protected void onPostExecute(Libro libro) {
			if (libro!=null){
				 etId.setText(libro.getId().toString());
	             etTitulo.setText(libro.getTitulo());
	             etAutor.setText(libro.getAutor());
	             etPuntuacion.setText(libro.getPuntuacion().toString());
	             etUsuario.setText(libro.getUsuarioLogeado().getEmail() + " " + libro.getUsuarioLogeado().getNickname());
			}else{
				Toast.makeText(context, "Error leyendo", Toast.LENGTH_SHORT).show();
			}
             
		}
        
        
    }

	
}
