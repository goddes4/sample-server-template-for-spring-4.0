package net.octacomm.util;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

/**
 *
 * @author tykim
 */
public class QuartzUtil {

    public static String getJobKeys(Scheduler scheduler) throws SchedulerException {
        StringBuilder sb = new StringBuilder();
        for (String group : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
                sb.append("JobKey : ").append(jobKey).append("\n");
            }
        }
        return sb.toString();
    }

    public static String getTriggerKeys(Scheduler scheduler) throws SchedulerException {
        StringBuilder sb = new StringBuilder();
        for (String group : scheduler.getTriggerGroupNames()) {
            for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(group))) {
                Trigger trigger = scheduler.getTrigger(triggerKey);
                sb.append("State : ").append(scheduler.getTriggerState(triggerKey))
                        .append(", TriggerKey : ").append(triggerKey)
                        .append(" - JobKey : ").append(trigger.getJobKey()).append("\n");
            }
        }
        return sb.toString();
    }
}
