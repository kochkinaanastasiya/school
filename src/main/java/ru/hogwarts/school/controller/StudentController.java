package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    //@GetMapping("/student/name/{name}")
    //public ResponseEntity<List<Student>> getStudentByName(@PathVariable ("name") String name){
    @GetMapping("/student")
    public ResponseEntity<List<Student>> getStudentByName(@RequestParam ("name") String name){
        List<Student> students = studentService.getStudentByName(name);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}/faculty")
    public Faculty getFacultyByStudent(@PathVariable long id) {
        return studentService.getFacultyByStudent(id);
    }

    @PatchMapping
    public Student patchStudentAvatar (@PathVariable long id, @RequestParam("avatarId") long avatarId) throws AvatarNotFoundException {
        return studentService.patchStudentAvatar(id, avatarId);
    }

    @GetMapping("/nameWithA")
    public ResponseEntity<Collection<String>> getStudentsByNameWithA(){
        Collection<String> studentsNameWithA = studentService.getByNameA();
        return ResponseEntity.ok(studentsNameWithA);
    }

    @GetMapping("/averageAge")
    public Double getAverageAgeOfStudent(){
        return studentService.getAverageAge();
    }

    @GetMapping("/parallel")
    public void parallelStreams(){

        studentService.printNames("Katya");
        studentService.printNames("Vika");

        new Thread(() -> {
            studentService.printNames("Maxim");
            studentService.printNames("Vasya");
        }).start();

        new Thread(() -> {
            studentService.printNames("Nastya");
            studentService.printNames("Petya");
        }).start();
    }

    @GetMapping("/synchronized")
    public void synchronizedStreams(String name){

        studentService.printSynchronizedNames("Katya");
        studentService.printSynchronizedNames("Vika");

        new Thread(() -> {
            studentService.printSynchronizedNames("Maxim");
            studentService.printSynchronizedNames("Vasya");
        }).start();

        new Thread(() -> {
            studentService.printSynchronizedNames("Nastya");
            studentService.printSynchronizedNames("Petya");
        }).start();
    }
}


