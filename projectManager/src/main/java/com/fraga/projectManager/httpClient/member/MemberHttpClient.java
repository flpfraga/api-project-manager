package com.fraga.projectManager.httpClient.member;

import com.fraga.projectManager.data.model.Member;
import com.fraga.projectManager.exception.HttpClientException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MemberHttpClient {

    private final WebClient memberWebClient;

    public MemberHttpClient(@Qualifier("memberWebClient") WebClient memberWebClient) {
        this.memberWebClient = memberWebClient;
    }

    @CircuitBreaker(name = "getMemberAPI", fallbackMethod = "fallbackGetMember")
    public Mono<Member> getMemberByName(String memberName) {
        return memberWebClient.get()
                .uri("/api/members/member/{memberName}", memberName)
                .retrieve()
                .bodyToMono(Member.class)
                .doOnError(e -> log.error("Error Member request: {}", e.getMessage()));
    }

    @CircuitBreaker(name = "sendMemberAPI", fallbackMethod = "fallbackSendMember")
    public Mono<Member> sendMember(Member member) {
        return memberWebClient.post()
                .uri("/api/members")
                .bodyValue(member)
                .retrieve()
                .bodyToMono(Member.class);
    }

    public Mono<Member> fallbackGetMember(String memberName, Throwable ex) {
        throw new HttpClientException("Error to find member in API", ex);
    }

    public Mono<Member> fallbackSendMember(Member member, Throwable ex) {
        throw new HttpClientException("Error to send member in API", ex);
    }
}
