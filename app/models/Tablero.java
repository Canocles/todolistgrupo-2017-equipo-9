package models;

public class Tablero {
  private String nombre;
  public Usuario administrador;

  public Tablero (Usuario administrador, String nombre) {
    this.nombre = nombre;
    this.administrador = administrador;
  }

  public String getNombre () {
    return nombre;
  }

  public Usuario getAdministrador () {
    return administrador;
  }
}
