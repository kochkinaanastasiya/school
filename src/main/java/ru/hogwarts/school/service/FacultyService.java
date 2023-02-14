package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty (Faculty faculty) {
        faculty.setId(null);
        logger.info("Was invoked method for add faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty read (long id) {
        logger.error("There is not faculty with id = " + id);
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
    }

    public Faculty editFaculty(long id, Faculty faculty) {
        Faculty oldFaculty = read(id);
        oldFaculty.setName(faculty.getName());
        oldFaculty.setColor(faculty.getColor());
        logger.info("Was invoked method for edit faculty");
        return facultyRepository.save(oldFaculty);
    }

    public Faculty deleteFaculty(long id) {
        Faculty faculty = read(id);
        facultyRepository.delete(faculty);
        logger.info("Was invoked method for delete faculty");
        return faculty;
    }

    public Collection<Faculty> findByColor(String color) {
        return facultyRepository.findAllByColor(color);
        }


    public Collection<Faculty> findByColorOrName(String colorOrName) {
        return facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(colorOrName,colorOrName);
    }

    public Collection<Student> getStudentsByFaculty(long id) {
        Faculty faculty = read(id);
        return studentRepository.findAllByFaculty_Id(faculty.getId());
    }

    public ResponseEntity<String> longestNameFaculty(){
        String longestName = String.valueOf(facultyRepository
                .findAll()
                .stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length)));
        if(longestName.isEmpty()){
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(longestName);
    }
}
