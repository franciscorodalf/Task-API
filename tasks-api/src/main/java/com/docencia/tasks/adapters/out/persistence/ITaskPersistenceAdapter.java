package com.docencia.tasks.adapters.out.persistence;

import java.util.List;
import java.util.Optional;

import com.docencia.tasks.domain.model.Task;

public interface ITaskPersistenceAdapter {

  /**
   * Guarda una tarea en la base de datos.
   *
   * @param task Tarea a guardar.
   * @return Tarea guardada.
   */
  Task save(Task task);

  /**
   * Obtiene todas las tareas de la base de datos.
   *
   * @return Lista de tareas.
   */
  List<Task> findAll();

  /**
   * Obtiene una tarea por su ID.
   *
   * @param id ID de la tarea.
   * @return Opcional con la tarea si existe, o vacío si no existe.
   */
  Optional<Task> findById(Long id);

  /**
   * Elimina una tarea por su ID.
   *
   * @param id ID de la tarea a eliminar.
   */
  void deleteById(Long id);

  /**
   * Verifica si una tarea existe por su ID.
   *
   * @param id ID de la tarea.
   * @return true si existe, false si no existe.
   */
  boolean existsById(Long id);
}
