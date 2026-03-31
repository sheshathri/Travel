package org.example.repository;

import org.example.model.TravelRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRecordRepository extends JpaRepository<TravelRecord, Long> {

    List<TravelRecord> findByNameContainingIgnoreCase(String name);

    List<TravelRecord> findByMobileNumber(String mobileNumber);

    List<TravelRecord> findAllByOrderByCreatedAtDesc();
}
