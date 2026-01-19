package com.docencia.tasks.business;

import org.springframework.stereotype.Service;

import com.docencia.tasks.adapters.out.persistence.ITaskPersistenceAdapter;
import com.docencia.tasks.domain.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de tareas.
 */
@Service
public class TaskService implements ITaskService {

  private final ITaskPersistenceAdapter repo;

  public TaskService(ITaskPersistenceAdapter repo) {
    this.repo = repo;
  }

  /**
   * Crea una nueva tarea.
   *
   * @param task Tarea a crear.
   * @return Tarea creada con su ID asignado.
   */
  public Task create(Task task) {
    task.setId(null);
    return repo.save(task);
  }

  /**
   * Obtiene todas las tareas.
   *
   * @return Lista de tareas.
   */
  public List<Task> getAll() {
    return repo.findAll();
  }

  /**
   * Obtiene una tarea por su ID.
   *
   * @param id ID de la tarea.
   * @return Tarea encontrada o vacío.
   */
  public Optional<Task> getById(Long id) {
    return repo.findById(id);
  }

  /**
   * Actualiza parcialmente una tarea.
   *
   * @param id    ID de la tarea.
   * @param patch Datos a actualizar.
   * @return Tarea actualizada o vacío si no existe.
   */
  public Optional<Task> update(Long id, Task patch) {
    return repo.findById(id).map(existing -> {
      if (patch.getTitle() != null)
        existing.setTitle(patch.getTitle());
      if (patch.getDescription() != null)
        existing.setDescription(patch.getDescription());
      existing.setCompleted(patch.isCompleted());
      return repo.save(existing);
    });
  }

  /**
   * Elimina una tarea.
   *
   * @param id ID de la tarea.
   * @return true si fue eliminada, false si no existía.
   */
  public boolean delete(Long id) {
    if (!repo.existsById(id))
      return false;
    repo.deleteById(id);
    return true;
  }
}
