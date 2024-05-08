package model;

public class Schedule {
	
    public static final class Entity {
        // 데이터베이스 테이블 이름을 상수로 선언. 
        public static final String TBL_SCHEDULE_DATE = "SCHEDULE_DATE";

        // 데이터베이스 FOOTBALL_TEAMS 테이블의 컬럼 이름들을 상수로 선언.
        public static final String COL_ID = "ID";
        public static final String COL_TEAM = "TEAM";
        public static final String COL_OTHER_TEAM = "OTHER_TEAM";
        public static final String COL_CREATED_DATE = "CREATED_DATE";
    }
	
	private int id;
	private String team;
	private String otherTeam;
	private String date;
	
	public Schedule() {}
	
	public Schedule(int id, String team, String otherTeam, String date) {
		this.id = id;
		this.team = team;
		this.otherTeam = otherTeam;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getOtherTeam() {
		return otherTeam;
	}

	public void setOtherTeam(String otherTeam) {
		this.otherTeam = otherTeam;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	// 날짜를 저장한 String 타입 date에서 년, 월, 일 추출
    public int getYear() {
        // "YYYY년 MM월 DD일" 형식의 날짜 문자열에서 연도 추출
        String[] dateParts = date.split(" ");
        String yearString = dateParts[0]; // 첫 번째 요소가 연도를 포함한 부분
        String[] yearParts = yearString.split("년");
        return Integer.parseInt(yearParts[0]); // "YYYY" 부분을 정수로 변환하여 반환
    }

    public int getMonth() {
        // "YYYY년 MM월 DD일" 형식의 날짜 문자열에서 월 추출
        String[] dateParts = date.split(" ");
        String monthString = dateParts[1]; // 두 번째 요소가 월을 포함한 부분
        String[] monthParts = monthString.split("월");
        return Integer.parseInt(monthParts[0]); // "MM" 부분을 정수로 변환하여 반환
    }

    public int getDay() {
        // "YYYY년 MM월 DD일" 형식의 날짜 문자열에서 일 추출
        String[] dateParts = date.split(" ");
        String dayString = dateParts[2]; // 세 번째 요소가 일을 포함한 부분
        String[] dayParts = dayString.split("일");
        return Integer.parseInt(dayParts[0]); // "DD" 부분을 정수로 변환하여 반환
    }
	

	@Override
	public String toString() {
		return "Schedule [id=" + id + ", team=" + team + ", otherTeam=" + otherTeam + ", date=" + date + "]";
	}

}
