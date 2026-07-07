CREATE DATABASE IF NOT EXISTS student_registration_portal
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE student_registration_portal;

CREATE TABLE IF NOT EXISTS students (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    registration_number VARCHAR(50) NOT NULL UNIQUE,
    student_name VARCHAR(100) NOT NULL,
    student_email VARCHAR(100) NOT NULL UNIQUE,
    student_phone VARCHAR(20) NULL,
    student_address TEXT NULL,
    date_of_birth DATE NULL,
    gender ENUM('Male', 'Female', 'Other') NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    
    PRIMARY KEY (id),
    INDEX idx_students_registration_number (registration_number),
    INDEX idx_students_student_name (student_name),
    INDEX idx_students_student_email (student_email),
    INDEX idx_students_gender (gender)
    
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = 'Stores all registered student information';

CREATE TABLE IF NOT EXISTS courses (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(200) NOT NULL,
    course_description TEXT NULL,
    credits TINYINT UNSIGNED NOT NULL CHECK (credits > 0),
    department VARCHAR(100) NOT NULL,
    lecturer VARCHAR(100) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    
    PRIMARY KEY (id),
    INDEX idx_courses_course_code (course_code),
    INDEX idx_courses_course_name (course_name),
    INDEX idx_courses_department (department)
    
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = 'Stores all course information';

CREATE TABLE IF NOT EXISTS enrollments (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    student_id BIGINT UNSIGNED NOT NULL,
    course_id BIGINT UNSIGNED NOT NULL,
    semester VARCHAR(50) NOT NULL,
    academic_year VARCHAR(20) NOT NULL,
    enrollment_status ENUM('Active', 'Completed', 'Dropped', 'Pending') 
        NOT NULL DEFAULT 'Active',
    grade VARCHAR(5) NULL,
    enrollment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    
    PRIMARY KEY (id),
    UNIQUE KEY unique_enrollment 
        (student_id, course_id, semester, academic_year),
    
    CONSTRAINT fk_enrollments_student 
        FOREIGN KEY (student_id) 
        REFERENCES students (id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
        
    CONSTRAINT fk_enrollments_course 
        FOREIGN KEY (course_id) 
        REFERENCES courses (id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    
    INDEX idx_enrollments_student_id (student_id),
    INDEX idx_enrollments_course_id (course_id),
    INDEX idx_enrollments_semester (semester),
    INDEX idx_enrollments_academic_year (academic_year),
    INDEX idx_enrollments_enrollment_status (enrollment_status)
    
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = 'Stores student course enrollments';

CREATE TABLE IF NOT EXISTS audit_log (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    table_name VARCHAR(50) NOT NULL,
    action ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    record_id BIGINT UNSIGNED NOT NULL,
    old_data JSON NULL,
    new_data JSON NULL,
    performed_by VARCHAR(100) NULL,
    action_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    PRIMARY KEY (id),
    INDEX idx_audit_log_table_action (table_name, action),
    INDEX idx_audit_log_action_time (action_time)
    
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = 'Audit trail for all data changes';

DELIMITER $$

CREATE TRIGGER students_before_insert
    BEFORE INSERT ON students
    FOR EACH ROW
BEGIN
    IF NEW.registration_number IS NULL OR NEW.registration_number = '' THEN
        SET NEW.registration_number = CONCAT(
            'REG', 
            DATE_FORMAT(NOW(), '%Y'), 
            LPAD(FLOOR(RAND() * 99999), 5, '0')
        );
    END IF;
END$$

CREATE TRIGGER audit_students_insert
    AFTER INSERT ON students
    FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, record_id, new_data, performed_by)
    VALUES (
        'students', 
        'INSERT', 
        NEW.id, 
        JSON_OBJECT(
            'registration_number', NEW.registration_number,
            'student_name', NEW.student_name,
            'student_email', NEW.student_email
        ),
        USER()
    );
END$$

CREATE TRIGGER audit_students_update
    AFTER UPDATE ON students
    FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, record_id, old_data, new_data, performed_by)
    VALUES (
        'students', 
        'UPDATE', 
        NEW.id, 
        JSON_OBJECT(
            'registration_number', OLD.registration_number,
            'student_name', OLD.student_name,
            'student_email', OLD.student_email
        ),
        JSON_OBJECT(
            'registration_number', NEW.registration_number,
            'student_name', NEW.student_name,
            'student_email', NEW.student_email
        ),
        USER()
    );
END$$

CREATE TRIGGER audit_students_delete
    AFTER DELETE ON students
    FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, record_id, old_data, performed_by)
    VALUES (
        'students', 
        'DELETE', 
        OLD.id, 
        JSON_OBJECT(
            'registration_number', OLD.registration_number,
            'student_name', OLD.student_name,
            'student_email', OLD.student_email
        ),
        USER()
    );
END$$

CREATE TRIGGER audit_courses_insert
    AFTER INSERT ON courses
    FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, record_id, new_data, performed_by)
    VALUES (
        'courses', 
        'INSERT', 
        NEW.id, 
        JSON_OBJECT(
            'course_code', NEW.course_code,
            'course_name', NEW.course_name,
            'credits', NEW.credits,
            'department', NEW.department
        ),
        USER()
    );
END$$

CREATE TRIGGER audit_courses_update
    AFTER UPDATE ON courses
    FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, record_id, old_data, new_data, performed_by)
    VALUES (
        'courses', 
        'UPDATE', 
        NEW.id, 
        JSON_OBJECT(
            'course_code', OLD.course_code,
            'course_name', OLD.course_name,
            'credits', OLD.credits
        ),
        JSON_OBJECT(
            'course_code', NEW.course_code,
            'course_name', NEW.course_name,
            'credits', NEW.credits
        ),
        USER()
    );
END$$

CREATE TRIGGER audit_courses_delete
    AFTER DELETE ON courses
    FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, record_id, old_data, performed_by)
    VALUES (
        'courses', 
        'DELETE', 
        OLD.id, 
        JSON_OBJECT(
            'course_code', OLD.course_code,
            'course_name', OLD.course_name
        ),
        USER()
    );
