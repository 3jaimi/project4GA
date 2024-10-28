package org.ga.chess.repository;

import org.ga.chess.model.User;
import org.ga.chess.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserImageRepository extends JpaRepository<UserImage,Long> {
    Optional<UserImage> findByUser(User user);
}
