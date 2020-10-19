package us.sep.biz.exam.service;

import us.sep.biz.exam.request.ExamRecordRequest;
import us.sep.exam.builder.ExamRecordBO;

import java.util.List;

public interface ExamRecordService {

    List<ExamRecordBO> findExamRecordData(ExamRecordRequest request , int pageNum ,int pageSize);

    ExamRecordBO deleteExamRecordByType(String examTypeId);

    ExamRecordBO deleteExamRecord(String examRecordId);

    ExamRecordBO modifyExamRecord(ExamRecordRequest request);

}
