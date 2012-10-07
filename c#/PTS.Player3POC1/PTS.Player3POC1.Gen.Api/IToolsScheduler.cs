using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;

namespace Dust.Tools
{
    public enum EToolsSchedulerMessage { HeartBeat };
    public enum EToolsSchedulerTaskMessage { DoTask, CountdownReset };

    public enum EToolsSchedulerTaskType { Once, Countdown, Repeat };

    public interface IToolsScheduler
    {
        int getHeartBeatMsec();
        IToolsList getTasks();
    }

    public interface MIToolsSchedulerHeartBeat
    {
        DateTime getHeartBeatTime();
    }

    public interface IToolsSchedulerTask : IComparable
    {
        EToolsSchedulerTaskType getType();

        DateTime getExecTime();
        int getWaitMsec();

        DustCompData getTarget();
        DustMessage getMessage();
    }
}
