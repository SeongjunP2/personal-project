package controller;

import static model.SportsAlarm.Entity.TBL_FOOTBALL_TEAMS;
import static model.SportsAlarm.Entity.COL_ID;
import static model.SportsAlarm.Entity.COL_LEAGUE;
import static model.SportsAlarm.Entity.COL_TEAM;
import static model.SportsAlarm.Entity.COL_COUNTRY;
import static model.SportsAlarm.Entity.COL_EMBLEMPATH;

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
import model.SportsAlarm;
import oracle.jdbc.OracleDriver;


public class SportsAlarmDao {
	
	//-----> singleton
    private static SportsAlarmDao instance = null;
    
    private SportsAlarmDao() {
        try {
            // Oracle 드라이버(라이브러리)를 등록.
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static SportsAlarmDao getInstance() {
        if (instance == null) {
            instance = new SportsAlarmDao();
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
    private SportsAlarm makeTeamFromResultSet(ResultSet rs) throws SQLException {
    	int id = rs.getInt(COL_ID);
        String league = rs.getString(COL_LEAGUE);
        String team = rs.getString(COL_TEAM);
        String country = rs.getString(COL_COUNTRY);
        String emblemPath = rs.getString(COL_EMBLEMPATH);
        
        SportsAlarm sportsAlarm = new SportsAlarm(id, league, team, country, emblemPath);
        
		return sportsAlarm;
    }
    
    // read() 메서드에서 사용할 SQL 문장: select * from blogs order by id desc
    private static final String SQL_SELECT_ALL = String.format(
            "select * from %s order by %s desc", 
            TBL_FOOTBALL_TEAMS, COL_ID);
    
    /**
     * 데이터베이스 테이블 FOOTBALL_TEAMS 테이블에서 모든 레코드(행)를 검색해서 
     * ID(고유키)의 내림차순으로 정렬된 리스트를 반환.
     * 테이블에 행이 없는 경우 빈 리스트를 리턴.
     * @return SportsAlarm를 원소로 갖는 ArrayList.
     */
    public List<SportsAlarm> read() {
        List<SportsAlarm> result = new ArrayList<>();
        
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
            	SportsAlarm sportsAlarm = makeTeamFromResultSet(rs);
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
    // insert into football_teams (league, team, country) values (?, ? ?)
    private static final String SQL_INSERT = String.format(
    		"insert into %s (%s, %s, %s) values (?, ?, ?, ?)",
    		TBL_FOOTBALL_TEAMS, COL_LEAGUE, COL_TEAM, COL_COUNTRY, COL_EMBLEMPATH);
    
    /**
     * 데이터베이스의 FOOTBALL_TEAMS 테이블에 행을 삽입.
     * @param football_teams 테이블에 삽입할 제목, 내용, 작성자 정보를 가지고 있는 객체
     * @return 테이블에 삽입된 행의 개수.
     */
    public int create(SportsAlarm sportsAlarm) {
    	int result = 0;
    	
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD); // DB 접속.
			stmt = conn.prepareStatement(SQL_INSERT); // Statement 객체 생성.
			stmt.setString(1, sportsAlarm.getLeague()); // statement의 첫번째 ? 채움.
			stmt.setString(2, sportsAlarm.getTeam()); // statenebt의 두번째 ? 채움.
			stmt.setString(3, sportsAlarm.getCountry()); // statenebt의 세번째 ? 채움.
			stmt.setString(4, sportsAlarm.getEmblemPath()); // statenebt의 세번째 ? 채움.
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
    		TBL_FOOTBALL_TEAMS, COL_ID);
    
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
    
}
