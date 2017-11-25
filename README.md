RUD Hbase
### Integración de Hbase y Java con Hadoop en modo Pseudodistribuido.

[![N|Solid](https://image.ibb.co/cxcEV6/powered.png)](https://github.com/ArturoGonzalezBecerril)

#### Herramientas necesarias
  - Hadoop 2.7 (Debe esta instalado y configurado tanto HDFS como Yarn)
  - Java 8
  - Hbase 1.1.12 (Se integra con Hadoop ya que para arrancar Hbase es nesesario tener una ruta HDFS donde se almacenan los metadatos del mismo)
  - Maven (Versión actual)

# La estructura del Proyecto queda definida de las siguente manera.
[![N|Solid](https://image.ibb.co/jOyKf6/crud_Estructurahbase.png)](https://github.com/ArturoGonzalezBecerril)

Definiciónes:
  - **Hadoop** : Herramienta de big data dividido en 2 partes(Datos y procesos)
  - **Hbase** : Motor de almacenamiento distribuido, orientado a columnas.
  - **Java** : Lenguaje de programación de proposito general para crear programas.
 
### Creando el proyecto con maven
```sh
arturo@arturo$ mvn archetype:generate -DgroupId=com.hbase.app -DartifactId=hbaseCrud -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
arturo@arturo$ ls -lhrt
drwxr-xr-x 3 bashroot bashroot 4.0K nov 25 09:21 hbaseCrud
arturo@arturo$ cd hbaseCrud
```

### Agregamos las dependencias al pom, quendando de la siguiente manera.
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.hbase.app</groupId>
    <artifactId>hbaseCrud</artifactId>
    <packaging>jar</packaging>
    <version>1.0-SNAPSHOT</version>
    <name>hbaseCrud</name>
    <url>http://maven.apache.org</url>
    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>3.8.1</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>2.7.3</version>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-mapreduce-client-common</artifactId>
            <version>2.7.3</version>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-mapreduce-client-core</artifactId>
            <version>2.7.3</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.hbase/hbase-client -->
        <dependency>
            <groupId>org.apache.hbase</groupId>
            <artifactId>hbase-client</artifactId>
            <version>1.2.2</version>
        </dependency>

    </dependencies>
</project>
```
#### La programación esta definida en multiples capas(Servicio y Datos)
[![N|Solid](https://image.ibb.co/bYJrtR/crud_Capas.png)](https://github.com/ArturoGonzalezBecerril)

### Estructura a nivel Proyecto
```sh

├── src
│  ├── main
│  │ └── java
│  │     └── com
│  │         └── hbase
│  │             └── app
│  │                 ├── dao
│  │                 └── service
│  └── test
│      └── java
│          └── com
│              └── hbase
│                  └── app
└── target
```

#### Definición de paquetes y clases.
```sh
arturo@arturo$ pwd
/home/arturo/Proyectos/hbaseCrud
arturo@arturo$ cd /src/main/java
#Crear 2 paquetes java
arturo@arturo$ mkdir /src/main/java/com/hbase/app/service
arturo@arturo$ mkdir /src/main/java/com/hbase/app/dao
#Crear las interfaces y sus implementaciones en java tanto para la capa de servicio como la de datos
arturo@arturo$ cd com/hbase/app/service
arturo@arturo$ touch PersonaService.java
arturo@arturo$ touch PersonaServiceImpl.java
arturo@arturo$ cd ..
arturo@arturo$ touch PersonaDao.java
arturo@arturo$ touch PersonaDaoImpl.java
```
### Las clases deben quedar de la siguiente manera
[![N|Solid](https://image.ibb.co/kUNHYR/paquetesy_Clases.png)](https://github.com/ArturoGonzalezBecerril)

#### El contenido de cada clase se muestra a continuación
##### PersonaService.java

```java
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
```
#### PersonaServiceImpl.java
```java
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
```
##### PersonaDao.java
```java
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
```
##### PersonaDaoImpl.java
```java
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
```
#### Test Unitario
Para poder realizar la prueba unitaria, es nesesario arrancar hadoop, por lo cual abrimos una terminal y lanzamos los siguientes comandos.
```sh
#Es nesesario tener registradas las variables de entorno Hadoop en /home/tuUsuario/.bashrc
#Arrancar Hdfs
arturo@arturo$ start-dfs.sh
#Arrancar el gestor de procesos Yarn
arturo@arturo$ start-yarn.sh
#Arrancar Hbase
arturo@arturo$ start-hbase.sh
#Validar que todos los procesos esten lanzados
arturo@arturo$ jps
9742 HMaster
7695 RemoteMavenServer
8496 NameNode
9670 HQuorumPeer
8821 SecondaryNameNode
7528 Main
8606 DataNode
9111 NodeManager
10003 Jps
9880 HRegionServer
9001 ResourceManager


```


##### TestHbase.java
```java
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
```
[![N|Solid](https://image.ibb.co/mgaeDR/hbase.png)](https://github.com/ArturoGonzalezBecerril)

Licencia
----
GNU
** Software Libre,  Yeah!**


