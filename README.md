# Calculator_API_Module
# :green_book: リポジトリの説明・概要
電卓アプリケーションで利用できる各種APIリポジトリです。  
マイクロサービスとして開発しているので、必要な機能だけを抽出し利用することができます。  
使い方については、**APIインタフェース定義**にてご確認ください。

# :green_book: 目的
電卓アプリケーションとしてのAPIだけではなく、上述のように必要な機能だけを抽出し利用する。  
例えば、足し算が必要なロジックをコーディングする際に当リポジトリの「足し算API」を呼び出すことで、ご自身で足し算を行うロジックを書かなくて良くなる。  
そんな利用シーンや目的をイメージしています。  

# :green_book: 利用言語
- Java  

# :green_book: APIの使い方
**＊当APIは予算の都合上publicなインターネット経由で利用できるようになっていません。その為、当リポジトリをクローンし、ご自身の環境で各種APIをご利用ください。**  
また、四則演算APIを利用するには、新規ユーザー登録APIにてユーザー情報を作成することが必須となっています。  
もし、WARファイルやjarファイルをご希望の方は、ご連絡をお待ちしております。  

## :snowflake: 実行環境の構築について  
構築DB：PostgreSQL 13  
ミドルウェアサーバ：WAS Liberty 8  
JDK：1.8  

## :snowflake: データベース構築SQL
・ユーザー情報管理テーブル作成SQL:  
```
CREATE TABLE T_USERINFO (
C_USERID VARCHAR(20) PRIMARY KEY DEFAULT 'First'
    ,C_PHONE VARCHAR(300) NOT NULL DEFAULT 08094209625
    ,C_PROFESSION VARCHAR(50) NOT NULL DEFAULT 'システムエンジニア'
    ,C_AGE SMALLINT NOT NULL DEFAULT 24
    ,C_DELETE_FLG SMALLINT NOT NULL DEFAULT 0
    ,C_LASTOPERATION TIMESTAMP NOT NULL DEFAULT '2022-02-02 00:00:00'
    ,C_REGISTRATIONDATE TIMESTAMP NOT NULL DEFAULT '2022-02-02 00:00:00'
    ,C_DELETEDATE TIMESTAMP DEFAULT '2022-02-02 00:00:00'
    ,C_PASSWORD VARCHAR(300) NOT NULL DEFAULT 'TestPassword@'
    ,C_LOGINDATE TIMESTAMP NOT NULL DEFAULT '2022-02-02 00:00:00'
    ,C_LOGOUTDATE TIMESTAMP NOT NULL DEFAULT '2022-02-02 00:00:00'
    ,C_JWTTOKEN VARCHAR(512) NOT NULL DEFAULT 'JWTToken'
    ,C_ADMINDELETE_FLG SMALLINT NOT NULL DEFAULT 0
    ,C_MAIL VARCHAR(300) NOT NULL DEFAULT 'masaki-m9420@softbank.ne.jp'
);
```
・ユーザー情報管理テーブルインデックス作成SQL:
```
CREATE INDEX ON T_USERINFO (C_PHONE, C_PROFESSION, C_DELETE_FLG, C_PASSWORD, C_ADMINDELETE_FLG, C_MAIL);
```
・計算結果格納テーブル作成SQL:
```
CREATE TABLE T_CALCRESULT (
C_USERID VARCHAR(20) NOT NULL DEFAULT 'First'
    ,C_NUM1 INTEGER NOT NULL DEFAULT 1
    ,C_NUM2 INTEGER NOT NULL DEFAULT 2
    ,C_SYMBOL VARCHAR(5) NOT NULL DEFAULT '＋'
    ,C_RESULT INTEGER NOT NULL DEFAULT 3
);
```
・計算結果格納テーブルインデック作成SQL:
```
CREATE INDEX ON T_CALCRESULT (C_USERID, C_RESULT);
```
・要望管理テーブル作成SQL:
```
CREATE TABLE T_USERREQUEST (
C_USERID VARCHAR(20) NOT NULL DEFAULT 'First'
    ,C_REQUEST VARCHAR(300) NOT NULL DEFAULT '使いやすくしてほしい'
    ,C_REGISTDATE TIMESTAMP NOT NULL DEFAULT '2022-02-02 00:00:00'
    ,C_PRIORITY VARCHAR(10) NOT NULL DEFAULT 'LOW'
);
```
・要望管理テーブルインデックス作成SQL:
```
CREATE INDEX ON T_USERREQUEST (C_USERID, C_PRIORITY);
```
・問い合わせ管理テーブル作成SQL
```
CREATE TABLE T_INQUIRY (
C_USERID VARCHAR(20) NOT NULL DEFAULT 'First'
    ,C_MAIL VARCHAR(50) NOT NULL DEFAULT 'masaki-m9420@softbank.ne.jp'
    ,C_INQUIRYCONTENT VARCHAR(300) NOT NULL DEFAULT 'ログインが出来ない'
    ,C_INQUIRYDATE TIMESTAMP NOT NULL DEFAULT '2022-02-02 00:00:00'
    ,C_PRIORITY VARCHAR(10) NOT NULL DEFAULT 'LOW'
);
```
・問い合わせ管理テーブルインデックス作成SQL
```
CREATE INDEX ON T_INQUIRY (C_USERID, C_MAIL, C_INQUIRYCONTENT, C_INQUIRYDATE, C_PRIORITY);
```
# :green_book: APIインタフェース定義
## :snowflake: 足し算API  
メソッド：GET  
足し算URI：/devcalc/addition   
リクエスト形式：APPLICATION/JSON  
###### :flags: リクエストボディ  
```
{
     "userid": "Testuser",
     "num1": 1,
     "num2": 2
}
```
###### :flags: レスポンスボディ  
・200 OK  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "S200",
     "message": "SUCCESS",
     "time": "2022-02-15T17:44:52.225",
     "result": "3",
     "apino": "1.AdditionCalcAPI"
}
```
・400 BAD REQUEST  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E400",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "1.AdditionCalcAPI"
}
```
・401 UNAUTHORIZED  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E401",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "1.AdditionCalcAPI"
}
```
・404 NOT_FOUND  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E401",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "1.AdditionCalcAPI"
}
```
・500 INTERNAL_SERVER_ERROR  
レスポンス形式：APPLICATION/JSON
```
{
     "status": "E500",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "1.AdditionCalcAPI"
}
```

