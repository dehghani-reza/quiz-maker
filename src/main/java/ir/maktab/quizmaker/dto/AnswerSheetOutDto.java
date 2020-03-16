package ir.maktab.quizmaker.dto;

import lombok.Value;

import java.util.Date;

@Value
public class AnswerSheetOutDto {

    private Long sheetId;

    private Date createdDate;

    private Date fillDate;

    private String isOnTime;

    private String isCalculated;

    private double finalScore;

    private String examinerName;


}
