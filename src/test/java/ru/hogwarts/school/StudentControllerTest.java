package ru.hogwarts.school;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

//    @Autowired
//    private StudentRepository studentRepository;

//    @Autowired
//    private FacultyRepository facultyRepository;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final Faker faker = new Faker();

//    @AfterEach
//    public void afterEachDelete(){
//        studentRepository.deleteAll();
//        facultyRepository.deleteAll();
//    }

    @Test
    public void createStudentTest(){
        createStudentTest(generateStudent(createFaculty(generateFaculty())));

    }


    private Faculty createFaculty(Faculty faculty){
        ResponseEntity<Faculty> facultyResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        // выполнение запроса, респонс это результат запроса
        assertThat(facultyResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(facultyResponseEntity.getBody()).isNotNull();
        assertThat(facultyResponseEntity.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(faculty);
        assertThat(facultyResponseEntity.getBody().getId()).isNotNull();
        System.out.println(facultyResponseEntity.getBody().getColor());
        System.out.println(facultyResponseEntity.getBody().getName());

        return facultyResponseEntity.getBody();
    }

    @Test
    public Student createStudentTest(Student student){
        ResponseEntity<Student> studentResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/student/", student, Student.class);
        assertThat(studentResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentResponseEntity.getBody()).isNotNull();
        assertThat(studentResponseEntity.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(student);
        assertThat(studentResponseEntity.getBody().getId()).isNotNull();

        System.out.println(studentResponseEntity.getBody().getName());
        return studentResponseEntity.getBody();
    }

    @Test
    public void editStudentTest(){
        Faculty faculty1 = createFaculty(generateFaculty());
        Faculty faculty2 = createFaculty(generateFaculty());
        Student student = createStudentTest(generateStudent(faculty1));

        ResponseEntity<Student> getForEntityResponse = testRestTemplate.getForEntity("http://localhost:" + port + "/student/" + student.getId(), Student.class);
        assertThat(getForEntityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getForEntityResponse.getBody()).isNotNull();
        assertThat(getForEntityResponse.getBody()).usingRecursiveComparison().isEqualTo(student);
        assertThat(getForEntityResponse.getBody().getFaculty()).usingRecursiveComparison().isEqualTo(faculty1);

        student.setFaculty(faculty2);

        ResponseEntity<Student> recordResponseEntity = testRestTemplate.exchange("http://localhost:" + port + "/student/" + student.getId(), HttpMethod.PUT, new HttpEntity<>(student), Student.class);
        assertThat(recordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordResponseEntity.getBody()).isNotNull();
        assertThat(recordResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(student);
        assertThat(recordResponseEntity.getBody().getFaculty()).usingRecursiveComparison().isEqualTo(faculty2);
    }

    @Test
    public void findByAgeBetween(){
        List<Faculty> faculties = Stream.generate(this::generateFaculty)
                .limit(5)
                .map(this::createFaculty)
                .toList();
        List <Student> students = Stream.generate(()-> generateStudent(faculties.get(faker.random().nextInt(faculties.size()))))
                .limit(50)
                .map(this::createStudentTest)
                .toList();

        int minAge = 13;
        int maxAge = 16;

        List <Student> expectedStudents = students.stream()
                .filter(st -> st.getAge() >= minAge && st.getAge() <= maxAge)
                .toList();

        //ResponseEntity<List<Student>>  getForEntityResponse = testRestTemplate.exchange("http://localhost:" + port + "/student?minAge={minAge}&maxAge={maxAge}",
                // HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {} , minAge, maxAge);
        ResponseEntity<List<Student>>  getForEntityResponse = testRestTemplate.exchange("http://localhost:" + port + "/student?minAge=" + minAge + "&maxAge=" + maxAge,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {});

        assertThat(getForEntityResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getForEntityResponse.getBody()).hasSize(expectedStudents.size());
    }

    private Student generateStudent(Faculty faculty){
        Student student = new Student();
        student.setName(faker.harryPotter().character());
        student.setAge(faker.random().nextInt(12,20));
        if(faculty != null){
            student.setFaculty(faculty);
        }
        System.out.println(student);
        return student;
    }

    private Faculty generateFaculty(){
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().location());
        faculty.setColor(faker.color().name());
        System.out.println(faculty);
        return faculty;
    }
}

