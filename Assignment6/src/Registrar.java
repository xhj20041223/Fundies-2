import tester.Tester;
import java.util.function.*;

// represents a course 
class Course {
  // the name of this course
  String name;
  // the professor teaching this course, cannot have no professor
  Instructor prof;
  // the students enrolled in this course
  IList<Student> students;

  // the constructor
  Course(String name, Instructor prof, IList<Student> students) {
    // checks that it is valid professor
    if (prof == null) {
      throw new IllegalArgumentException("Course must have a valid professor.");
    }
    this.name = name;
    this.prof = prof;
    this.students = students;
    // adds this course to the instructor's list of courses
    this.prof.addCourse(this);
  }

  // convenience constructor
  Course(String name, Instructor prof) {
    // checks that it is valid professor
    if (prof == null) {
      throw new IllegalArgumentException("Course must have a valid professor.");
    }
    this.name = name;
    this.prof = prof;
    this.students = new MtList<Student>();
    // adds this course to the instructor's list of courses
    this.prof.addCourse(this);
  }

  // EFFECT: adds the given student to the list of students taking this course
  void addStudent(Student s) {
    this.students = this.students.add(s);
  }
}

// represents an instructor 
class Instructor {
  // the name of this instructor
  String name;
  // the courses taught by this professor, initially empty
  IList<Course> courses;

  // the constructor
  Instructor(String name, IList<Course> courses) {
    this.name = name;
    this.courses = courses;
  }

  // convenience constructor
  Instructor(String name) {
    this.name = name;
    this.courses = new MtList<Course>();
  }

  // adds the given course to this instructor's list of courses
  void addCourse(Course c) {
    this.courses = this.courses.add(c);
  }

  // determines whether the given Student is in more than one of this Instructor’s
  // Courses
  boolean dejavu(Student s) {
    // compares this instructor's list of courses with the given student's list of
    // courses return true if contains > 1 same course
    return s.inMoreThanOneCourse(this.courses);
  }
}

// represents a student 
class Student {
  // the name of this student
  String name;
  // the id number of this student
  int id;
  // the courses this student is taking, initially empty
  IList<Course> courses;

  // the constructor
  Student(String name, int id, IList<Course> courses) {
    this.name = name;
    this.id = id;
    this.courses = courses;
  }

  // convenience constructor
  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new MtList<Course>();
  }

  // enrolls this student in the given course
  // EFFECT: adds the given course to this student's list of courses and mutates
  // list of students for the course and corresponding professor
  void enroll(Course c) {
    // adds to this list of courses
    this.courses = this.courses.add(c);
    // adds this student to the list of students in the given course
    c.addStudent(this);
  }

  // determines whether the given Student is in any of the same classes as this
  // Student
  boolean classmates(Student s) {
    // compare course lists
    return s.courses.sharesElement(this.courses, new CourseEquals());
  }

  // determines whether this student is in more than one of the given list of
  // courses
  boolean inMoreThanOneCourse(IList<Course> profCourses) {
    return this.courses.numberOfSharedElements(profCourses, new CourseEquals()) > 1;
  }
}

class ExamplesRegistrar {
  // five Students, at least four Courses and at least two Instructors

  // students
  Student eric = new Student("Eric", 001, new MtList<Course>());
  Student sam = new Student("Sam", 001, new MtList<Course>());
  Student tina = new Student("Tina", 001, new MtList<Course>());
  Student sarah = new Student("Sarah", 001, new MtList<Course>());
  Student sky = new Student("Sky", 001, new MtList<Course>());

  // instructors
  Instructor prof1 = new Instructor("Park", new MtList<Course>());
  Instructor prof2 = new Instructor("Lerner", new MtList<Course>());

  // courses
  Course fundies1 = new Course("Fundies 1", prof1, new MtList<Student>());
  Course fundies2 = new Course("Fundies 2", prof2, new MtList<Student>());

  void initData() {
    // students
    Student eric = new Student("Eric", 001, new MtList<Course>());
    Student sam = new Student("Sam", 001, new MtList<Course>());
    Student tina = new Student("Tina", 001, new MtList<Course>());
    Student sarah = new Student("Sarah", 001, new MtList<Course>());
    Student sky = new Student("Sky", 001, new MtList<Course>());

    // instructors
    Instructor prof1 = new Instructor("Park", new MtList<Course>());
    Instructor prof2 = new Instructor("Lerner", new MtList<Course>());

    // courses
    Course fundies1 = new Course("Fundies 1", prof1, new MtList<Student>());
    Course fundies2 = new Course("Fundies 2", prof2, new MtList<Student>());
  }

  // ----------------------------------------------------------------------------------------------
  // Tests for enroll(Course c)

  void testEnroll(Tester t) {
    this.initData();
    eric.enroll(fundies1);
    sam.enroll(fundies2);
    IList<Student> fundies1Students = new ConsList<Student>(eric, new MtList<Student>());
    IList<Student> fundies2Students = new ConsList<Student>(sam, new MtList<Student>());
    t.checkExpect(fundies1, new Course("Fundies 1", prof1, fundies1Students));
    t.checkExpect(fundies2, new Course("Fundies 2", prof2, fundies2Students));
  }

