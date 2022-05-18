package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.AddDataFileRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddDataFileRequestRepository extends CrudRepository<AddDataFileRequest, Integer> {
}
