package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.DataFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DataFileRepository extends CrudRepository<DataFile, Integer> {
    Iterable<DataFile> findAllByDataSetId(Integer dataSetId);
    @Transactional
    void deleteAllByDataSetId(int dataSetId);
    DataFile findByLinkToFile(String linkToFile);
    boolean existsByDataSetId(int dataSetId);
}
