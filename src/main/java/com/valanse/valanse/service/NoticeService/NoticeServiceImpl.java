package com.valanse.valanse.service.NoticeService;

import com.valanse.valanse.dto.NoticeDto;
import com.valanse.valanse.dto.NoticeRegisterDto;
import com.valanse.valanse.entity.Notice;
import com.valanse.valanse.repository.jpa.NoticeRepository;
import com.valanse.valanse.security.util.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void registerNotice(HttpServletRequest httpServletRequest, NoticeRegisterDto noticeRegisterDto) {
        try {
            int userIdx = jwtUtil.getUserIdxFromRequest(httpServletRequest);

            String token = jwtUtil.getAccessTokenFromRequest(httpServletRequest);
            String userRole = jwtUtil.getUserRole(token);

            // 'role' 이 'admin' 이 아니면 예외 처리
            if (!"admin".equals(userRole)) {
                throw new AccessDeniedException("User does not have permission to register notices.");
            }

            Notice notice = Notice.builder()
                    .title(noticeRegisterDto.getTitle())
                    .content(noticeRegisterDto.getContent())
                    .authorId(userIdx)
                    .createdAt(LocalDateTime.now())
                    .views(0)
                    .build();

            noticeRepository.save(notice);
        } catch (AccessDeniedException e) {
            log.error("User does not have permission to register notices.", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public NoticeDto getNotice(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);

        return NoticeDto.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .authorId(notice.getAuthorId())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .views(notice.getViews())
                .build();
    }

    @Override
    @Transactional
    public void updateNotice(HttpServletRequest httpServletRequest, Integer noticeId, NoticeRegisterDto noticeRegisterDto) {
        try {
            String token = jwtUtil.getAccessTokenFromRequest(httpServletRequest);
            String userRole = jwtUtil.getUserRole(token);

            Notice existingNotice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);

            if (!"admin".equals(userRole)) {
                throw new AccessDeniedException("User does not have permission to update notices.");
            }

            existingNotice = Notice.builder()
                    .noticeId(existingNotice.getNoticeId())
                    .title(noticeRegisterDto.getTitle() != null ? noticeRegisterDto.getTitle() : existingNotice.getTitle())
                    .content(noticeRegisterDto.getContent() != null ? noticeRegisterDto.getContent() : existingNotice.getContent())
                    .authorId(existingNotice.getAuthorId())
                    .createdAt(existingNotice.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .views(existingNotice.getViews())
                    .build();

            noticeRepository.save(existingNotice);
        } catch (AccessDeniedException e) {
            log.error("User does not have permission to update notices.", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteNotice(HttpServletRequest httpServletRequest, Integer noticeId) {
        try {
            String token = jwtUtil.getAccessTokenFromRequest(httpServletRequest);
            String userRole = jwtUtil.getUserRole(token);

            Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);

            if (!"admin".equals(userRole)) {
                throw new AccessDeniedException("User does not have permission to delete notices.");
            }

            noticeRepository.delete(notice);
        } catch (AccessDeniedException e) {
            log.error("User does not have permission to delete notices.", e);
            throw e;
        }
    }

    @Override
    public void increaseView(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);

        noticeRepository.increaseView(notice.getNoticeId());
    }
}