END$$

CREATE TRIGGER audit_enrollments_insert
    AFTER INSERT ON enrollments
    FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, record_id, new_data, performed_by)
    VALUES (
        'enrollments', 
        'INSERT', 
        NEW.id, 
        JSON_OBJECT(
            'student_id', NEW.student_id,
            'course_id', NEW.course_id,
            'semester', NEW.semester,
            'academic_year', NEW.academic_year,
            'enrollment_status', NEW.enrollment_status
        ),
        USER()
    );
END$$

CREATE TRIGGER audit_enrollments_update
    AFTER UPDATE ON enrollments
    FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, record_id, old_data, new_data, performed_by)
    VALUES (
        'enrollments', 
        'UPDATE', 
        NEW.id, 
        JSON_OBJECT(
            'enrollment_status', OLD.enrollment_status,
            'grade', OLD.grade
        ),
        JSON_OBJECT(
            'enrollment_status', NEW.enrollment_status,
            'grade', NEW.grade
        ),
        USER()
    );
END$$

CREATE TRIGGER audit_enrollments_delete
    AFTER DELETE ON enrollments
    FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, action, record_id, old_data, performed_by)
    VALUES (
        'enrollments', 
        'DELETE', 
        OLD.id, 
        JSON_OBJECT(
            'student_id', OLD.student_id,
            'course_id', OLD.course_id,
            'semester', OLD.semester
        ),
        USER()
    );
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE add_student(
    IN p_registration_number VARCHAR(50),
    IN p_student_name VARCHAR(100),
    IN p_student_email VARCHAR(100),
    IN p_student_phone VARCHAR(20),
    IN p_student_address TEXT,
    IN p_date_of_birth DATE,
    IN p_gender VARCHAR(10),
    OUT p_student_id BIGINT UNSIGNED,
    OUT p_error_message VARCHAR(255)
)
sp_begin: BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_error_message = 'Database error occurred.';
        SET p_student_id = NULL;
    END;
    
    START TRANSACTION;
    
    IF p_student_email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$' THEN
        SET p_error_message = 'Invalid email format.';
        SET p_student_id = NULL;
        ROLLBACK;
        LEAVE sp_begin;
    END IF;
    
    IF p_registration_number IS NOT NULL AND p_registration_number != '' THEN
        IF EXISTS (SELECT 1 FROM students WHERE registration_number = p_registration_number) THEN
            SET p_error_message = 'Registration number already exists.';
            SET p_student_id = NULL;
            ROLLBACK;
            LEAVE sp_begin;
        END IF;
    END IF;
    
    IF EXISTS (SELECT 1 FROM students WHERE student_email = p_student_email) THEN
        SET p_error_message = 'Email already registered.';
        SET p_student_id = NULL;
        ROLLBACK;
        LEAVE sp_begin;
    END IF;
    
    INSERT INTO students (
        registration_number, student_name, student_email,
        student_phone, student_address, date_of_birth, gender
    ) VALUES (
        p_registration_number, p_student_name, p_student_email,
        p_student_phone, p_student_address, p_date_of_birth, p_gender
    );
    
    SET p_student_id = LAST_INSERT_ID();
    SET p_error_message = NULL;
    
    COMMIT;
