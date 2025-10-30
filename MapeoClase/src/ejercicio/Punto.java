package ejercicio;

public class Punto {
	private int id;
	private int x;
	private int y;
	private int activo;
	public Punto(int x, int y, int id) {

		this.x = x;
		this.y = y;
		this.id = id;
		activo=1;
	}

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		String cadena = "Punto: [ id: " + id + ", x: " + x + ", y:" + y +" ]";
		return cadena;
	}
}
