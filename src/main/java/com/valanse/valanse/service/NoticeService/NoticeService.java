package com.valanse.valanse.service.NoticeService;

import com.valanse.valanse.dto.NoticeDto;
import com.valanse.valanse.dto.NoticeRegisterDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface NoticeService {
    void registerNotice(HttpServletRequest httpServletRequest, NoticeRegisterDto noticeRegisterDto);

    NoticeDto getNotice(Integer noticeId);

    List<NoticeDto> getAllNotice();

    void updateNotice(HttpServletRequest httpServletRequest, Integer noticeId, NoticeRegisterDto noticeRegisterDto);

    void deleteNotice(HttpServletRequest httpServletRequest, Integer noticeId);

    void increaseView(Integer noticeId);
}
