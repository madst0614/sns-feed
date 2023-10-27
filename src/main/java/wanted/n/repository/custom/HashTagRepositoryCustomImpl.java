package wanted.n.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static wanted.n.domain.QHashTag.hashTag;

@RequiredArgsConstructor
public class HashTagRepositoryCustomImpl implements HashTagRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> findIdByName(String name) {
        return Optional.ofNullable(queryFactory.select(hashTag.id).from(hashTag).where(hashTag.name.eq(name)).fetchOne());
    }
}
