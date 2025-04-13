# Chapter1. 리팩터링: 첫 번째 예시

- 예제를 통해 설명을 시작하는 것이 이해하기 좋다.
- 너무 큰 프로그램은 이해하기에 어렵고 너무 작으면 refactoring 의 가치를 느끼기 어렵다.

## 요구사항 분석

- 다양한 연극을 외주로 받아서 공연하는 극단
- 공연 요청이 들어오면 연극의 장르와 관객 규모를 기초로 비용을 책정한다.
- 공연료와 별개의 포인트를 지급해서 다음번 의뢰 시 공연료를 할인 받을 수도 있다.


play.json
```json
{
  "hamlet": { "name": "Hamlet", "type": "tragedy" },
  "as-like": { "name": "As You Like It", "type": "comedy" },
  "othello": { "name": "Othello", "type": "tragedy" }
}
```

invoices.json

```json
[
  {
    "customer": "BigCo",
    "performances": [
      {
        "playID": "hamlet",
        "audience": 55
      },
      {
        "playID": "as-like",
        "audience": 35
      },
      {
        "playID": "othello",
        "audience": 40
      }
    ]
  }
]
```