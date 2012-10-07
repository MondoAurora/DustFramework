using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using Dust.Core;
using System.Diagnostics;

namespace Dust.Tools
{
    public class ToolsSchedulerTask : DustComponent<DToolsSchedulerTask>
    {
        public override void init()
        {
            DToolsSchedulerTask d = getData();
            EToolsSchedulerTaskType t = d.getType();

            switch (t) {
                case EToolsSchedulerTaskType.Repeat:
                    if (null == d.getExecTime())
                    {
                        d.execTime = DateTime.Now;
                    }
                    break;
                case EToolsSchedulerTaskType.Countdown:
                    d.execTime = DateTime.Now.AddMilliseconds(d.getWaitMsec());
                    break;
            }
        }

        public override ProcessReturn process(DustMessage message, DustCompData source)
        {
            Enum msgId = message.getMessageId();
            ProcessReturn ret = ProcessReturn.Unhandled;

            if (msgId is EToolsSchedulerTaskMessage)
            {
                DToolsSchedulerTask d = getData();
                EToolsSchedulerTaskType t = d.getType();

                switch ((EToolsSchedulerTaskMessage)msgId)
                {
                    case EToolsSchedulerTaskMessage.DoTask:
                        send(d.getMessage(), d.getTarget());
                        switch (t)
                        {
                            case EToolsSchedulerTaskType.Repeat:
                               d.execTime = DateTime.Now.AddMilliseconds(d.getWaitMsec());
                                break;
                            case EToolsSchedulerTaskType.Once:
                            case EToolsSchedulerTaskType.Countdown:
                                d.execTime = DateTime.MaxValue;
                                break;
                        }
                        ret = ProcessReturn.Updated;
                        break;
                    case EToolsSchedulerTaskMessage.CountdownReset:
                        if (EToolsSchedulerTaskType.Countdown == t)
                        {
                            d.execTime = DateTime.Now.AddMilliseconds(d.getWaitMsec());
                            ret = ProcessReturn.Updated;
                        }
                        break;
                }
            }

            return ret;
        }
    }

    public class ToolsScheduler : ToolsWorker<DToolsScheduler>
    {
        int heartBeat = 100;

        public ToolsScheduler()
        {
            name = "Scheduler";
        }

        public override void init()
        {
            heartBeat = getData().getHeartBeatMsec();

            foreach (DustData act in getData().getTasks().getItems())
            {
                ((DustCompData)act).ping(); // ensure initialization
            }

            listen((DustCompData)getData().getTasks());
        }

        public override ProcessReturn process(DustMessage message, DustCompData source)
        {
            if (message.getMessageId() is EToolsListMessage)
            {
                foreach (DustData act in getData().getTasks().getItems())
                {
                    ((DustCompData)act).ping(); // ensure initialization
                }
            }

            return ProcessReturn.Unhandled;
        }

        protected override void loop()
        {
            DToolsScheduler data = getData();
            bool doSort = false;

            IToolsList tasks = data.getTasks();

            lock (tasks)
            {
                MToolsSchedulerHeartBeat hbmsg = new MToolsSchedulerHeartBeat();
                MToolsSchedulerTaskMessagesDoTask msgDoTask = new MToolsSchedulerTaskMessagesDoTask();
                DateTime now = hbmsg.getHeartBeatTime();

                send(hbmsg);

                foreach (DustData act in tasks.getItems())
                {
                    if (!isRun())
                    {
                        return;
                    }

                    if (((IToolsSchedulerTask)act).getExecTime() <= now)
                    {
                        send(msgDoTask, (DustCompData) act);
                        doSort = true;
                    }
                    else
                    {
                        // Not sorted yet!
//                        break;
                    }

                }

                if (doSort)
                {
//                    actions.Sort();
                }

                // not perfect, should consider using msgHeartBeat.t + heartBeat
                Monitor.Wait(tasks, heartBeat);
            }
        }
    }
}
