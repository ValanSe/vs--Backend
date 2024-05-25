package com.valanse.valanse.service.NoticeService;

import com.valanse.valanse.dto.NoticeDto;
import com.valanse.valanse.dto.NoticeRegisterDto;
import jakarta.servlet.http.HttpServletRequest;

public interface NoticeService {
    void registerNotice(HttpServletRequest httpServletRequest, NoticeRegisterDto noticeRegisterDto);

    NoticeDto getNotice(Integer noticeId);

    void updateNotice(HttpServletRequest httpServletRequest, Integer noticeId, NoticeRegisterDto noticeRegisterDto);

    void deleteNotice(HttpServletRequest httpServletRequest, Integer noticeId);
}
