package org.ga.chess.repository;

import org.ga.chess.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<Long, User> {
    Optional <User> findByEmail (String Email);
}
