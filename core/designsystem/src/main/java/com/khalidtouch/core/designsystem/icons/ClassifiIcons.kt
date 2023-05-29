package com.khalidtouch.core.designsystem.icons

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector
import com.khalidtouch.core.designsystem.R

object ClassifiIcons {
    val Abort = R.drawable.icon_abort
    val About = R.drawable.icon_about
    val Add = R.drawable.icon_add
    val Admin = R.drawable.icon_admin
    val Address = R.drawable.icon_address
    val Album = R.drawable.icon_album
    val ArrowDown = R.drawable.icon_arrow_down
    val ArrowUp = R.drawable.icon_arrow_up
    val Assessment = R.drawable.icon_assessment
    val AssessmentOutline = R.drawable.icon_assessment_outline
    val Attach = R.drawable.icon_attach
    val Back = R.drawable.icon_back
    val Book = R.drawable.icon_book
    val Brand = R.drawable.icon_brand
    val BrandingBig = R.drawable.icon_branding_big
    val Calendar = R.drawable.icon_calendar
    val Camera = R.drawable.icon_camera
    val Cap = R.drawable.icon_cap
    val City = R.drawable.icon_city
    val Classroom = R.drawable.icon_classroom
    val Close = R.drawable.icon_close
    val CommentOutline = R.drawable.icon_comment_outline
    val CommentSolid = R.drawable.icon_comment_solid
    val Compose = R.drawable.icon_compose
    val Copy = R.drawable.icon_copy
    val Copyright = R.drawable.icon_copyright
    val Country = R.drawable.icon_country
    val Customize = R.drawable.icon_customize
    val Dark = R.drawable.icon_dark
    val Delete = R.drawable.icon_delete
    val Discussion = R.drawable.icon_discussion
    val Doc = R.drawable.icon_doc
    val Dob = R.drawable.icon_dob
    val DocxFile = R.drawable.icon_docx_file
    val DoubleArrowDown = R.drawable.icon_double_arrow_down
    val DoubleArrowUp = R.drawable.icon_double_arrow_up
    val Dropdown = R.drawable.icon_dropdown
    val DropdownClose = R.drawable.icon_dropdown_close
    val Edit = R.drawable.icon_edit
    val EditSolid = R.drawable.icon_edit_solid
    val Enroll = R.drawable.icon_enroll
    val Error = R.drawable.icon_error
    val Exam = R.drawable.icon_exam
    val Facebook = R.drawable.icon_facebook
    val Feeds = R.drawable.icon_feeds
    val FeedsSolid = R.drawable.icon_feeds_solid
    val FlashOff = R.drawable.icon_flash_off
    val FlashOn = R.drawable.icon_flash_on
    val FlipCamera = R.drawable.icon_flip_camera
    val Homework = R.drawable.icon_homework
    val Image = R.drawable.icon_image
    val Import = R.drawable.icon_import
    val Info = R.drawable.icon_info
    val JpgFile = R.drawable.icon_jpg_file
    val LikeOutline = R.drawable.icon_like_outline
    val LikeSolid = R.drawable.icon_like_solid
    val Light = R.drawable.icon_light
    val Lock = R.drawable.icon_lock
    val Mark = R.drawable.icon_mark
    val OptionsHorizontal = R.drawable.icon_options_horizontal
    val OptionsVertical = R.drawable.icon_options_vertical
    val Parent = R.drawable.icon_parent
    val Pause = R.drawable.icon_pause
    val PdfFile = R.drawable.icon_pdf_file
    val Personal = R.drawable.icon_personal
    val Phone = R.drawable.icon_phone
    val PngFile = R.drawable.icon_png_file
    val Play = R.drawable.icon_play
    val Portrait = R.drawable.icon_portrait
    val Postal = R.drawable.icon_postal
    val PptFile = R.drawable.icon_ppt_file
    val Preference = R.drawable.icon_preference
    val Preview = R.drawable.icon_preview
    val Profile = R.drawable.icon_profile
    val Success = R.drawable.icon_success
    val Question = R.drawable.icon_question
    val Quiz = R.drawable.icon_quiz
    val Repeat = R.drawable.icon_repeat
    val Replace = R.drawable.icon_replace
    val Reports = R.drawable.icon_reports
    val ReportsOutline = R.drawable.icon_reports_outline
    val Results = R.drawable.icon_results
    val School = R.drawable.icon_school
    val Settings = R.drawable.icon_settings
    val Share = R.drawable.icon_share
    val Snapshot = R.drawable.icon_snapshot
    val Spreadsheet = R.drawable.icon_spreadsheet
    val Stop = R.drawable.icon_stop
    val StudentCap = R.drawable.icon_student_cap
    val Students = R.drawable.icon_students
    val StudentsOutline = R.drawable.icon_students_outline
    val Subject = R.drawable.icon_subject
    val Support = R.drawable.icon_support
    val Tick = R.drawable.icon_tick
    val VideoCamera = R.drawable.icon_video_camera
    val Week = R.drawable.icon_week
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}