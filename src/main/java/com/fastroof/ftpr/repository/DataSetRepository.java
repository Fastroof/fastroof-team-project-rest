package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.DataSet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSetRepository extends CrudRepository<DataSet, Integer> {
    List<DataSet> getDataSetsByNameContains(String name);
    List<DataSet> findAllByTagId(Integer tagId);
}
