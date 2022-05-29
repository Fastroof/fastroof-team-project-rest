package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.AddDataFileRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AddDataFileRequestRepository extends CrudRepository<AddDataFileRequest, Integer> {
    @Transactional
    void deleteAllByDataSetId(int dataSetId);
    boolean existsByDataSetId(int dataSetId);
}
