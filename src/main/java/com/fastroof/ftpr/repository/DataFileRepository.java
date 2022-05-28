package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.DataFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataFileRepository extends CrudRepository<DataFile, Integer> {
    Iterable<DataFile> findAllByDataSetId(Integer dataSetId);
}
