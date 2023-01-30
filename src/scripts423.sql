SELECT stubent.name, student.age, faculty.name,
FROM student
         INNER JOIN faculties ON student.faculties_id = faculties.id;

SELECT stubent.name, student.age,
FROM student
         INNER JOIN avatar a ON student.id = a.student.id;