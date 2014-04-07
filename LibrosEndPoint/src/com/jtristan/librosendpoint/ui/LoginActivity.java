package com.jtristan.librosendpoint.ui;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.jtristan.librosendpoint.Ids;
import com.jtristan.librosendpoint.R;
import com.jtristan.librosendpoint.preferencias.helper.PreferenciasHelper;

public class LoginActivity extends Activity {
	
	private final static String TAG = LoginActivity.class.getSimpleName();
	private final static int RECOGER_SOLICITUD_CUENTA = 1;
	//private final static String NOMBRE_FICHERO_PREFERENCIAS = "Preferencias";
	
	//private static final String PREF_NOMBRE_CUENTA = "nombreCuenta";	
	private SharedPreferences preferencias;	
	private GoogleAccountCredential credenciales;	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		obtenerCredenciales();
		
	}
	
	
	private void obtenerCredenciales() {
		
		credenciales = PreferenciasHelper.getCredenciales(this);
		if (credenciales==null){
			credenciales = GoogleAccountCredential.usingAudience(this, "server:client_id:"+Ids.WEB_CLIENT_ID);
			seleccionarCuenta();
		}else{
			startActivity(new Intent(this, MainActivity.class));
		}
	}
	
	private void seleccionarCuenta() {
		 startActivityForResult(credenciales.newChooseAccountIntent(), RECOGER_SOLICITUD_CUENTA);
	 }
	
	@Override protected void onActivityResult(int  requestCode,  int  resultCode,  Intent  data)  {          
		super.onActivityResult(requestCode,  resultCode,  data);          
		
		switch  (requestCode)  {          	
			case  RECOGER_SOLICITUD_CUENTA:                  
				if  (data  !=  null  &&  data.getExtras()  !=  null)  {                          
					Bundle  bundle  =  data.getExtras();                          
					String  nombreCuenta  =  bundle.getString(AccountManager.KEY_ACCOUNT_NAME); 					
					if  (nombreCuenta !=  null)  {                                  
						setNombreCuenta(nombreCuenta);
					}                  
				}                  
				startActivity(new Intent(this, MainActivity.class));
				break;          
			}  
		}   

	private void setNombreCuenta(String nombreCuenta) {
		PreferenciasHelper.guardarUsuario(this, nombreCuenta);
		credenciales.setSelectedAccountName(nombreCuenta);
	}


}
