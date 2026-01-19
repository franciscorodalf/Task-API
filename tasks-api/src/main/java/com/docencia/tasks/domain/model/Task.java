package com.docencia.tasks.domain.model;

public class Task {

  private Long id;
  private String title;
  private String description;
  private boolean completed;

  /**
   * Constructor vacio
   */
  public Task() {
  }

  /**
   * Constructor con todos los parametros
   */
  public Task(Long id, String title, String description, boolean completed) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.completed = completed;
  }

  /**
   * GETTERS AND SETTERS
   */

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }
}
