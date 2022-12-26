package ru.hogwarts.school.service;

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

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty (Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    public Faculty read (long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
    }

    public Faculty editFaculty(long id, Faculty faculty) {
        Faculty oldFaculty = read(id);
        oldFaculty.setName(faculty.getName());
        oldFaculty.setColor(faculty.getColor());
        return facultyRepository.save(oldFaculty);
    }

    public Faculty deleteFaculty(long id) {
        Faculty faculty = read(id);
        facultyRepository.delete(faculty);
        return faculty;
    }

    public Collection<Faculty> findByColor(String color) {
        return facultyRepository.findAllByColor(color);
        }


    public Collection<Faculty> findByColorOrName(String color, String name) {
        return facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }

    public Collection<Student> getStudentsByFaculty(long id) {
        Faculty faculty = read(id);
        return studentRepository.findAllByFaculty_Id(faculty.getId());
    }

}
