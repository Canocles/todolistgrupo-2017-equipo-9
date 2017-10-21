package services;

import javax.inject.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;

import models.Usuario;
import models.UsuarioRepository;
import models.Tablero;
import models.TableroRepository;

public class TableroService {
  UsuarioRepository usuarioRepository;
  TableroRepository tableroRepository;

  @Inject
  public TableroService(UsuarioRepository usuarioRepository, TableroRepository tableroRepository) {
     this.usuarioRepository = usuarioRepository;
     this.tableroRepository = tableroRepository;
  }

  public List<Tablero> allTablerosUsuario(Long idUsuario) {
     Usuario usuario = usuarioRepository.findById(idUsuario);
     if (usuario == null) {
        throw new UsuarioServiceException("No existe el usuario");
     }
     Set<Tablero> tableros = usuario.getAdministrados();
     List<Tablero> lista = new ArrayList<Tablero>(tableros);
     Collections.sort(lista, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
     return lista;
  }

  public Tablero crearTableroUsuario(String nombre, Long idUsuario) {
    System.out.println(idUsuario);
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if (usuario == null) {
      throw new UsuarioServiceException("No existe el usuario");
    }
    Tablero tablero = new Tablero(usuario, nombre);
    return tableroRepository.add(tablero);
  }

  public List<Tablero> obtenerTablerosAdministradosUsuario (Long idUsuario) {
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if (usuario == null) {
      throw new UsuarioServiceException("No existe el usuario");
    }
    Set<Tablero> tableros = usuario.getAdministrados();
    List<Tablero> lista = new ArrayList<Tablero>(tableros);
    Collections.sort(lista, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return lista;
  }
}
