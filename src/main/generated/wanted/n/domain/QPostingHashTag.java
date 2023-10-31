package wanted.n.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostingHashTag is a Querydsl query type for PostingHashTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostingHashTag extends EntityPathBase<PostingHashTag> {

    private static final long serialVersionUID = -649147179L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostingHashTag postingHashTag = new QPostingHashTag("postingHashTag");

    public final QHashTag hashTag;

    public final QPosting posting;

    public QPostingHashTag(String variable) {
        this(PostingHashTag.class, forVariable(variable), INITS);
    }

    public QPostingHashTag(Path<? extends PostingHashTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostingHashTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostingHashTag(PathMetadata metadata, PathInits inits) {
        this(PostingHashTag.class, metadata, inits);
    }

    public QPostingHashTag(Class<? extends PostingHashTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hashTag = inits.isInitialized("hashTag") ? new QHashTag(forProperty("hashTag")) : null;
        this.posting = inits.isInitialized("posting") ? new QPosting(forProperty("posting")) : null;
    }

}

