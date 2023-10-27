package wanted.n.dto;

import lombok.*;
import wanted.n.enums.PostingType;
import wanted.n.enums.SearchType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostingSearchConditionDto {
    // 1건의 태그
    private Long hashTagId;

    private PostingType type;

    // Search
    private SearchType searchType;
    private String searchKeyword;
}
