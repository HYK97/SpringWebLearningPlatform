package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Entity.CourseEvaluation;
import com.hy.demo.Utils.QueryDsl4RepositorySupport;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hy.demo.Domain.Board.Entity.QCourse.course;
import static com.hy.demo.Domain.Board.Entity.QCourseEvaluation.courseEvaluation;


public class CourseEvaluationRepositoryImpl extends QueryDsl4RepositorySupport implements CourseEvaluationRepositoryCustom  {



    public CourseEvaluationRepositoryImpl() {
        super(CourseEvaluation.class);
    }




    public Map<String, Double> countScope(Long id){
        JPAQueryFactory queryFactory=getQueryFactory();
         List<Tuple> fetch = queryFactory.select(courseEvaluation.scope, courseEvaluation.scope.count())
                .from(courseEvaluation)
                .where(course.id.eq(id))
                .groupBy(courseEvaluation.scope)
                .fetch();
        Map<Double,Long> map = new HashMap();
        for (Tuple tuple : fetch) {
            System.out.println("tuple.get(courseEvaluation.course),tuple.get(courseEvaluation.scope.count() = " + tuple.get(courseEvaluation.scope)+ tuple.get(courseEvaluation.scope.count()));
            map.put(tuple.get(courseEvaluation.scope),tuple.get(courseEvaluation.scope.count()));
        }
        Double one = 0.0;
        Double two = 0.0;
        Double three= 0.0;
        Double four= 0.0;
        Double five= 0.0;
        Map<String, Double> list =new HashMap<>();
        for( Double key : map.keySet() ){
            if ((int) (key * 10) % 10 == 5) {//x.5 일때
                int scope=(int)(key  % 10);
                switch (scope){
                    case 1:
                        one+=0.5;
                        two+=0.5;
                        break;
                    case 2:
                        two+=0.5;
                        three+=0.5;
                        break;
                    case 3:
                        three+=0.5;
                        four+=0.5;
                        break;
                    case 4:
                        four+=0.5;
                        five+=0.5;
                        break;
                    case 5:
                        five+=0.5f;
                        break;
                }
            } else {
                if (key == 1.0) {
                    one += 1.0;
                } else if (key == 2.0) {
                    two += 1.0;
                } else if (key == 3.0) {
                    three += 1.0;
                } else if (key == 4.0) {
                    four += 1.0;
                } else if (key == 5.0) {
                    five += 1.0;
                }
            }

        }
        list.put("5",five);
        list.put("4",four);
        list.put("3",three);
        list.put("2",two);
        list.put("1",one);
        return list;
    }



}