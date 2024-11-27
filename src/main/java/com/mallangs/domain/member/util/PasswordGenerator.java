package com.mallangs.domain.member.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

    public static String generatePassword() {
        // 각 조건에 맞는 문자 집합
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*?";

        // 랜덤 생성기
        SecureRandom random = new SecureRandom();

        // 최소 조건을 만족하기 위해 각 집합에서 하나씩 추가
        List<Character> passwordChars = new ArrayList<>();
        passwordChars.add(lowerCase.charAt(random.nextInt(lowerCase.length())));
        passwordChars.add(upperCase.charAt(random.nextInt(upperCase.length())));
        passwordChars.add(digits.charAt(random.nextInt(digits.length())));
        passwordChars.add(specialChars.charAt(random.nextInt(specialChars.length())));

        // 나머지 길이를 랜덤으로 채움 (최소 8자, 최대 20자)
        String allChars = lowerCase + upperCase + digits + specialChars;
        int remainingLength = 12; // 최소 8자 + 추가 12자 = 최대 20자

        for (int i = 0; i < remainingLength; i++) {
            passwordChars.add(allChars.charAt(random.nextInt(allChars.length())));
        }

        // 비밀번호를 랜덤하게 섞음
        Collections.shuffle(passwordChars);

        // 최종 비밀번호 생성
        StringBuilder finalPassword = new StringBuilder();
        for (char c : passwordChars) {
            finalPassword.append(c);
        }

        return finalPassword.toString();
    }
}
