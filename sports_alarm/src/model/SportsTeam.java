package model;

public class SportsTeam {
	
    public static final class Entity {
        // 데이터베이스 테이블 이름을 상수로 선언. 
        public static final String TBL_SPORTS_TEAMS = "SPORTS_TEAMS";

        // 데이터베이스 FOOTBALL_TEAMS 테이블의 컬럼 이름들을 상수로 선언.
        public static final String COL_ID = "ID";
        public static final String COL_LEAGUE = "LEAGUE";
        public static final String COL_TEAM = "TEAM";
        public static final String COL_EMBLEMPATH = "EMBLEM_PATH";
    }
	
	private int id;
	private String league;
	private String team;
	private String emblemPath;
	
	public SportsTeam() {}
	
	public SportsTeam(int id, String league, String team, String emblemPath) {
		this.id = id;
		this.league = league;
		this.team = team;
		this.emblemPath = emblemPath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getEmblemPath() {
		return emblemPath;
	}

	public void setEmblemPath(String emblemPath) {
		this.emblemPath = emblemPath;
	}

	@Override
	public String toString() {
		return "SportsTeam [id=" + id + ", league=" + league + ", team=" + team + ", emblemPath=" + emblemPath + "]";
	}

}
