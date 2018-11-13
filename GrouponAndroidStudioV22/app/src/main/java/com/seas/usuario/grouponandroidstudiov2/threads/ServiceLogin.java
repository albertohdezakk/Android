package com.seas.usuario.grouponandroidstudiov2.threads;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;


import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.seas.usuario.grouponandroidstudiov2.LoginActivity;
import com.seas.usuario.grouponandroidstudiov2.beans.Cliente;
import com.seas.usuario.grouponandroidstudiov2.datos.GrouponData;
import com.seas.usuario.grouponandroidstudiov2.tools.Post;

public class ServiceLogin{
	private final static Handler manejador = new Handler();
	private static String messageUser = null;
	private static ArrayList<Cliente> listaClientes = null;
	private static Thread threadLogin = null;
	
	public static void accionLogin(final String user,final String pass){
		threadLogin = new Thread(){
			public void run(){
				JSONArray datos = null;
				HashMap<String, String> parametros = new HashMap<String, String>();
				parametros.put("Usuario",user );
				parametros.put("Contrasena",pass );
				// Llamada a Servidor Web PHP
				try {
					Post post = new Post();
					datos = post.getServerDataPost(parametros,"http://ofertasalbertoakkari.readyrunners.x10host.com/loginGroupon.php");
					listaClientes = Cliente.getArrayListFromJSon(datos);
				} catch (Exception e) {
					messageUser = "Error al conectar con el servidor. ";
				}
				manejador.post(proceso);
			}
		};
		threadLogin.start();
	}
	
	private final static Runnable proceso = new Runnable(){
		public void run() {
			try{
				if (listaClientes != null && listaClientes.size() > 0) {
					Cliente cliente = listaClientes.get(0);
					if (cliente.getIdUsuario() > 0) {
						GrouponData.setCliente(cliente);
						Toast.makeText(LoginActivity.getInstance().getBaseContext(),"" +
								"Usuario correcto. ", Toast.LENGTH_SHORT).show();
					}
				} else {
						Toast.makeText(LoginActivity.getInstance().getBaseContext(),"" +
								"Usuario incorrecto. ", Toast.LENGTH_SHORT).show();
						Toast.makeText(LoginActivity.getInstance().getBaseContext(),
								messageUser, Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){}
		}
	};
}