END$$

CREATE PROCEDURE get_student_enrollments(
    IN p_student_id BIGINT UNSIGNED
)
BEGIN
    SELECT 
        e.id AS enrollment_id,
        c.course_code,
        c.course_name,
        c.credits,
        c.department,
        e.semester,
        e.academic_year,
        e.enrollment_status,
        e.grade,
        e.enrollment_date,
        e.updated_at AS enrollment_updated_at
    FROM enrollments e
    INNER JOIN courses c ON e.course_id = c.id
    WHERE e.student_id = p_student_id
    ORDER BY e.academic_year DESC, e.semester ASC;
END$$

CREATE PROCEDURE get_course_enrollments(
    IN p_course_id BIGINT UNSIGNED
)
BEGIN
    SELECT 
        e.id AS enrollment_id,
        s.student_name,
        s.registration_number,
        s.student_email,
        e.semester,
        e.academic_year,
        e.enrollment_status,
        e.grade,
        e.enrollment_date
    FROM enrollments e
    INNER JOIN students s ON e.student_id = s.id
    WHERE e.course_id = p_course_id
    ORDER BY s.student_name ASC;
END$$

CREATE PROCEDURE search_students(
    IN p_search_term VARCHAR(100)
)
BEGIN
    SET p_search_term = CONCAT('%', p_search_term, '%');
    
    SELECT 
        id,
        registration_number,
        student_name,
        student_email,
        student_phone,
        date_of_birth,
        gender,
        created_at
    FROM students
    WHERE student_name LIKE p_search_term
       OR registration_number LIKE p_search_term
    ORDER BY student_name ASC;
END$$

CREATE PROCEDURE get_student_summary(
    IN p_student_id BIGINT UNSIGNED
)
BEGIN
    SELECT 
        s.id,
        s.registration_number,
        s.student_name,
        s.student_email,
        COUNT(DISTINCT e.id) AS total_courses_enrolled,
        SUM(CASE WHEN e.enrollment_status = 'Active' THEN 1 ELSE 0 END) AS active_courses,
        SUM(CASE WHEN e.enrollment_status = 'Completed' THEN 1 ELSE 0 END) AS completed_courses,
        SUM(CASE WHEN e.enrollment_status = 'Dropped' THEN 1 ELSE 0 END) AS dropped_courses,
        SUM(CASE WHEN e.enrollment_status = 'Pending' THEN 1 ELSE 0 END) AS pending_courses
    FROM students s
    LEFT JOIN enrollments e ON s.id = e.student_id
    WHERE s.id = p_student_id
    GROUP BY s.id, s.registration_number, s.student_name, s.student_email;
END$$

CREATE PROCEDURE enroll_student(
    IN p_student_id BIGINT UNSIGNED,
    IN p_course_id BIGINT UNSIGNED,
    IN p_semester VARCHAR(50),
    IN p_academic_year VARCHAR(20),
    IN p_enrollment_status VARCHAR(20),
    OUT p_enrollment_id BIGINT UNSIGNED,
    OUT p_error_message VARCHAR(255)
)
sp_begin: BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_error_message = 'Database error occurred.';
        SET p_enrollment_id = NULL;
    END;
    
    START TRANSACTION;
    
    IF NOT EXISTS (SELECT 1 FROM students WHERE id = p_student_id) THEN
        SET p_error_message = 'Student not found.';
        SET p_enrollment_id = NULL;
        ROLLBACK;
        LEAVE sp_begin;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM courses WHERE id = p_course_id) THEN
        SET p_error_message = 'Course not found.';
        SET p_enrollment_id = NULL;
        ROLLBACK;
        LEAVE sp_begin;
    END IF;
    
    IF EXISTS (
        SELECT 1 FROM enrollments 
        WHERE student_id = p_student_id 
          AND course_id = p_course_id 
          AND semester = p_semester 
          AND academic_year = p_academic_year
    ) THEN
        SET p_error_message = 'Student already enrolled in this course for this semester.';
        SET p_enrollment_id = NULL;
        ROLLBACK;
        LEAVE sp_begin;
    END IF;
    
    INSERT INTO enrollments (
        student_id, course_id, semester, academic_year, enrollment_status
    ) VALUES (
        p_student_id, p_course_id, p_semester, p_academic_year,
        COALESCE(p_enrollment_status, 'Active')
    );
    
    SET p_enrollment_id = LAST_INSERT_ID();
    SET p_error_message = NULL;
    
    COMMIT;
