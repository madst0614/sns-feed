package wanted.n.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 특정 태그에 대한 log 데이터
 */
@Getter
@Setter
@AllArgsConstructor
public class LogDTO {

    private String tag;
    private long createdAt;

}
