package com.example.transaction.authorizer.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.transaction.authorizer.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

	Optional<Account> findByMerchantNameLike(String name);
}
