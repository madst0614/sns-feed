package wanted.n.domain.pk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HashTagStatisticsPK implements Serializable {
    private Long id;
    private Long hashTagId;
}
