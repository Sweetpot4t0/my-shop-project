package com.example.domain;

import com.example.domain.Member;
import com.example.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional // ✅ 데이터 변경(수정, 삭제)이 있을 때 꼭 필요해요!
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder; // ✅ WebConfig에서 만든 금고 가져오기

    // 1. 로그인 로직
    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> passwordEncoder.matches(password, m.getPassword())) // ✅ 비밀번호 대조!
                .orElse(null);
    }

    // 2. 비밀번호 수정 로직
    public void updatePassword(Long id, String newPassword) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

        // ✅ 새로운 비번도 구워서(암호화) 저장!
        member.setPassword(passwordEncoder.encode(newPassword));
    }

    // 3. 회원 탈퇴
    public void withdraw(Long id) {
        memberRepository.deleteById(id);
    }
    public void join(Member member) {
        // 1. 비밀번호를 BCrypt로 맛있게 굽습니다(암호화).
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);

        // 2. 암호화된 비번을 가진 멤버를 DB에 저장합니다.
        memberRepository.save(member);
    }

    // 4.중복체크
    public boolean isDuplicateId(String loginId) {
        // .isPresent()
        return memberRepository.findByLoginId(loginId).isPresent();
    }


}