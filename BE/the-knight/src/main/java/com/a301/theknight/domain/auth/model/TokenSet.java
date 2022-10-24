package com.a301.theknight.domain.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenSet {
    private String access;
    private String refresh;
}