END$$

CREATE PROCEDURE update_enrollment_status(
    IN p_enrollment_id BIGINT UNSIGNED,
    IN p_new_status VARCHAR(20),
    IN p_grade VARCHAR(5),
    OUT p_error_message VARCHAR(255)
)
sp_begin: BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_error_message = 'Database error occurred.';
    END;
    
    START TRANSACTION;
    
    IF p_new_status NOT IN ('Active', 'Completed', 'Dropped', 'Pending') THEN
        SET p_error_message = 'Invalid enrollment status.';
        ROLLBACK;
        LEAVE sp_begin;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM enrollments WHERE id = p_enrollment_id) THEN
        SET p_error_message = 'Enrollment not found.';
        ROLLBACK;
        LEAVE sp_begin;
    END IF;
    
    UPDATE enrollments 
    SET 
        enrollment_status = p_new_status,
        grade = COALESCE(p_grade, grade),
        updated_at = NOW()
    WHERE id = p_enrollment_id;
    
    SET p_error_message = NULL;
    COMMIT;
END$$

DELIMITER ;

CREATE OR REPLACE VIEW vw_student_enrollment_summary AS
SELECT 
    s.id AS student_id,
    s.registration_number,
    s.student_name,
    s.student_email,
    COUNT(e.id) AS total_courses,
    SUM(CASE WHEN e.enrollment_status = 'Active' THEN 1 ELSE 0 END) AS active_courses,
    SUM(CASE WHEN e.enrollment_status = 'Completed' THEN 1 ELSE 0 END) AS completed_courses,
    SUM(CASE WHEN e.enrollment_status = 'Dropped' THEN 1 ELSE 0 END) AS dropped_courses,
    SUM(CASE WHEN e.enrollment_status = 'Pending' THEN 1 ELSE 0 END) AS pending_courses,
    COUNT(DISTINCT e.academic_year) AS years_enrolled
FROM students s
LEFT JOIN enrollments e ON s.id = e.student_id
GROUP BY s.id, s.registration_number, s.student_name, s.student_email;

CREATE OR REPLACE VIEW vw_course_enrollment_summary AS
SELECT 
    c.id AS course_id,
    c.course_code,
    c.course_name,
    c.department,
    c.credits,
    COUNT(e.id) AS total_enrolled,
    SUM(CASE WHEN e.enrollment_status = 'Active' THEN 1 ELSE 0 END) AS active_enrollments,
    SUM(CASE WHEN e.enrollment_status = 'Completed' THEN 1 ELSE 0 END) AS completed_enrollments,
    COUNT(DISTINCT e.academic_year) AS years_offered
FROM courses c
LEFT JOIN enrollments e ON c.id = e.course_id
GROUP BY c.id, c.course_code, c.course_name, c.department, c.credits;

CREATE OR REPLACE VIEW vw_recent_enrollments AS
SELECT 
    e.id AS enrollment_id,
    s.registration_number,
    s.student_name,
    c.course_code,
    c.course_name,
    e.semester,
    e.academic_year,
    e.enrollment_status,
    e.enrollment_date
FROM enrollments e
INNER JOIN students s ON e.student_id = s.id
INNER JOIN courses c ON e.course_id = c.id
ORDER BY e.enrollment_date DESC
LIMIT 100;

