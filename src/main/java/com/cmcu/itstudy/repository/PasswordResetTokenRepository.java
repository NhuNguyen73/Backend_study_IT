package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.PasswordResetToken;
import com.cmcu.itstudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByToken(String token);

    List<PasswordResetToken> findByUser(User user);
}

