import time
import pymysql
import requests  # 로그 전송을 위해 추가
import sys
import io
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# 한글 깨짐 방지 설정
sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='utf-8')

# 1. DB 설정
db_config = {
    'host': 'localhost', 'port': 10000, 'user': 'root',
    'password': '1234', 'db': 'crolling', 'charset': 'utf8mb4'
}

# [추가] 자바 서버 로그 전송 함수
def send_log_to_java(message):
    try:
        # 자바 컨트롤러 주소 (localhost 대신 127.0.0.1이 더 안정적입니다)
        url = "http://127.0.0.1:8086/api/v1/logs"
        requests.post(url, json={"message": message}, timeout=0.5)
    except:
        pass  # 전송 실패해도 크롤링 로직은 멈추지 않음

def analyze_sentiment(text):
    pos_words = ['승리', '화이팅', '잘한다', '가자', '득점', '나이스', '최고', '이긴다', '응원', '우승']
    neg_words = ['실망', '뭐하냐', '에휴', '패배', '실책', '범실', '나가라', '답없다', '짐']
    return sum(text.count(w) for w in pos_words), sum(text.count(w) for w in neg_words)

def save_to_db(game_id, team_a, team_b, a_p, a_n, b_p, b_n):
    conn = pymysql.connect(**db_config)
    try:
        with conn.cursor() as cur:
            sql = """
            INSERT INTO game_predict 
            (game_id, team_a_name, team_b_name, team_a_pos_cnt, team_a_neg_cnt, team_b_pos_cnt, team_b_neg_cnt)
            VALUES (%s, %s, %s, %s, %s, %s, %s)
            ON DUPLICATE KEY UPDATE 
            team_a_pos_cnt = team_a_pos_cnt + VALUES(team_a_pos_cnt),
            team_a_neg_cnt = team_a_neg_cnt + VALUES(team_a_neg_cnt),
            team_b_pos_cnt = team_b_pos_cnt + VALUES(team_b_pos_cnt),
            team_b_neg_cnt = team_b_neg_cnt + VALUES(team_b_neg_cnt);
            """
            cur.execute(sql, (game_id, team_a, team_b, a_p, a_n, b_p, b_n))
        conn.commit()
    finally: conn.close()

def start_crawling(game_url, team_a, team_b):
    options = Options()
    options.add_argument("--start-maximized")
    driver = webdriver.Chrome(options=options)
    collected_ids = set()
    game_id = game_url.rstrip('/').split('/')[-2]

    try:
        driver.get(game_url)
        wait = WebDriverWait(driver, 10)
        print(f"[{team_a} vs {team_b}] 크롤링 및 자동화 시작.")
        send_log_to_java(f"🚀 분석 시작: {team_a} vs {team_b}") # 시작 로그
        time.sleep(3)

        # --- [1단계] 응원톡 내부 스크롤 + 더보기 클릭 (과거 데이터 로드) ---
        while True:
            try:
                cbox_list = driver.find_element(By.CLASS_NAME, "u_cbox_list")
                driver.execute_script("arguments[0].scrollTop = arguments[0].scrollHeight", cbox_list)
                time.sleep(1)

                more_btn_xpath = "//div[contains(@class, 'u_cbox_paginate')]//a[contains(@class, 'u_cbox_btn_more')]"
                more_btn = wait.until(EC.presence_of_element_located((By.XPATH, more_btn_xpath)))
                
                if more_btn.is_displayed():
                    driver.execute_script("arguments[0].scrollIntoView({block: 'center'});", more_btn)
                    time.sleep(0.5)
                    driver.execute_script("arguments[0].click();", more_btn)
                    print("더보기 클릭 완료.")
                    send_log_to_java("과거 데이터 불러오는 중 (더보기 클릭)...") # 진행 로그
                    time.sleep(1.5)
                else:
                    break
            except:
                print("모든 과거 댓글 로드 완료.")
                break

        # --- [2단계] 수집 및 DB 저장 ---
        print("과거 데이터 분석 중...")
        comments = driver.find_elements(By.CLASS_NAME, "u_cbox_comment")
        for comment in reversed(comments):
            try:
                info = comment.get_attribute("data-info")
                c_id = info.split("commentNo:")[1].split(",")[0].strip("'")
                if c_id not in collected_ids:
                    content = comment.find_element(By.CLASS_NAME, "u_cbox_contents").text
                    
                    # [로그 추가] 수집되는 모든 댓글 자바로 전송
                    send_log_to_java(f"[수집] {content[:20]}...")
                    
                    p, n = analyze_sentiment(content)
                    if team_a in content: save_to_db(game_id, team_a, team_b, p, n, 0, 0)
                    if team_b in content: save_to_db(game_id, team_a, team_b, 0, 0, p, n)
                    collected_ids.add(c_id)
            except: continue

        # --- [3단계] 안정화 대기 및 자동 업데이트 활성화 ---
        print("과거 수집 완료. 실시간 모드 준비를 위해 상단으로 이동합니다.")
        try:
            cbox_list = driver.find_element(By.CLASS_NAME, "u_cbox_list")
            driver.execute_script("arguments[0].scrollTop = 0", cbox_list)
            
            print("5초간 대기하며 자동 업데이트 토글을 확인합니다...")
            time.sleep(5)

            refresh_toggle = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, "u_cbox_btn_refresh")))
            driver.execute_script("arguments[0].click();", refresh_toggle)
            print("✔️ 자동 업데이트 토글 활성화 성공!")
            send_log_to_java("✅ 실시간 자동 업데이트 모드 활성화 완료")
        except Exception as e:
            print(f"⚠️ 토글 활성화 중 문제 발생: {e}")

        # --- [4단계] 실시간 수집 무한 루프 ---
        print("실시간 감시 모드 작동 중...")
        while True:
            try:
                curr_comments = driver.find_elements(By.CLASS_NAME, "u_cbox_comment")[:20]
                for comment in curr_comments:
                    info = comment.get_attribute("data-info")
                    c_id = info.split("commentNo:")[1].split(",")[0].strip("'")
                    if c_id not in collected_ids:
                        content = comment.find_element(By.CLASS_NAME, "u_cbox_contents").text
                        
                        # [로그 추가] 실시간 신규 댓글 자바로 전송
                        send_log_to_java(f"[실시간] {content[:30]}...")
                        
                        p, n = analyze_sentiment(content)
                        if team_a in content: save_to_db(game_id, team_a, team_b, p, n, 0, 0)
                        if team_b in content: save_to_db(game_id, team_a, team_b, 0, 0, p, n)
                        collected_ids.add(c_id)
                        print(f"[LIVE] {content[:15]}...")
            except: pass
            time.sleep(3)

    finally:
        driver.quit()

if __name__ == "__main__":
    start_crawling("https://m.sports.naver.com/game/20260304022F227/preview", "한국도로공사", "페퍼저축은행")