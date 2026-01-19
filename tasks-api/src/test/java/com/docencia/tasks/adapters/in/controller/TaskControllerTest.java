package com.docencia.tasks.adapters.in.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.mapper.TaskMapper;
import com.docencia.tasks.business.ITaskService;
import com.docencia.tasks.domain.model.Task;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

  @Mock
  private ITaskService service;

  @Mock
  private TaskMapper mapper;

  @InjectMocks
  private TaskController controller;

  @Test
  void getAll_returnsMappedResponses() {
    Task task = new Task(1L, "a", "b", false);
    when(service.getAll()).thenReturn(List.of(task));
    when(mapper.toResponse(task)).thenReturn(new TaskResponse(1L, "a", "b", false));

    List<TaskResponse> res = controller.getAll();

    assertEquals(1, res.size());
    assertEquals(1L, res.get(0).getId());
    verify(service).getAll();
    verify(mapper).toResponse(task);
  }

  /**
   * Test para obtener una tarea por ID
   */
  @Test
  void getById_returns200_whenFound() {
    Task task = new Task(1L, "a", "b", false);
    when(service.getById(1L)).thenReturn(Optional.of(task));
    when(mapper.toResponse(task)).thenReturn(new TaskResponse(1L, "a", "b", false));

    var resp = controller.getById(1L);

    assertEquals(200, resp.getStatusCode().value());
    assertNotNull(resp.getBody());
    assertEquals(1L, resp.getBody().getId());
  }

  /**
   * Test para obtener una tarea por ID
   */
  @Test
  void getById_returns404_whenNotFound() {
    when(service.getById(10L)).thenReturn(Optional.empty());

    var resp = controller.getById(10L);

    assertEquals(404, resp.getStatusCode().value());
  }

  /**
   * Test para crear una tarea
   */
  @Test
  void create_returns201_andBody() {
    TaskRequest req = new TaskRequest();
    req.setTitle("t");
    req.setDescription("d");
    req.setCompleted(false);

    Task domain = new Task(null, "t", "d", false);
    Task saved = new Task(1L, "t", "d", false);

    when(mapper.toDomain(req)).thenReturn(domain);
    when(service.create(domain)).thenReturn(saved);
    when(mapper.toResponse(saved)).thenReturn(new TaskResponse(1L, "t", "d", false));

    var resp = controller.create(req);

    assertEquals(201, resp.getStatusCode().value());
    assertNotNull(resp.getBody());
    assertEquals(1L, resp.getBody().getId());
  }

  /**
   * Test para actualizar una tarea
   */
  @Test
  void update_returns200_whenFound() {
    Long id = 1L;
    TaskRequest req = new TaskRequest();
    req.setTitle("New Title");
    req.setCompleted(true);

    Task updatedTask = new Task(id, "New Title", null, true);

    when(service.update(eq(id), any(Task.class))).thenReturn(Optional.of(updatedTask));
    when(mapper.toResponse(updatedTask)).thenReturn(new TaskResponse(id, "New Title", null, true));

    var resp = controller.update(id, req);

    assertEquals(200, resp.getStatusCode().value());
    assertEquals("New Title", resp.getBody().getTitle());
    verify(service).update(eq(id), any(Task.class));
  }

  /**
   * Test para actualizar una tarea
   */
  @Test
  void update_returns404_whenNotFound() {
    Long id = 99L;
    TaskRequest req = new TaskRequest();

    when(service.update(eq(id), any(Task.class))).thenReturn(Optional.empty());

    var resp = controller.update(id, req);

    assertEquals(404, resp.getStatusCode().value());
  }

  /**
   * Test para eliminar una tarea
   */
  @Test
  void delete_returns204_whenFound() {
    when(service.delete(1L)).thenReturn(true);

    var resp = controller.delete(1L);

    assertEquals(204, resp.getStatusCode().value());
  }

  /**
   * Test para eliminar una tarea
   */
  @Test
  void delete_returns404_whenNotFound() {
    when(service.delete(99L)).thenReturn(false);

    var resp = controller.delete(99L);

    assertEquals(404, resp.getStatusCode().value());
  }
}
