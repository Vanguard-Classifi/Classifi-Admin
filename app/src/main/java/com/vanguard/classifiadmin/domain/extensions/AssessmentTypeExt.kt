package com.vanguard.classifiadmin.domain.extensions

import com.vanguard.classifiadmin.ui.screens.assessments.AssessmentType

fun String.toAssessmentType(): AssessmentType {
    return when(this){
        AssessmentType.Quiz.name -> AssessmentType.Quiz
        AssessmentType.HomeWork.name -> AssessmentType.HomeWork
        AssessmentType.Exam.name -> AssessmentType.Exam
        else -> AssessmentType.HomeWork
    }
}