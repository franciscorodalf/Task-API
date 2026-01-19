package com.docencia.tasks.business;

import java.util.List;
import java.util.Optional;

import com.docencia.tasks.domain.model.Task;


public interface ITaskService {

  /**
   * Crea una nueva tarea.
   *
   * @param task Tarea a crear.
   * @return Tarea creada.
   */
  Task create(Task task);

  /**
   * Obtiene todas las tareas.
   *
   * @return Lista de tareas.
   */
  List<Task> getAll();

  /**
   * Obtiene una tarea por su ID.
   *
   * @param id ID de la tarea.
   * @return Opcional con la tarea si existe, o vacío si no existe.
   */
  Optional<Task> getById(Long id);

  /**
   * Actualiza una tarea existente.
   *
   * @param id   ID de la tarea a actualizar.
   * @param task Tarea con los datos a actualizar.
   * @return Opcional con la tarea actualizada si existe, o vacío si no existe.
   */
  Optional<Task> update(Long id, Task patch);

  /**
   * Elimina una tarea por su ID.
   *
   * @param id ID de la tarea a eliminar.
   * @return true si se eliminó, false si no existe.
   */
  boolean delete(Long id);
}
