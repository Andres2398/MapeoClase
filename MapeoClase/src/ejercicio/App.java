package ejercicio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.io.RandomAccessFile;
import java.util.ArrayList;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {
	
	static int longitudRegistro = 16;
	static File fichero;
	static List<Punto> listaPunto;

	public static void main(String[] args) {

		File f = new File("./files/datos");
		fichero = new File(f.getAbsolutePath());

		listaPunto = new ArrayList<Punto>();
		Random r = new Random();
		int cantidadPuntos = r.nextInt(20);
		for (int i = 0; i < cantidadPuntos; i++) {
			listaPunto.add(new Punto(r.nextInt(30), r.nextInt(30), i));

		}

		fichero.delete();

		meterPuntosDisco();
		Scanner sc = new Scanner(System.in);
		while (true) {
			
			System.out.println();
			System.out.println("1. para modificar Punto");
			System.out.println("2. para borrar Punto");
			System.out.println("3. mostrarPuntos");
			String input = sc.nextLine();
			switch (input) {
			case "1": {
				introducirPunto();
				break;
			}
			case "2": {
				borrado();
				break;
			}
			case "3": {
				mostrarPuntos();
				break;
			}
			default:

			}
		}

	}

	private static void borrado() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce el id");
		int id = sc.nextInt();
		
		//preguntar porque se queda algo en el buffer si el scanner es static
	
		int contador = 0;
		try (RandomAccessFile file = new RandomAccessFile(fichero, "rw")) {
			
			while (true) {
				long posicion = (contador * longitudRegistro);
				file.seek(posicion + 12);
				int idfichero = file.readInt();
			
				if (id == idfichero) {
					file.seek(posicion);
					file.writeInt(0);
					return;
				}
				contador++;
			}
		}

		catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			
		}
	}

	private static void meterPuntosDisco() {

		try (RandomAccessFile file = new RandomAccessFile(fichero, "rw")) {

			for (Punto punto : listaPunto) {
				long posicion = punto.getId() * longitudRegistro;

				file.seek(posicion);
				file.writeInt(punto.getActivo());

				file.writeInt(punto.getX());
				System.out.println(punto.getX());
				file.writeInt(punto.getY());
				System.out.println(punto.getY());
				file.writeInt(punto.getId());

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void introducirPunto() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce x");
		int x = sc.nextInt();
		System.out.println("Introduce y");
		int y = sc.nextInt();

		comprobarPuntos(x, y);
	}

	private static void comprobarPuntos(int x, int y) {

		int xFichero;
		int yFichero;
		int id = -1;
		

		long posicion = 0;
		int contador = 1;
		try (RandomAccessFile file = new RandomAccessFile(fichero, "r")) {
			while (file.getFilePointer() < file.length()) {
				if (file.readInt() == 1) {
					xFichero = file.readInt();

					if (xFichero == x) {
						yFichero = file.readInt();

						if (yFichero == y) {
							id = file.readInt();
							
						}

					}
				}
				posicion = contador * longitudRegistro;
				file.seek(posicion);
				

				contador++;
			}

			if (id != -1) {
				System.out.println(listaPunto.get(id));
				modificarPunto(posicion);
			} else
				System.out.println("No se ha encontrado el punto");

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private static void modificarPunto(long posicion) {
		Scanner sc = new Scanner(System.in);
		System.out.println("¿Desea modificar el punto? (S/N)");
		if (sc.nextLine().equalsIgnoreCase("s")) {
			try (RandomAccessFile file = new RandomAccessFile(fichero, "rw")) {
				int nuevoX;
				int nuevoY;

				System.out.println("Introduce el nuevo X");
				nuevoX = sc.nextInt();
				System.out.println("Introduce el nuevo Y");
				nuevoY = sc.nextInt();

				file.seek(posicion - longitudRegistro);

				file.writeInt(nuevoX);
				file.writeInt(nuevoY);
				file.seek(posicion - longitudRegistro);
				System.out.println(file.readInt());
				System.out.println(file.readInt());
				int id = file.readInt();
				listaPunto.get(id).setX(nuevoX);
				listaPunto.get(id).setY(nuevoY);
				System.out.println(listaPunto.get(id));
			} catch (Exception e) {

			}
		}
	}

	static void mostrarPuntos() {
		try (RandomAccessFile file = new RandomAccessFile(fichero, "r")) {
			while (file.getFilePointer() < file.length()) {
				if (file.readInt() == 1) {
					int x = file.readInt();
					int y = file.readInt();
					int id = file.readInt();
					System.out.print("ID: " + id);
					System.out.print(" X: " + x);
					System.out.println(" Y: " + y);
				}

			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

		}
	}
}
