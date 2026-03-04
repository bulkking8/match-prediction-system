package org.duhyeok.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;

@Service
public class AnalysisEngineManager {

    // 로거 직접 선언
    private static final Logger log = LoggerFactory.getLogger(AnalysisEngineManager.class);
    private Process pythonProcess;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String url = "https://m.sports.naver.com/game/20260304022F227/preview";
        startPythonEngine(url, "한국도로공사", "페퍼저축은행");
    }

    @Async
    public void startPythonEngine(String gameUrl, String teamA, String teamB) {
        if (pythonProcess != null && pythonProcess.isAlive()) {
            log.info("▶ 분석 엔진이 이미 작동 중입니다.");
            return;
        }

        try {
            // 경로 설정 (현재 프로젝트 루트 기준)
            String scriptPath = new File("src/main/resources/python/crawler.py").getAbsolutePath();
            
            // 파이썬 실행 명령 (환경에 따라 "python" 또는 "python3")
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath, gameUrl, teamA, teamB);
            
            // 파이썬의 print 결과를 자바 콘솔에 그대로 출력
            pb.inheritIO(); 

            log.info("🚀 분석 엔진(Python) 구동 시작: {} vs {}", teamA, teamB);
            pythonProcess = pb.start();

        } catch (IOException e) {
            log.error("❌ 분석 엔진 구동 실패: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void stopPythonEngine() {
        if (pythonProcess != null && pythonProcess.isAlive()) {
            pythonProcess.destroy();
            log.info("🛑 분석 엔진 프로세스를 안전하게 종료했습니다.");
        }
    }
}