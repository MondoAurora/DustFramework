using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using Dust.Core;
using System.Diagnostics;

namespace Dust.Tools
{
    public abstract class ToolsWorker<DataType> : DustComponent<DataType> where DataType : DustCompData
    {
        Thread threadDispatch;
        private volatile bool run = true;
        private object stateLock = new object();

        protected String name;

        public void start()
        {
            lock (stateLock)
            {
                if (null == threadDispatch)
                {
                    threadDispatch = new Thread(new ThreadStart(work));
                    threadDispatch.Name = name;
                    threadDispatch.SetApartmentState(ApartmentState.STA);
                    threadDispatch.Start();
                }
            }
        }

        public void stop()
        {
            Debug.WriteLine("Want to stop " + name);

            /* OUTSIDE the state lock!
             * The shutdown request comes from the GUI, to which the last events will
             * be disposed, but the gui thread is not responsive anymore because it was 
             * waiting for stateLock to access the run flag.
            */

            lock (stateLock)
            {
                run = false;
                Debug.WriteLine("Waiting for finished " + name);
                Monitor.Wait(stateLock);
            }
            Debug.WriteLine("Stopped " + name);
        }

        public bool isRun()
        {
            lock (stateLock)
            {
                return run;
            }
        }

        protected void finished()
        {
            lock (stateLock)
            {
                Monitor.Pulse(stateLock);
            }
        }

        void work()
        {
            loopInit();
            bool r;

            while (r = isRun())
            {
                loop();
            }

            loopEnd(r);

            finished();
        }

        protected Object getSateLock() {
            return stateLock;
        }

        protected void pulse(Object ob)
        {
            Monitor.Pulse(ob);
        }

        protected bool wait(Object ob)
        {
            bool ret = false;

            while (isRun() && !(ret = Monitor.Wait(ob, 100))) { }

            return ret;
        }

        protected virtual void loopInit() { }
        protected abstract void loop();
        protected virtual void loopEnd(bool normal) {}
    }
}
