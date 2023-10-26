package wanted.n.domain;

import lombok.Getter;

import javax.persistence.*;
@Entity
@Getter
@Table(name = "posting_hashtag")
public class PostingHashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id")
    private Posting posting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private HashTag hashTag;
}
