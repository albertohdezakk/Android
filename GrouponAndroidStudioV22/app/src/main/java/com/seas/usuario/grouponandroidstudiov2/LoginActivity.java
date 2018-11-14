package com.seas.usuario.grouponandroidstudiov2;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.seas.usuario.grouponandroidstudiov2.beans.Cliente;
import com.seas.usuario.grouponandroidstudiov2.datos.GrouponData;
import com.seas.usuario.grouponandroidstudiov2.threads.ServiceLogin;
import com.seas.usuario.grouponandroidstudiov2.tools.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends Activity {
	private EditText edtEmail;
	private EditText edtPass;
	private Button btnLogin;

	/*Patrón Singleton*/
		private static LoginActivity loginActivity;
		public static LoginActivity getInstance(){
			return loginActivity;
		}
	/*Fin*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loginActivity = this;
		
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtPass = (EditText) findViewById(R.id.edtPass);
		

        btnLogin = (Button) findViewById(R.id.btnEnviar);
	        btnLogin.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					/*ServiceLogin.accionLogin(edtEmail.getText().toString(), edtPass.getText().toString());*/
					HashMap<String, String> parametros = new HashMap<String, String>();
									// CLAVE------VALOR
						parametros.put("ACTION","User.login");
						parametros.put("USER",edtEmail.getText().toString());
						parametros.put("PASS",edtPass.getText().toString() );

					TareaSegundoPlano tarea = new TareaSegundoPlano(parametros);
						tarea.execute("http://192.168.42.244/glovo/Controller.php");
				}
			});
	}

	class TareaSegundoPlano extends AsyncTask<String, Integer, Boolean> {
		private HashMap<String, String> parametros;
		private ArrayList<Cliente> listaClientes;
		public TareaSegundoPlano(HashMap<String, String> parametros){
			this.parametros = parametros;
		}
		@Override
		protected Boolean doInBackground(String... params) {
			String url = params[0];//http://192.168.42.244/glovo/Controller.php
			// Importantísimo la clase "POST":
				// Hacer una petición al servidor y recuperar la respuesta en JSON.
			Post post = new Post();
			JSONArray result = post.getServerDataPost(parametros, url);
			listaClientes = Cliente.getArrayListFromJSon(result);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean aBoolean) {
			if(aBoolean){
				if(listaClientes!=null && listaClientes.size()>0){
					Cliente cliente = listaClientes.get(0);
					Toast.makeText(LoginActivity.this,
							"Usuario Correcto" + "Id="
									+ cliente.getIdUsuario(),
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}




}
