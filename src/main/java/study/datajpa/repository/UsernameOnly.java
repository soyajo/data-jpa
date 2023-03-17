package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

/**
 * projections
 *
 */
public interface UsernameOnly {

    //open projections - 쿼리 다가지고옴
//    @Value("#{target.username + ' ' + target.age}")
    String getUsername();

}
