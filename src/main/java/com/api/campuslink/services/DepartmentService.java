package com.api.campuslink.services;

import com.api.campuslink.dao.DepartmentRepository;
import com.api.campuslink.models.entities.Department;
import com.api.campuslink.helpers.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Result<Department> insertDepartment(Department department) {
        try {
            log.info("Saving department with department code = " + department.getCode());
            Department savedDepartment = departmentRepository.save(department);
            log.info("Department saved successfully with department code = " + department.getCode());
            return Result.success(savedDepartment);

        } catch (Exception e) {
            log.debug("Unable to save department.");
            return Result.error(e.getMessage());
        }
    }

    public Result<List<Department>> getAllDepartment(String sortBy, Boolean ascending) {
        try {
            log.info("Fetching all the departments");
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            List<Department> departmentList = departmentRepository.findAll(sort);
            log.info("Successfully fetched all the department.");
            return Result.success(departmentList);
        } catch (Exception e) {
            log.debug("Unable to fetch the department");
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    public Result<Department> getDepartment(String prop, String value) {
        try {
            log.info("Fetching department with " + prop + ": " + value);
            Optional<Department> departmentOptional = switch (prop) {
                case "id" -> this.departmentRepository.findById(Integer.parseInt(value));
                case "code" -> this.departmentRepository.findByCode(value);
                case "name" -> this.departmentRepository.findByName(value);
                default -> Optional.empty();
            };
            log.info("Department with " + prop + ": " + value + "fetched successfully");
            return departmentOptional.map(Result::success).orElseGet(() -> Result.success(null));
        } catch (Exception e) {
            log.debug("Unable to fetch department with " + prop + ": " + value);
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    public Result<Department> updateDepartment(Department department) {
        try {
            log.info("Updating department with department id = " + department.getId());
            if (!this.departmentRepository.existsById(department.getId())) {
                log.info("Department with given id = " + department.getId() + " is not available");
                return Result.error("Department with given id = " + department.getId() + " is not available");
            }
            Department updatedDepartment = this.departmentRepository.save(department);
            log.info("Department with given id = " + department.getId() + " is updated successfully");
            return Result.success(updatedDepartment);
        } catch (Exception e) {
            log.debug("Unable to update department");
            log.error(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

    public Result<Department> removeDepartment(int id) {
        try {
            log.info("Got request to delete department with id = " + id);
            if (!this.departmentRepository.existsById(id)) {
                return Result.error("Department with given id = " + id + " is not available");
            }
            this.departmentRepository.deleteById(id);
            log.info("Department wiht given id = " + id + " removed successfully");
            return Result.success(null);
        } catch (Exception e) {
            log.debug("Unable to remove the department with given id = " + id);
            log.error(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

    public Result<Department> removeDepartments(String ids) {
        try {
            log.info("Removing departments with ids = " + ids);
            List<Integer> idList = Arrays.stream(ids.split(","))
                    .map(Integer::parseInt)
                    .toList();
            this.departmentRepository.deleteAllById(idList);
            log.info("Removed departments with given id " + ids);
            return Result.success(null);
        } catch (Exception e) {
            log.debug("Unable to remove the departments with ids = " + ids);
            log.error(Arrays.toString(e.getStackTrace()));
            return Result.error(e.getMessage());
        }
    }

}