  // ----------------------------------------------------------------------------------------------
  // Tests for classmates(Student s)

  /*
   * void testSameClassmates(Tester t) { // }
   */

  // ----------------------------------------------------------------------------------------------
  // Tests for dejavu(Student s)
}

// generic list
interface IList<T> {
  // map over a list, and produce a new list with a (possibly different)
  // element type
  <U> IList<U> map(Function<T, U> f);

  <U> U foldr(BiFunction<T, U, U> func, U base);

  // returns a new list, adding the given element to the beginning of this list
  <U> IList<T> add(T item);

  // determines whether the given list of elements share an element with this list
  boolean sharesElement(IList<T> otherList, IEquals<T> isEq);

  // checks whether the given element is contained in this list
  boolean contains(T item, IEquals<T> isEq);

  // checks whether the given list contains all the elements of this list
  boolean hasSameElements(IList<T> otherList, IEquals<T> isEq);

  // returns the number of shared elements of this list and the given list
  int numberOfSharedElements(IList<T> otherList, IEquals<T> isEq);
}

// empty generic list
class MtList<T> implements IList<T> {
  public <U> IList<U> map(Function<T, U> f) {
    return new MtList<U>();
  }

  //
  public <U> U foldr(BiFunction<T, U, U> func, U base) {
    return base;
  }

  // adds the given element to this empty list
  public <U> IList<T> add(T item) {
    return new ConsList<T>(item, this);
  }

  // determines whether the given list of elements share an element with this
  // empty list
  public boolean sharesElement(IList<T> otherList, IEquals<T> isEq) {
    return false;
  }

  // checks whether the given element is contained in this list
  public boolean contains(T item, IEquals<T> isEq) {
    return false;
  }

  // checks whether the given list contains all the elements of this list
  public boolean hasSameElements(IList<T> otherList, IEquals<T> isEq) {
    return true;
  }

  // returns the number of shared elements of this empty list and the given list
  public int numberOfSharedElements(IList<T> otherList, IEquals<T> isEq) {
    return 0;
  }
}

// non-empty generic list
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  public <U> IList<U> map(Function<T, U> f) {
    return new ConsList<U>(f.apply(this.first), this.rest.map(f));
  }

  public <U> U foldr(BiFunction<T, U, U> func, U base) {
    return func.apply(this.first, this.rest.foldr(func, base));
  }

  // adds the given element to the beginning of this non-empty list
  public <U> IList<T> add(T item) {
    return new ConsList<T>(item, this);
  }

  // determines whether the given list of elements share an element with this
  // non-empty list
  public boolean sharesElement(IList<T> otherList, IEquals<T> isEq) {
    return otherList.contains(this.first, isEq) || this.rest.sharesElement(otherList, isEq);
  }

  // checks whether the given element is contained in this list
  public boolean contains(T item, IEquals<T> isEq) {
    return isEq.isSame(this.first, item) || this.rest.contains(item, isEq);
  }

  // checks whether the given list contains all the elements of this list
  public boolean hasSameElements(IList<T> otherList, IEquals<T> isEq) {
    return otherList.contains(this.first, isEq) && this.rest.hasSameElements(otherList, isEq);
  }

  // returns the number of shared elements of this non-empty list and the given
  // list
  public int numberOfSharedElements(IList<T> otherList, IEquals<T> isEq) {
    if (otherList.contains(this.first, isEq)) {
      return 1 + this.rest.numberOfSharedElements(otherList, isEq);
    }
    return this.rest.numberOfSharedElements(otherList, isEq);
  }
}

// represents a function that tests two objects for equality
interface IEquals<T> {
  // tests if the two given objects are equal
  boolean isSame(T object1, T object2);
}

// function that tests whether two students are the same
class StudentEquals implements IEquals<Student> {
  // checks whether this student is the same as the given student
  public boolean isSame(Student s1, Student s2) {
    // check name and id since id should be unique, courses being taken doesn't need
    // to be checked
    // only need to check id?
    return s1.name.equals(s2.name) && s1.id == s2.id;
  }
}

// function that tests whether two courses are the same
class CourseEquals implements IEquals<Course> {
  // checks whether this course is the same as the given course
  public boolean isSame(Course c1, Course c2) {
    InstructorEquals instrEqs = new InstructorEquals();
    StudentEquals studentEqs = new StudentEquals();
    // check same name and professor
    return c1.name.equals(c2.name) && instrEqs.isSame(c1.prof, c2.prof)
        && c1.students.hasSameElements(c2.students, studentEqs);
    // c1.prof.equals(c2.prof);
  }
}

// function that tests whether two instructors are the same
class InstructorEquals implements IEquals<Instructor> {
  // checks whether this instructor is the same as the given instructor
  public boolean isSame(Instructor prof1, Instructor prof2) {
    // check same name and courses being taught
    return prof1.name.equals(prof2.name);
  }
}
