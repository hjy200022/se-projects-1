package us.sep.biz.exam.service;

import us.sep.biz.exam.request.ExamLocationRequest;
import us.sep.exam.builder.ExamLocationBO;

import java.util.List;


public interface ExamLocationService {


    List<ExamLocationBO> findExamLocation(ExamLocationRequest request, int pageNum, int pageSize);

    List<ExamLocationBO> findByExamDetailId(ExamLocationRequest request);

    ExamLocationBO findByExamLocationId(ExamLocationRequest request);

    List<ExamLocationBO> findByUserId(ExamLocationRequest request);

    ExamLocationBO createExamLocation(ExamLocationRequest request);

    ExamLocationBO modifyExamLocation(ExamLocationRequest request);

    ExamLocationBO deleteByExamLocationId(String examLocationId);

    ExamLocationBO deleteByExamDetailId(String examDetailId);

    ExamLocationBO deleteByUserId(String userId);
}
