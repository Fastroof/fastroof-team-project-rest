package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.DataSet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSetRepository extends CrudRepository<DataSet, Integer> {
    DataSet findByName(String name);
}
