package com.example.demo;

import com.example.demo.controller.StudentController;
import com.example.demo.modal.Student;
import com.example.demo.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class DemoApplicationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentRepository studentRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    private Student makeStudent(long id, String title, String desc, boolean published) {
        Student s = new Student(title, desc, published);
        try {
            Student.class.getMethod("setId", long.class).invoke(s, id);
        } catch (Exception ignore) { /* if no setter, ignore id asserts */ }
        return s;
    }

    // ---------- GET /api/students
    @Test
    @DisplayName("GET /api/students -> 200 with list")
    void getAllStudents_ok() throws Exception {
        List<Student> data = Arrays.asList(
                makeStudent(1L, "A", "a", true),
                makeStudent(2L, "B", "b", false)
        );
        given(studentRepository.findAll()).willReturn(data);

        mvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("A"))
                .andExpect(jsonPath("$[1].published").value(false));
    }

    // ---------- GET /api/students/{id}
    @Test
    @DisplayName("GET /api/students/{id} -> 200 when found")
    void getStudentById_found() throws Exception {
        Student s = makeStudent(10L, "Title", "Desc", true);
        given(studentRepository.findById(10L)).willReturn(Optional.of(s));

        mvc.perform(get("/api/students/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.published").value(true));
    }

    // ---------- POST /api/students
    @Test
    @DisplayName("POST /api/students -> 201 Created")
    void createStudent_created() throws Exception {
        Student request = new Student("New", "NewDesc", false);
        Student saved = makeStudent(100L, "New", "NewDesc", false);

        given(studentRepository.save(ArgumentMatchers.any(Student.class))).willReturn(saved);

        mvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New"));
    }

    // ---------- PUT /api/students/{id}
    @Test
    @DisplayName("PUT /api/students/{id} -> 200 when found and updated")
    void updateStudent_ok() throws Exception {
        Student existing = makeStudent(5L, "Old", "OldDesc", false);
        Student updateReq = new Student("NewTitle", "NewDesc", true);
        Student updated = makeStudent(5L, "NewTitle", "NewDesc", true);

        given(studentRepository.findById(5L)).willReturn(Optional.of(existing));
        given(studentRepository.save(any(Student.class))).willReturn(updated);

        mvc.perform(put("/api/students/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("NewTitle"))
                .andExpect(jsonPath("$.published").value(true));
    }

    @Test
    @DisplayName("PUT /api/students/{id} -> 404 when not found")
    void updateStudent_notFound() throws Exception {
        given(studentRepository.findById(123L)).willReturn(Optional.empty());

        Student req = new Student("T", "D", true);
        mvc.perform(put("/api/students/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    // ---------- DELETE /api/students/{id}
    @Test
    @DisplayName("DELETE /api/students/{id} -> 204")
    void deleteStudent_ok() throws Exception {
        doNothing().when(studentRepository).deleteById(7L);

        mvc.perform(delete("/api/students/7"))
                .andExpect(status().isNoContent());

        verify(studentRepository, times(1)).deleteById(7L);
    }

    // ---------- DELETE /api/students
    @Test
    @DisplayName("DELETE /api/students -> 204")
    void deleteAllStudents_ok() throws Exception {
        doNothing().when(studentRepository).deleteAll();

        mvc.perform(delete("/api/students"))
                .andExpect(status().isNoContent());

        verify(studentRepository, times(1)).deleteAll();
    }

    // ---------- GET /api/students/published
    @Test
    @DisplayName("GET /api/students/published -> 200 with list")
    void findByPublished_ok() throws Exception {
        List<Student> published = Arrays.asList(
                makeStudent(1L, "P1", "D1", true),
                makeStudent(2L, "P2", "D2", true)
        );
        given(studentRepository.findByPublished(true)).willReturn(published);

        mvc.perform(get("/api/students/published"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].published").value(true));
    }

    // ---------- GET /api/student?title=...
    @Test
    @DisplayName("GET /api/student?title=foo -> 200 with matches")
    void getAllStudentsByTitle_ok() throws Exception {
        List<Student> matches = Arrays.asList(
                makeStudent(3L, "foo bar", "x", false)
        );
        given(studentRepository.findByTitleContaining("foo")).willReturn(matches);

        mvc.perform(get("/api/student").param("title", "foo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("foo bar"));
    }
}
