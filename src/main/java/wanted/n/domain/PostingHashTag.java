package wanted.n.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.domain.pk.PostingHashTagPK;

import javax.persistence.*;

@Entity
@Table(name = "posting_hashtag")
@IdClass(PostingHashTagPK.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostingHashTag{
    @Id
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "posting_id")
    private Posting posting;

    @Id
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "hashtag_id")
    private HashTag hashTag;
}
