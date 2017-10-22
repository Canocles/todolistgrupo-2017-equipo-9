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

  private Usuario comprobarUsuarioExiste (Long idUsuario) {
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if (usuario == null) {
       throw new UsuarioServiceException("No existe el usuario");
    }
    return usuario;
  }

  private Tablero comprobarTableroExiste (Long idTablero) {
    Tablero tablero = tableroRepository.findById(idTablero);
    if (tablero == null) {
      throw new TableroServiceException("No existe el tablero");
    }
    return tablero;
  }

  public Tablero crearTableroUsuario(String nombre, Long idUsuario) {
    Usuario usuario = comprobarUsuarioExiste (idUsuario);
    Tablero tablero = new Tablero(usuario, nombre);
    return tableroRepository.add(tablero);
  }

  public List<Tablero> obtenerTablerosAdministradosUsuario (Long idUsuario) {
    Usuario usuario = comprobarUsuarioExiste (idUsuario);
    Set<Tablero> tableros = usuario.getAdministrados();
    List<Tablero> lista = new ArrayList<Tablero>(tableros);
    Collections.sort(lista, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return lista;
  }

  public List<Tablero> obtenerTablerosParticipaUsuario (Long idUsuario) {
    Usuario usuario = comprobarUsuarioExiste (idUsuario);
    Set<Tablero> tableros = usuario.getTableros();
    List<Tablero> lista = new ArrayList<Tablero>(tableros);
    Collections.sort(lista, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return lista;
  }

  public List<Tablero> obtenerTablerosNoParticipaNiAdministraUsuario (Long idUsuario) {
    Usuario usuario = comprobarUsuarioExiste (idUsuario);
    List<Tablero> tableros = tableroRepository.getAll();
    List<Tablero> lista = new ArrayList<Tablero>();
    for (Tablero tablero: tableros) {
      if (!usuario.getAdministrados().contains(tablero) &&
          !usuario.getTableros().contains(tablero) &&
          !lista.contains(tablero)) {
            lista.add(tablero);
      }
    }
    Collections.sort(lista, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return lista;
  }

  public Tablero obtenerDetalleDeTablero (Long idTablero) {
    Tablero tablero = comprobarTableroExiste (idTablero);
    return tablero;
  }

  public Tablero anyadirParticipanteTablero (Long idTablero, Long idUsuario) {
    Usuario usuario = comprobarUsuarioExiste (idUsuario);
    Tablero tablero = comprobarTableroExiste (idTablero);
    tablero.getParticipantes().add(usuario);
    return tableroRepository.update(tablero);
  }
}
