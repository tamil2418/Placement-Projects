package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.modal.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByPublished(boolean published);
	List<Student> findByTitleContaining(String title);
}
