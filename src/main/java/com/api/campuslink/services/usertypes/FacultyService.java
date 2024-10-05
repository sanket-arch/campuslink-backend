package com.api.campuslink.services.usertypes;

import com.api.campuslink.dao.CourseRepository;
import com.api.campuslink.dao.DepartmentRepository;
import com.api.campuslink.dao.usertypes.FacultyRepository;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.models.entities.Course;
import com.api.campuslink.models.entities.Department;
import com.api.campuslink.models.entities.usertypes.Faculty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FacultyService {
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    public Result<Faculty> insertFaculty(Faculty faculty) {
        try {
            log.info("Saving faculty");
            Result<Faculty> facultyDetails = this.getFacultyOtherDetails(faculty);

            if (!facultyDetails.isSuccess()) {
                log.debug("Got error while saving faculty");
                return Result.error(facultyDetails.getError());
            }

            Faculty savedFaculty = this.facultyRepository.save(faculty);
            log.info("Faculty with id" + savedFaculty.getUserId() + " saved successfully");
            return Result.success(savedFaculty);
        } catch (Exception e) {
            log.debug("Got error while saving faculty");
            log.error(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

    public Result<List<Faculty>> getAllFaculty(String sortBy, boolean ascending) {
        try {
            log.info("Fetching all faculty.");
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            List<Faculty> facultyList = this.facultyRepository.findAll(sort);
            log.info("All faculties fetched successfully.");
            return Result.success(facultyList);
        } catch (Exception e) {
            log.debug("Something went wrong while fetching faculties");
            log.error(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

    public Result<Faculty> getFaculty(String prop, String value) {
        try {
            log.info("Fetching faculty with " + prop + " = " + value);
            Optional<Faculty> facultyOptional = switch (prop) {
                case "id" -> this.facultyRepository.findByUserId(Long.parseLong(value));
                case "facultyCode" -> this.facultyRepository.findByFacultyCode(value);
                case "userName" -> this.facultyRepository.findByUserName(value);
                default -> Optional.empty();
            };
            log.info("Faculty with " + prop + " = " + value + " fetched successfully");
            return facultyOptional.map(Result::success).orElseGet(() -> Result.success(null));
        } catch (Exception e) {
            log.debug("Something went wrong while fetching faculty with " + prop + " = " + value);
            log.error(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

    public Result<Faculty> updateFaculty(Faculty faculty) {
        try {
            if (!facultyRepository.existsById(faculty.getUserId())) {
                log.info("Faculty with given ID does not exist");
                return Result.error("Faculty with given ID does not exist");
            }
            log.info("Updating faculty with id = " + faculty.getUserId());
            Faculty facultyObject = this.getFacultyDataToUpdate(faculty);

            assert facultyObject != null;
            Faculty updatedFaculty = this.facultyRepository.save(facultyObject);
            log.info("Faculty with id" + updatedFaculty.getUserId() + " updated successfully");
            return Result.success(updatedFaculty);
        } catch (Exception e) {
            log.debug("Something went wrong while updating faculty");
            log.error(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

    private Faculty getFacultyDataToUpdate(Faculty faculty) throws Exception {

        Result<Faculty> result = this.getFaculty("id", String.valueOf(faculty.getUserId()));

        if (!result.isSuccess()) {
            throw new Exception(result.getError());
        }

        Faculty facultyDetails = result.getData();
        Optional.ofNullable(faculty.getFirstName())
                .ifPresent(facultyDetails::setFirstName);
        Optional.ofNullable(faculty.getLastName())
                .ifPresent(facultyDetails::setLastName);
        Optional.ofNullable(faculty.getUserName())
                .ifPresent(facultyDetails::setUserName);
        Optional.ofNullable(faculty.getEmail())
                .ifPresent(facultyDetails::setEmail);
        Optional.ofNullable(faculty.getPhoneNumber())
                .ifPresent(facultyDetails::setPhoneNumber);
        Optional.ofNullable(faculty.getProfilePicture())
                .ifPresent(facultyDetails::setProfilePicture);
        Optional.ofNullable(faculty.getRole())
                .ifPresent(facultyDetails::setRole);
        Optional.ofNullable(faculty.getCampus())
                .ifPresent(facultyDetails::setCampus);
        Optional.ofNullable(faculty.getFacultyCode())
                .ifPresent(facultyDetails::setFacultyCode);
        Optional.ofNullable(faculty.getOfficeLocation())
                .ifPresent(facultyDetails::setOfficeLocation);
        Optional.ofNullable(faculty.getDepartment())
                .ifPresent(facultyDetails::setDepartment);

        return facultyDetails;
    }

    private Result<Faculty> getFacultyOtherDetails(@NotNull Faculty faculty) {
        try {
            int courseID = faculty.getCourse().getId();
            int departmentID = faculty.getDepartment().getId();

            if (!courseRepository.existsById(courseID)) {
                return Result.error("Cannot find the course with given id = " + courseID);
            }

            if (!departmentRepository.existsById(departmentID)) {
                return Result.error("Cannot find the department with the given id = " + departmentID);
            }

            Course course = this.courseRepository.findById(courseID);
            Optional<Department> department = this.departmentRepository.findById(departmentID);

            faculty.setCourse(course);
            faculty.setDepartment(department.get());

            return Result.success(faculty);

        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


}
