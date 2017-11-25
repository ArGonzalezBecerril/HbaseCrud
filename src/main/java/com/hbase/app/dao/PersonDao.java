package com.hbase.app.dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.HBaseAdmin;


public interface PersonDao {

    public HBaseAdmin hbaseAdmin=null;
    public Connection conexion=null;

    public HBaseAdmin getConnection(Configuration configuracion);

    public Connection getCon(Configuration configuracion);

}
