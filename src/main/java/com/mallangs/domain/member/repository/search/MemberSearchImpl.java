package com.mallangs.domain.member.repository.search;

import com.mallangs.domain.member.dto.MemberListResponseDTO;
import com.mallangs.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class MemberSearchImpl extends QuerydslRepositorySupport implements MemberSearch {
    public MemberSearchImpl() {
        super(Member.class);
    }

    @Override
    public Page<MemberListResponseDTO> getMemberPage() {


        return null;
    }
}
