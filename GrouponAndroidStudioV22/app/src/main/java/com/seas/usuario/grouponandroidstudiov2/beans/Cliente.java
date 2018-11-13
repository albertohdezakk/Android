package com.seas.usuario.grouponandroidstudiov2.beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cliente {
	private int idUsuario;
	private String email;
	private String pass;

	private final static String ID_USUARIO = "ID_USUARIO";
	private final static String EMAIL = "EMAIL";
	private final static String PASS = "PASS";

	
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}

	public static ArrayList<Cliente> getArrayListFromJSon(JSONArray datos){
		ArrayList<Cliente> lista = null;
		Cliente cliente = null;
		try {
			if(datos!=null && datos.length() > 0 ){
				lista = new ArrayList<Cliente>();
			}
			for (int i = 0; i < datos.length(); i++) {
				JSONObject json_data = datos.getJSONObject(i);
				cliente = new Cliente();
					cliente.setIdUsuario(json_data.getInt(ID_USUARIO));
					cliente.setEmail(json_data.getString(EMAIL));
					cliente.setPass(json_data.getString(PASS));
				lista.add(cliente);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return lista;

	}
	
}
