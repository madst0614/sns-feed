package wanted.n.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;

/**
 * 특정 태그에 대한 log 데이터
 */
@Getter
@AllArgsConstructor
public class LogDTO {
    private String tag;

    @CreatedDate
    @Column(updatable = false)
    private long createdAt;
}
