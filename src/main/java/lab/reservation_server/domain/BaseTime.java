package lab.reservation_server.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTime {

  @CreatedDate
  @Column(name="created_date")
  private LocalDateTime createdDate;

  @LastModifiedDate
  @Column(name="modified_date")
  private LocalDateTime modifiedDate;
}
