package com.godLife.project.service.impl.adminImpl;
import com.godLife.project.dto.categories.FaqCateDTO;
import com.godLife.project.dto.categories.QnaCateDTO;
import com.godLife.project.dto.categories.TopCateDTO;
import com.godLife.project.dto.contents.FaQDTO;
import com.godLife.project.dto.datas.IconDTO;
import com.godLife.project.dto.list.QnaListDTO;
import com.godLife.project.exception.FaqCategoryDeletePendingException;
import com.godLife.project.exception.QnaCategoryDeletePendingException;
import com.godLife.project.mapper.AdminMapper.CompSystemMapper;
import com.godLife.project.service.impl.redis.RedisService;
import com.godLife.project.service.interfaces.AdminInterface.CompSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CompSystemServiceImpl implements CompSystemService {
  private CompSystemMapper compSystemMapper;
  private RedisService redisService;

  public CompSystemServiceImpl(CompSystemMapper compSystemMapper, RedisService redisService) {
    this.compSystemMapper = compSystemMapper;
    this.redisService = redisService;
  }


  //                           FAQ 카테고리 관리 테이블
  // FAQ 카테고리 LIST 조회
  public List<FaqCateDTO> selectFaqCate() {
    return compSystemMapper.selectAllFaqCate();
  }

  // FAQ 카테고리 추가
  public int insertFaqCate(FaqCateDTO faqCateDTO) {
    int count = compSystemMapper.countByFaqName(faqCateDTO.getFaqCategoryName());
    if (count > 0) {
      throw new IllegalStateException("이미 존재하는 카테고리 이름입니다.");
    }
    return compSystemMapper.insertFaqCate(faqCateDTO);
  }

  // FAQ 카테고리 수정
  public int updateFaqCate(FaqCateDTO faqCateDTO) {
    int count = compSystemMapper.countByFaqName(faqCateDTO.getFaqCategoryName());
    if (count > 0) {
      throw new IllegalStateException("이미 존재하는 카테고리 이름입니다.");
    }
    return compSystemMapper.updateFaqCate(faqCateDTO);
  }

  @Transactional
  // FAQ 카테고리 삭제
  public int deleteFaqCate(int faqCategoryIdx) {
    String redisKey = "FaqCategoryDelete:" + faqCategoryIdx;

    boolean confirmFlag = redisService.checkExistsValue(redisKey);

    try{
      if (!confirmFlag){
        // 첫 번째 요청: 예외 발생 -> 프론트에서 사용자에게 삭제 여부 물어보게 함.
        int count = compSystemMapper.countFaqByCategory(faqCategoryIdx);
        if (count > 0) {
          // Redis에 삭제 대기 상태 저장 (TTL 10분)
          redisService.saveStringData(redisKey, "true", 'm',10);
          throw new IllegalStateException("해당 카테고리에 연결된 FAQ가 존재합니다. 삭제하시겠습니까 ?");
        }
      } else{
        // Redis에 확인값이 있으므로 FAQ 먼저 삭제
        compSystemMapper.deleteFaqByCate(faqCategoryIdx);
      }
      // 카테고리 삭제 시도
      int deleted = compSystemMapper.deleteFaqCate(faqCategoryIdx);

      // Redis 키 삭제 (정상 작동 완료시)
      redisService.deleteData(redisKey);

      return deleted;
    } catch (IllegalStateException e){
      // 연결된 FAQ가 존재하여 삭제 불가 + 리스트 반환 포함된 예외 던짐
      List<FaQDTO> faqList = compSystemMapper.faqListByCategory(faqCategoryIdx);
      throw new FaqCategoryDeletePendingException(e.getMessage(), faqList);
    }


  }


  //                           QNA 카테고리 관리 테이블
  // QNA 카테고리 LIST 조회
  public List<QnaCateDTO> selectQnaCate() {
    return compSystemMapper.selectAllQnaCate();
  }

  // QNA 카테고리 추가
  public int insertQnaCate(QnaCateDTO qnaCateDTO) {
    return compSystemMapper.insertQnaCate(qnaCateDTO);
  }


  // QNA 카테고리 수정
  public int updateQnaCate(QnaCateDTO qnaCateDTO) {
    int count = compSystemMapper.countByQnaName(qnaCateDTO.getCategoryName());
    if (count > 0) {
      throw new IllegalStateException("이미 존재하는 카테고리 이름입니다.");
    }
    return compSystemMapper.updateQnaCate(qnaCateDTO);
  }

  // QNA 카테고리 삭제
  @Transactional
  public int deleteQnaCate(int categoryIdx) {
    String redisKey = "QnaCategoryDelete:" + categoryIdx;
    boolean confirmed = redisService.checkExistsValue(redisKey);

    try {
      if (!confirmed) {
        int qnaCount = compSystemMapper.countQnaByCategory(categoryIdx);
        int childCateCount = compSystemMapper.countQnaChildCategories(categoryIdx);

        if (qnaCount > 0 || childCateCount > 0) {
          // redis에 삭제 플래그 저장 (10분 유효)
          redisService.saveStringData(redisKey, "true", 'm', 10);
          List<QnaListDTO> qnaList = compSystemMapper.qnaListByCategory(categoryIdx);
          throw new QnaCategoryDeletePendingException("하위 항목이 존재합니다. 삭제하시겠습니까?", qnaList);
        }

        return compSystemMapper.deleteQnaCate(categoryIdx); // 자식 없을 때 바로 삭제
      }

      // 2차 요청: 강제 삭제
      compSystemMapper.deleteQnaByCate(categoryIdx); // 카테고리 연결된 QNA 삭제
      compSystemMapper.deleteChildQnaCategories(categoryIdx); // 자식 카테고리 삭제
      int deleted = compSystemMapper.deleteQnaCate(categoryIdx); // 부모 카테고리 삭제

      redisService.deleteData(redisKey); // Redis 플래그 제거
      return deleted;

    } catch (Exception e) {
      throw new RuntimeException("QNA 카테고리 삭제 중 오류 발생", e);
    }
  }



  //                           TopMenu 카테고리 관리 테이블

  // 추가
  @Override
  public int insertTopMenu(TopCateDTO topCateDTO) {
    int count = compSystemMapper.countByTopMenuName(topCateDTO.getTopName());
    if (count > 0) {
      throw new IllegalStateException("이미 존재하는 카테고리 이름입니다.");
    }
    return compSystemMapper.insertTopMenu(topCateDTO);
  }

  // 수정
  @Override
  public int updateTopMenu(TopCateDTO topCateDTO) {
    int count = compSystemMapper.countByTopMenuName(topCateDTO.getTopName());
    if (count > 0) {
      throw new IllegalStateException("이미 존재하는 카테고리 이름입니다.");
    }
    return compSystemMapper.updateTopMenu(topCateDTO);
  }
  // 삭제
  @Override
  public int deleteTopMenu(int topIdx) {
    return compSystemMapper.deleteTopMenu(topIdx);
  }
  // TopMenu 재배열
  @Override
  @Transactional
  public void updateOrderTopMenu(List<TopCateDTO> orderedList) {
    for (TopCateDTO topCateDTO : orderedList) {
      /// [희만 - 20250619 - 탑메뉴 데이터 구조 변경으로 인해 임시로 주석 처리함]
      // compSystemMapper.updateOrderTopMenu(topCateDTO.getTopIdx(), topCateDTO.getOrdIdx());
    }
  }

  //                           ICON 관리 테이블

  // ICON 추가
  public int insertIcon(IconDTO iconDTO) {
    int count = compSystemMapper.countByIconName(iconDTO.getIconKey());
    if (count > 0) {
      throw new IllegalStateException("이미 존재하는 ICON 이름입니다.");
    }
    return compSystemMapper.insertIcon(iconDTO);
  }

  // ICON 수정
  public int updateIcon(IconDTO iconDTO) {
    int count = compSystemMapper.countByIconKeyExcludingSelf(
            iconDTO.getIconKey(),             // 바꾸려는 새 키
            iconDTO.getOriginalIconKey()      // 기존의 키 (자기 자신)
    );
    if (count > 0) {
      throw new IllegalStateException("이미 존재하는 ICON 이름입니다.");
    }
    return compSystemMapper.updateIcon(iconDTO);
  }

  // ICON 삭제
  public int deleteIcon(String iconKey) {
    return compSystemMapper.deleteIcon(iconKey);
  }

  /*
    // 조회
  public List<TopCateDTO> selectTopMenu() {
    return compSystemMapper.selectTopMenu();
  }

  // ICON   조회
  public List<IconDTO> selectIcon() {
    return compSystemMapper.selectIcon();
  }
   */
}
