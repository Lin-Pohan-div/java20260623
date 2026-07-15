# CI/CD 執行紀錄與 AI 審查留言

本檔記錄每次 GitHub Actions（`Java CI` 工作流程）的執行結果，以及 NVIDIA Nemotron AI 程式碼審查留言。最新的紀錄放在最上方。

- **Repo**：<https://github.com/Lin-Pohan-div/java20260623>
- **Actions 總覽**：<https://github.com/Lin-Pohan-div/java20260623/actions>
- **工作流程檔**：[.github/workflows/build.yml](../.github/workflows/build.yml)

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
