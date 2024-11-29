package com.mallangs.domain.chat.repository;

import com.mallangs.domain.chat.entity.IsRead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IsReadRepository extends JpaRepository<IsRead, Long> {
}
