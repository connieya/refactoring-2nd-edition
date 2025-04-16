# Chapter1. 리팩터링: 첫 번째 예시

- 예제를 통해 설명을 시작하는 것이 이해하기 좋다.
- 너무 큰 프로그램은 이해하기에 어렵고 너무 작으면 refactoring 의 가치를 느끼기 어렵다.

## 요구사항 분석

- 다양한 연극을 외주로 받아서 공연하는 극단
- 공연 요청이 들어오면 연극의 장르와 관객 규모를 기초로 비용을 책정한다.
- 공연료와 별개의 포인트를 지급해서 다음번 의뢰 시 공연료를 할인 받을 수도 있다.

공연할 연극 정보

play.json
```json
{
  "hamlet": { "name": "Hamlet", "type": "tragedy" },
  "as-like": { "name": "As You Like It", "type": "comedy" },
  "othello": { "name": "Othello", "type": "tragedy" }
}
```

청구서에 들어갈 데이터

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

공연료 청구서를 출력하는 코드

```java
package java.client.chapter01.before;

import java.client.chapter01.data.Invoice;
import java.client.chapter01.data.Performance;
import java.client.chapter01.data.Play;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Statement {

    public String statement(Invoice invoice , Map<String , Play> plays) {
        int totalAmount = 0;
        int volumeCredits = 0;
        StringBuilder result = new StringBuilder("청구 내역 (고객명 : " + invoice.getCustomer() + ")\n");
        NumberFormat format =  NumberFormat.getCurrencyInstance(Locale.US);

        for (Performance performance : invoice.getPerformances()) {
            Play play = plays.get(performance.getPlayID());
            int thisAmount = 0;

            switch (play.getType()) {
                case "tragedy":
                    thisAmount = 40000;
                    if (performance.getAudience() > 30) {
                        thisAmount += 1000 * (performance.getAudience() - 30);
                    }
                    break;
                case "comedy" :
                    thisAmount = 30000;
                    if (performance.getAudience() > 20) {
                        thisAmount += 10000 + 500 *(performance.getAudience() - 20);
                    }
                    thisAmount += 300 * performance.getAudience();
                    break;
                default:
                    throw new IllegalArgumentException("알 수 없는 장르: " + play.getType());
            }
            volumeCredits += Math.max(performance.getAudience() - 30 , 0);

            if ("comedy".equals(play.getType())) {
                volumeCredits += Math.floor(performance.getAudience() / 5);
            }

            result.append(
                    String.format(
                            " %s : %s원 (%d석) \n",
                            play.getName(),
                            format.format(thisAmount / 100.0),
                            performance.getAudience()
                    )
            );

            totalAmount += thisAmount;

        }

        result.append(String.format("총액 : %s원\n" , format.format(totalAmount / 100.0)));
        result.append(String.format("적립 포인트 : %d점\n", volumeCredits));

        return result.toString();

    }
}

```

## 예시 프로그램을 본 소감

- 설계가 나쁜 시스템은 수정하기 어렵다. 
- 원하는 동작을 수행하도록 하기 위해 수정해야 할 부분을 찾고, 기존 코드와 잘 맞물려 작동하게 할 방법을 강구하기가 어렵기 때문이다.
- 무엇을 수정할지 찾기 어렵다면 실수를 저질러서 버그가 생길 가능성도 높아진다.


=> 수백 줄짜리 코드를 수정할 때면 먼저 프로그램의 작동 방식을 더 쉽게 파악할 수 있도록
코드를 여러 함수와 프로그램 요소로 재구성한다.

> 프로그램이 새로운 기능을 추가하기에 편한 구조가 아니라면, 먼저 기능을 추가하기 쉬운 형태로
> 리팩토링하고 나서 원하는 기능을 추가한다. 

## 리팩터링의 첫 단계

> 리팩터링하기 전에 제대로 된 테스트부터 마련한다. 테스트는 반드시 자가진단하도록 만든다.

statement() 함수의 테스트는 어떻게 구성하면 될까? 
- 이 함수가 문자열을 반환하므로, 다양한 장르의 공연들로 구성된 공연료 청구서 몇 개를 미리 작성하여 문자열 형태로 준비해둔다.

## statement() 함수 쪼개기

### 함수 추출하기

> 리팩터링은 프로그램 수정을 작은 단계로 나눠 진행한다. 그래서 중간에 실수하더라고 버그를 쉽게 찾을 수 있다.


```java
 private int amountFor(Performance aPerformance , Play play) {
    int result = 0;

    switch (play.getType()) {
        case "tragedy":
            result = 40000;
            if (aPerformance.getAudience() > 30) {
                result += 1000 * (aPerformance.getAudience() - 30);
            }
            break;
        case "comedy" :
            result = 30000;
            if (aPerformance.getAudience() > 20) {
                result += 10000 + 500 *(aPerformance.getAudience() - 20);
            }
            result += 300 * aPerformance.getAudience();
            break;
        default:
            throw new IllegalArgumentException("알 수 없는 장르: " + play.getType());
    }

    return result;
}
```

- 함수 추출하기
  - 코드가 하는 일을 설명하는 이름을 지어줌으로써, 코드를 분석하며 파악한 정보를 코드에 반영한다.
- 변수 네이밍 변경하기
  - 변수의 역할을 쉽게 알 수 있다.
- 파라미터 네이밍 변경하기
  - 동적 타입 언어를 사용할 때 타입이 드러나게 작성하면 도움이 된다.
  - 매개변수의 역할이 뚜렷하지 않을 때는 부정 관사(a/an)을 붙인다.

> 컴퓨터가 이해하는 코드는 바보도 작성할 수 있다. 사람이 이해하도록 작성하는 프로그래머가 진정한 실력자다.


