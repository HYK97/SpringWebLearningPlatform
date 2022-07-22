package com.hy.demo.Domain.Board.Controller;


import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Service.CourseBoardService;
import com.hy.demo.Domain.Board.form.CourseBoardForm;
import com.hy.demo.Domain.Comments.Service.CommentsService;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Service.CourseEvaluationService;
import com.hy.demo.Domain.Course.Service.CourseService;
import com.hy.demo.Domain.File.Dto.FileDto;
import com.hy.demo.Domain.File.Service.FileService;
import com.hy.demo.Domain.User.Service.UserService;
import com.hy.demo.Utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hy.demo.enumcode.AJAXResponseCode.*;

;

@Controller
@RequestMapping("/courseboard/*")
@RequiredArgsConstructor
@Slf4j
public class CourseBoardController {


    private final CourseBoardService courseBoardService;

    private final CourseService courseService;

    private final CommentsService commentsService;

    private final CourseEvaluationService courseEvaluationService;

    private final UserService userService;

    private final FileService fileService;


    @GetMapping("/{id}")
    @PreAuthorize("@authorizationChecker.isAccessBoard(#id)")
    public String viewBoardList(Model model, @PathVariable Long id) {
        model.addAttribute("courseId", id);
        return "courseboard/view";
    }

    @PostMapping("/viewPlus/{id}")
    @ResponseBody
    public void viewPlus(Model model, @PathVariable Long id) {
        courseBoardService.viewPlus(id);
    }


    @GetMapping("/data/{id}")
    @ResponseBody
    public List<CourseBoardDto> Data(@PathVariable Long id) {
        List<CourseBoardDto> courseBoardList = courseBoardService.findCourseBoardList(id);

        return courseBoardList;
    }


    @GetMapping("/BoardManagement/{id}")
    @PreAuthorize("@authorizationChecker.isManagementBoard(#id)")
    public String BoardManagement(Model model, @PathVariable Long id) {
        model.addAttribute("courseId", id);
        return "courseboard/management";
    }


    @PostMapping("/deleteCourseBoard/{id}")
    @ResponseBody
    public String deleteBoard(@PathVariable Long id) {
        log.debug("id = {}", id);
        try {
            courseBoardService.deleteBoardAndFiles(id);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
            return FAIL.toString(); //실패
        }

        return OK.toString(); //성공
    }


