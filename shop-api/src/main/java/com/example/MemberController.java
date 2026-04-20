package com.example;

import com.example.domain.Member;
import com.example.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    // MemberController 내부에 추가
    @GetMapping("/api/member/current-info")
    public Member getCurrentMemberInfo(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            // 로그인이 안 되어 있으면 id가 0인 가짜 객체라도 리턴
            return Member.builder().loginId("guest").role("USER").build();
        }
        return loginMember;
    }
    // 1. 현재 사용자 이름 확인 (텍스트 반환)
    @GetMapping("/api/member/current")
    public String getCurrentMember(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) return "guest";
        return loginMember.getName();
    }

    // 2. 로그인 (전통적인 폼 데이터 방식)
    @PostMapping("/api/member/login")
    public String login(@RequestParam String loginId, @RequestParam String password, HttpSession session) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if (member.isPresent() && member.get().getPassword().equals(password)) {
            session.setAttribute("loginMember", member.get());
            return member.get().getName();
        }
        return "fail";
    }

    // 3. 로그아웃
    @PostMapping("/api/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "ok";
    }

    // 4. 비밀번호 수정
    @PostMapping("/api/member/update-password")
    public String updatePassword(@RequestParam String newPassword, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) return "fail";
        Optional<Member> memberOpt = memberRepository.findById(loginMember.getId());
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            member.setPassword(newPassword);
            memberRepository.save(member);
            session.setAttribute("loginMember", member);
            return "success";
        }
        return "fail";
    }

    // 5. 회원 탈퇴
    @PostMapping("/api/member/withdraw")
    public String withdraw(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) return "fail";
        memberRepository.deleteById(loginMember.getId());
        session.invalidate();
        return "success";
    }
}