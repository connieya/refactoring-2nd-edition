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

