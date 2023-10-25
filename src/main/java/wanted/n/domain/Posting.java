package wanted.n.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import wanted.n.enums.PositngType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
@Getter
public class Posting extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private PositngType type;

    @OneToMany(mappedBy = "posting")
    private List<PostingTag> tagList;

    @ColumnDefault("0")
    private Long viewCount;

    @ColumnDefault("0")
    private Long likeCount;

    @ColumnDefault("0")
    private Long shareCount;
}