INSERT INTO students (registration_number, student_name, student_email, student_phone, student_address, date_of_birth, gender) VALUES
('REG2024001', 'John Mwangi', 'john.mwangi@example.com', '+254712345678', '123 University Way, Nairobi', '2000-01-15', 'Male'),
('REG2024002', 'Sarah Wanjiru', 'sarah.wanjiru@example.com', '+254723456789', '456 Campus Road, Nairobi', '2001-03-22', 'Female'),
('REG2024003', 'Michael Odhiambo', 'michael.odhiambo@example.com', '+254734567890', '789 Faculty Lane, Nairobi', '1999-07-10', 'Male'),
('REG2024004', 'Grace Akinyi', 'grace.akinyi@example.com', '+254745678901', '321 Student Plaza, Nairobi', '2000-11-05', 'Female'),
('REG2024005', 'James Omondi', 'james.omondi@example.com', '+254756789012', '456 Student Village, Nairobi', '2001-09-30', 'Male'),
('REG2024006', 'Mary Chelangat', 'mary.chelangat@example.com', '+254767890123', '789 Education Drive, Nairobi', '2000-06-18', 'Female');

INSERT INTO courses (course_code, course_name, course_description, credits, department, lecturer) VALUES
('CS101', 'Introduction to Programming', 'Basic programming concepts using Java', 3, 'Computer Science', 'Dr. Jane Smith'),
('CS102', 'Object-Oriented Programming', 'Advanced programming with OOP principles', 4, 'Computer Science', 'Dr. Peter Ochieng'),
('CS201', 'Data Structures and Algorithms', 'Efficient data organization and manipulation', 4, 'Computer Science', 'Prof. Mary Wanjiru'),
('CS301', 'Database Systems', 'Design and implementation of relational databases', 4, 'Computer Science', 'Dr. Samuel Kiprop'),
('MATH101', 'Calculus I', 'Introduction to differential and integral calculus', 3, 'Mathematics', 'Dr. Samuel Kiprop'),
('MATH201', 'Linear Algebra', 'Vector spaces, matrices, and linear transformations', 3, 'Mathematics', 'Dr. Anne Kamau'),
('PHYS101', 'Physics I', 'Introduction to classical mechanics and thermodynamics', 3, 'Physics', 'Dr. Anne Kamau');

INSERT INTO enrollments (student_id, course_id, semester, academic_year, enrollment_status) VALUES
(1, 1, 'Semester 1', '2024/2025', 'Active'),
(1, 2, 'Semester 1', '2024/2025', 'Active'),
(1, 5, 'Semester 1', '2024/2025', 'Active'),
(2, 1, 'Semester 1', '2024/2025', 'Active'),
(2, 3, 'Semester 2', '2024/2025', 'Pending'),
(2, 6, 'Semester 2', '2024/2025', 'Active'),
(3, 2, 'Semester 2', '2024/2025', 'Active'),
(3, 4, 'Semester 2', '2024/2025', 'Active'),
(3, 5, 'Semester 2', '2024/2025', 'Active'),
(4, 1, 'Semester 1', '2024/2025', 'Active'),
(4, 7, 'Semester 2', '2024/2025', 'Active'),
(5, 2, 'Semester 1', '2024/2025', 'Active'),
(5, 3, 'Semester 1', '2024/2025', 'Active'),
(6, 4, 'Semester 1', '2024/2025', 'Active');

DELIMITER $$

CREATE FUNCTION get_student_full_name(p_student_id BIGINT UNSIGNED)
RETURNS VARCHAR(100)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_name VARCHAR(100);
    
    SELECT student_name INTO v_name
    FROM students
    WHERE id = p_student_id;
    
    RETURN v_name;
END$$

CREATE FUNCTION get_course_name_by_id(p_course_id BIGINT UNSIGNED)
RETURNS VARCHAR(200)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_name VARCHAR(200);
    
    SELECT course_name INTO v_name
    FROM courses
    WHERE id = p_course_id;
    
    RETURN v_name;
END$$

CREATE FUNCTION get_student_age(p_student_id BIGINT UNSIGNED)
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_dob DATE;
    DECLARE v_age INT;
    
    SELECT date_of_birth INTO v_dob
    FROM students
    WHERE id = p_student_id;
    
    IF v_dob IS NOT NULL THEN
        SET v_age = TIMESTAMPDIFF(YEAR, v_dob, CURDATE());
    ELSE
        SET v_age = NULL;
    END IF;
    
    RETURN v_age;
