package com.a301.theknight.domain.auth.resolver;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.auth.model.MemberPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginMemberIdAnnotation = parameter.getParameterAnnotation(LoginMemberId.class) != null;
        boolean isMemberIdClass = long.class.equals(parameter.getParameterType()) || Long.class.equals(parameter.getParameterType());

        return isLoginMemberIdAnnotation && isMemberIdClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getMemberId();
    }
}