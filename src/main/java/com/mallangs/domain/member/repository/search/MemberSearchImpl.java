//package com.mallangs.domain.member.repository.search;
//
//import com.mallangs.domain.member.dto.MemberAddressResponse;
//import com.mallangs.domain.member.dto.MemberGetResponse;
//import com.mallangs.domain.member.entity.QAddress;
//import com.mallangs.domain.member.entity.QMember;
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.JPQLQuery;
//import com.querydsl.jpa.JPQLQueryFactory;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import jakarta.persistence.EntityManager;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//
//import static com.querydsl.core.group.GroupBy.groupBy;
//import static com.querydsl.core.group.GroupBy.list;
//
//@Log4j2
//public class MemberSearchImpl implements MemberSearch {
//
//    private final JPQLQueryFactory queryFactory;
//
//    public MemberSearchImpl(EntityManager em) {
//        this.queryFactory = new JPAQueryFactory(em);
//    }
//
//    @Override
//    public Page<MemberGetResponse> searchMembers(Pageable pageable) {
//
//        QMember member = QMember.member;
//        QAddress address = QAddress.address;
//
//        List<MemberGetResponse> results = queryFactory
//                .selectDistinct(Projections.constructor(
//                                MemberGetResponse.class,
//                                member.userId.value.as("userId"),
//                                member.nickname.value.as("nickname"),
//                                member.email.value.as("email"),
//                                member.profileImage.as("profileImage"),
//                                member.createdAt.as("createdAt"),
//                                member.updatedAt.as("updatedAt"),
//                                list(
//                                        Projections.constructor(
//                                                MemberAddressResponse.class,
//                                                address.addressName,
//                                                address.region3depthName,
//                                                address.mainAddressNo,
//                                                address.roadName,
//                                                address.x,
//                                                address.y
//                                        )
//                                ).as("addresses")
//                        )
//                )
//                .from(member)
//                .leftJoin(member.addresses, address)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//
//        // 전체 개수를 조회하기 위한 쿼리
//        JPQLQuery<Long> countQuery = queryFactory
//                .select(member.count())
//                .from(member);
//
//        // null 처림
//        Long total = countQuery.fetchOne();
//        long totalCount = total == null ? 0L : total;
//
//        return new PageImpl<>(results, pageable, totalCount);
//    }
//
//}
