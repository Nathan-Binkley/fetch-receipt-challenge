package com.fetch.fetch.repository;

import com.fetch.fetch.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, String> {
    @Query("SELECT r.points FROM Receipt r WHERE r.id = ?1")
    Integer getPoints(String id);
}
