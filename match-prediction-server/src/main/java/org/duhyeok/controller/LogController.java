package org.duhyeok.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/logs")
@CrossOrigin(origins = "*") // 테스트 편의를 위해 모든 도메인 허용
public class LogController {

    // 최신 로그를 담을 스레드 안전한 리스트
    private final List<String> logList = Collections.synchronizedList(new LinkedList<>());

    @PostMapping
    public void receiveLog(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");
        if (message != null) {
            // [추가] 이 코드가 있어야 스프링 부트 콘솔에 로그가 보입니다!
            System.out.println(">>> 분석 엔진 로그 수신: " + message); 
            
            logList.add(0, message); 
            if (logList.size() > 10) { // 5개는 너무 적으니 10개 정도로 늘려보세요
                logList.remove(logList.size() - 1);
            }
        }
    }

    @GetMapping
    public List<String> getLogs() {
        return new ArrayList<>(logList);
    }
}