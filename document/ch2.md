# Chapter2. 리팩터링 원칙

## 리팩터링 정의

> 리팩터링 : 소프트웨어의 겉보기 동작은 그대로 유지한 채, 코드를 이해하고 수정하기 쉽도록 내부 구조를 
> 변경하는 기법

리팩터링은 결국 동작을 보존하는 작은 단계들을 거쳐 코드를 수정하고, 이러한 단계들을 
순차적으로 연결하여 큰 변화를 만들어내는 일이다. 개별 리팩터링은 그 자체로 아주 작을 수도 있고,
작은 단계 여러 개가 합쳐진 모습일 수도 있다.
따라서 리팩터링 하는 동안에는 코드가 항상 정상 작동하기 때문에 전체 작업이 끝나지 않았더라도 
언제든 멈출 수있다.

> 누군가 "리팩터링하다가 코드가 깨져서 며칠이나 고생했다" 라고 한다면, 십중팔구 리팩터링 한 것이 아니다.


## 리팩터링하는 이유

### 리팩터링하면 소프트웨어 설계가 좋아진다

리팩터링하지 않으면 소프트웨의 내부 설계 (아키텍처)가 썩기 쉽다.
아키텍처를 충분히 이해하지 못한 채 단기 목표만을 위해 코드를 수정하다 보면 기반 구조가 
무너지기 쉽다.

### 리팩터링하면 소프트웨어를 이해하기 쉬워진다.

내 소스코드를 컴퓨터만 사용하는 것이 아니다. 예컨대 몇 달이 지나 누군가 내 코드를
수정하고자 읽게 될 수 있다. 사실 프로그래밍에서는 사람이 가장 중요하지만 소홀하기 쉽다.

### 리팩터링하면 버그를 쉽게 찾을 수 있다.

코드를 이해하기 쉽다는 말은 버그를 찾기 쉽다는 말이기도 하다.

### 리팩터링하면 프로그래밍 속도를 높일 수 있다.

새로운 기능을 추가할수록 기존 코드베이스에 잘 녹여낼 방법을 찾는 데 드는 시간이
늘어난다 게다가 기능을 추가하고 나면 버그가 발생하는 일이 잦고, 이를 해결하는 시간은
한층 더 걸린다.

내부 설계가 잘 된 소프트웨어는 새로운 기능을 추가할 지점과 어떻게 고칠지를 쉽게 찾을 수 있다.
모듈화가 잘 되어 있으면 전체 코드베이스 중 작은 일부만 이해하면 된다.


## 언제 리팩터링해야 할까?

> 3의 법칙
> <br/>
> 1. 처음에는 그냥 한다.
> 2. 비슷한 일을 두 번째로 하게 되면(중복이 생겼다는 사실에 당황스럽겠지만), 일단 계속 진행한다.
> 3. 비슷한 일을 세 번째 하게 되면 리팩터한다.


### 준비를 위한 리팩터링 : 기능을 쉽게 추가하게 만들기
리팩터하기 가장 좋은 시점은 코드배이스에 기능을 새로 추가하기 직전이다.


### 이해를 위한 리팩터링 : 코드를 이해하기 쉽게 만들기

코드를 수정하려면 먼저 그 코드가 하는 일을 파악해야 한다.
이해를 위한 리팩터링을 의미 없이 코드를 만지작거리는 것이라고 무시하는 이들은
복잡한 코드 아래 숨어 있는 다양한 기회를 결코 발견할 수 없다.

### 쓰레기 줍기 리팩터링

간단히 수정할 수 있는 것은 즉시 고치고, 시간이 좀 걸리는 일은 짧은 메모만 남긴 다음,
하던 일을 끝내고 나서 처리 한다.
이것이 이해를 위한 리팩터링의 변형인 쓰레기 줍기 리팩터링이다.

### 계획된 리팩터링과 수시로 하는 리팩터링

> 보기 싫은 코드를 발견하면 리팩터링하자. 그런데 잘 작성된 코드 역시 수많은 리팩터링을 거쳐야 한다.


뛰어난 개발자는 새 기능을 추가하기 쉽도록 코드를 '수정' 하는 것이 그 기능을 
빠르게 추가하는 길 일수 있음을 안다.


### 코드 리뷰에 리팩터링 활용하기

리팩터링은 다른이의 코드를 리뷰하는 데도 도움이 된다.

리팩터링은 코드 리뷰의 결과를 더 구체적으로 도출하는 데에도 도움된다.


### 리팩터링 하지 말아야 할 때 

지저분한 코드를 발견해도 굳이 수정할 필요가 없다면 리팩터링 하지 않는다. 외부 API 다루듯 
호출해서 쓰는 코드라면 지저분해도 그냥 둔다.
내부 동작을 이해해야 할 시점에 리팩터해야 효과를 제대로 볼 수 있다.

## 리팩터링 시 고려할 문제

### 새 기능 개발 속도 저하

> 리팩터링의 궁극적인 목적은 개발 속도를 높여서 , 더 적은 노력으로 더 많은 가치를 창출하는 것이다.


건강한 코드의 위력을 충분히 경험해보지 않고서는 코드베이스가 건강할 때와 허약할 때의 생산성 차이를
체감하기 어렵다.

리팩터링의 본질은 코드 베이스를 예쁘게 꾸미는 데 있지 않다.  
오로지 경제적인 이유로 하는 것이다.
리팩터링은 개발 기간을 단축하고자 하는 것이다.


### 레거시 코드

래거시 시스템을 파악할 때 리팩터링이 굉장히 도움된다.

## 리팩터링과 성능

리팩터링하면 소프트웨어가 느려질 수도 있는 건 사실이다.
하지만 그와 동시에 성능을 튜닝하기는 더 쉬워진다.
하드 리얼타임 시스템을 제외한 소프트웨어를 빠르게 만드는 비결은, 먼저 튜닝하기 쉽게 만들고 나서
원하는 속도가 나게끔 튜닝하는 것이다.



