package us.sep.biz.exam.service;

import us.sep.exam.builder.UserExamEntryRecordBO;

public interface UserExamEntryRecordService {


    UserExamEntryRecordBO getUserExamEntryRecordByExamEntry(String examEntryId);

    UserExamEntryRecordBO getUserExamEntryRecordByUserId(String examEntryId);

    UserExamEntryRecordBO getUserExamEntryRecordByIfAttend(String examEntryId);


}
