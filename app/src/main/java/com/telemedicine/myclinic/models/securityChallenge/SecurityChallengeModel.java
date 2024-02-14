package com.telemedicine.myclinic.models.securityChallenge;

import com.google.gson.annotations.SerializedName;
import com.telemedicine.myclinic.models.BaseEntity;
import com.telemedicine.myclinic.models.MobileOpResult;

public class SecurityChallengeModel extends BaseEntity {

    @SerializedName("ChallengeEnabled")
    boolean ChallengeEnabled;

    @SerializedName("QuestionEn")
    String QuestionEn;

    @SerializedName("QuestionAr")
    String QuestionAr;

    @SerializedName("Answer")
    String Answer;

    public boolean isChallengeEnabled() {
        return ChallengeEnabled;
    }

    public void setChallengeEnabled(boolean challengeEnabled) {
        ChallengeEnabled = challengeEnabled;
    }

    public String getQuestionEn() {
        return QuestionEn;
    }

    public void setQuestionEn(String questionEn) {
        QuestionEn = questionEn;
    }

    public String getQuestionAr() {
        return QuestionAr;
    }

    public void setQuestionAr(String questionAr) {
        QuestionAr = questionAr;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }
}
