package com.jtristan.librosendpoint.preferencias.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.jtristan.librosendpoint.Ids;

public class PreferenciasHelper {

	public final static String NOMBRE_FICHERO_PREFERENCIAS = "Preferencias";	
	public static final String PREF_NOMBRE_CUENTA = "nombreCuenta";		
	
	/**
	 * Devuelve las credenciales de un usuario que ya ha sido autenticado.
	 * @param context
	 * @return
	 */
	public static GoogleAccountCredential getCredenciales(Context context){
		
		GoogleAccountCredential credenciales = null;
		SharedPreferences preferencias = context.getSharedPreferences(NOMBRE_FICHERO_PREFERENCIAS, Context.MODE_PRIVATE);
		if (preferencias.getString(PREF_NOMBRE_CUENTA, "")!=""){			
			credenciales = GoogleAccountCredential.usingAudience(context, "server:client_id:"+Ids.WEB_CLIENT_ID);
			credenciales.setSelectedAccountName(preferencias.getString(PREF_NOMBRE_CUENTA, ""));			
		}
		
		return credenciales;
		
	}
	
	public static void guardarUsuario(Context context, String nombreCuenta){
		SharedPreferences preferencias = context.getSharedPreferences(NOMBRE_FICHERO_PREFERENCIAS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferencias.edit();
		editor.putString(PreferenciasHelper.PREF_NOMBRE_CUENTA, nombreCuenta);
		editor.commit();
	}

}
