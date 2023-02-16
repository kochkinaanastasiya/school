package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RequestMapping("/faculty")
@RestController
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @GetMapping("{id}")
    public Faculty getFacultyInfo(@PathVariable Long id) {
        return facultyService.read(id);
    }

    @PutMapping()
    public Faculty editFaculty(@RequestBody Faculty faculty) {
        return facultyService.editFaculty(faculty.getId(), faculty);
    }

    @DeleteMapping("{id}")
    public Faculty deleteFaculty(@PathVariable Long id){
        return facultyService.deleteFaculty(id);
    }
    @GetMapping(params = "!colorOrName")
    public Collection<Faculty> findByColor(@RequestParam String color) {
        return facultyService.findByColor(color);
    }

    @GetMapping(params = "colorOrName")
    public Collection<Faculty> findByColorOrName(@RequestParam String colorOrName) {
        return facultyService.findByColorOrName(colorOrName);
    }

    @GetMapping("/{id}/students")
    public Collection<Student> getStudentsByFaculty(@PathVariable long id) {
        return facultyService.getStudentsByFaculty(id);
    }

    @GetMapping("/longestNameFaculty")
    public String getFacultyWithLongestName(){
        return facultyService.longestNameFaculty();
    }
}