END$$


CREATE FUNCTION get_total_credits_for_student(p_student_id BIGINT UNSIGNED)
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_total INT;
    
    SELECT SUM(c.credits) INTO v_total
    FROM enrollments e
    INNER JOIN courses c ON e.course_id = c.id
    WHERE e.student_id = p_student_id
      AND e.enrollment_status = 'Active';
    
    RETURN IFNULL(v_total, 0);
END$$

CREATE FUNCTION get_gpa_for_student(p_student_id BIGINT UNSIGNED)
RETURNS DECIMAL(3,2)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_total_points DECIMAL(10,2);
    DECLARE v_total_credits INT;
    DECLARE v_gpa DECIMAL(3,2);
    
    SELECT 
        SUM(
            CASE 
                WHEN e.grade = 'A' THEN c.credits * 4.0
                WHEN e.grade = 'B' THEN c.credits * 3.0
                WHEN e.grade = 'C' THEN c.credits * 2.0
                WHEN e.grade = 'D' THEN c.credits * 1.0
                WHEN e.grade = 'F' THEN c.credits * 0.0
                ELSE 0
            END
        ),
        SUM(CASE 
            WHEN e.grade IN ('A', 'B', 'C', 'D', 'F') THEN c.credits
            ELSE 0
        END)
    INTO v_total_points, v_total_credits
    FROM enrollments e
    INNER JOIN courses c ON e.course_id = c.id
    WHERE e.student_id = p_student_id
      AND e.enrollment_status = 'Completed';
    
    IF v_total_credits > 0 THEN
        SET v_gpa = ROUND(v_total_points / v_total_credits, 2);
    ELSE
        SET v_gpa = 0.00;
    END IF;
    
    RETURN v_gpa;
END$$

CREATE FUNCTION is_student_enrolled_in_course(
    p_student_id BIGINT UNSIGNED,
    p_course_id BIGINT UNSIGNED
)
RETURNS BOOLEAN
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_count INT;
    
    SELECT COUNT(*) INTO v_count
    FROM enrollments
    WHERE student_id = p_student_id
      AND course_id = p_course_id;
    
    RETURN v_count > 0;
END$$

CREATE FUNCTION get_student_status_summary(p_student_id BIGINT UNSIGNED)
RETURNS VARCHAR(200)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_active INT;
    DECLARE v_completed INT;
    DECLARE v_dropped INT;
    DECLARE v_pending INT;
    DECLARE v_summary VARCHAR(200);
    
    SELECT 
        SUM(CASE WHEN enrollment_status = 'Active' THEN 1 ELSE 0 END),
        SUM(CASE WHEN enrollment_status = 'Completed' THEN 1 ELSE 0 END),
        SUM(CASE WHEN enrollment_status = 'Dropped' THEN 1 ELSE 0 END),
        SUM(CASE WHEN enrollment_status = 'Pending' THEN 1 ELSE 0 END)
    INTO v_active, v_completed, v_dropped, v_pending
    FROM enrollments
    WHERE student_id = p_student_id;
    
    SET v_summary = CONCAT(
        'Active: ', IFNULL(v_active, 0),
        ', Completed: ', IFNULL(v_completed, 0),
        ', Dropped: ', IFNULL(v_dropped, 0),
        ', Pending: ', IFNULL(v_pending, 0)
    );
    
    RETURN v_summary;
END$$

CREATE FUNCTION get_course_enrollment_count(p_course_id BIGINT UNSIGNED)
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_count INT;
    
    SELECT COUNT(*) INTO v_count
    FROM enrollments
    WHERE course_id = p_course_id
      AND enrollment_status = 'Active';
    
    RETURN v_count;
END$$

CREATE FUNCTION get_course_completion_rate(p_course_id BIGINT UNSIGNED)
RETURNS DECIMAL(5,2)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_total INT;
    DECLARE v_completed INT;
    DECLARE v_rate DECIMAL(5,2);
    
    SELECT COUNT(*) INTO v_total
    FROM enrollments
    WHERE course_id = p_course_id;
    
    SELECT COUNT(*) INTO v_completed
    FROM enrollments
    WHERE course_id = p_course_id
      AND enrollment_status = 'Completed';
    
    IF v_total > 0 THEN
        SET v_rate = ROUND((v_completed / v_total) * 100, 2);
    ELSE
        SET v_rate = 0.00;
    END IF;
    
    RETURN v_rate;
