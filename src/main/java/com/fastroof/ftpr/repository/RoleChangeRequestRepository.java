package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.RoleChangeRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleChangeRequestRepository extends CrudRepository<RoleChangeRequest, Integer> {
}
