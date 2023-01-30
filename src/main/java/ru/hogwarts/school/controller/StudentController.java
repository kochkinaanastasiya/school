package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RequestMapping("/student")
@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("{id}")
    public Student getStudentInfo(@PathVariable Long id) {
        return studentService.read(id);
    }

    @PutMapping("{id}")
    public Student editStudent(@PathVariable long id , @RequestBody Student student) {
        return studentService.editStudent(id, student);
    }

    @DeleteMapping("{id}")
    public Student deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping
    public Collection<Student> findStudents(@RequestParam int age) {
        return studentService.findByAge(age);
    }

    @GetMapping(params = {"minAge", "maxAge"})
    public Collection<Student> findByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public Faculty getFacultyByStudent(@PathVariable long id) {
        return studentService.getFacultyByStudent(id);
    }

    @PatchMapping
    public Student patchStudentAvatar (@PathVariable long id, @RequestParam("avatarId") long avatarId) throws AvatarNotFoundException {
        return studentService.patchStudentAvatar(id, avatarId);
    }
}

