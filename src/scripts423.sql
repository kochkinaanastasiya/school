SELECT student.name, student.age, faculties.name,
FROM student
         LEFT JOIN faculties ON student.faculties_id = faculties.id;

SELECT student.name, student.age,
FROM student
         INNER JOIN avatar ON student.avatar_id = avatar.id;
