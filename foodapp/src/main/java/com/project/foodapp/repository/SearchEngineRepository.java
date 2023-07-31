package com.project.foodapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.foodapp.model.SearchEngine;

@Repository
public interface SearchEngineRepository extends JpaRepository<SearchEngine, Long> {

}
