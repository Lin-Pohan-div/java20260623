# CI/CD 執行紀錄與 AI 審查留言

本檔記錄每次 GitHub Actions（`Java CI` 工作流程）的執行結果，以及 NVIDIA Nemotron AI 程式碼審查留言。最新的紀錄放在最上方。

- **Repo**：<https://github.com/Lin-Pohan-div/java20260623>
- **Actions 總覽**：<https://github.com/Lin-Pohan-div/java20260623/actions>
- **工作流程檔**：[.github/workflows/build.yml](../.github/workflows/build.yml)

---

## #2 · 2026-07-15 · PR #2（Practice02／03 測試 + AI 審查再驗證）

| 項目 | 內容 |
|------|------|
| 事件 | `pull_request` |
| 分支 | `test/practice02-03-tests` → `main` |
| PR | [#2 新增 Practice02、Practice03 的 JUnit 測試](https://github.com/Lin-Pohan-div/java20260623/pull/2) |
| Run | [29406299376](https://github.com/Lin-Pohan-div/java20260623/actions/runs/29406299376)（Java CI #10） |
| 總結果 | ✅ success（35 秒，全步驟通過） |
| 模型 | `nvidia/nemotron-3-ultra-550b-a55b` |

**本次變更**

- 將 `Practice02` 的階乘邏輯抽成可測的 `factorial(int n)`（負數丟 `IllegalArgumentException`）。
- 新增 `Practice02Test`（0!／1!／一般值／20! 不溢位／負數例外，共 5 個）。
- 新增 `Practice03Test`（數字／字母／中文字／空白／其他，共 5 個）。
- 本機與 CI 皆 16 個測試全數通過。

**各步驟結果**

| 步驟 | 結果 | 說明 |
|------|------|------|
| Checkout / Set up JDK 21 | ✅ success | |
| Cache / Download JUnit console launcher | ✅ success | |
| Compile sources | ✅ success | Practice02 重構後正常編譯 |
| **AI code review (NVIDIA Nemotron)** | ✅ success | 成功於 PR 留言，無 `NVIDIA_API_KEY` warning |
| Compile tests / Run tests | ✅ success | 16 個測試全過 |
| Run App | ✅ success | |

**AI 審查留言**（摘要，[原始留言見 PR #2](https://github.com/Lin-Pohan-div/java20260623/pull/2)）

> ## 🤖 NVIDIA Nemotron 程式碼審查
>
> Code Review 結果：整體來說這是一個不錯的 PR，將階乘邏輯抽出（`factorial`）使其可測試，測試涵蓋 0!、邊界（20! 不溢位）與負數例外，設計良好。
>
> <sub>模型：`nvidia/nemotron-3-ultra-550b-a55b`；此審查僅供參考。</sub>

> 註：與 #1（push 到 main 不觸發 AI 審查）對照，本次為 `pull_request` 事件，AI 審查如預期執行 — 印證審查僅在 PR 觸發之設計。

---

## #1 · 2026-07-15 · PR #1（AI 審查實測）

| 項目 | 內容 |
|------|------|
| 事件 | `pull_request` |
| 分支 | `test/ai-review-practice02` → `main` |
| PR | [#1 測試 AI 審查：Practice02 階乘（含刻意 bug）](https://github.com/Lin-Pohan-div/java20260623/pull/1) |
| Run | [29402255309](https://github.com/Lin-Pohan-div/java20260623/actions/runs/29402255309) |
| 總結果 | ❌ failure（因刻意的編譯錯誤，符合預期） |
| 模型 | `nvidia/nemotron-3-ultra-550b-a55b` |

**各步驟結果**

| 步驟 | 結果 | 說明 |
|------|------|------|
| Checkout / Set up JDK 21 | ✅ success | |
| Cache / Download JUnit console launcher | ✅ success | |
| Compile sources | ❌ failure | `Practice02.java` 使用未宣告的 `i`，編譯器擋下 |
| **AI code review (NVIDIA Nemotron)** | ✅ success | 靠 `always()`，編譯失敗仍執行並留言 |
| Compile tests / Run tests / Run App | ⏭ skipped | 因前面編譯失敗而略過 |

**AI 審查留言**（[原始留言](https://github.com/Lin-Pohan-div/java20260623/pull/1#issuecomment-4978713843)）

> ## Code Review: Practice02.java
>
> ### ❌ 編譯錯誤（阻斷性）
>
> | 行號 | 問題 | 說明 |
> |------|------|------|
> | 4 | **變數 `i` 未宣告** | `System.out.println(i);` 使用了未定義的變數 `i`，會導致編譯失敗：`error: cannot find symbol` |
>
> ### ⚠️ 潛在邏輯問題（視需求而定）
>
> 1. **缺少變數初始化邏輯** — 若意圖是印出某個計算結果（如階乘、累加等），需補上完整的演算法實作。
> 2. **類別命名** — `Practice02` 為練習用命名，正式專案建議使用語意化名稱（如 `FactorialCalculator`）。
>
> ### 🔧 改進建議
>
> ```java
> public class Practice02 {
>     public static void main(String[] args) {
>         // 範例：計算 5! = 120
>         int n = 5;
>         long result = 1;
>         for (int i = 2; i <= n; i++) {
>             result *= i;
>         }
>         System.out.println(result);
>     }
> }
> ```
>
> ### 總結
> **無法通過編譯，請修正後再提交。** 修復上述錯誤後可再請求複審。

---

<!--
新增紀錄範本（複製到「最新紀錄」區塊上方）：

## #N · YYYY-MM-DD · 事件說明

| 項目 | 內容 |
|------|------|
| 事件 | push / pull_request |
| 分支 | ... |
| PR | ... |
| Run | https://github.com/Lin-Pohan-div/java20260623/actions/runs/<run_id> |
| 總結果 | ✅ success / ❌ failure |
| 模型 | nvidia/nemotron-3-ultra-550b-a55b |

**各步驟結果**

| 步驟 | 結果 | 說明 |
|------|------|------|
| ... | ... | ... |

**AI 審查留言**

> （貼上留言內容）
-->
