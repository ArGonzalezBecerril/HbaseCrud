package com.hbase.app.service;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public interface PersonService {

    public void crearTabla(String nombreTabla, String[] famColumnas, HBaseAdmin contexto);
    public void delTabla(String nombreTabla,HBaseAdmin contexto);
    public void insertarFila(String tabla,String key,String familia,String qualifier,String valor,Connection contexto);
    public void eliminarFila(String nombreTabla,String llaveFila,Connection contexto);
    public void imprimirDatos(String nombreTabla,Connection contexto);

}
