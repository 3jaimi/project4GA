package org.ga.chess.repository;

import org.ga.chess.model.Admin;
import org.ga.chess.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAdminRepository extends JpaRepository<Long, Admin> {
    Optional<Admin> findByEmail (String Email);
}
