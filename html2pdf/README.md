# html2pdf

reference:

http://hmkcode.com/itext-html-to-pdf-using-java/
http://swordshadow.iteye.com/blog/1983935


use iText and JTidy implement simple html to pdf and support chinese.


## useage

- change your font style and path
  - fontResolver.addFont("/xxxx/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED)

- mvn compile

- mvn package

- java -jar ./target/itext-java-html-pdf-1.0-SNAPSHOT-fat.jar ./index.html ./test.pdf
