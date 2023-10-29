package wanted.n.dto.posting.request;

import lombok.*;
import wanted.n.enums.PostingType;
import wanted.n.enums.SearchType;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostingSearchRequestDTO {
    // 1건의 태그
    private String hashTagName;

    private PostingType type;

    // Search
    private SearchType searchType;
    private String searchKeyword;
}