    @PostMapping("/updateCourseBoard/{id}")
    @ResponseBody
    public ResponseEntity updateCourseBoard(@PathVariable Long id, @ModelAttribute @Validated CourseBoardForm form) throws IOException {
        //파일 확인
        log.info("updateCourseBoard/{} ", id);
        log.info("form.toString() = {} ", form.toString());
        try {
            courseBoardService.updateCourseBoard(id, form.getFile(), form.getTitle(), form.getContents());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
            //실패
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/getDashBoard/{id}")
    @ResponseBody
    public Map getDashBoard(@PathVariable Long id) throws IOException {
        Map map = new HashMap();
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(formatter);
        String yesterday = now.minusDays(1).format(formatter);

        //어제오늘 유저수
        Long todayRegisteredUser = userService.countDateRegisteredUserCount(id, today);
        Long yesterdayRegisteredUser = userService.countDateRegisteredUserCount(id, yesterday);
        map.put("todayRegisteredUser", todayRegisteredUser);
        map.put("yesterdayRegisteredUser", yesterdayRegisteredUser);

        //어제오늘 별점수
        Double todayScope = courseEvaluationService.avgDateScope(id, today);
        Double yesterdayScope = courseEvaluationService.avgDateScope(id, yesterday);
        map.put("todayScope", Double.parseDouble(String.format("%.1f", todayScope == null ? 0 : todayScope)));
        map.put("yesterdayScope", Double.parseDouble(String.format("%.1f", yesterdayScope == null ? 0 : yesterdayScope)));

        //어제오늘 댓글수
        Long todayComment = commentsService.countDateCommentCount(id, today);
        Long yesterdayComment = commentsService.countDateCommentCount(id, yesterday);
        map.put("todayComment", todayComment);
        map.put("yesterdayComment", yesterdayComment);

        //강의 총 view 수
        Long allView = courseBoardService.countAllView(id);
        map.put("allView", allView);

        //1달 가입자 집계
        Map DailyForAMonthUser = userService.countMonthlyToDayRegisteredUser(id, today);
        map.put("DailyForAMonthUser", DailyForAMonthUser);

        //1달 별점 집계
        Map DailyForAMonthScope = courseEvaluationService.monthlyToDayScopeAvg(id, yesterday);
        map.put("DailyForAMonthScope", DailyForAMonthScope);

        //1달 댓글집계
        Map DailyForAMonthComments = commentsService.monthlyToDayComments(id, yesterday);
        map.put("DailyForAMonthComments", DailyForAMonthComments);

        //courseBoard 랭킹뷰

        List<CourseBoardDto> courseBoardDtos = courseBoardService.rankView(id);
        map.put("RankingView", courseBoardDtos);


        return map;
    }


    /**
     * UserRegistered
     */

    @PostMapping("/userDayChart/{id}")
    @ResponseBody
    public Map userDayChart(@PathVariable Long id) {

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(formatter);
        Map DailyForAMonthUser = userService.countMonthlyToDayRegisteredUser(id, today);
        return DailyForAMonthUser;
    }


    @PostMapping("/userMonthChart/{id}")
    @ResponseBody
    public Map userMonthChart(@PathVariable Long id) {

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(formatter);
        Map DailyForAMonthUser = userService.countThisYearToMonthlyRegisteredUser(id, today);
        return DailyForAMonthUser;
    }

    @PostMapping("/userYearChart/{id}")
    @ResponseBody
    public Map userYearChart(@PathVariable Long id) {

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(formatter);
        Map DailyForAMonthUser = userService.countTenYearToYearRegisteredUser(id, today);
        return DailyForAMonthUser;
    }


    /**
     * Scope
     */

    @PostMapping("/scopeDayChart/{id}")
    @ResponseBody
    public Map scopeDayChart(@PathVariable Long id) {

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(formatter);
        Map DailyForAMonthUser = courseEvaluationService.monthlyToDayScopeAvg(id, today);
        return DailyForAMonthUser;
    }

    @PostMapping("/scopeMonthChart/{id}")
    @ResponseBody
    public Map scopeMonthChart(@PathVariable Long id) {

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(formatter);
        Map DailyForAMonthUser = courseEvaluationService.thisYearToMonthlyScopeAvg(id, today);
        return DailyForAMonthUser;
    }

    @PostMapping("/scopeYearChart/{id}")
    @ResponseBody
    public Map scopeYearChart(@PathVariable Long id) {

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(formatter);
        Map DailyForAMonthUser = courseEvaluationService.tenYearToYearScopeAvg(id, today);
        return DailyForAMonthUser;
    }


    /**
     * comments
     */

    @PostMapping("/commentsDayChart/{id}")
    @ResponseBody
    public Map commentsDayChart(@PathVariable Long id) {

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(formatter);
        Map DailyForAMonthUser = commentsService.monthlyToDayComments(id, today);
        return DailyForAMonthUser;
    }

    @PostMapping("/commentsMonthChart/{id}")
    @ResponseBody
    public Map commentsMonthChart(@PathVariable Long id) {

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(formatter);
        Map DailyForAMonthUser = commentsService.thisYearToMonthlyComments(id, today);
        return DailyForAMonthUser;
    }

    @PostMapping("/commentsYearChart/{id}")
    @ResponseBody
    public Map commentsYearChart(@PathVariable Long id) {

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(formatter);
        Map DailyForAMonthUser = commentsService.tenYearToYearComments(id, today);
        return DailyForAMonthUser;
    }


    @GetMapping({"/getCourseBoard/{id}"})
    @ResponseBody
    public CourseBoardDto getCourseBoard(@PathVariable Long id) throws Exception {
        CourseBoardDto findCourseDto = courseBoardService.findCourseBoardByCourseBoardId(id);
        return findCourseDto;
    }


    @PostMapping("/createBoard/{id}")
    @ResponseBody
    public String createBoardcreateBoard(@PathVariable Long id, @ModelAttribute @Validated CourseBoardForm form) {
        List<FileDto> fileDtos = new ArrayList<>();
        Course findCourse;
        try {
            findCourse = courseService.findCourseById(id);
        } catch (Exception e) {
            return FAIL.toString(); //잘못된 courseId
        }
        CourseBoard courseBoard = CourseBoard.builder()
                .title(form.getTitle())
                .contents(form.getContents())
                .views(0L)
                .course(findCourse)
                .build();
        if (!ObjectUtils.isEmpty(form.getFile())) {
            try {
                fileDtos = fileService.localSaveFile(form.getFile());
                log.debug("fileDtos = {}", fileDtos.size());
            } catch (IOException e) {
                e.printStackTrace();
                return ERROR.toString(); //파일오류
            }
        }
        courseBoardService.save(courseBoard, fileDtos);
        return OK.toString();//성공
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (e.getReason().equals(OK.toString())) {
            String requestURI = request.getRequestURI().trim();
            String[] split = requestURI.split("/");
            ModelAndView model = new ModelAndView();
            log.debug("requestURI = {}", split[2]);
            model.addObject("id", split[2]);
            model.addObject("joinBtn", true);
            model.setViewName("redirect:/course/detailcourse");
            return model;
        } else if (e.getReason().equals(FAIL.toString())) {
            throw new AccessDeniedException("403 에러");
        } else {
            throw new AccessDeniedException("403 에러");
        }
    }


}