### play 변수 제거하기

#### 임시 변수를 질의 함수로 바꾸기 

- play는 개별 공연(aPerformance) 에서 얻기 때문에 애초에 매개변수로 전달할 필요가 없다.
- 임시 변수들 때문에 로컬 범위에 존재하는 이름이 늘어나서 추출 작업이 복잡해진다.

#### 변수 인라인하기
- 함수 선언 바꾸기
- playFor() 메서드를 사용하도록 amountFor() 수정
- play 매개변수 삭제하기
- 변수 인라인하기

지역 변수를 제거해서 얻는 가장 큰 장점은 추출 작업이 훨씬 쉬워진다는 것이다.
유효범위를 신경 써야할 대상이 줄어들기 때문이다.

#### 함수 선언 바꾸기

format 변수 제거하기

- 함수 변수를 일반함수로 변경
- 함수 선언 바꾸기
- 단위 변환 로직 (나눗셈)을 함수 내로 위치 이동

```java
private String usd(final int aNumber) {
    final NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

    return format.format(aNumber / 100.0);
}
```

이름짓기는 중요하면서도 쉽지 않은 작업이다. 
긴 함수를 작게 쪼개는 리팩터링은 이름을 잘 지어야만 효과가 있다.
이름이 좋으면 함수 본문을 앍지 않고도 무슨 일을 하는지 알 수 있다.

물론 단번에 좋은 이름을 짓기는 쉽지 않다. 따라서 처음에는 당장 떠오르는 
최선의 이름을 사용하다가, 나중에 더 좋은 이름이 떠오를 때 바꾸는 식이 좋다.
흔히 코드를 두 번 이상 읽고 나서야 가장 적합한 이름이 떠오르곤 한다.

### volumeCredits 변수 제거하기

- 반복문 쪼개기로 volumeCredits 값이 누적되는 부분을 따로 빼낸다.
- 문장 슬라이드하기를 적용해서 volumeCredits 변수를 선언하는 문장을 반복문 바로 앞으로 옮긴다.

```java
public String statement(Invoice invoice, Map<String, Play> plays) {
  int totalAmount = 0;

  StringBuilder result = new StringBuilder("청구 내역 (고객명 : " + invoice.getCustomer() + ")\n");

  for (Performance performance : invoice.getPerformances()) {

    int thisAmount = amountFor(performance, playFor(performance, plays));
    // 청구 내역을 출력한다.
    result.append(
            String.format(
                    " %s : %s원 (%d석) \n",
                    playFor(performance, plays).getName(),
                    usd(thisAmount),
                    performance.getAudience()
            )
    );

    totalAmount += thisAmount;
  }

  int volumeCredits = 0;
  for (Performance performance : invoice.getPerformances()) {
    volumeCredits += volumeCreditsFor(performance,plays);
  }

  result.append(String.format("총액 : %s원\n", usd(totalAmount)));
  result.append(String.format("적립 포인트 : %d점\n", volumeCredits));

  return result.toString();

}
```

- volumeCredits 값 갱신과 관련한 문장들을 한데 모아두면 임시 변수를 질의 함수로 바꾸기가 수월해진다.

volumeCredits 깂 계산 코드를 함수로 추출하는 작업 부터 한다.

```java
private int totalVolumeCredits(Invoice invoice , Map<String, Play> plays) {
        int volumeCredits = 0;
        for (Performance performance : invoice.getPerformances()) {
            volumeCredits += volumeCreditsFor(performance,plays);
        }
        
        return volumeCredits;
    }
```

함수 추출이 끝나면 volumeCredits 변수를 인라인한다.


### totalAmount 제거하기

위 volumeCredits 변수 제거하기와 똑같은 절차로 제거하기

- 반복문 쪼개기
- 문장 슬라이드하기
- 임시 변수를 질의 함수로 바꾸기
  - 함수 추출하기
  - 변수 인라인하기


## 계산 단계와 포맷팅 단계 분리하기

statement() 의 HTML 버전을 만드는 작업

#### 단계 쪼개기

statement() 의 로직 두 단계로 나누기

#### 함수 추출하기

- 청구 내역을 출력하는 코드
- 두 단계 사이의 중간 데이터 구조 역할을 할 객체 만들어서 인수로 전달하기


#### 함수 호출을 중간 데이터 사용으로 변경

### 계산 단계와 포맷팅 단계 분리 결과
|단계|	담당 클래스|
|----|---|
|계산|	StatementData, EnrichPerformance|
|포매팅 |	Statement | 

- 코드량이 늘었다. 
  - 주된 원인 : 함수로 추출하면서 함수 본문을 열고 닫는 괄호가 덧붙었기 때문
- 전체 로직을 구성하는 요소 각각이 더 뚜렷이 부각 
  - 계산하는 부분과 출력 형식을 다루는 부분이 분리 됐다.
  - 모듈화 하면 각 부분이 하는 일과 그 부분들이 맞물려 돌아가는 과정을 파악하기 쉬워진다.

> 간결함이 지혜의 정수일지 몰라도, 프로그래밍에서만큼은 명료함이 진화할 수 있는 소프트웨어의 정수다.

### 다형성을 활용해 계산 코드 재구성하기 

amountFor 함수를 보면 연극 장르에 따라 계산 방식이 달라진다.
이런 형태의 조건부 로직은 코드 수정 횟수가 늘어날수록 골칫거리로 전략하기 쉽다.

#### 조건부 형식을 다형성으로 바꾸기 

- 공연료 계산기 만들기

amountFor() , volumeCreditsFor() 
이 두 함수를 전용 클래스로 옮기기

- 함수들을 계산기로 옮기기 


#### 공연료 계산기를 다형성 버전으로 만들기

