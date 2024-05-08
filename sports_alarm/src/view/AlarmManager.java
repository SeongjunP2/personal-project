package view;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmManager {
    private Timer timer;

    public AlarmManager() {
        timer = new Timer();
    }

    // 알람을 설정하고 해당 시간이 되면 실행되는 메서드
    public void setAlarm(int delayMillis) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 알람 울림 동작 (팝업 창 띄우기 등)
                showPopup();
            }
        }, delayMillis);
    }

    // 팝업 창을 띄우는 메서드
    private void showPopup() {
        // 팝업 창을 띄우는 코드 작성
    }
}
