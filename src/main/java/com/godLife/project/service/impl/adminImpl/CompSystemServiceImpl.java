package com.godLife.project.service.impl.AdminImpl;

import com.godLife.project.dto.categories.FaqCateDTO;
import com.godLife.project.dto.categories.QnaCateDTO;
import com.godLife.project.dto.categories.TopCateDTO;
import com.godLife.project.dto.datas.IconDTO;
import com.godLife.project.mapper.AdminMapper.CompSystemMapper;
import com.godLife.project.service.interfaces.AdminInterface.CompSystemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompSystemServiceImpl implements CompSystemService {
  private CompSystemMapper compSystemMapper;

  public CompSystemServiceImpl(CompSystemMapper compSystemMapper) {
    this.compSystemMapper = compSystemMapper;
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

  // FAQ 카테고리 삭제
  public int deleteFaqCate(int faqCategoryIdx) {
    int count = compSystemMapper.countFaqByCategory(faqCategoryIdx);

    if (count > 0) {
      throw new IllegalStateException("해당 카테고리는 현재 FAQ에서 사용 중입니다.");
    }
    return compSystemMapper.deleteFaqCate(faqCategoryIdx);
  }


  //                           QNA 카테고리 관리 테이블
  // QNA 카테고리 LIST 조회
  public List<QnaCateDTO> selectQnaCate() {
    return compSystemMapper.selectAllQnaCate();
  }

  // QNA 카테고리 추가
  public int insertQnaCate(QnaCateDTO qnaCateDTO) {
    int count = compSystemMapper.countByQnaName(qnaCateDTO.getQnaCategoryName());
    if (count > 0) {
      throw new IllegalStateException("이미 존재하는 카테고리 이름입니다.");
    }
    return compSystemMapper.insertQnaCate(qnaCateDTO);
  }

  // QNA 카테고리 수정
  public int updateQnaCate(QnaCateDTO qnaCateDTO) {
    int count = compSystemMapper.countByQnaName(qnaCateDTO.getQnaCategoryName());
    if (count > 0) {
      throw new IllegalStateException("이미 존재하는 카테고리 이름입니다.");
    }
    return compSystemMapper.updateQnaCate(qnaCateDTO);
  }

  // QNA 카테고리 삭제
  public int deleteQnaCate(int qnaCategoryIdx) {
    int count = compSystemMapper.countQnaByCategory(qnaCategoryIdx);

    if (count > 0) {
      throw new IllegalStateException("해당 카테고리는 현재 QNA에서 사용 중입니다.");
    }
    return compSystemMapper.deleteQnaCate(qnaCategoryIdx);
  }

  //                           TopMenu 카테고리 관리 테이블
  // 조회
  @Override
  public List<TopCateDTO> selectTopMenu() {
    return compSystemMapper.selectTopMenu();
  }

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
       compSystemMapper.updateOrderTopMenu(topCateDTO.getTopIdx(), topCateDTO.getOrdIdx());
    }
  }

  //                           ICON 관리 테이블
  // ICON   조회
  public List<IconDTO> selectIcon() {
    return compSystemMapper.selectIcon();
  }

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
}
