package com.docencia.tasks;

import com.docencia.tasks.adapters.in.api.TaskRequest;
import com.docencia.tasks.adapters.in.api.TaskResponse;
import com.docencia.tasks.adapters.out.persistence.TaskJpaEntity;
import com.docencia.tasks.domain.model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataObjectsCoverageTest {

    /**
     * Test para verificar la funcionalidad de TaskRequest
     */
    @Test
    void testTaskRequest() {
        TaskRequest req = new TaskRequest();
        req.setTitle("T");
        req.setDescription("D");
        req.setCompleted(true);

        assertEquals("T", req.getTitle());
        assertEquals("D", req.getDescription());
        assertEquals(true, req.getCompleted());
    }

    /**
     * Test para verificar la funcionalidad de TaskResponse
     */
    @Test
    void testTaskResponse() {
        TaskResponse res = new TaskResponse(1L, "T", "D", true);
        assertEquals(1L, res.getId());
        assertEquals("T", res.getTitle());
        assertEquals("D", res.getDescription());
        assertTrue(res.isCompleted());

        TaskResponse e = new TaskResponse();
        e.setId(2L);
        e.setTitle("T2");
        e.setDescription("D2");
        e.setCompleted(false);

        assertEquals(2L, e.getId());
        assertEquals("T2", e.getTitle());
        assertEquals("D2", e.getDescription());
        assertFalse(e.isCompleted());

        TaskResponse res2 = new TaskResponse(1L, "T", "D", true);
        assertEquals(res, res2);
        assertEquals(res.hashCode(), res2.hashCode());
        assertNotNull(res.toString());

        assertNotEquals(res, null);
        assertNotEquals(res, new Object());

        TaskResponse res3 = new TaskResponse(2L, "T", "D", true);
        assertNotEquals(res, res3);

        TaskResponse res4 = new TaskResponse(1L, "X", "D", true);
        assertNotEquals(res, res4);

        TaskResponse res5 = new TaskResponse(1L, "T", "X", true);
        assertNotEquals(res, res5);

        TaskResponse res6 = new TaskResponse(1L, "T", "D", false);
        assertNotEquals(res, res6);
    }

    /**
     * Test para verificar la funcionalidad de TaskDomain
     */
    @Test
    void testTaskDomain() {
        Task t = new Task(1L, "T", "D", true);
        assertEquals(1L, t.getId());
        assertEquals("T", t.getTitle());
        assertEquals("D", t.getDescription());
        assertTrue(t.isCompleted());

        t.setId(2L);
        t.setTitle("T2");
        t.setDescription("D2");
        t.setCompleted(false);

        assertEquals(2L, t.getId());
        assertEquals("T2", t.getTitle());
        assertEquals("D2", t.getDescription());
        assertFalse(t.isCompleted());

        Task empty = new Task();
        assertNull(empty.getId());
    }

    /**
     * Test para verificar la funcionalidad de TaskJpaEntity
     */
    @Test
    void testTaskJpaEntity() {
        TaskJpaEntity e = new TaskJpaEntity();
        e.setId(1L);
        e.setTitle("T");
        e.setDescription("D");
        e.setCompleted(true);

        assertEquals(1L, e.getId());
        assertEquals("T", e.getTitle());
        assertEquals("D", e.getDescription());
        assertTrue(e.isCompleted());

        TaskJpaEntity e2 = new TaskJpaEntity(1L, "T", "D", true);
        assertEquals(e.getId(), e2.getId());
        assertEquals(e.getTitle(), e2.getTitle());

        assertNotNull(e.toString());
    }
}
