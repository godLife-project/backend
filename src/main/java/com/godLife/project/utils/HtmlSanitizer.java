package com.godLife.project.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class HtmlSanitizer {

  /**
   * HTML에서 위험한 태그(script, iframe 등) 제거하고 허용된 태그만 남김
   */
  public static String sanitize(String dirtyHtml) {
    return Jsoup.clean(dirtyHtml, Safelist.relaxed()
        .addTags("img") // 이미지 태그 추가 허용
        .addAttributes("img", "src", "alt", "width", "height")
        .addProtocols("img", "src", "http", "https", "data") // base64도 허용
    );
  }

}
