package controller;

import static model.SportsTeam.Entity.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static jdbc.OracleJdbc.*;
//import static model.SportsAlarm.Entity.*;
import model.SportsTeam;
import oracle.jdbc.OracleDriver;


public class SportsTeamDao {
	
	//-----> singleton
    private static SportsTeamDao instance = null;
    
    private SportsTeamDao() {
        try {
            // Oracle 드라이버(라이브러리)를 등록.
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static SportsTeamDao getInstance() {
        if (instance == null) {
            instance = new SportsTeamDao();
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
    private SportsTeam makeTeamFromResultSet(ResultSet rs) throws SQLException {
    	int id = rs.getInt(COL_ID);
        String league = rs.getString(COL_LEAGUE);
        String team = rs.getString(COL_TEAM);
        String emblemPath = rs.getString(COL_EMBLEMPATH);
        
        SportsTeam footballTeam = new SportsTeam(id, league, team, emblemPath);
        
		return footballTeam;
    }
    
    // read() 메서드에서 사용할 SQL 문장: select * from football_teams order by id desc
    private static final String SQL_SELECT_ALL = String.format(
            "select * from %s order by %s desc", 
            TBL_SPORTS_TEAMS, COL_ID);
    
    /**
     * 데이터베이스 테이블 FOOTBALL_TEAMS 테이블에서 모든 레코드(행)를 검색해서 
     * ID(고유키)의 내림차순으로 정렬된 리스트를 반환.
     * 테이블에 행이 없는 경우 빈 리스트를 리턴.
     * @return SportsAlarm를 원소로 갖는 ArrayList.
     */
    public List<SportsTeam> read() {
        List<SportsTeam> result = new ArrayList<>();
        
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
            	SportsTeam sportsAlarm = makeTeamFromResultSet(rs);
                result.add(sportsAlarm);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return result;
    }
    
    // create(SportsAlarm sportsAlarm) 메서드에서 사용할 SQL:
    // insert into football_teams (league, team, country) values (?, ?, ?)
    private static final String SQL_INSERT = String.format(
    		"insert into %s (%s, %s, %s) values (?, ?, ?)",
    		TBL_SPORTS_TEAMS, COL_LEAGUE, COL_TEAM, COL_EMBLEMPATH);
    
    /**
     * 데이터베이스의 FOOTBALL_TEAMS 테이블에 행을 삽입.
     * @param football_teams 테이블에 삽입할 제목, 내용, 작성자 정보를 가지고 있는 객체
     * @return 테이블에 삽입된 행의 개수.
     */
    public int create(SportsTeam sportsAlarm) {
    	int result = 0;
    	
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD); // DB 접속.
			stmt = conn.prepareStatement(SQL_INSERT); // Statement 객체 생성.
			stmt.setString(1, sportsAlarm.getLeague()); // statement의 첫번째 ? 채움.
			stmt.setString(2, sportsAlarm.getTeam()); // statenebt의 두번째 ? 채움.
			stmt.setString(3, sportsAlarm.getEmblemPath()); // statenebt의 세번째 ? 채움.
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
    		TBL_SPORTS_TEAMS, COL_ID);
    
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
    
    // 리그에 검색 키워드가 포함된 검색 결과:
    // select * from FOOTBALL_TEAMS where lower(league) like ? order by id desc
    private static final String SQL_SELECT_BY_LEAGUE = String.format(
    		"select * from %s where lower(%s) like ? order by %s desc", 
    		TBL_SPORTS_TEAMS, COL_LEAGUE, COL_ID);
    
    // 팀에 검색 키워드가 포함된 검색 결과: 
    // select * from FOOTBALL_TEAMS where lower(team) like ? order by id desc
    private static final String SQL_SELECT_BY_TEAM = String.format(
    		"select * from %s where lower(%s) like ? order by %s desc", 
    		TBL_SPORTS_TEAMS, COL_LEAGUE, COL_ID);
    
    // 리그 또는 팀에 검색 키워드가 포함된 검색 결과:
    // select * from FOOTBALL_TEAMS where lower(league) like ? or lower(team) like ? order by id desc
    private static final String SQL_SELECT_BY_LEAGUE_OR_TEAM = String.format(
    		"select * from %s where lower(%s) like ? or lower(%s) like ? order by %s desc",
    		TBL_SPORTS_TEAMS, COL_LEAGUE, COL_LEAGUE, COL_ID);
    
    /**
     * 제목, 내용, 제목 또는 내용, 작성자로 검색하기.
     * 검색 타입과 검색어를 전달받아서, 해당 SQL 문장을 실행하고 그 결과를 리턴.
     * @param type 0 - 제목 검색, 1 - 내용 검색, 2 - 제목 또는 내용 검색, 3 - 작성자 검색.
     * @param keyword 검색 문자열.
     * @return 검색 결과 리스트. 검색 결과가 없으면 빈 리스트.
     */
    public List<SportsTeam> search(int type, String keyword) {
    	List<SportsTeam> result = new ArrayList<>();
    	
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	String searchKeyword = "%" + keyword.toLowerCase() + "%"; // like 검색에서 사용할 파라미터
    	try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			switch (type) {
			case 0: // 리그로 검색
				stmt = conn.prepareStatement(SQL_SELECT_BY_LEAGUE);
				stmt.setString(1, searchKeyword);
				break;
			case 1: // 팀으로 검색
				stmt = conn.prepareStatement(SQL_SELECT_BY_TEAM);
				stmt.setString(1, searchKeyword);
				break;
			case 2: // 리그 또는 팀으로 검색
				stmt = conn.prepareStatement(SQL_SELECT_BY_LEAGUE_OR_TEAM);
				stmt.setString(1, searchKeyword);
				stmt.setString(2, searchKeyword);
				break;
			}
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				SportsTeam footballTeam = makeTeamFromResultSet(rs);
                result.add(footballTeam);
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
            TBL_SPORTS_TEAMS, COL_ID);
    
    /**
     * FOOTBALL_TEAMS 테이블의 고유키 id를 전달받아서, 해당 FootballTeam 객체를 리턴.
     * @param id 검색하기 위한 고유키.
     * @return 테이블에서 검색한 FootballTeam 객체. 고유키에 해당하는 행이 없는 경우 null을 리턴.
     */
    public SportsTeam read(int id) {
    	SportsTeam footballTeam = null;
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	footballTeam = makeTeamFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return footballTeam;
    }
    
    private static final String SQL_UPDATE = String.format(
            "update %s set %s = ?, %s = ?, %s = systimestamp where %s = ?", 
            TBL_SPORTS_TEAMS, COL_LEAGUE, COL_TEAM, COL_EMBLEMPATH, COL_ID);
    
    /**
     * football_teams 테이블 업데이트.
     * @param FootballTeams 업데이트할 id(고유키), 리그, 팀을 가지고 있는 객체.
     * @return 업데이트한 행의 개수.
     */
    public int update(SportsTeam footballTeam) {
        int result = 0;
        
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, footballTeam.getLeague());
            stmt.setString(2, footballTeam.getTeam());
            stmt.setInt(3, footballTeam.getId());
            result = stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt);
        }
        
        return result;
    }

}
