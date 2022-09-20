package lab.reservation_server.repository;

import java.util.Optional;
import lab.reservation_server.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Long> {

  /**
   * 토큰의 value값으로 존재하는지 확인
   * @param token 토큰
   * @return 유효한 토큰
   */
  @Query("select t from Token t where t.value = :token")
  Optional<Token> findByValue(@Param("token") String token);
}
