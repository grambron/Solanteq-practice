package me.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuerBankRepository extends JpaRepository<IssuerBank, Integer> {
    IssuerBank findByBin(String bin);
}
