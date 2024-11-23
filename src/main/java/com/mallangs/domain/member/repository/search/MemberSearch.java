package com.mallangs.domain.member.repository.search;

import com.mallangs.domain.member.dto.MemberListResponseDTO;
import org.springframework.data.domain.Page;

public interface MemberSearch {
    Page<MemberListResponseDTO> getMemberPage();
}
