package study.datajpa.repository;

/**
 * 팀의 컬럼은 다 가지고 옴
 */
public interface NestedClosedProjections {

    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }

}
