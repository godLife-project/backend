package com.godLife.project.controller.AdminController;

import com.godLife.project.dto.categories.AuthorityCateDTO;
import com.godLife.project.dto.list.customDTOs.AdminListDTO;
import com.godLife.project.dto.list.customDTOs.AdminUserDTO;
import com.godLife.project.handler.GlobalExceptionHandler;
import com.godLife.project.service.interfaces.AdminInterface.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;


    private final GlobalExceptionHandler handler;

    // 유저 정보 리스트 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsersPaged(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            List<AdminUserDTO> users = adminUserService.getPagedUserList(page, size);
            int total = adminUserService.countAllActiveUsers();

            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(handler.createResponse(204, "조회된 유저가 없습니다."));
            }

            Map<String, Object> result = new HashMap<>();
            result.put("users", users);
            result.put("page", page);
            result.put("size", size);
            result.put("count", users.size());
            result.put("total", total);
            result.put("totalPages", (int) Math.ceil((double) total / size));

            return ResponseEntity.ok(handler.createResponseWithData(200, "유저 리스트 조회 성공", result));

        } catch (Exception e) {
            log.error("유저 리스트 페이징 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(handler.createResponse(500, "서버 오류로 유저 리스트 조회에 실패했습니다."));
        }
    }


    // 유저 정지 로직
    @PatchMapping("/ban/{userIdx}")
    public ResponseEntity<Map<String, Object>> banUser(@PathVariable int userIdx) {
        try {
            int result = adminUserService.banUser(userIdx);

            if (result > 0) {
                return ResponseEntity.ok(handler.createResponse(200, "해당 유저가 정상적으로 추방되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(handler.createResponse(404, "존재하지 않거나 이미 추방된 유저입니다."));
            }

        } catch (Exception e) {
            log.error("회원 추방 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(handler.createResponse(500, "서버 오류로 회원 추방에 실패했습니다."));
        }
    }


    // 권한 목록 조회
    @GetMapping("/authority/list")
    public ResponseEntity<Map<String, Object>> getAuthorityList() {
        try {
            List<AuthorityCateDTO> authorityList = adminUserService.getAuthorityList();

            if (authorityList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(handler.createResponse(404, "등록된 권한이 없습니다."));
            }

            Map<String, Object> response = handler.createResponse(200, "권한 목록 조회 성공");
            response.put("authorityList", authorityList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("권한 목록 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(handler.createResponse(500, "서버 오류로 권한 목록을 불러오지 못했습니다."));
        }
    }

    // 관리자 명단 조회
    @GetMapping("/authority/adminList")
    public ResponseEntity<Map<String, Object>> AdminList(){
        try {
            List<AdminListDTO> userList = adminUserService.adminList();

            if (userList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(handler.createResponse(404, "관리자 권한을 가진 유저가 없습니다."));
            }

            Map<String, Object> response = handler.createResponse(200, "권한별 유저 조회 성공");
            response.put("userList", userList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("관리자 권한 유저 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(handler.createResponse(500, "서버 오류로 관리자 목록을 불러오지 못했습니다."));
        }
    }



    // 권한 별 유저 조회
    @GetMapping("/authority/{authorityIdx}")
    public ResponseEntity<Map<String, Object>> getUsersByAuthority(@PathVariable int authorityIdx) {
        try {
            List<AdminUserDTO> userList = adminUserService.getUsersByAuthority(authorityIdx);

            if (userList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(handler.createResponse(404, "해당 권한을 가진 유저가 없습니다."));
            }

            Map<String, Object> response = handler.createResponse(200, "권한별 유저 조회 성공");
            response.put("userList", userList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("권한별 유저 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(handler.createResponse(500, "서버 오류로 유저 목록을 불러오지 못했습니다."));
        }
    }


    // 유저 권한 변경
    @PatchMapping("/authority/updateAuth/{userIdx}")
    public ResponseEntity<Map<String, Object>> changeUserAuthority(
            @PathVariable int userIdx,
            @RequestBody Map<String, Integer> body) {
        try {
            int newAuthorityIdx = body.get("authorityIdx");

            int result = adminUserService.updateUserAuthority(userIdx, newAuthorityIdx);

            if (result > 0) {
                return ResponseEntity.ok(handler.createResponse(200, "유저 권한이 성공적으로 변경되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(handler.createResponse(404, "존재하지 않는 유저입니다."));
            }

        } catch (Exception e) {
            log.error("유저 권한 변경 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(handler.createResponse(500, "서버 오류로 권한 변경에 실패했습니다."));
        }
    }

}
