package wanted.n.domain.pk;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostingHashTagPK implements Serializable {
    private Long posting;
    private Long hashTag;
}
