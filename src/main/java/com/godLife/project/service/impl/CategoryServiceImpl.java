package com.godLife.project.service.impl;

import com.godLife.project.dto.categories.*;
import com.godLife.project.dto.datas.FireDTO;
import com.godLife.project.dto.datas.IconDTO;
import com.godLife.project.dto.datas.UserLevelDTO;
import com.godLife.project.dto.response.qna.QnaChild;
import com.godLife.project.dto.response.qna.QnaParent;
import com.godLife.project.dto.response.top.TopMenu;
import com.godLife.project.exception.CustomException;
import com.godLife.project.mapper.CategoryMapper;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final RedisService redisService;


    // 탑메뉴 카테고리 조회 (데이터 가공)
    @Override
    public List<TopMenu> getProcessedAllTopCategories() {
        try {
            List<TopCateDTO> origin = getOriginAllTopCategories();

            if (origin == null || origin.isEmpty()) {
                throw new CustomException("탑메뉴 카테고리가 조회되지 않습니다.", HttpStatus.NOT_FOUND);
            }

            // 대분류 먼저 모으기
            Map<Integer, TopMenu> parentMap = new HashMap<>();

            List<TopCateDTO> parentList = origin.stream()
                .filter(dto -> dto.getCategoryLevel() == 1)
                .sorted(Comparator.comparingInt(TopCateDTO::getOrdCol)) // 대분류 정렬
                .toList();

            for (TopCateDTO dto : parentList) {
                TopMenu parent = new TopMenu();
                parent.setTopIdx(dto.getTopIdx());
                parent.setName(dto.getTopName());
                parent.setAddr(dto.getTopAddr());
                parent.setOrdCol(dto.getOrdCol());
                parent.setChildren(new ArrayList<>());

                parentMap.put(dto.getTopIdx(), parent);
            }

            // 소분류 정렬 후 부모에 추가
            List<TopCateDTO> childList = origin.stream()
                .filter(dto -> dto.getCategoryLevel() != 1)
                .sorted(Comparator.comparingInt(TopCateDTO::getOrdCol)) // 소분류 정렬
                .toList();

            for (TopCateDTO dto : childList) {
                TopMenu child = new TopMenu();
                child.setTopIdx(dto.getTopIdx());
                child.setName(dto.getTopName());
                child.setAddr(dto.getTopAddr());
                child.setOrdCol(dto.getOrdCol());

                TopMenu parent = parentMap.get(dto.getParentIdx());
                if (parent != null) {
                    parent.getChildren().add(child);
                } else {
                    log.warn("소분류 [{}]의 부모 [{}]가 존재하지 않습니다.", dto.getTopIdx(), dto.getParentIdx());
                }
            }

            // 최종 정렬된 대분류 리스트 반환 (이미 ordCol 기준 정렬됨)
            return new ArrayList<>(parentList.stream()
                .map(p -> parentMap.get(p.getTopIdx()))
                .toList());

        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("알 수 없는 오류가 발생했습니다.", e);
            throw e;
        }
    }



    // 탑메뉴 카테고리 조회 (데이터 미가공)
    @Override
    public List<TopCateDTO> getOriginAllTopCategories() {
        return categoryMapper.getAllTopCategories();
    }

    // 직업 카테고리 조회
    @Override
    public List<JobCateDTO> getAllJobCategories() {
        List<JobCateDTO> data = redisService.getListData("category::job", JobCateDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getAllJOBCategories();
        }
        return data;
    }
    // 관심사 카테고리 조회
    @Override
    public  List<TargetCateDTO> getAllTargetCategories() {
        List<TargetCateDTO> data = redisService.getListData("category::target", TargetCateDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getAllTargetCategories();
        }
        return data;
    }
    // 챌린지 카테고리 조회
    @Override
    public List<ChallengeCateDTO> getAllChallCategories() {
        List<ChallengeCateDTO> data = redisService.getListData("category::chall", ChallengeCateDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getAllChallCategories();
        }
        return data;
    }
    // 숏컷 카테고리
    @Override
    public List<ShortCutCateDTO> getAllShortCategories() { return categoryMapper.getAllShortCategories(); }
    // 권한 카테고리
    @Override
    public List<AuthorityCateDTO> getAllAuthorityCategories() { return categoryMapper.getAllAuthorityCategories(); }
    // 아이콘 정보 조회
    @Override
    public List<IconDTO> getUserIconInfos() {
        List<IconDTO> data = redisService.getListData("category::userIcon", IconDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getUserIconInfos();
        }
        return data;
    }
    // 아이콘 정보 조회 ( 관리자 용 )
    @Override
    public List<IconDTO> getAllIconInfos() {
        List<IconDTO> data = redisService.getListData("category::adminIcon", IconDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getAllIconInfos();
        }
        return data;
    }

    // 불꽃 정보 조회
    @Override
    public List<FireDTO> getAllFireInfos() {
        List<FireDTO> data = redisService.getListData("category::fire", FireDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getAllFireInfos();
        }
        return data;
    }
    // 유저 레벨 정보 조회
    @Override
    public List<UserLevelDTO> getAllUserLevelInfos() {
        List<UserLevelDTO> data = redisService.getListData("category::userLv", UserLevelDTO.class);
        if (data == null || data.isEmpty()) {
            return categoryMapper.getAllUserLevelInfos();
        }
        return data;
    }

    // qna 카테고리
    @Override
    public List<QnaParent> getAllQnaCategories() {
        try {
            List<QnaCateDTO> origin = categoryMapper.getAllQnaCategories(false);

            if (origin == null || origin.isEmpty()) {
                throw new CustomException("qna 카테고리가 조회되지 않습니다.", HttpStatus.NOT_FOUND);
            }

            Map<Integer, QnaParent> parentMap = new HashMap<>();

            for (QnaCateDTO dto : origin) {
                int categoryLevel = dto.getCategoryLevel();

                // 대분류 처리
                if (categoryLevel == 1) {
                    QnaParent parent = new QnaParent();
                    parent.setParentIdx(dto.getCategoryIdx());
                    parent.setParentName(dto.getCategoryName());
                    parent.setChildCategory(new ArrayList<>());
                    parentMap.put(dto.getCategoryIdx(), parent);
                }
                // 소분류 처리
                else {
                    QnaChild child = new QnaChild();
                    child.setCategoryIdx(dto.getCategoryIdx());
                    child.setCategoryName(dto.getCategoryName());

                    // 상위 카테고리가 존재할 경우 소분류 추가
                    QnaParent parent = parentMap.get(dto.getParentIdx());
                    if (parent != null) {
                        parent.getChildCategory().add(child);
                    }
                }
            }

            return new ArrayList<>(parentMap.values());

        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("알 수 없는 오류가 발생 했습니다.", e);
            throw e;
        }

    }
}
