package wanted.n.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class PostingTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "posting_id")
    private Posting posting;
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
