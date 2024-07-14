package com.api.campuslink.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.api.campuslink.dao.RoleRepository;
import com.api.campuslink.entities.Role;
import com.api.campuslink.helpers.Result;

@Service
public class RolesServices {

    @Autowired
    RoleRepository roleRepository;

    public Result<List<Role>> getAllRoles(String sortBy, boolean ascending) {
        try {
            List<Role> roleList = new ArrayList<Role>();
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            roleList = roleRepository.findAll(sort);
            return Result.success(roleList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }

    }

    public Result<Role> getRoleById(int id) {
        try {
            Role roleFound = roleRepository.findById(id);
            return Result.success(roleFound);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }

    }

    public Result<Role> insertNewRole(Role role) {
        try {

            if (roleRepository.existsById(role.getId())) {
                throw new DataIntegrityViolationException("this role already exists");
            }
            Role savedRole = roleRepository.save(role);
            return Result.success(savedRole);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<Role> updateRole(Role req) {
        try {
            int id = req.getId();

            if (!roleRepository.existsById(id)) {
                throw new Exception("this role with " + id + " does not exists");
            }

            Role savedRole = roleRepository.save(req);
            return Result.success(savedRole);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<Role> deleteRole(int id) {
        try {

            if (!roleRepository.existsById(id)) {
                throw new Exception("this role with " + id + " does not exists");
            }
            roleRepository.deleteById(id);
            return Result.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }

    }

    public Result<Role> deleteRole(Role role) {
        try {
            roleRepository.delete(role);
            return Result.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    public Result<Role> deleteRoles(List<Integer> ids) {
        try {
            roleRepository.deleteAllById(ids);
            return Result.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}
