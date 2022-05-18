package com.fastroof.ftpr.repository;

import com.fastroof.ftpr.entity.RequestStatuses;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestStatusesRepository extends CrudRepository<RequestStatuses, Integer> {
}
