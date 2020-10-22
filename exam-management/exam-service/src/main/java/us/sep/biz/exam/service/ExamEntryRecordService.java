package us.sep.biz.exam.service;

import us.sep.exam.builder.ExamEntryRecordBO;

public interface ExamEntryRecordService {

    ExamEntryRecordBO getExamEntryRecordByExamEntry(String examEntryId);

    ExamEntryRecordBO getExamEntryRecordByUserId(String examEntryId);
}
