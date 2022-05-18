package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.EditDataFileRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditDataFileRequestRepository extends CrudRepository<EditDataFileRequest, Integer> {
}
