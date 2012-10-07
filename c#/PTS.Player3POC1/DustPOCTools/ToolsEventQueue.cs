using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using Dust.Core;
using System.Diagnostics;

namespace Dust.Tools
{
    class ToolsEventQueue : ToolsWorker<DustCompData>
    {
        class Processor
        {
            public DustComponentBase processor;
            public DustMessage.Filter filter;

            public ProcessReturn process(DustMessage message, DustCompData source, DustCompData target)
            {
                return ((null == filter) || filter.accept(message, source, target)) 
                    ? processor.process(message, source) 
                    : ProcessReturn.Unhandled;
            }
        }
        class Event
        {
            public DustCompData source;
            public DustCompData target;
            public DustMessage message;

            public void dispatch(ToolsEventQueue eq)
            {
                if (null == target)
                {
                    foreach (Processor proc in eq.processors)
                    {
                        if (ProcessReturn.Updated == proc.process(message, source, target))
                        {
                            eq.send(message, proc.processor.getDataDef(), null);
                        }
                    }
                }
                else
                {
                    if (ProcessReturn.Updated == ToolsEnvironment.process(target, message, source))
                    {
                        eq.send(message, target, null);
                    }
                }
            }
        }

        private int read = 0;
        private int dispatch = 1;

        private Queue<Event>[] qEvents = new Queue<Event>[] { new Queue<Event>(), new Queue<Event>() };

        List<Processor> processors = new List<Processor>();

        public ToolsEventQueue()
        {
            name = "Event dispatcher";
        }

        public void send(DustMessage message_, DustCompData source_, DustCompData target_)
        {
            lock (qEvents)
            {
                qEvents[read].Enqueue(new Event { source = source_, target = target_, message = message_ });
                pulse(qEvents);
            }
        }

        public void registerProcessor(DustMessage.Filter filter_, DustComponentBase proc)
        {
            lock (processors)
            {
                processors.Add(new Processor(){ filter = filter_, processor = proc});
            }
        }

        protected override void loop()
        {
            lock (qEvents)
            {
                if (0 == qEvents[read].Count)
                {
                    if (!wait(qEvents))
                    {
                        return;
                    }
                }

                int r = read;
                read = dispatch;
                dispatch = r;

                qEvents[read].Clear();
            }

            lock (processors)
            {
                foreach (Event evt in qEvents[dispatch])
                {
                    lock (getSateLock())
                    {
                        if (isRun())
                        {
                            evt.dispatch(this);
                        }
                        else
                        {
                            return;
                        }
                    }
                }
            }
        }
    }
}
