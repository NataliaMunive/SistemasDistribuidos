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


package com.mycompany.clientezookeeperbasico;

/**
 *
 * @author natalia
 */

import org.apache.zookeeper.*; // Importar las clases de Zookeeper
import java.io.IOException; // Importar la clase IOException para manejar errores de entrada/salida

public class ClienteZooKeeperBasico implements Watcher { // Implementa la interfaz Watcher

    private static final String ZOOKEEPER_ADDRESS = "localhost:2181"; // Dirección del servidor Zookeeper
    private static final int SESSION_TIMEOUT = 3000; // Tiempo de espera de la sesión en milisegundos, si se pasa de este tiempo sin recibir respuesta, la sesión expira
    private ZooKeeper zooKeeper; // Instancia de la clase ZooKeeper

    public static void main(String[] arg) throws IOException, InterruptedException, KeeperException { // Método principal
        // IOException: para manejar errores de entrada/salida
        // InterruptedException: para manejar interrupciones en los hilos
        // KeeperException: para manejar errores propias de Zookeeper

        ClienteZooKeeperBasico clienteBasico = new ClienteZooKeeperBasico(); // Crear una instancia del cliente Zookeeper
        clienteBasico.connectToZookeeper(); // metodo para conectarse al servidor Zookeeper
        clienteBasico.run(); // metodo para mantener la conexión activa
        clienteBasico.close(); // metodo para cerrar la conexión 
        System.out.println("Desconectado del servidor Zookeeper. Terminando la aplicación cliente."); // Mensaje de desconexión
    }

    public void connectToZookeeper() throws IOException { // Conectarse al servidor Zookeeper
        this.zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, SESSION_TIMEOUT, this); // Crear una nueva instancia de ZooKeeper
    }

    private void run() throws InterruptedException { // Mantener la conexión activa
        synchronized (zooKeeper) { // Sincronizar en el objeto zooKeeper
            zooKeeper.wait(); // Esperar hasta que se reciba una notificación para cerrar la conexión
        }
    }

    private void close() throws InterruptedException { // Cerrar la conexión con Zookeeper
        this.zooKeeper.close(); // Cerrar la conexión con Zookeeper
    }

    @Override // sobrescribir el método process de la interfaz Watcher
    // unico método de la interfaz Watcher, que maneja los eventos de Zookeeper
    public void process(WatchedEvent event) { 
        switch (event.getType()) { // Manejar diferentes tipos de eventos 
            case None: // Evento de conexión
                if (event.getState() == Event.KeeperState.SyncConnected) { // Si el estado es SyncConnected, la conexión se ha establecido correctamente
                    System.out.println("Conectado exitosamente a Zookeeper"); // Mensaje de conexión exitosa
                } else { // Si el estado no es SyncConnected, la conexión se ha perdido o cerrado
                    synchronized (zooKeeper) { // Sincronizar en el objeto zooKeeper
                        System.out.println("Desconectando de Zookeeper..."); // Mensaje de desconexión
                        zooKeeper.notifyAll(); // Notificar a todos los hilos que están esperando en el objeto zooKeeper
                    }
                }
        }
    }
}
