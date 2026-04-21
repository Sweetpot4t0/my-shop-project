package com.example;

import com.example.domain.Member;
import com.example.domain.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    // 이제 Repository 대신 Service를 사용
    private final MemberService memberService;

    // 1. 현재 사용자 정보 (객체 반환)
    @GetMapping("/api/member/current-info")
    public Member getCurrentMemberInfo(HttpSession session) {
        return (Member) session.getAttribute("loginMember");
    }

    // 2. 현재 사용자 이름 확인 (텍스트 반환)
    @GetMapping("/api/member/current")
    public String getCurrentMember(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "guest";
        }
        return loginMember.getName();
    }

    // 3. 로그인 (BCrypt 적용된 서비스 호출)
    @PostMapping("/api/member/login")
    public String login(@RequestParam String loginId, @RequestParam String password, HttpSession session) {
        Member loginMember = memberService.login(loginId, password);

        if (loginMember != null) {
            session.setAttribute("loginMember", loginMember);
            return loginMember.getName();
        }
        return "fail";
    }

    // 4. 로그아웃
    @PostMapping("/api/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "ok";
    }

    @PostMapping("/api/member/signup")
    public String signup(@RequestBody Member member) { //  JSON 데이터를 받으려면 @RequestBody
        try {
            // 서비스에게 회원가입(비밀번호 암호화 및 저장)을 시킵니다.
            memberService.join(member);
            return "success";
        } catch (Exception e) {
            // 중복 아이디 등 에러 발생 시 fail 반환
            return "fail";
        }
    }

    // 5. 비밀번호 수정 (경로 추가 및 서비스 호출)
    @PostMapping("/api/member/update-password")
    public String updatePassword(@RequestParam String newPassword, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        // 서비스에서 BCrypt로 암호화하여 수정 처리
        memberService.updatePassword(loginMember.getId(), newPassword);

        // 수정된 정보로 세션 최신화 (옵션)
        return "success";
    }
    // MemberController.java
    @GetMapping("/api/member/check-id")
    public boolean checkId(@RequestParam String loginId) {
        // 서비스의 메서드를 호출해서 결과
        return memberService.isDuplicateId(loginId);
    }

    // 6. 회원 탈퇴
    @PostMapping("/api/member/withdraw")
    public String withdraw(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        memberService.withdraw(loginMember.getId());
        session.invalidate();

        return "success";
    }
}