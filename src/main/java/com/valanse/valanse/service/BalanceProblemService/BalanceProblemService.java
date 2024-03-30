package com.valanse.valanse.service.BalanceProblemService;

import com.valanse.valanse.entity.BalanceProblem;
import com.valanse.valanse.entity.ClientResponse;

import java.util.List;

public interface BalanceProblemService {
    BalanceProblem getBalanceProblemById(Integer problemId); // 문제를 데이터베이스에서 가져오는 메서드
    List<BalanceProblem> getAllBalanceProblems(); // 모든 문제를 데이터베이스에서 가져오는 메서드
    BalanceProblem provideBalanceProblemToClient(); // 문제를 클라이언트에 제공하는 메서드
    void saveClientResponse(ClientResponse clientResponse); // 클라이언트의 답변을 데이터베이스에 저장하는 메서드
}