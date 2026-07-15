# 手動建立 GitHub Action 說明（Java CI + AI 程式碼審查）

本文件說明如何從零手動建立本專案的 GitHub Actions 工作流程：
**編譯 → JUnit 自動測試 → NVIDIA Nemotron AI 程式碼審查**。

適用情境：純 `javac` 專案（無 Maven／Gradle 建置工具）。

- 對應工作流程檔：[.github/workflows/build.yml](../.github/workflows/build.yml)
- 執行紀錄：[CI執行紀錄.md](CI執行紀錄.md)

---

## 目錄
1. [前置需求](#1-前置需求)
2. [專案結構](#2-專案結構)
3. [步驟一：撰寫 JUnit 測試](#3-步驟一撰寫-junit-測試)
4. [步驟二：建立工作流程檔](#4-步驟二建立工作流程檔)
5. [步驟三：設定 NVIDIA API 金鑰 Secret](#5-步驟三設定-nvidia-api-金鑰-secret)
6. [步驟四：觸發與驗證](#6-步驟四觸發與驗證)
7. [常見問題](#7-常見問題)

---

## 1. 前置需求

| 項目 | 說明 |
|------|------|
| GitHub repo | 專案已推上 GitHub |
| JDK 21 | 本機開發用；CI 由 `setup-java` 自動安裝 |
| NVIDIA API 金鑰 | 至 <https://build.nvidia.com> 註冊取得，用於 AI 審查 |
| （選用）本機 JUnit jar | 想在本機跑測試時下載 `junit-platform-console-standalone` |

---

## 2. 專案結構

```
javaPra03/
├─ .github/
│  └─ workflows/
│     └─ build.yml          ← GitHub Actions 工作流程
├─ src/                     ← 主程式原始碼
│  ├─ App.java
│  ├─ Practice01.java
│  └─ Practice02.java
├─ test/                    ← JUnit 測試
│  └─ Practice01Test.java
├─ lib/                     ← CI 下載的 JUnit jar（.gitignore）
└─ .gitignore
```

`.gitignore` 建議內容：

```gitignore
bin/
bin-test/
lib/*.jar
sources.txt
tests.txt
pr.diff
review.md
```

---

## 3. 步驟一：撰寫 JUnit 測試

在 `test/` 下建立測試類別，使用 JUnit 5（Jupiter）。範例 `test/Practice01Test.java`：

```java
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class Practice01Test {
    @Test
    void 一般順序() {
        assertEquals(2, Practice01.median(3, 1, 2));
    }
    // ... 其他測試案例
}
```

> 純 javac 專案不需 `pom.xml`／`build.gradle`；CI 會下載
> `junit-platform-console-standalone.jar` 來編譯與執行測試。

### （選用）本機執行測試

```bash
# 下載 JUnit console launcher
curl -sSL -o lib/junit-platform-console-standalone.jar \
  "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar"

# 編譯主程式與測試（Windows 用 ; 分隔 classpath，Linux/macOS 用 :）
javac -encoding UTF-8 -d bin src/*.java
javac -encoding UTF-8 -cp "bin;lib/junit-platform-console-standalone.jar" -d bin-test test/*.java

# 執行測試
java -jar lib/junit-platform-console-standalone.jar execute -cp "bin;bin-test" --scan-classpath --details=tree
```

---

## 4. 步驟二：建立工作流程檔

建立 `.github/workflows/build.yml`。重點說明如下（完整內容見實際檔案）。

### 4.1 觸發條件與權限

```yaml
name: Java CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

permissions:
  contents: read
  pull-requests: write        # AI 審查要能在 PR 留言

env:
  JUNIT_VERSION: '1.11.4'
  NEMOTRON_MODEL: 'nvidia/nemotron-3-ultra-550b-a55b'   # 至 build.nvidia.com 確認 id
```

### 4.2 編譯與測試步驟

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'

      - name: Cache JUnit console launcher
        uses: actions/cache@v4
        with:
          path: lib/junit-platform-console-standalone.jar
          key: junit-${{ env.JUNIT_VERSION }}

      - name: Download JUnit console launcher
        run: |
          mkdir -p lib
          if [ ! -f lib/junit-platform-console-standalone.jar ]; then
            curl -sSL -o lib/junit-platform-console-standalone.jar \
              "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${JUNIT_VERSION}/junit-platform-console-standalone-${JUNIT_VERSION}.jar"
          fi

      - name: Compile sources
        run: |
          mkdir -p bin
          find src -name "*.java" > sources.txt
          javac -encoding UTF-8 -d bin @sources.txt

      # ↓↓↓ AI 審查步驟（見 4.3） ↓↓↓

      - name: Compile tests
        run: |
          mkdir -p bin-test
          find test -name "*.java" > tests.txt
          javac -encoding UTF-8 -cp "bin:lib/junit-platform-console-standalone.jar" -d bin-test @tests.txt

      - name: Run tests
        run: |
          java -jar lib/junit-platform-console-standalone.jar execute \
            -cp "bin:bin-test" --scan-classpath --details=tree

      - name: Run App
        run: java -cp bin App
```

> 注意：Linux runner 的 classpath 分隔符號是 `:`（本機 Windows 才是 `;`）。

### 4.3 AI 程式碼審查步驟（NVIDIA Nemotron）

放在 `Compile sources` 之後、`Compile tests` 之前：

```yaml
      - name: AI code review (NVIDIA Nemotron)
        # always()：即使前面編譯失敗，仍對 diff 做審查
        if: always() && github.event_name == 'pull_request'
        continue-on-error: true
        env:
          NVIDIA_API_KEY: ${{ secrets.NVIDIA_API_KEY }}
          GH_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          PR_NUMBER: ${{ github.event.pull_request.number }}
          BASE_REF: ${{ github.base_ref }}
        run: |
          set -euo pipefail
          if [ -z "${NVIDIA_API_KEY:-}" ]; then
            echo "::warning::未設定 NVIDIA_API_KEY secret，略過 AI 程式碼審查。"
            exit 0
          fi

          # 取得 PR diff
          gh pr diff "$PR_NUMBER" --patch > pr.diff \
            || git diff "origin/${BASE_REF}...HEAD" > pr.diff
          [ -s pr.diff ] || { echo "沒有可審查的差異，略過。"; exit 0; }
          DIFF_CONTENT=$(head -c 60000 pr.diff)   # 限制輸入長度

          # 用 jq 安全組出 request payload
          PAYLOAD=$(jq -n \
            --arg model "$NEMOTRON_MODEL" \
            --arg sys "你是一位資深 Java 工程師。請針對以下 PR diff 做程式碼審查，用繁體中文條列指出潛在 bug、邊界情況、風格與可讀性問題並給改進建議；若沒有問題請明確回覆 LGTM。" \
            --arg diff "$DIFF_CONTENT" \
            '{model:$model, temperature:0.2, max_tokens:1024,
              messages:[{role:"system",content:$sys},
                        {role:"user",content:("以下是 PR 的 diff：\n\n"+$diff)}]}')

          # 呼叫 NVIDIA OpenAI 相容端點
          RESPONSE=$(curl -sS https://integrate.api.nvidia.com/v1/chat/completions \
            -H "Authorization: Bearer $NVIDIA_API_KEY" \
            -H "Content-Type: application/json" -d "$PAYLOAD")

          REVIEW=$(echo "$RESPONSE" | jq -r '.choices[0].message.content // empty')
          [ -n "$REVIEW" ] || { echo "::warning::API 回應異常：$RESPONSE"; exit 0; }

          # 把審查結果貼回 PR
          {
            echo "## 🤖 NVIDIA Nemotron 程式碼審查"; echo
            echo "$REVIEW"; echo
            echo "<sub>模型：\`${NEMOTRON_MODEL}\`；此審查僅供參考。</sub>"
          } > review.md
          gh pr comment "$PR_NUMBER" --body-file review.md
```

**設計重點**

| 設定 | 用意 |
|------|------|
| `if: always() && ... pull_request` | 只在 PR 觸發；即使編譯失敗也要審查程式碼 |
| `continue-on-error: true` | AI 服務不穩不會擋掉整條 pipeline（純顧問性質） |
| `permissions: pull-requests: write` | 讓 `gh pr comment` 有權限留言 |
| `head -c 60000` | 避免 diff 過大超過模型輸入上限 |
| `jq -n --arg` | 安全組 JSON、自動處理特殊字元跳脫 |

---

## 5. 步驟三：設定 NVIDIA API 金鑰 Secret

1. 到 <https://build.nvidia.com> 取得 API 金鑰。
2. 在 GitHub repo：**Settings → Secrets and variables → Actions → New repository secret**。
3. 名稱填 `NVIDIA_API_KEY`，值貼上金鑰，儲存。

> 未設定時，AI 審查步驟會發出 warning 並自動略過（不會使建置失敗）。

同時確認 `env.NEMOTRON_MODEL` 的模型 id 與 build.nvidia.com 目錄一致。

---

## 6. 步驟四：觸發與驗證

| 觸發方式 | 會執行什麼 |
|----------|-----------|
| push 到 `main` | 編譯 + 測試（AI 審查因非 PR 而略過） |
| 對 `main` 開 PR | 編譯 + 測試 + **AI 審查留言** |

驗證位置：

- **Actions 分頁**：看各步驟綠勾／紅叉與 log
  <https://github.com/Lin-Pohan-div/java20260623/actions>
- **PR 的 Conversation 分頁**：看 `github-actions[bot]` 貼的 AI 審查留言
- **PR 的 Checks 分頁**：看整體 CI 狀態

> 實測範例見 [CI執行紀錄.md](CI執行紀錄.md)。

---

## 7. 常見問題

**Q：AI 審查步驟沒有留言？**
- 檢查是否為 `pull_request` 事件（push 不會觸發 AI 審查）。
- 檢查 `NVIDIA_API_KEY` secret 是否已設定。
- 看該步驟 log 是否有 `::warning::` 訊息。

**Q：`gh pr comment` 權限錯誤？**
- 確認工作流程有 `permissions: pull-requests: write`。

**Q：編譯測試時找不到 JUnit 類別？**
- 確認 classpath 分隔符號：Linux runner 用 `:`，本機 Windows 用 `;`。

**Q：模型回應異常／404？**
- 確認 `NEMOTRON_MODEL` 的 id 正確（以 build.nvidia.com 為準）。

**Q：想讓編譯失敗就不要審查？**
- 把 AI 步驟的 `if` 改成 `github.event_name == 'pull_request'`（移除 `always()`）。
