package ru.hogwarts.school.service;

import liquibase.pro.packaged.S;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student createStudent(Student student) {
        student.setId(null);
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student read(long id) {
        logger.error("There is not student with id = " + id);
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student editStudent(long id, Student student) {
        Student oldStudent = read(id);
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        oldStudent.setFaculty(student.getFaculty());
        logger.info("Was invoked method for edit student");
        return studentRepository.save(oldStudent);
    }

    public Student deleteStudent(long id) {
        Student student = read(id);
        studentRepository.delete(student);
        logger.info("Was invoked method for delete student");
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
            logger.debug("Student is empty");
            throw new StudentNotFoundException(id);
        }
        if(optionalAvatar.isEmpty()){
            logger.debug("Avatar is empty");
            throw new AvatarNotFoundException(avatarId);
        }
        Student student = optionalStudent.get();
        student.setAvatar(optionalAvatar.get());
        logger.info("Student with avatars added");
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

    public Collection<String> getByNameA() {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(s-> s.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getAverageAge() {
        return studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }

    public void printNames(String name){
        studentRepository.findAll().stream()
                .forEach(s -> System.out.println(s.getName()));
    }

    public synchronized void printSynchronizedNames(String name){
        studentRepository.findAll().stream()
                .forEach(s -> System.out.println(s.getName()));
    }
}
