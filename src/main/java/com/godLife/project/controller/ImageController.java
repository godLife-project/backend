package com.godLife.project.controller;

import com.godLife.project.exception.CustomException;
import com.godLife.project.handler.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/upload/auth")
public class ImageController {

  // qna 이미지 경로
  @Value("${app.upload.root-dir}")
  private String uploadRootDir;

  private final GlobalExceptionHandler handler;

  @PostMapping("/image-upload/{category}")
  public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image,
                                       @PathVariable String category) {
    try {
      if (image == null || image.isEmpty()) {
        log.warn("업로드 요청이 왔지만 이미지 파일이 비어 있음");
        throw new CustomException("이미지 파일이 비어 있습니다.", HttpStatus.BAD_REQUEST);
      }

      String contentType = image.getContentType();
      if (contentType == null || !contentType.startsWith("image/")) {
        log.warn("업로드 요청이 왔지만 허용 하지 않은 확장자 입니다.");
        throw new CustomException("허용된 확장자 파일이 아닙니다.", HttpStatus.BAD_REQUEST);
      }


      String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
      String subfolder = getUploadSubfolder(category);
      Path savePath = Paths.get(uploadRootDir, subfolder, fileName);
      Files.createDirectories(savePath.getParent()); // 디렉토리 없으면 생성
      Files.write(savePath, image.getBytes());

      // 클라이언트에서 접근 가능한 URL 반환
      String imageUrl = "/uploads/" + subfolder + "/" + fileName;

      log.info("ImageController - uploadImage :: 파일 업로드 성공 -- [파일 경로: /uploads/{}] -- [파일 이름: {}]", category, fileName);
      return ResponseEntity.ok(Map.of("url", imageUrl));
    } catch (IOException e) {
      log.error("ImageController - uploadImage :: 파일 업로드 실패", e);
      throw new CustomException("이미지 저장 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // 카테고리 분류 함수
  private String getUploadSubfolder(String category) {
    return switch (category.toLowerCase()) {
      case "qna" -> "qna";
      case "plan" -> "plan";
      case "notice" -> "notice";
      default -> "etc";
    };
  }
}
