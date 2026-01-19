package com.docencia.tasks.adapters.in.api;

public class TaskResponse {

  private Long id;

  private String title;

  private String description;

  private boolean completed;

  /**
   * Constructor vacio
   */
  public TaskResponse() {
  }

  /**
   * Constructor con todos los parametros.
   *
   * @param id          ID de la tarea.
   * @param title       Título.
   * @param description Descripción.
   * @param completed   Estado de completitud.
   */
  public TaskResponse(Long id, String title, String description, boolean completed) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    TaskResponse that = (TaskResponse) o;
    return completed == that.completed &&
        java.util.Objects.equals(id, that.id) &&
        java.util.Objects.equals(title, that.title) &&
        java.util.Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(id, title, description, completed);
  }

  @Override
  public String toString() {
    return "TaskResponse{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", completed=" + completed +
        '}';
  }
}
