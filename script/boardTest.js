//게시글 전체 글 조회 테스트
import http from "k6/http";
import { check, sleep } from "k6";

// Test configuration
export const options = {
    thresholds: {
        // Assert that 99% of requests finish within 3000ms.
        http_req_duration: ["p(99) < 3000"],
    },
    stages: [
        { duration: "1m", target: 1000 },  // 0 -> 15명 증가 30초 동안
        // { duration: "10s", target: 500 },   // 1분간 15명 유지
        { duration: "1m", target: 0 },   // 20초 동안 15 -> 0으로 감소
    ],
};

// Authentication token
const TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cGUiOiJKV1QifQ.eyJyb2xlIjoiUk9MRV9BRE1JTiIsIm5pY2tuYW1lIjoi6rmA64-Z7ZiEIiwiY2F0ZWdvcnkiOiJBQ0NFU1NfVE9LRU4iLCJ1c2VySWQiOiJ0ZXN0MTIzNDEyMyIsImVtYWlsIjoidGVzdC5lbWFpbDIwMTI0QHByb3ZpZGVyLm5ldCIsIm1lbWJlcklkIjoxMjMsImlhdCI6MTczMzczOTYyNSwiZXhwIjoxNzMzNzQxNDI1fQ.LwgSGH-387LHQFtc51OBvPEtPGv7x_iQOmks6kpM-b8";  // 여기에 실제 토큰을 입력하세요

// Simulated user behavior
export default function () {

    // HTTP headers
    const headers = {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${TOKEN}`,
    };

    // Send POST request
    const url = "http://mallangplace.ap-northeast-2.elasticbeanstalk.com/api/v1/board/community/?page=1";

    // Send GET request
    const res = http.get(url, { headers });

    // Validate response
    if (res.status === 200 && res.body) {
        try {
            const responseBody = JSON.parse(res.body);

            check(responseBody, {
                "response is not empty": () => responseBody.content.length > 0,  // content는 백엔드의 응답 구조 확인 필요
                "status is 200": () => res.status === 200,
            });
        } catch (e) {
            console.error("JSON parsing failed: ", e.message);
        }
    } else {
        console.error(`Request failed. Status: ${res.status}, Body: ${res.body}`);
    }

    sleep(1);
}
