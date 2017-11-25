package com.hbase.app.service;

import com.hbase.app.dao.PersonDao;
import com.hbase.app.dao.PersonDaoImpl;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class PersonServiceImpl implements PersonService {

    private PersonDao personaDao;
    private HBaseAdmin hBaseAdmin;

    public void crearTabla(String nombreTabla, String[] famColumnas, HBaseAdmin contexto) {
        try {
            if (contexto.tableExists(nombreTabla)) {
                System.out.println(nombreTabla + " ya existe");
            } else {
                HTableDescriptor descriptor = new HTableDescriptor(TableName.valueOf(nombreTabla));
                for (String fC : famColumnas) {
                    descriptor.addFamily(new HColumnDescriptor(fC));
                }
                contexto.createTable(descriptor);
            }
        } catch (IOException e) {
            System.out.println("Error al crear la tabla:" + e.getMessage());
        }
    }

    public void delTabla(String nombreTabla, HBaseAdmin contexto) {
        try {
            if (contexto.tableExists(nombreTabla)) {
                contexto.disableTable(nombreTabla);
                contexto.deleteTable(nombreTabla);
                System.out.println("La tabla se elimino correctamente.");
            } else {
                System.out.println("La tabla no existe:" + nombreTabla);
            }
        } catch (IOException e) {
            System.out.println("Error al eliminar la tabla:" + nombreTabla);
        }
    }

    public void insertarFila(String nombreTabla, String key, String familia, String qualifier, String valor, Connection conexion) {
        try {
            Table tabla = conexion.getTable(TableName.valueOf(nombreTabla));
            Put insertar = new Put(Bytes.toBytes(key));
            insertar.addColumn(Bytes.toBytes(familia), Bytes.toBytes(qualifier), Bytes.toBytes(valor));
            tabla.put(insertar);
        } catch (IOException e) {
            System.out.println("Error al insertar el dato:" + e.getMessage());
        }
    }

    public void eliminarFila(String nombreTabla, String llaveFila, Connection conexion) {
        try {
            Table tabla = conexion.getTable(TableName.valueOf(nombreTabla));
            Delete eliminar = new Delete(Bytes.toBytes(llaveFila));
            tabla.delete(eliminar);
            System.out.println("Fila eliminada correctamente");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Error al tratar de eliminar una fila" + e.getMessage());
        }

    }

    public void imprimirDatos(String nombreTabla, Connection conexion) {
        ResultScanner resultados = null;
        try {
            Table tabla = conexion.getTable(TableName.valueOf(nombreTabla));
            Scan scan = new Scan();
            resultados = tabla.getScanner(scan);
            for (Result r : resultados) {
                System.out.println(Bytes.toString(r.getRow()));
                for (Cell celda : r.rawCells()) {
                    System.out.println("Familia: " + Bytes.toString(CellUtil.cloneFamily(celda)));
                    System.out.println("Qualifier: " + Bytes.toString(CellUtil.cloneQualifier(celda)));
                    System.out.println("Valor: " + Bytes.toString(CellUtil.cloneValue(celda)));

                }
            }
            resultados.close();
        } catch (IOException e) {
            System.out.println("Error al iterar los datos:" + e.getMessage());
        }

    }
}
