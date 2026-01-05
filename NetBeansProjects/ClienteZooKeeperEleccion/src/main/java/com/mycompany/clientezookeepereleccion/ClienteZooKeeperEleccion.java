/*
 *  MIT License
 *
 *  Copyright (c) 2019 Michael Pogrebinsky - Distributed Systems & Cloud Computing with Java
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.mycompany.clientezookeepereleccion;

// ================= IMPORTS =================

import org.apache.zookeeper.*; // Importar las clases de ZooKeeper
import java.io.IOException; // Importar la clase IOException para manejar errores de entrada/salida
import java.util.List; // Importar List para manejar colecciones de nodos
import java.util.Collections; // Importar Collections para ordenar listas

// ================= CLASE PRINCIPAL =================

public class ClienteZooKeeperEleccion implements Watcher { // Implementa la interfaz Watcher

    // Dirección del servidor ZooKeeper (localhost y puerto por defecto)
    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";

    // Tiempo de espera de la sesión en milisegundos
    // Si se pasa de este tiempo sin recibir respuesta, la sesión expira
    private static final int SESSION_TIMEOUT = 3000;

    // Instancia de la clase ZooKeeper
    private ZooKeeper zooKeeper;

    // Namespace donde se realizará la elección de líder
    private static final String ELECTION_NAMESPACE = "/eleccion";

    // Nombre del znode creado por este cliente
    private String currentZnodeName;

    // ================= MÉTODO MAIN =================

    public static void main(String[] arg)
            throws IOException, InterruptedException, KeeperException {
        // IOException: para manejar errores de entrada/salida
        // InterruptedException: para manejar interrupciones en los hilos
        // KeeperException: para manejar errores propios de ZooKeeper

        // Crear una instancia del cliente ZooKeeper
        ClienteZooKeeperEleccion clienteBasico = new ClienteZooKeeperEleccion();

        // Conectarse al servidor ZooKeeper
        clienteBasico.connectToZookeeper();

        // Registrarse como candidato a líder creando un znode efímero secuencial
        clienteBasico.voluntarioParaLiderazgo();

        // Determinar si este cliente es el líder
        clienteBasico.liderElecto();

        // Mantener la conexión activa
        clienteBasico.run();

        // Cerrar la conexión con ZooKeeper
        clienteBasico.close();

        // Mensaje final de desconexión
        System.out.println("Desconectado del servidor Zookeeper. Terminando la aplicación cliente.");
    }

    // ================= CONEXION A ZOOKEEPER =================

    // Conectarse al servidor ZooKeeper
    public void connectToZookeeper() throws IOException {

        // Crear una nueva instancia de ZooKeeper
        // Se pasa la dirección del servidor, el timeout y el watcher (this)
        this.zooKeeper = new ZooKeeper(
                ZOOKEEPER_ADDRESS,
                SESSION_TIMEOUT,
                this
        );
    }

    // Mantener la conexión activa
    private void run() throws InterruptedException {

        // Se sincroniza sobre el objeto zooKeeper
        synchronized (zooKeeper) {

            // El hilo queda en espera hasta recibir una notificación
            zooKeeper.wait();
        }
    }

    // Cerrar la conexión con ZooKeeper
    private void close() throws InterruptedException {

        // Cerrar la sesión con ZooKeeper
        this.zooKeeper.close();
    }

    // ================= ELECCIÓN DE LÍDER =================

    // Método para que el cliente se registre como candidato a líder
    public void voluntarioParaLiderazgo()
            throws KeeperException, InterruptedException {

        // Se define el prefijo del znode que se creará dentro del namespace /eleccion
        // El sufijo numérico será asignado automáticamente por ZooKeeper
        String znodePrefix = ELECTION_NAMESPACE + "/c_";

        // Se crea un znode efímero secuencial
        // - Es efímero: se elimina cuando el cliente se desconecta
        // - Es secuencial: ZooKeeper añade un número incremental al nombre
        String znodeFullPath = zooKeeper.create(
                znodePrefix,                   // Ruta base del znode
                new byte[]{},                  // Datos almacenados (vacío)
                ZooDefs.Ids.OPEN_ACL_UNSAFE,    // Lista de control de acceso abierta
                CreateMode.EPHEMERAL_SEQUENTIAL // Tipo de znode
        );

        // Se imprime el nombre completo del znode creado
        System.out.println("Nombre del znode: " + znodeFullPath);

        // Se guarda únicamente el nombre del znode sin el prefijo /eleccion/
        // Esto se usa posteriormente para comparar si este cliente es el líder
        this.currentZnodeName = znodeFullPath.replace("/eleccion/", "");
    }

    // Método que determina si el cliente actual es el líder
    public void liderElecto()
            throws KeeperException, InterruptedException {

        // Se obtiene la lista de nodos hijos que existen dentro del namespace /eleccion
        List<String> children = zooKeeper.getChildren(ELECTION_NAMESPACE, false);

        // Se ordena la lista de nodos de forma ascendente
        // El nodo con el número secuencial más pequeño será el líder
        Collections.sort(children);

        // Se obtiene el primer elemento de la lista (el menor)
        String smallestChild = children.get(0);

        // Se compara si el znode actual coincide con el menor
        if (smallestChild.equals(currentZnodeName)) {

            // Si coincide, este cliente es el líder
            System.out.println("Yo soy el lider");
            return;
        }

        // Si no coincide, otro cliente es el líder
        System.out.println("Yo no soy el lider, " + smallestChild + " es el lider");
    }

    // ================= WATCHER =================

    @Override // Sobrescribir el método process de la interfaz Watcher
    // Único método de la interfaz Watcher, maneja los eventos de ZooKeeper
    public void process(WatchedEvent event) {

        // Manejar diferentes tipos de eventos
        switch (event.getType()) {

            case None: // Evento de conexión

                // Si el estado es SyncConnected, la conexión fue exitosa
                if (event.getState() == Event.KeeperState.SyncConnected) {

                    System.out.println("Conectado exitosamente a Zookeeper");

                } else {

                    // Si el estado no es SyncConnected, la conexión se cerró
                    synchronized (zooKeeper) {

                        System.out.println("Desconectando de Zookeeper...");

                        // Notificar a los hilos en espera
                        zooKeeper.notifyAll();
                    }
                }
        }
    }
}
