package com.cashew.repository;

import com.cashew.entity.ReceiptHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptHistoryRepository extends CrudRepository<ReceiptHistory, Long> {
}
