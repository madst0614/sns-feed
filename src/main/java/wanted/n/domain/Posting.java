package wanted.n.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import wanted.n.enums.PostingType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
@Table(name="posting")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Posting extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @NotNull
    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private PostingType type;

    @OneToMany(mappedBy = "posting")
    private List<PostingHashTag> hashTagList;

    @Column(name = "view_count")
    @ColumnDefault("0")
    private Long viewCount;

    @Column(name = "like_count")
    @ColumnDefault("0")
    private Long likeCount;

    @Column(name = "share_count")
    @ColumnDefault("0")
    private Long shareCount;
}
