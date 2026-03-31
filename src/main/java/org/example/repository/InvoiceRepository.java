package org.example.repository;

import org.example.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByNameContainingIgnoreCase(String name);

    List<Invoice> findByMobileNumber(String mobileNumber);

    List<Invoice> findAllByOrderByCreatedAtDesc();
}
