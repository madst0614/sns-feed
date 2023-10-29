package wanted.n.dto.posting.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostingExternalFeaturesRequestDTO {
    private Long postingId;
}
