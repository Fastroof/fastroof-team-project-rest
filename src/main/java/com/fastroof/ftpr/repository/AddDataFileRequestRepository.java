package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.AddDataFileRequest;
import com.fastroof.ftpr.entity.RoleChangeRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddDataFileRequestRepository extends CrudRepository<AddDataFileRequest, Integer> {
    List<AddDataFileRequest> findAllByStatus(int i);
}