END$$

CREATE FUNCTION get_next_registration_number()
RETURNS VARCHAR(50)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_current_year VARCHAR(4);
    DECLARE v_next_number INT;
    DECLARE v_reg_number VARCHAR(50);
    
    SET v_current_year = DATE_FORMAT(CURDATE(), '%Y');
    
    SELECT COALESCE(MAX(CAST(SUBSTRING(registration_number, 8) AS UNSIGNED)), 0) + 1
    INTO v_next_number
    FROM students
    WHERE registration_number LIKE CONCAT('REG', v_current_year, '%');
    
    SET v_reg_number = CONCAT(
        'REG',
        v_current_year,
        LPAD(v_next_number, 5, '0')
    );
    
    RETURN v_reg_number;
END$$

CREATE FUNCTION count_students_by_gender(p_gender VARCHAR(10))
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_count INT;
    
    SELECT COUNT(*) INTO v_count
    FROM students
    WHERE gender = p_gender;
    
    RETURN v_count;
END$$

CREATE FUNCTION get_student_rank(p_student_id BIGINT UNSIGNED)
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_rank INT;
    
    SELECT COUNT(*) + 1 INTO v_rank
    FROM (
        SELECT s.id, get_gpa_for_student(s.id) AS gpa
        FROM students s
        WHERE get_gpa_for_student(s.id) > get_gpa_for_student(p_student_id)
          AND get_gpa_for_student(s.id) > 0
    ) AS ranked_students;
    
    RETURN IFNULL(v_rank, 1);
END$$

CREATE FUNCTION is_email_valid(p_email VARCHAR(100))
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    RETURN p_email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$';
END$$

CREATE FUNCTION format_student_name(p_name VARCHAR(100))
RETURNS VARCHAR(100)
DETERMINISTIC
BEGIN
    DECLARE v_result VARCHAR(100);
    
    SET v_result = LOWER(p_name);
    SET v_result = CONCAT(
        UPPER(SUBSTRING(v_result, 1, 1)),
        SUBSTRING(v_result, 2)
    );


    RETURN v_result;
END$$

CREATE FUNCTION get_enrollment_duration_months(p_student_id BIGINT UNSIGNED)
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_first_enrollment TIMESTAMP;
    DECLARE v_months INT;
    
    SELECT MIN(enrollment_date) INTO v_first_enrollment
    FROM enrollments
    WHERE student_id = p_student_id;
    
    IF v_first_enrollment IS NOT NULL THEN
        SET v_months = TIMESTAMPDIFF(MONTH, v_first_enrollment, NOW());
    ELSE
        SET v_months = 0;
    END IF;
    
    RETURN v_months;
END$$

DELIMITER ;


SHOW TABLES;

SELECT COUNT(*) AS total_students FROM students;

SELECT COUNT(*) AS total_courses FROM courses;

SELECT COUNT(*) AS total_enrollments FROM enrollments;

SELECT * FROM students;

SELECT * FROM courses;

SELECT * FROM enrollments;

SELECT * FROM vw_student_enrollment_summary;

SELECT * FROM vw_course_enrollment_summary;

CALL add_student('REG2024007', 'Test Student', 'test@example.com', '+254700000000', 'Test Address', '2000-01-01', 'Male', @student_id, @error);

SELECT @student_id, @error;

CALL search_students('John');

CALL get_student_enrollments(1);

CALL get_student_summary(1);

SELECT '----- FUNCTION TESTS -----' AS '';

SELECT get_student_full_name(1) AS 'Student Name';

SELECT get_course_name_by_id(1) AS 'Course Name';

SELECT get_student_age(1) AS 'Age';

SELECT get_total_credits_for_student(1) AS 'Total Credits';

SELECT get_gpa_for_student(1) AS 'GPA';

SELECT is_student_enrolled_in_course(1, 3) AS 'Enrolled in CS201?';

SELECT get_student_status_summary(1) AS 'Status Summary';

SELECT get_course_enrollment_count(1) AS 'CS101 Enrollments';

SELECT get_course_completion_rate(1) AS 'CS101 Completion Rate';

SELECT get_next_registration_number() AS 'Next Registration Number'; 
