package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.EditDataFileRequest;
import com.fastroof.ftpr.entity.RoleChangeRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EditDataFileRequestRepository extends CrudRepository<EditDataFileRequest, Integer> {
    List<EditDataFileRequest> findAllByStatus(int i);
}
