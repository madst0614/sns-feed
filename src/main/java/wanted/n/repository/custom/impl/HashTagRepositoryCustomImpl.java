package wanted.n.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import wanted.n.domain.HashTag;
import wanted.n.repository.custom.HashTagRepositoryCustom;

import java.util.Optional;

import static wanted.n.domain.QHashTag.hashTag;

@RequiredArgsConstructor
public class HashTagRepositoryCustomImpl implements HashTagRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<HashTag> findByName(String name) {
        return Optional.ofNullable(queryFactory.selectFrom(hashTag).where(hashTag.name.eq(name)).fetchOne());
    }
}
