[![Stories in Ready](https://badge.waffle.io/dictav/androidsample-kotlin.png?label=ready&title=Ready)](https://waffle.io/dictav/androidsample-kotlin)
AndroidSample-kotlin
====================

Import form
https://github.com/JetBrains/kotlin-examples

Reference
http://blog.nkzn.info/entry/2014/02/11/191841

Thank you, @nkzn !

これなに？
=========
AndroidStudio + Kotlinで Androidアプリを作る学習サンプルです。
次のような機能を実装します。

* Redisへアクセス
* MessagePackのpack/unpack
* AWS S3 アップロード
* DNS resolv
* Push notification
* Facebook認証
* 消耗型の課金システムの実装
* Socket + SSL プログラミング

# じゃあ、具体的に何作る？
**Facebook で「おれイケメンだろ？」ランキングアプリを作成する**

（ぼこられランキングという名前にしてタップすると顔をぼこぼこ殴れるアプリで、ぼこられ具合でランキングにしようかとも思ったけど、画像処理入れるのめんどいのでパス）

1. Facebookでユーザ登録
2. 今日一のドヤ顔を撮って投稿
3. みんなでドヤ顔 0 - 5 点付ける
4. 今日一のドヤ顔決定
