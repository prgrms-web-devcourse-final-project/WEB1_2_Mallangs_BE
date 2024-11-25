package com.mallangs.domain.community.repository;

import com.mallangs.domain.community.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

}
