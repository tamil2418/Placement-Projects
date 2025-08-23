package com.example.demo.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.demo.modal.Student;
import com.example.demo.repository.StudentRepository;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

	@GetMapping("/students")
	public ResponseEntity<List<Student>> getAllStudents() {
		try {
			List<Student> students = new ArrayList<Student>();

			studentRepository.findAll().forEach(students::add);

			if (students.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(students, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/students/{id}")
	public ResponseEntity<Student> getStudentById(@PathVariable("id") long id) {
		Optional<Student> studentData = studentRepository.findById(id);
		System.out.println(studentData);
		if (studentData.isPresent()) {
			return new ResponseEntity<>(studentData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/students")
	public ResponseEntity<Student> createStudent(@RequestBody Student student) {
		try {
			Student s1 = new Student(student.getTitle(), student.getDescription(), false);
			
			Student _student = studentRepository
					.save(s1);

			return new ResponseEntity<>(_student, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/students/{id}")
	public ResponseEntity<Student> updateStudent(@PathVariable("id") long id, @RequestBody Student student) {
		Optional<Student> studentData = studentRepository.findById(id);

		if (studentData.isPresent()) {
			Student _student = studentData.get();
			_student.setTitle(student.getTitle());
			_student.setDescription(student.getDescription());
			_student.setPublished(student.isPublished());
			return new ResponseEntity<>(studentRepository.save(_student), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/students/{id}")
	public ResponseEntity<HttpStatus> deleteStudent(@PathVariable("id") long id) {
		try {
			studentRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/students")
	public ResponseEntity<HttpStatus> deleteAllStudent() {
		try {
			studentRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/students/published")
	public ResponseEntity<List<Student>> findByPublished() {
		try {
			List<Student> student = studentRepository.findByPublished(true);

			if (student.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(student, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/student")
	public ResponseEntity<List<Student>> getAllStudentsByTitle(@RequestParam(required = true) String title) {
		try {
			List<Student> students = new ArrayList<Student>();

			studentRepository.findByTitleContaining(title).forEach(students::add);

			if (students.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(students, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
