# SocialMediaExample
Social Media Example with Kotlin

- Kotlin Programalama Dili kullanılarak yapılmıştır.
- Bir sosyal medya uygulamasından referans alınmıştır.
- Firebase Authtication, Firebase Firestore veritabanı ve Firebase Storage kullanılmıştır.
- Uygulamanın içerisinde;
-  * Kullanıcılar sisteme e-mail adresleri ile kayıt olabilir ve giriş yapablirler.
   * Yeni bir gönderi oluşturabilir ve diğer kullanıcıların görmesini sağlayabilirler.
   * Kullanıcılar gönderileri ana sayfa üzeirnde tarihe göre sıralanarak ve kim adına hangi açıklama ile paylaşıldığını görebilirler.
- Uygulamada Image için uygun permission yazılarak her android seviyesindeki cihazlar için kullanılabilir hale getirilmiştir.
- Görseller proje içerisine kayıt edilmez. Firebase Storage üzerine kayıt edilir. Görsellerin link adresleri Firestore'da bulunan collectionlar içerisinde gönderi bilgileri ile tutulur.
- Picasso ile kayıt edilen görsellerin linkleri ile görseller indirilir ve gönderi içeriği ile gösterilir.
