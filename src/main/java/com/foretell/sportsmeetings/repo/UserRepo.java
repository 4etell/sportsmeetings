package com.foretell.sportsmeetings.repo;

import com.foretell.sportsmeetings.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);

    @Query(value = "SELECT * FROM users WHERE users.telegram_bot_activation_code = ?1", nativeQuery = true)
    Optional<User> findByTelegramBotActivationCode(String telegramBotActivationCode);
}
