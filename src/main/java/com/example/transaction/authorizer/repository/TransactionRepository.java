package com.example.transaction.authorizer.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.transaction.authorizer.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}
