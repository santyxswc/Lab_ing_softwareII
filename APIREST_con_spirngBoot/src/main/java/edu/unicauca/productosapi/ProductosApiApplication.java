/**
 * @file ProductosApiApplication.java
 * @brief Punto de entrada de la API REST de productos.
 * @author Santiago Caicedo
 * @author Adrian Araujo
 * @author Ivan Alexander Lopez
 */
package edu.unicauca.productosapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @brief Aplicación Spring Boot.
 *
 * Levanta el servidor en el puerto 8080 con la API en /api/productos, la interfaz web de prueba en la raíz
 * y la consola de H2 en /h2-console.
 */
@SpringBootApplication
public class ProductosApiApplication {

	/**
	 * @brief Inicia la aplicación.
	 * @param args Argumentos de la línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProductosApiApplication.class, args);
	}

}
