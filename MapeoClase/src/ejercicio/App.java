package ejercicio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.RandomAccessFile;
import java.util.ArrayList;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {
	static int longitudRegistro = 12;
	static File fichero;
	static List<Punto> listaPunto;

	public static void main(String[] args) {
		File f = new File("./files/datos");
		fichero = new File(f.getAbsolutePath());

		listaPunto = new ArrayList<Punto>();
		Random r = new Random();
		int cantidadPuntos = r.nextInt(50);
		for (int i = 0; i < cantidadPuntos; i++) {
			listaPunto.add(new Punto(r.nextInt(30), r.nextInt(30), i));

		}

		fichero.delete();

		meterPuntosDisco();

		introducirPunto();
	}

	private static void meterPuntosDisco() {

		try (RandomAccessFile file = new RandomAccessFile(fichero, "rw")) {

			for (Punto punto : listaPunto) {
				long posicion = punto.getId() * longitudRegistro;

				file.seek(posicion);

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
		boolean terminar = false;

		long posicion = 0;
		int contador = 1;
		try (RandomAccessFile file = new RandomAccessFile(fichero, "r")) {
			while (!terminar) {

				xFichero = file.readInt();

				if (xFichero == x) {
					yFichero = file.readInt();

					if (yFichero == y) {
						id = file.readInt();
						terminar = true;
					}

				}
				posicion = contador * longitudRegistro;
				file.seek(posicion);
				if (file.getFilePointer() == file.length())
					terminar = true;

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
				int id=file.readInt();
				listaPunto.get(id).setX(nuevoX);
				listaPunto.get(id).setY(nuevoY);
				System.out.println(listaPunto.get(id));
			} catch (Exception e) {

			}
		}
	}

}
