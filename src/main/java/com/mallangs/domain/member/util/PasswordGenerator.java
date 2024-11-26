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
        StringBuilder password = new StringBuilder();
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // 나머지 길이를 랜덤으로 채움
        String allChars = lowerCase + upperCase + digits + specialChars;

        for (int i = 0; i < 20; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // 비밀번호를 랜덤하게 섞음
        List<Character> passwordChars = new ArrayList<>();
        for (char c : password.toString().toCharArray()) {
            passwordChars.add(c);
        }
        Collections.shuffle(passwordChars);

        StringBuilder finalPassword = new StringBuilder();
        for (char c : passwordChars) {
            finalPassword.append(c);
        }
        String lastPassword = "12345678" + finalPassword;

        return lastPassword.substring(9,20);
    }
}
