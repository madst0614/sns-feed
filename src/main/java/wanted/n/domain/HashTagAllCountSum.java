package wanted.n.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashTagAllCountSum {

    private Long hashTagId;

    private Long countSum;

    private Long viewCountSum;

    private Long likeCountSum;

    private Long shareCountSum;
}
