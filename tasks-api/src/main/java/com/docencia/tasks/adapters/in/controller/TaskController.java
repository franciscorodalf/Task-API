package com.docencia.tasks.adapters.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.mapper.TaskMapper;
import com.docencia.tasks.business.ITaskService;
import com.docencia.tasks.domain.model.Task;

import java.util.List;

/**
 * Controlador REST para gestionar la API de tareas.
 * Provee endpoints para crear, leer, actualizar y eliminar tareas.
 */
@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Tasks API")
@CrossOrigin
public class TaskController {

  private final ITaskService service;
  private final TaskMapper mapper;

  /**
   * Constructor para inyección de dependencias.
   *
   * @param service Servicio de negocio de tareas.
   * @param mapper  Mapeador entre DTOs y dominio.
   */
  public TaskController(ITaskService service, TaskMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  /**
   * Obtiene todas las tareas registradas.
   *
   * @return Lista de respuestas de tareas.
   */
  @GetMapping
  @Operation(summary = "Get all tasks")
  public List<TaskResponse> getAll() {
    return service.getAll().stream().map(mapper::toResponse).toList();
  }

  /**
   * Obtiene una tarea específica por su ID.
   *
   * @param id ID de la tarea.
   * @return Respuesta de la tarea o 404 si no se encuentra.
   */
  @GetMapping("/{id}")
  @Operation(summary = "Get task by id")
  public ResponseEntity<TaskResponse> getById(@PathVariable Long id) {
    return service.getById(id)
        .map(mapper::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Crea una nueva tarea.
   *
   * @param request Datos de la tarea a crear.
   * @return Respuesta de la tarea creada con código 201.
   */
  @PostMapping
  @Operation(summary = "Create task")
  public ResponseEntity<TaskResponse> create(@RequestBody TaskRequest request) {
    Task created = service.create(mapper.toDomain(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
  }

  /**
   * Actualiza parcialmente una tarea existente.
   *
   * @param id      ID de la tarea a actualizar.
   * @param request Datos parciales a actualizar.
   * @return Respuesta de la tarea actualizada o 404 si no existe.
   */
  @PatchMapping("/{id}")
  @Operation(summary = "Update task (partial)")
  public ResponseEntity<TaskResponse> update(@PathVariable Long id, @RequestBody TaskRequest request) {
    Task patch = new Task();
    patch.setTitle(request.getTitle());
    patch.setDescription(request.getDescription());
    patch.setCompleted(Boolean.TRUE.equals(request.getCompleted()));

    return service.update(id, patch)
        .map(mapper::toResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Elimina una tarea por su ID.
   *
   * @param id ID de la tarea a eliminar.
   * @return 204 No Content si se eliminó, o 404 si no existe.
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete task")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    boolean deleted = service.delete(id);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
