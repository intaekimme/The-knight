package com.a301.theknight.domain.chatting.repository;

import com.a301.theknight.domain.chatting.entity.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRepository extends JpaRepository<Chatting, Long> {
}
