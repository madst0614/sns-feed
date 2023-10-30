package wanted.n.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPosting is a Querydsl query type for Posting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPosting extends EntityPathBase<Posting> {

    private static final long serialVersionUID = -2025849129L;

    public static final QPosting posting = new QPosting("posting");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> likeCount = createNumber("likeCount", Long.class);

    public final ListPath<PostingHashTag, QPostingHashTag> postingHashTagList = this.<PostingHashTag, QPostingHashTag>createList("postingHashTagList", PostingHashTag.class, QPostingHashTag.class, PathInits.DIRECT2);

    public final NumberPath<Long> shareCount = createNumber("shareCount", Long.class);

    public final StringPath title = createString("title");

    public final EnumPath<wanted.n.enums.PostingType> type = createEnum("type", wanted.n.enums.PostingType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QPosting(String variable) {
        super(Posting.class, forVariable(variable));
    }

    public QPosting(Path<? extends Posting> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPosting(PathMetadata metadata) {
        super(Posting.class, metadata);
    }

}

