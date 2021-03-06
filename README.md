# 우리 동네 캡짱 [![Build Status](https://travis-ci.org/woowacourse-teams/2020-legeno-around-here.svg?branch=develop)](https://travis-ci.org/woowacourse-teams/2020-legeno-around-here)

## 개요

우리 동네 캡짱은 사용자가 특정 지역에서 자랑하고 싶은 것을 뽐내고 경쟁할 수 있는 서비스입니다. 사용자는 특정 지역에 하나의 부문에 속하는 자랑글을 작성할 수 있고, 자랑글은 좋아요 수를 기준으로 랭킹에 기록됩니다. 예를 들어, 사용자는 지역을 '신천동'으로 설정하고 부문을 '코딩'이라고 설정 후 '저는 프로그래밍을 잘합니다'라는 자랑글을 작성할 수 있습니다. 좋아요 수가 많은 자랑글은 실시간 및 누적 랭킹에 기록됩니다.

## 핵심 용어

### 사용자

사용자(`user`)는 이메일 본인 인증을 통한 회원가입 및 로그인을 통해 우리 동네 캡짱을 이용할 수 있습니다.

### 지역

지역(`area`)은 사용자가 자랑글을 작성할 때 설정하는 공간을 의미합니다. 지역은 위계에 따라 분류됩니다. 예를 들어, 지역은 동/구/시 등으로 분류됩니다. 자랑글은 최소 단위 지역(현재 기준, 동)에 속하고, 하위 지역은 상위 지역에 속합니다.

### 부문

부문(`sector`)은 사용자가 자랑글을 작성할 때 설정하는 '글의 주제'를 의미합니다. 이는 사용자가 자랑거리를 소개할 '종목' 혹은 '글의 카테고리' 등으로 해석될 수 있습니다. 예를 들어, '노래 부르기', '시 쓰기' 등이 부문이 될 수 있습니다.

부문은 서비스 런칭 전에 운영자가 다수의 부문을 등록해 놓습니다. 운영자는 서비스 런칭 후에 부문을 승인 없이 등록할 수 있습니다. 사용자는 운영자에게 부문 등록을 신청할 수 있습니다. 사용자는 부문 등록을 신청한 즉시 그 부문에 자랑글을 작성할 수 있습니다. 다만, 해당 자랑글은 다른 사용자들에게 보이지 않습니다. 운영자가 사용자의 부문 등록 신청을 승인하면, 부문은 등록됩니다. 부문이 등록되기 전 작성되어 작성자만 볼 수 있던 자랑글은 부문이 등록된 후 비작성자들에게 보여집니다. 부문이 적절치 않아 운영자가 부문 등록 신청을 반려하면, 사용자는 운영자로부터 부문 등록 재신청을 요청받게 됩니다. 부문은 다음과 같은 상태를 갖습니다.

| 부문 상태       | 설명                                                         |
| --------------- | ------------------------------------------------------------ |
| ApprovalPending | 사용자가 운영자에게 등록 요청을 했고, 운영자는 승인/반려 처리를 하지 않은 상태입니다. |
| Approved        | 운영자가 사용자의 등록 요청을 승인한 상태입니다.             |
| Rejected        | 운영자가 사용자의 등록 요청을 반려한 상태입니다.             |

### 자랑글

자랑글(`post`)은 사용자가 지역 및 부문을 설정하고 내용을 작성한 글입니다. 하나의 자랑글은 하나의 최소 단위 지역에 속하고, 하나의 부문에 매칭됩니다. 하나의 자랑글은 다수의 댓글을 포함할 수 있습니다. 자랑글의 내용은 글귀, 사진, 동영상, 음성 등입니다. 

사용자는 자랑글에 좋아요 표시 및 신고를 할 수 있습니다. 좋아요가 많은 자랑글은 지역과 부문에 따라 기간별 및 누적 랭킹에 기록됩니다. 예를 들어, '신천동' 지역에 속하고 '코딩' 부문에 매칭되는 자랑글들 중 일정 기간(현재 기준, 한 달) 동안 좋아요 수가 많은 자랑글은 기간별 랭킹에 등록됩니다. 또한 '신천동' 지역에 속하고 '코딩' 부문에 매칭되는 자랑글들 중 누적 좋아요 수가 많은 자랑글은 누적 랭킹에 등록됩니다. 

사용자는 다수의 자랑글을 다양한 기준으로 조회할 수 있습니다. 예를 들어, 사용자는 지역별 및 부문별로 다수의 자랑글을 조회할 수 있습니다. 조회된 자랑글들은 최신순으로 정렬됩니다.

## 이따비 팀

- [라흐](https://github.com/Hamliet)
- [빙봉](https://github.com/aegis1920)
- [제이미](https://github.com/jamie9504)
- [임루트](https://github.com/ihoph)
- [히히](https://github.com/yelimkim98)