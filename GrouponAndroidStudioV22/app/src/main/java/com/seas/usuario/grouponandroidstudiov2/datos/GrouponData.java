package com.seas.usuario.grouponandroidstudiov2.datos;

import com.seas.usuario.grouponandroidstudiov2.beans.Cliente;

import java.util.ArrayList;


public class GrouponData {
	private static Cliente cliente;

	public static Cliente getCliente() {
		return cliente;
	}

	public static void setCliente(Cliente cliente) {
		GrouponData.cliente = cliente;
	}
	
}
