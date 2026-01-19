package com.docencia.tasks.adapters.out.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.docencia.tasks.adapters.mapper.TaskMapper;
import com.docencia.tasks.domain.model.Task;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskPersistenceAdapterTest {

  @Mock
  private TaskRepositoryRepository jpaRepo;

  @Mock
  private TaskMapper mapper;

  @InjectMocks
  private TaskPersistenceAdapter adapter;

  /**
   * Test para obtener todas las tareas
   */
  @Test
  void findAllMapsEntitiesToDomainTest() {
    TaskJpaEntity e = new TaskJpaEntity(1L, "t", "d", false);
    when(jpaRepo.findAll()).thenReturn(List.of(e));

    Task task = new Task(1L, "t", "d", false);
    when(mapper.toDomain(e)).thenReturn(task);

    List<Task> result = adapter.findAll();

    assertEquals(1, result.size());
    verify(jpaRepo).findAll();
    verify(mapper).toDomain(e);
  }

  /**
   * Test para obtener una tarea por ID
   */ 
  @Test
  void findByIdMapsOptionalTest() {
    TaskJpaEntity taskEntity = new TaskJpaEntity(1L, "t", "d", false);
    when(jpaRepo.findById(1L)).thenReturn(Optional.of(taskEntity));
    when(mapper.toDomain(taskEntity)).thenReturn(new Task(1L, "t", "d", false));

    assertTrue(adapter.findById(1L).isPresent());
    verify(jpaRepo).findById(1L);
  }

  /**
   * Test para guardar una tarea
   */ 
  @Test
  void save_convertsAndPersists() {
    Task task = new Task(null, "t", "d", false);
    TaskJpaEntity entity = new TaskJpaEntity(null, "t", "d", false);
    TaskJpaEntity savedEntity = new TaskJpaEntity(1L, "t", "d", false);
    Task savedTask = new Task(1L, "t", "d", false);

    when(mapper.toJpa(task)).thenReturn(entity);
    when(jpaRepo.save(entity)).thenReturn(savedEntity);
    when(mapper.toDomain(savedEntity)).thenReturn(savedTask);

    Task result = adapter.save(task);

    assertEquals(1L, result.getId());
    verify(jpaRepo).save(entity);
  }

  /**
   * Test para eliminar una tarea
   */   
  @Test
  void deleteById_delegatesToRepo() {
    doNothing().when(jpaRepo).deleteById(1L);
    adapter.deleteById(1L);
    verify(jpaRepo).deleteById(1L);
  }

  /**
   * Test para verificar si una tarea existe
   */ 
  @Test
  void existsById_delegatesToRepo() {
    when(jpaRepo.existsById(1L)).thenReturn(true);
    assertTrue(adapter.existsById(1L));
    verify(jpaRepo).existsById(1L);
  }
}
