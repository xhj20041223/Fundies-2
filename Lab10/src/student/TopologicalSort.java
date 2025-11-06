package student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Curriculum {
  ArrayList<Course> courses;

  Curriculum() {
    this.courses = new ArrayList<Course>();
  }

  // EFFECT: adds another course to the set of known courses
  void addCourse(Course c) {
    this.courses.add(c);
  }
  // add methods here...
}

class Course {
  String name;
  ArrayList<Course> prereqs;

  Course(String name) {
    this.name = name;
    this.prereqs = new ArrayList<Course>();
  }

  // EFFECT: adds a course as a prereq to this one
  void addPrereq(Course c) {
    this.prereqs.add(c);
  }
  // add methods here
}