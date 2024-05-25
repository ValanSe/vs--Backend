package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Notice n SET n.views = n.views + 1 WHERE n.noticeId = :noticeId")
    void increaseView(@Param("noticeId") Integer noticeId);
}
