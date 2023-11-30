package com.example.store_authorization.repository;

import com.example.store_authorization.domain.entity.Refresh;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshRepository extends CrudRepository<Refresh, String> {
}
