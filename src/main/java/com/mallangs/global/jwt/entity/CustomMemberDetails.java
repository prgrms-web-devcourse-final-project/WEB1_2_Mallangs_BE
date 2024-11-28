package com.mallangs.global.jwt.entity;

import com.mallangs.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Log4j2
@RequiredArgsConstructor
public class CustomMemberDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> member.getMemberRole().name());
        return collection;
    }

    public String getUserId() {
        return member.getUserId().getValue();
    }

    public Long getMemberId() {
        log.info("Accessing Member ID: {}", member.getMemberId());
        return member.getMemberId();
    }

    public String getEmail() {
        return member.getEmail().getValue();
    }

    public String getRole() {
        return member.getMemberRole().name();
    }

    @Override
    public String getPassword() {
        return member.getPassword().getValue();
    }

    public String getNickname() {
        return member.getNickname().getValue();
    }

    @Override
    public String getUsername() {
        return member.getUserId().getValue();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
