package wanted.n.dto.posting;

import lombok.*;
import wanted.n.dto.posting.request.PostingSearchRequestDTO;
import wanted.n.enums.PostingType;
import wanted.n.enums.SearchType;

@Getter
@Builder
public class PostingSearchConditionDTO {
    // 1건의 태그
    private Long hashTagId;

    private PostingType type;

    // Search
    private SearchType searchType;
    private String searchKeyword;

    public static PostingSearchConditionDTO of(Long hashTagId, PostingSearchRequestDTO dto){
        return PostingSearchConditionDTO.builder()
                .hashTagId(hashTagId)
                .type(dto.getType())
                .searchType(dto.getSearchType())
                .searchKeyword(dto.getSearchKeyword())
                .build();
    }
}
