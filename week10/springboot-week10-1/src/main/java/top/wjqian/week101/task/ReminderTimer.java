package top.wjqian.week101.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Slf4j
public class ReminderTimer {
    public static void main(String[] args) {
        Timer timer=new Timer();
        TimerTask task=new TimerTask(){
            @Override
            public void run() {
//                log.info("请休息一下，喝点水");
                JOptionPane.showMessageDialog(
                        null,
                        "请休息一下，喝点水");
            }
        };
        //使用定时器对定时任务进行调度
        timer.schedule(task, 0,3*1000);
    }

}
