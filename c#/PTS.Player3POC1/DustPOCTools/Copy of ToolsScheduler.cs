using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using Dust.Core;
using System.Diagnostics;

namespace Dust.Tools
{
    public class XToolsScheduler : ToolsWorker<DustCompData>
    {

        class Action : IComparable
        {
            DateTime next;

            XToolsScheduler scheduler;
            DToolsSchedulerTask data;

            internal Action(XToolsScheduler scheduler, DToolsSchedulerTask data)
            {
                this.scheduler = scheduler;

                this.data = data;

                if (null == (next = data.getExecTime()))
                {
                    next = DateTime.Now;
                }
            }

            internal bool isDue(DateTime now)
            {
                return next >= now;
            }

            public int CompareTo(object obj)
            {
                if (obj is Action)
                {
                    return this.next.CompareTo((obj as Action).next);
                }

                throw new ArgumentException("Object is not an Action");
            }

            internal bool execute()
            {
                DateTime t = DateTime.Now;

                scheduler.send(new MToolsActionExecute(), (DustCompData) data.getTarget());

                int wms = data.getWaitMsec();
                if (-1 == wms)
                {
                    return false;
                }
                else
                {
                    next = t.AddMilliseconds(wms);
                    return true;
                }
            }
        }

        List<Action> actions = new List<Action>();
        ToolsList actionList;

        DToolsScheduler data;

        int heartBeat = 100;

        public XToolsScheduler()
        {
            name = "Scheduler";
        }

        public override void init()
        {
            this.data = (DToolsScheduler)getData();

            actionList = new ToolsList();

            foreach (DustData taskData in data.getTasks().getItems())
            {
                actions.Add(new Action(this, (DToolsSchedulerTask) taskData));
            }

            heartBeat = data.getHeartBeatMsec();
        }

        protected override void loop()
        {
            bool doSort = false;

            lock (actions)
            {
                MToolsSchedulerHeartBeat hbmsg = new MToolsSchedulerHeartBeat();
                DateTime now = hbmsg.getHeartBeatTime();

                send(hbmsg);

                foreach (Action act in actions)
                {
                    if (!isRun())
                    {
                        return;
                    }

                    if (act.isDue(now))
                    {
                        if (!act.execute())
                        {
                            actions.Remove(act);
                        }
                        doSort = true;
                    }
                    else
                    {
                        break;
                    }

                }

                if (doSort)
                {
                    actions.Sort();
                }

                // not perfect, should consider using msgHeartBeat.t + heartBeat
                Monitor.Wait(actions, heartBeat);
            }
        }
    }
}
