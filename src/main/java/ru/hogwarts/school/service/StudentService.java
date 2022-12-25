package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        student.setId(null);
        return studentRepository.save(student);
    }

    public Student read(long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student editStudent(long id, Student student) {
        Student oldStudent = read(id);
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        return studentRepository.save(oldStudent);
    }

    public Student deleteStudent(long id) {
        Student student = read(id);
        studentRepository.delete(student);
        return student;
    }

    // Service
    public Collection<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }
}
