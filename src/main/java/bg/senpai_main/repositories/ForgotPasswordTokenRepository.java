package bg.senpai_main.repositories;


import bg.senpai_main.entities.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Long> {

    Optional<ForgotPasswordToken> findByEmail(String email);

    Optional<ForgotPasswordToken> findByToken(String token);

    void deleteByEmail(String email);
}

