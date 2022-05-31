package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.EditDataFileRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EditDataFileRequestRepository extends CrudRepository<EditDataFileRequest, Integer> {
    @Transactional
    void deleteAllByDataFileId(int dataFileId);
    boolean existsByDataFileId(int dataFileId);
    boolean existsByDataFileIdAndStatus(int dataSetId, int status);
    List<EditDataFileRequest> findAllByStatus(int i);
}
