package com.hbase.app.dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

public class PersonDaoImpl implements PersonDao {
    private HBaseAdmin hbaseContext;
    private Connection conexion = null;

    public HBaseAdmin getConnection(Configuration configuracion) {
        try {
            conexion = ConnectionFactory.createConnection(configuracion);
            hbaseContext = (HBaseAdmin) conexion.getAdmin();
            return this.hbaseContext;
        } catch (IOException e) {
            System.out.println("Error al crear la conexion:" + e.getMessage());
        }
        return null;
    }

    public Connection getCon(Configuration configuracion) {
        try {
            this.conexion = ConnectionFactory.createConnection(configuracion);
            return this.conexion;
        } catch (IOException e) {
            System.out.println("Error al tratar de obtener la conexion:" + e.getMessage());
            return null;
        }
    }
}
