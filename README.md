# :green_book: Calculator_API_Module :green_book:  
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

## :snowflake: データベース構成
当リポジトリのWikiに公開しております。  
そちらをご参照ください。  

# :green_book: APIインタフェース定義
当リポジトリのWiKiに公開しております。  
そちらをご参照ください。  
