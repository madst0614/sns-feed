package wanted.n.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 임시 posting 데이터
 */
@Getter
@NoArgsConstructor // 직렬화
public class LogPostingDTO {
    private List<Long> tag;
}