## :snowflake: 掛け算API  
メソッド：GET  
足し算URI：/devcalc/multiplication  
リクエスト形式：APPLICATION/JSON  
###### :flags: リクエストボディ  
```
{
     "userid": "Testuser",
     "num1": 1,
     "num2": 2
}
```
###### :flags: レスポンスボディ  
・200 OK  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "S200",
     "message": "SUCCESS",
     "time": "2022-02-15T17:44:52.225",
     "result": "2",
     "apino": "2.MultiplicationCalcAPI"
}
```
・400 BAD_REQUEST  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E400",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "2.MultiplicationCalcAPI"
}
```
・401 UNAUTHORIZED  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E401",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "2.MultiplicationCalcAPI"
}
```
・404 UNAUTHORIZED  
レスポンス形式：APPLICATION/JSON
```
{
     "status": "E404",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "2.MultiplicationCalcAPI"
}
```
・500 INTERNAL_SERVER_ERROR  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E500",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "2.MultiplicationCalcAPI"
}
```

## :snowflake: 引き算API  
メソッド：GET  
足し算URI：/devcalc/subtraction  
リクエスト形式：APPLICATION/JSON  
###### :flags: リクエストボディ  
```
{
     "userid": "Testuser",
     "num1": 1,
     "num2": 2
}
```
###### :flags: レスポンスボディ  
・200 OK  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "S200",
     "message": "SUCCESS",
     "time": "2022-02-15T17:44:52.225",
     "result": "-1",
     "apino": "3.SubtractionCalcAPI"
}
```
・400 BAD_REQUEST  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E400",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "3.SubtractionCalcAPI"
}
```
・401 UNAUTHORIZED  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E401",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "3.SubtractionCalcAPI"
}
```
・404 NOT_FOUND  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E404",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "3.SubtractionCalcAPI"
}
```
・500 INTERNAL_SERVER_ERROR  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E500",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "3.SubtractionCalcAPI"
}
```

## :snowflake: 割り算API  
メソッド：GET  
足し算URI：/devcalc/division  
リクエスト形式：APPLICATION/JSON  
###### :flags: リクエストボディ  
```
{
     "userid": "Testuser",
     "num1": 1,
     "num2": 2
}
```
###### :flags: レスポンスボディ  
・200 OK  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "S200",
     "message": "SUCCESS",
     "time": "2022-02-15T17:44:52.225",
     "result": "0.5",
     "apino": "4.DivisionCalcAPI"
}
```
・400 BAD_REQUEST  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E400",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "4.DivisionCalcAPI"
}
```
・401 UNAUTHORIZED  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E401",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "4.DivisionCalcAPI"
}
```
・404 NOT_FOUND  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E404",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "4.DivisionCalcAPI"
}
```
・500 INTERNAL_SERVER_ERROR  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E500",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "4.DivisionCalcAPI"
}
```

## :snowflake: 新規ユーザー登録API  
メソッド：POST  
足し算URI：/devcalc/registuser  
リクエスト形式：APPLICATION/JSON  
###### :flags: リクエストボディ  
```
{
  "userid": "Testuser23",
  "password": "Test@Test1234",
  "phone": 08000000000,
  "profession": "サラリーマン",
  "mail": "mfukaya9420@gmail.com",
  "age": 24
}
```

###### :flags: レスポンスボディ  
・201 CREATED  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "S201",
     "message": "SUCCESS",
     "time": "2022-02-15T17:44:52.225",
     "result": "0.5",
     "apino": "5.UserRegistrationAPI"
}
```
・400 BAD_REQUEST  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E400",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "5.UserRegistrationAPI"
}
```
・409 CONFLICT  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E409",
     "message": "既にそのユーザーIDは利用されています。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "5.UserRegistrationAPI"
}
```
・500 INTERNAL_SERVER_ERROR  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E500",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "5.UserRegistrationAPI"
}
```

## :snowflake: ログインAPI  
メソッド：GET  
足し算URI：/devcalc/login  
リクエスト形式：APPLICATION/JSON  
###### :flags: リクエストボディ  
```
{
     "userid": "Testuser",
     "password": "Test@Test1234"
}
```

###### :flags: レスポンスボディ  
・200 OK  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "S201",
     "message": "SUCCESS",
     "time": "2022-02-15T17:44:52.225",
     "apino": "6.LoginAPI"
}
```
・400 BAD_REQUEST  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E400",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "6.LoginAPI"
}
```
・401 UNAUTHORIZED  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E401",
     "message": "ユーザーのログイン資格がありません。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "6.LoginAPI"
}
```
・404 NOT_FOUND  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E404",
     "message": "ユーザーID・パスワードが間違っています。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "6.LoginAPI"
}
```
・500 INTERNAL_SERVER_ERROR  
レスポンス形式：APPLICATION/JSON  
```
{
     "status": "E500",
     "message": "システムエラーが発生しました。管理者に問い合わせてください。",
     "time": "2022-02-15T17:44:52.225",
     "apino": "6.LoginAPI"
}
```
