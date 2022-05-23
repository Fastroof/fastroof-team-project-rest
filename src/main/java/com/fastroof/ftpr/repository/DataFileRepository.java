package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.DataFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataFileRepository extends CrudRepository<DataFile, Integer> {
    List<DataFile> findAllByDataSetId(Integer dataSetId);
}
