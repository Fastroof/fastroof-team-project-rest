package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.RoleChangeRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoleChangeRequestRepository extends CrudRepository<RoleChangeRequest, Integer> {
    RoleChangeRequest findByUserIdAndStatus(Integer userId, Integer status);
    RoleChangeRequest findByUserIdAndStatusAndProcessedAt(Integer userId, Integer status, LocalDate processedAt);

    List<RoleChangeRequest> findAllByStatus(Integer status);
}
