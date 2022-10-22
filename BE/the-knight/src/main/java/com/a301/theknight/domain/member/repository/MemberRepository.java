package com.a301.theknight.domain.member.repository;

import com.a301.theknight.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
