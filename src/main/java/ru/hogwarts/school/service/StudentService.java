package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student createStudent(Student student) {
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
        oldStudent.setFaculty(student.getFaculty());
        return studentRepository.save(oldStudent);
    }

    public Student deleteStudent(long id) {
        Student student = read(id);
        studentRepository.delete(student);
        return student;
    }

    public Collection<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int minAge, int maxAge){
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    public Faculty getFacultyByStudent(long id) {
        return read(id).getFaculty();
    }

    public Student patchStudentAvatar (long id,long avatarId) throws AvatarNotFoundException {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        Optional<Avatar> optionalAvatar = avatarRepository.findById(avatarId);
        if(optionalStudent.isEmpty()){
            throw new StudentNotFoundException(id);
        }
        if(optionalAvatar.isEmpty()){
            throw new AvatarNotFoundException(avatarId);
        }
        Student student = optionalStudent.get();
        student.setAvatar(optionalAvatar.get());
        return studentRepository.save(student);
    }

    public List<Student> getStudentByName(String name){
        return studentRepository.getStudentByName(name);
    }

    public Long getAllStudentsOfAmount(){
        return studentRepository.getAllStudentsOfAmount();
    }

    public Long getAverageAgeStudents(){
        return studentRepository.getAverageAgeStudents();
    }

    public List<Student> getLastFiveStudents(){
        return studentRepository.getLastFiveStudents();
    }
}
