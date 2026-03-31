package org.example.repository;

import org.example.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

    List<Billing> findByNameContainingIgnoreCase(String name);

    List<Billing> findByMobileNumber(String mobileNumber);

    List<Billing> findAllByOrderByCreatedAtDesc();

    @Query("SELECT MAX(b.id) FROM Billing b")
    Long findMaxId();
}
