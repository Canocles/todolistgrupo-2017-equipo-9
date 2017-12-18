package models;

import javax.persistence.*;

import play.data.format.*;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import java.text.SimpleDateFormat;


@Entity
public class Tarea {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)
   private Long id;
   private String titulo;

   @ManyToOne
   @JoinColumn(name="usuarioId")
   public Usuario usuario;

   @ManyToOne
   @JoinColumn(name="columnaId")
   public Columna columna;

   private Date fechaCreacion;

   @Formats.DateTime(pattern="dd-MM-yyyy") // para el formulario
   @Temporal(TemporalType.DATE)
   private Date fechaLimite;
   private Boolean terminada;

   @ManyToOne
   @JoinColumn(name="tableroId")
   public Tablero tablero;

   @ManyToMany(mappedBy="tareas", fetch=FetchType.EAGER)
   private Set<Etiqueta> etiquetas = new HashSet<Etiqueta>();

   public Tarea() {}

   public Tarea(Usuario usuario, String titulo, Date fechaLimite, Boolean terminada) {
      this.usuario = usuario;
      this.titulo = titulo;
      this.fechaCreacion = new Date();
      this.fechaLimite = fechaLimite;
      this.terminada = terminada;
   }

   public Tarea(Usuario usuario, String titulo, Date fechaLimite, Boolean terminada, Tablero tablero) {
      this.usuario = usuario;
      this.titulo = titulo;
      this.fechaCreacion = new Date();
      this.fechaLimite = fechaLimite;
      this.terminada = terminada;
      this.tablero = tablero;
  }

   // Getters y setters necesarios para JPA

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getTitulo() {
      return titulo;
   }

   public void setTitulo(String titulo) {
      this.titulo = titulo;
   }

   public Usuario getUsuario() {
      return usuario;
   }

   public void setUsuario(Usuario usuario) {
      this.usuario = usuario;
   }

   public Date getFechaLimite() {
      return fechaLimite;
   }

   public void setFechaLimite(Date fechaLimite) {
      this.fechaLimite = fechaLimite;
   }

   public String getFechaLimiteString() {
      if(fechaLimite != null){
        return new SimpleDateFormat("dd-MM-yyyy").format(fechaLimite);
      }
      return "";
   }

   public Boolean getTerminada() {
     return terminada;
   }

   public void setTerminada(Boolean terminada) {
     this.terminada = terminada;
   }

   public Columna getColumna(){
	   return columna;
   }

   public void setColumna(Columna columna){
	   this.columna = columna;
   }

   public Tablero getTablero() {
     return tablero;
   }

   public void setTablero(Tablero tablero) {
     this.tablero = tablero;
   }

   public String toString() {
      return String.format("Tarea id: %s titulo: %s fechaLimite: %s usuario: %s",
                      id, titulo, fechaLimite, usuario.toString());
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = prime + ((titulo == null) ? 0 : titulo.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (getClass() != obj.getClass()) return false;
      Tarea other = (Tarea) obj;
      // Si tenemos los ID, comparamos por ID
      if (id != null && other.id != null)
      return ((long) id == (long) other.id);
      // sino comparamos por campos obligatorios
      else {
         if (titulo == null) {
            if (other.titulo != null) return false;
         } else if (!titulo.equals(other.titulo)) return false;
         if (usuario == null) {
            if (other.usuario != null) return false;
            else if (!usuario.equals(other.usuario)) return false;
         }
      }
      return true;
   }
}
