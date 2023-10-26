package wanted.n.dto;

import lombok.*;
import wanted.n.enums.PostingType;
import wanted.n.enums.SearchType;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostingSearchRequestDto {
    // 1건의 태그
    private String hashTagName;

    private PostingType type;

    // Search
    private SearchType searchType;
    private String searchKeyword;

    // Pageable
    // default created_At
    private String orderBy;
}
