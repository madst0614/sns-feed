package wanted.n.dto.posting.response;

import lombok.*;
import wanted.n.domain.Posting;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostingDetailResponseDTO {
    private Posting posting;
}
