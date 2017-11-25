package com.hbase.app;

import java.io.IOException;

import com.hbase.app.dao.PersonDao;
import com.hbase.app.dao.PersonDaoImpl;
import com.hbase.app.service.PersonService;
import com.hbase.app.service.PersonServiceImpl;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class TestHbase {

    public static void main(String[] args){
        Configuration conf = HBaseConfiguration.create();
        PersonService servicionPersona =new PersonServiceImpl();
        PersonDao daoPersona = new PersonDaoImpl();
        String nombreTabla = "Persona";
        String[] familia = { "Educacion", "Empresa" };


        servicionPersona.delTabla(nombreTabla,daoPersona.getConnection(conf));
        //servicionPersona.crearTabla(nombreTabla,familia,daoPersona.getConnection(conf));

        //servicionPersona.insertarFila(nombreTabla,"Auditor","Educacion","Programador","Java",daoPersona.getCon(conf));
        //servicionPersona.insertarFila(nombreTabla,"Auditor","Educacion","Analista","Python",daoPersona.getCon(conf));
        //servicionPersona.insertarFila(nombreTabla,"Auditor","Educacion","Estudiante","Lenguas",daoPersona.getCon(conf));

       // servicionPersona.insertarFila(nombreTabla,"Docente","Empresa","Gerente","Sistemas",daoPersona.getCon(conf));
        //servicionPersona.insertarFila(nombreTabla,"Docente","Empresa","Supervisor","Ventas",daoPersona.getCon(conf));
        //servicionPersona.insertarFila(nombreTabla,"Docente","Empresa","Gerente","Marketing",daoPersona.getCon(conf));


        //servicionPersona.imprimirDatos(nombreTabla,daoPersona.getCon(conf));

    }
}
