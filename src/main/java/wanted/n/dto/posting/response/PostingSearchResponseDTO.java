package wanted.n.dto.posting.response;

import lombok.*;
import wanted.n.domain.Posting;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostingSearchResponseDTO {
    private List<Posting> postingList;
}
