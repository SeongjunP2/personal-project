package controller;

import static model.Schedule.Entity.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static jdbc.OracleJdbc.*;

import model.Schedule;
//import static model.SportsAlarm.Entity.*;
import oracle.jdbc.OracleDriver;


public class ScheduleDao {
	
	//-----> singleton
    private static ScheduleDao instance = null;
    
    private ScheduleDao() {
        try {
            // Oracle 드라이버(라이브러리)를 등록.
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static ScheduleDao getInstance() {
        if (instance == null) {
            instance = new ScheduleDao();
        }
        
        return instance;
    }
    //<----- singleton
    
    /**
     * CRUD 메서드들에서 사용했던 리소스들을 해제.
     * @param conn Connection 객체
     * @param stmt Statement 객체
     * @param rs ResultSet 객체
     */
    private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * CRUD 메서드들에서 사용했던 리소스들을 해제.
     * @param conn Connection 객체
     * @param stmt Statement 객체
     */
    private void closeResources(Connection conn, Statement stmt) {
        closeResources(conn, stmt, null);
    }
    
    // ResultSet에서 각 컬럼의 값들을 읽어서 SportsAlarm 타입 객체를 생성하고 리턴.
    private Schedule makeScheduleFromResultSet(ResultSet rs) throws SQLException {
    	int id = rs.getInt(COL_ID);
        String team = rs.getString(COL_TEAM);
        String otherTeam = rs.getString(COL_OTHER_TEAM);
        String date = rs.getString(COL_CREATED_DATE);
        
        Schedule schedule = new Schedule(id, team, otherTeam, date);
        
		return schedule;
    }
    
    // read() 메서드에서 사용할 SQL 문장: select * from football_teams order by id desc
    private static final String SQL_SELECT_ALL = String.format(
            "select * from %s order by %s desc", 
            TBL_SCHEDULE_DATE, COL_ID);
    
    /**
     * 데이터베이스 테이블 FOOTBALL_TEAMS 테이블에서 모든 레코드(행)를 검색해서 
     * ID(고유키)의 내림차순으로 정렬된 리스트를 반환.
     * 테이블에 행이 없는 경우 빈 리스트를 리턴.
     * @return SportsAlarm를 원소로 갖는 ArrayList.
     */
    public List<Schedule> read() {
        List<Schedule> result = new ArrayList<>();
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // 데이터베이스에 접속.
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // 실행할 SQL 문장을 갖고 있는 PreparedStatement 객체를 생성.
            stmt = conn.prepareStatement(SQL_SELECT_ALL);
            // SQL 문장을 데이터베이스로 전송해서 실행.
            rs = stmt.executeQuery();
            // 결과 처리.
            while (rs.next()) {
            	Schedule schedule = makeScheduleFromResultSet(rs);
                result.add(schedule);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return result;
    }
    
    // 현재 팀 이름만 불러오는 메서드
    public List<String> getTeamNames() {
        List<String> teamNames = new ArrayList<>();
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.prepareStatement("select team from " + TBL_SCHEDULE_DATE); // 팀이름만 불러오는 SQL 문장
            rs = stmt.executeQuery();
            while (rs.next()) {
                teamNames.add(rs.getString("team"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return teamNames;
    }
    
    // 상대 팀 이름만 반환하는 메서드 추가
    public List<String> getOtherTeam(String league) {
        List<String> teamNames = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.prepareStatement("select otherteam from " + TBL_SCHEDULE_DATE); // 상대 팀이름만 불러오는 SQL 문장
            rs = stmt.executeQuery();
            while (rs.next()) {
                teamNames.add(rs.getString("team"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return teamNames;
    }
    
    // 수정된 getOtherTeamAndCreatedDate 메서드
    public List<Schedule> getOtherTeamAndCreatedDate(String teamName) {
        List<Schedule> schedules = new ArrayList<>();
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // 수정된 SQL 쿼리: 선택한 팀이 team 열이나 other_team 열 중 어느 쪽에도 나오는 경기 일정을 모두 가져옴.
            String sql = "select other_team, created_date from " + TBL_SCHEDULE_DATE +  
                         " where team = ?" + 
                         " union" + 
                         " select team, created_date from " + TBL_SCHEDULE_DATE +  
                         " where other_team = ?" +
                         " order by created_date";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, teamName);
            stmt.setString(2, teamName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setOtherTeam(rs.getString("other_team"));
                schedule.setDate(rs.getString("created_date"));
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return schedules;
    }

    
	// 특정 팀의 일정을 가져오는 메서드
	public List<Schedule> getTeamSchedules(String teamName) {
		List<Schedule> teamSchedules = new ArrayList<>();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			String sql = "select created_date from " + TBL_SCHEDULE_DATE + " where " + COL_TEAM + " = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, teamName);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Schedule schedule = makeScheduleFromResultSet(rs);
				teamSchedules.add(schedule);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}

		return teamSchedules;
	}
	
	// sports_teams 테이블과 schedule_date 테이블을 조인하여 해당하는 emblem_path를 반환하는 메서드
	public String getOtherTeamEmblemPath(String otherTeam) {
	    String emblemPath = null;
	    
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try {
	        conn = DriverManager.getConnection(URL, USER, PASSWORD);
	        // sports_teams와 schedule_date를 조인하여 상호 일치하는 행을 가져오는 SQL 문장
	        String sql = "select st.emblem_path " +
	                     "from sports_teams st " +
	                     "inner join schedule_date sd on st.team = sd.other_team " +
	                     "where sd.other_team = ?";
	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, otherTeam);
	        rs = stmt.executeQuery();
	        if (rs.next()) {
	            emblemPath = rs.getString("emblem_path");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        closeResources(conn, stmt, rs);
	    }
	    
	    return emblemPath;
	}

    
    // create(SportsAlarm sportsAlarm) 메서드에서 사용할 SQL:
    // insert into football_teams (team, otherTeam, date) values (?, ?, ?)
    private static final String SQL_INSERT = String.format(
    		"insert into %s (%s, %s, %s) values (?, ?, ?)",
    		TBL_SCHEDULE_DATE, COL_TEAM, COL_OTHER_TEAM, COL_CREATED_DATE);
    
    /**
     * 데이터베이스의 FOOTBALL_TEAMS 테이블에 행을 삽입.
     * @param football_teams 테이블에 삽입할 제목, 내용, 작성자 정보를 가지고 있는 객체
     * @return 테이블에 삽입된 행의 개수.
     */
    public int create(Schedule schedule) {
    	int result = 0;
    	
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD); // DB 접속.
			stmt = conn.prepareStatement(SQL_INSERT); // Statement 객체 생성.
			stmt.setString(1, schedule.getTeam()); // statement의 첫번째 ? 채움.
			stmt.setString(2, schedule.getOtherTeam()); // statenebt의 두번째 ? 채움.
			stmt.setString(3, schedule.getDate()); // statenebt의 세번째 ? 채움.
			result = stmt.executeUpdate(); // SQL 실행.
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt);
		}
    	
    	return result;
    }
    
    // delete(int id) 메서드에서 사용할 SQL: delete from football_teams where id = ?
    private static final String SQL_DELETE = String.format(
    		"delete from %s where %s = ?",
    		TBL_SCHEDULE_DATE, COL_ID);
    
    /**
     * 테이블 FOOTBALL_TEAMS 에서 고유키(PK) id에 해당하는 레코드(행)를 삭제.
     * @param id 삭제하려는 레코드의 고유키.
     * @return 테이블에서 삭제된 행의 개수.
     */
    public int delete(int id) {
    	int result = 0;
    	
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_DELETE);
			stmt.setInt(1, id);
			result = stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt);
		}
    	
    	return result;
    }
    
    // 현재 팀에 검색 키워드가 포함된 검색 결과:
    // select * from SPORTS_TEAMS where lower(league) like ? order by id desc
    private static final String SQL_SELECT_BY_TEAM = String.format(
    		"select * from %s where lower(%s) like ? order by %s desc", 
    		TBL_SCHEDULE_DATE, COL_TEAM, COL_ID);
    
    // 상대 팀에 검색 키워드가 포함된 검색 결과: 
    // select * from SPORTS_TEAMS where lower(team) like ? order by id desc
    private static final String SQL_SELECT_BY_OTHERTEAM = String.format(
    		"select * from %s where lower(%s) like ? order by %s desc", 
    		TBL_SCHEDULE_DATE, COL_OTHER_TEAM, COL_ID);
    
    // 현재 팀 또는 상대 팀에 검색 키워드가 포함된 검색 결과:
    // select * from SPORTS_TEAMS where lower(league) like ? or lower(team) like ? order by id desc
    private static final String SQL_SELECT_BY_TEAM_OR_OTHERTEAM = String.format(
    		"select * from %s where lower(%s) like ? or lower(%s) like ? order by %s desc",
    		TBL_SCHEDULE_DATE, COL_TEAM, COL_OTHER_TEAM, COL_ID);
    
    /**
     * 제목, 내용, 제목 또는 내용, 작성자로 검색하기.
     * 검색 타입과 검색어를 전달받아서, 해당 SQL 문장을 실행하고 그 결과를 리턴.
     * @param type 0 - 제목 검색, 1 - 내용 검색, 2 - 제목 또는 내용 검색, 3 - 작성자 검색.
     * @param keyword 검색 문자열.
     * @return 검색 결과 리스트. 검색 결과가 없으면 빈 리스트.
     */
    public List<Schedule> search(int type, String keyword) {
    	List<Schedule> result = new ArrayList<>();
    	
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	String searchKeyword = "%" + keyword.toLowerCase() + "%"; // like 검색에서 사용할 파라미터
    	try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			switch (type) {
			case 0: // 리그로 검색
				stmt = conn.prepareStatement(SQL_SELECT_BY_TEAM);
				stmt.setString(1, searchKeyword);
				break;
			case 1: // 팀으로 검색
				stmt = conn.prepareStatement(SQL_SELECT_BY_OTHERTEAM);
				stmt.setString(1, searchKeyword);
				break;
			case 2: // 리그 또는 팀으로 검색
				stmt = conn.prepareStatement(SQL_SELECT_BY_TEAM_OR_OTHERTEAM);
				stmt.setString(1, searchKeyword);
				stmt.setString(2, searchKeyword);
				break;
			}
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				Schedule schedule = makeScheduleFromResultSet(rs);
                result.add(schedule);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}
    	
    	return result;
    }
    
    // 아이디(PK)로 검색하기:
    private static final String SQL_SELECT_BY_ID = String.format(
            "select * from %s where %s = ?", 
            TBL_SCHEDULE_DATE, COL_ID);
    
    /**
     * FOOTBALL_TEAMS 테이블의 고유키 id를 전달받아서, 해당 FootballTeam 객체를 리턴.
     * @param id 검색하기 위한 고유키.
     * @return 테이블에서 검색한 FootballTeam 객체. 고유키에 해당하는 행이 없는 경우 null을 리턴.
     */
    public Schedule read(int id) {
    	Schedule schedule = null;
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	schedule = makeScheduleFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return schedule;
    }
    
    private static final String SQL_UPDATE = String.format(
            "update %s set %s = ?, %s = ?, %s = ? where %s = ?", 
            TBL_SCHEDULE_DATE, COL_TEAM, COL_OTHER_TEAM, COL_CREATED_DATE, COL_ID);
    
    /**
     * football_teams 테이블 업데이트.
     * @param FootballTeams 업데이트할 id(고유키), 리그, 팀을 가지고 있는 객체.
     * @return 업데이트한 행의 개수.
     */
    public int update(Schedule schedule) {
        int result = 0;
        
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, schedule.getTeam());
            stmt.setString(2, schedule.getOtherTeam());
            stmt.setString(3, schedule.getDate());
            stmt.setInt(4, schedule.getId());
            result = stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt);
        }
        
        return result;
    }

}
