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

	/*Singleton*/
	private static LoginActivity loginActivity;
	public static LoginActivity getInstance(){
		return loginActivity;
	}
	/*Fin Singleton*/
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
					parametros.put("User",edtEmail.getText().toString());
					parametros.put("Pass",edtPass.getText().toString() );

					TareaSegundoPlano tarea = new TareaSegundoPlano(parametros);
					tarea.
execute("http://127.0.0.1/loginGroupon.php");
				}
			});
	}

	class TareaSegundoPlano extends
			AsyncTask<String, Integer, Boolean> {

		private ProgressDialog progressDialog =
				new ProgressDialog(LoginActivity.this);
		private HashMap<String, String> parametros = null;
		private ArrayList<Cliente> listaClientes = null;


		public TareaSegundoPlano( HashMap<String, String>
										  parametros) {
			this.parametros = parametros;
		}

		/*
                 * onPreExecute().
                 * Se ejecutará antes del código principal de nuestra tarea.
                 * Se suele utilizar para preparar la ejecución de la tarea, inicializar la interfaz, etc.
                 * */
		@Override
		protected void onPreExecute() {
			progressDialog.
					setProgressStyle(ProgressDialog.
							STYLE_HORIZONTAL);
			progressDialog.setMessage("Procesando...");
			progressDialog.setCancelable(true);
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
			progressDialog.show();
			progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface arg0) {
					TareaSegundoPlano.this.cancel(true);
				}
			});
		}

		/*
         * doInBackground().
         * Contendrá el código principal de nuestra tarea.
         * */
		@Override
		protected Boolean  doInBackground(String... params) {
			String url_select = params[0];
			publishProgress(0);
			try {
				Post post = new Post();
				publishProgress(50);
				JSONArray result = post.
						getServerDataPost
							(parametros,url_select);
				listaClientes = Cliente.
						getArrayListFromJSon(result);
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				//messageUser = "Error al conectar con el servidor. ";
			}
			publishProgress(100);
			return true;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int progreso = values[0].intValue();
			progressDialog.setProgress(progreso);
		}

		/*
		 * onPostExecute().
		 * Se ejecutará cuando finalice nuestra tarea, o dicho de otra forma,
		 * tras la finalización del método doInBackground().
		 * */
		@Override
		protected void onPostExecute(Boolean resp) {
			try {
				if (listaClientes != null
						&& listaClientes.size() > 0) {
					Cliente cliente = listaClientes.get(0);
					if (cliente.getIdUsuario() > 0) {
						GrouponData.setCliente(cliente);
						Toast.makeText(LoginActivity.
								getInstance().getBaseContext(), "" +
								"Usuario correcto. ",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(LoginActivity.getInstance().getBaseContext(), "" +
							"Usuario incorrecto. ", Toast.LENGTH_SHORT).show();
				}
			}catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag", "Error parsing data "+e.toString());
			}
		}
	}




}
