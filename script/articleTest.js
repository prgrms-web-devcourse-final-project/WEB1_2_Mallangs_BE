//지도에서 글타래 찾기 테스트
import http from "k6/http";
import { check, sleep } from "k6";

// Test configuration
export const options = {
    thresholds: {
        // Assert that 99% of requests finish within 3000ms.
        http_req_duration: ["p(99) < 6000"],
    },
    stages: [
        { duration: "1m", target: 250 },  // 0 -> 250명 증가 30초 동안
        // { duration: "10s", target: 500 },   // 10초간 500명 유지
        { duration: "1m", target: 0 },   // 1분동안 250 -> 0으로 감소
    ],
};

// Authentication token
const TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cGUiOiJKV1QifQ.eyJyb2xlIjoiUk9MRV9BRE1JTiIsIm5pY2tuYW1lIjoiSm9obkRvZSIsImNhdGVnb3J5IjoiQUNDRVNTX1RPS0VOIiwidXNlcklkIjoiYXpzQWQ0MTIxMjMiLCJlbWFpbCI6InJrcmt3bmoxMDQ2MjAwQGdtYWlsLmNvbSIsIm1lbWJlcklkIjo3OCwiaWF0IjoxNzMzNzI1MTkzLCJleHAiOjE3MzM3MjY5OTN9.RsLpLeL28uGsu8lKPnQ53qiAYWb2miregfk8bp3E8ds";  // 여기에 실제 토큰을 입력하세요

// Simulated user behavior
export default function () {
    // Request payload
    const payload = JSON.stringify({
        southWestLat: 35.4600,
        southWestLon: 127.1390,
        northEastLat: 38.5170,
        northEastLon: 120.8220,
    });

    // HTTP headers
    const headers = {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${TOKEN}`,
    };

    // Send POST request
    const url = "http://mallangplace.ap-northeast-2.elasticbeanstalk.com/api/v1/articles/public/articlesMarkers?articleType=lost";
    const res = http.post(url, payload, { headers });

    // Validate response
    if (res.status === 200 && res.body) {
        try {
            const responseBody = JSON.parse(res.body);

            check(responseBody, {
                "response is not empty": () => responseBody.length > 0,
            });
        } catch (e) {
            console.error("JSON parsing failed: ", e.message);
        }
    } else {
        console.error(`Request failed. Status: ${res.status}, Body: ${res.body}`);
    }

    sleep(1);
}
