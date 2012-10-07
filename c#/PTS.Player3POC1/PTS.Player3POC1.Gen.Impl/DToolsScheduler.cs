using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;

namespace Dust.Tools
{
    public class DToolsScheduler : DustCompData, IToolsScheduler
    {
        public int heartBeatMsec = 1000;
        public IToolsList tasks;

        public int getHeartBeatMsec()
        {
            return heartBeatMsec;
        }


        public IToolsList getTasks()
        {
            return tasks;
        }
    }

    public class MToolsSchedulerHeartBeat : DustMessage, MIToolsSchedulerHeartBeat
    {
        public override Enum getMessageId() { return EToolsSchedulerMessage.HeartBeat; }

        DateTime heartBeatTime = DateTime.Now;

        public DateTime getHeartBeatTime()
        {
            return heartBeatTime;
        }
    }

    public class DToolsSchedulerTask : DustCompData, IToolsSchedulerTask
    {
        public EToolsSchedulerTaskType type;
        public int waitMsec;
        public DateTime execTime;

        public DustCompData target;
        public DustMessage message;

        public EToolsSchedulerTaskType getType()
        {
            return type;
        }
        
        public DustCompData getTarget()
        {
            return target;
        }

        public DustMessage getMessage()
        {
            return message;
        }

        public int getWaitMsec()
        {
            return waitMsec;
        }

        public DateTime getExecTime()
        {
            return execTime;
        }

        public int CompareTo(object obj)
        {
            throw new NotImplementedException();
        }
    }

    public class MToolsSchedulerTaskMessagesCountdownReset : DustMessage
    {
        public override Enum getMessageId() { return EToolsSchedulerTaskMessage.CountdownReset; }
    }

    public class MToolsSchedulerTaskMessagesDoTask : DustMessage
    {
        public override Enum getMessageId() { return EToolsSchedulerTaskMessage.DoTask; }
    }
}
