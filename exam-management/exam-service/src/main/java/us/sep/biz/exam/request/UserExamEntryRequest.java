package us.sep.biz.exam.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * @author kana-cr
 * @version  2020/10/20 15:13
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExamEntryRequest {


    /**
     * 考试报名信息id
     */
    @Valid
    private String examEntryId;


    /**
     * 用户报名考试id
     */
    private String userExamEntryId;

    /**
     * 报名用户id
     */
    @Valid
    private String userId;




}
